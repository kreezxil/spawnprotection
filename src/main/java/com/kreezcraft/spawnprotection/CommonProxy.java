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
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
			return event;
		} else {
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

			if (event.isCancelable()) {
				if (block != null && block != player) {
					if(event instanceof PlayerInteractEvent.LeftClickBlock) {
						event.setCanceled(true); //don't be harvesting circuits things that are being checked for below
					}
					if (block.canProvidePower() && Config.allowCircuits) {
						// button, lever
						return null;
					}
					if (block.hasComparatorInputOverride() && Config.allowContainers) {
						// containers
						return null;
					}
					if (block.getProperties().containsKey(OPEN) && Config.allowDoors) {
						// door, trapdoor
						return null;
					}
				}

				event.setCanceled(true);
			}
			return null;
		}
	}
	
	//the following causes intense lag!
//	@SubscribeEvent
//	public static GetCollisionBoxesEvent escapingSpawn(GetCollisionBoxesEvent event) {
//
//		SpawnProtection.logger.debug(event.toString());
//
//		EntityPlayer player;
//		if(event.getEntity() instanceof EntityPlayer) {
//			player = (EntityPlayer) event.getEntity();
//		} else {
//			return event; //get out of here
//		}
//
//		World world = player.getEntityWorld();
//
//		MinecraftServer server = player.getServer();
//
//		int px, pz, wx, wz;
//
//		int worldId = world.provider.getDimension();
//
//		/*
//		 * Actually it's not the player I need the check but the block with which they
//		 * are interacting. Previously this was player.getPosition().getX() etc just so
//		 * you know
//		 * 
//		 * What the javadoc says is that event's getPos, will return the position of the
//		 * thing being acted upon. And should there be no thing, then the player becomes
//		 * the thing.
//		 * 
//		 * Therefore it should be a more accurate representation of any player trying to
//		 * much around within the protected area.
//		 * 
//		 * -- Kreezxil 15 Aug, 2017
//		 */
//		px = player.getPosition().getX();
//		pz = player.getPosition().getZ();
//		wx = world.getSpawnPoint().getX();
//		wz = world.getSpawnPoint().getZ();
//
//		IBlockState block = world.getBlockState(player.getPosition());
//
//		// if the dimension that the player is in not configured for true, this will
//		// exit
//		// and allow them to edit the dimension spawn.
//		switch (worldId) {
//
//		// overWorld
//		case 0:
//			if (!Config.overWorld) {
//				return null;
//			}
//			break;
//
//		// Nether
//		case -1:
//			if (!Config.theNether) {
//				return null;
//			}
//			break;
//
//		// The End
//		case 1:
//			if (!Config.theEnd) {
//				return null;
//			}
//			break;
//
//		// Some other dimensions definitely return null
//		default:
//			return null;
//		}
//
//		if (px > wx + Config.spawnProtection || pz > wz + Config.spawnProtection || px < wx - Config.spawnProtection
//				|| pz < wz - Config.spawnProtection) {
//			return event;
//		} else {
//			// is the player in creative or an operator?
//			if (player.isCreative()) {
//				return null;
//			}
//			// is it a real server?
//			if (server != null && !server.isSinglePlayer()) {
//				if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(player.getName()) != null) {
//					// player is op
//					return null;
//				}
//			}
//
//			if (event.isCancelable()) {
//				if (block != null && block != player) {
//					if (block.canProvidePower() && Config.allowCircuits) {
//						//pressure plate
//						return null;
//					}
//				}
//
//				event.setCanceled(true);
//			}
//			return null;
//		}
//	}
}

