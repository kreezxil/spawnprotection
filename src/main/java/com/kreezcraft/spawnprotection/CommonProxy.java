package com.kreezcraft.spawnprotection;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "spawnprotection.cfg"));
        Config.readConfig();
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
   }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    }
    
	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public static PlayerInteractEvent escapingSpawn(PlayerInteractEvent event) {

		//SpawnProtection.logger.debug(event.toString());

		EntityPlayer player = event.getEntityPlayer();

		World world = player.getEntityWorld();

		MinecraftServer server = player.getServer();

		int px, pz, wx, wz;

		int worldId = world.provider.getDimension();

		/*
		 * Actually it's not the player I need the check but the block with which they
		 * are interacting. Previously this was player.getPosition().getX() etc just so
		 * you know
		 * 
		 * What the javadoc says is that event's getPos, will return the position of the
		 * thing being acted upon. And should there be no thing, then the player becomes
		 * the thing.
		 * 
		 * Therefore it should be a more accurate representation of any player trying to
		 * much around within the protected area.
		 * 
		 * -- Kreezxil 15 Aug, 2017
		 */
		px = event.getPos().getX();
		pz = event.getPos().getZ();
		wx = world.getSpawnPoint().getX();
		wz = world.getSpawnPoint().getZ();

		IBlockState block = world.getBlockState(event.getPos());

		// needed to get doors
		PropertyBool OPEN = PropertyBool.create("open");

		// if the dimension that the player is in not configured for true, this will
		// exit
		// and allow them to edit the dimension spawn.
		switch (worldId) {

		// overWorld
		case 0:
			if (!Config.overWorld) {
				return null;
			}
			break;

		// Nether
		case -1:
			if (!Config.theNether) {
				return null;
			}
			break;

		// The End
		case 1:
			if (!Config.theEnd) {
				return null;
			}
			break;

		// Some other dimensions definitely return null
		default:
			return null;
		}

		if (    px > wx + Config.spawnProtection || 
				pz > wz + Config.spawnProtection || 
				px < wx - Config.spawnProtection || 
				pz < wz - Config.spawnProtection) {
			//player is outside the spawn protected area
			return event;
		} 
		
		// is the player in creative or an operator?
		if (player.isCreative()) {
			return null;
		}
		// is it a real server?
		if (server != null && !server.isSinglePlayer()) {
			if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(player.getName()) != null) {
				// player is op
				return null;
			}
		}

		//is the event something that I can actually do something with?
		if (event.isCancelable()) {
			//does the block value contain something that I can test and is the block being intereacted upon not the player
			if (block != null && block != player) {
				//no left clicking!
				if(event instanceof PlayerInteractEvent.LeftClickBlock) {
					event.setCanceled(true); //don't be harvesting circuits things that are being checked for below
				}
				
				//is it a button or lever?
				if (block.canProvidePower()) {
					//are you we allowed to do use them
					if ( Config.allowCircuits) {
						return null;
					}
				}
				
				//is the block a container?
				if (block.hasComparatorInputOverride()) {
					//are we allowed to use them?
					if (Config.allowContainers) {
						return null;
					}
				}
				
				//is it a door?
				if (block.getProperties().containsKey(OPEN)) {
					//are we allowed to use doors?
					if (Config.allowDoors) {
						return null;
					}
				}
				
				//make sure it's not a right click event and if it is let it pass
				if(event instanceof RightClickItem) {
					return event;
				}
				//if we got here then the event should be canceled
				event.setCanceled(true);
			}
			//let something else process the event
			return event;
		}
		
		return null;
		
	}

}

