package com.kreezcraft.spawnprotection;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent;
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

	/*
	 * there are two methods following each with duplicate code i world like to use
	 * a function to reduce the code complexity kreezxil 11/16/2017
	 */

	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public static BlockEvent thoseDarnBlocks(BlockEvent event) {

		int px, pz, wx, wz;

		boolean cancelIt = true;

		World world = event.getWorld();
		MinecraftServer server = world.getMinecraftServer();

		EntityPlayer player = event.getWorld().getClosestPlayer(event.getPos().getX(), event.getPos().getY(),
				event.getPos().getZ(), 1, false);

		Block theBlock = event.getState().getBlock();
		// bet these two are the same
		IBlockState block = world.getBlockState(event.getPos());

		int worldId = world.provider.getDimension();
		px = event.getPos().getX();
		pz = event.getPos().getZ();
		wx = world.getSpawnPoint().getX();
		wz = world.getSpawnPoint().getZ();

		switch (worldId) {

		// overWorld
		case 0:
			if (!Config.overWorld) {
				return event;
			}
			break;

		// Nether
		case -1:
			if (!Config.theNether) {
				return event;
			}
			break;

		// The End
		case 1:
			if (!Config.theEnd) {
				return event;
			}
			break;

		// Some other dimensions definitely return null
		default:
			return event;
		}

		if (px > wx + Config.spawnProtection || pz > wz + Config.spawnProtection || px < wx - Config.spawnProtection
				|| pz < wz - Config.spawnProtection) {
			// player is outside the spawn protected area
			return event;
		}

		// is the player in creative or an operator?
		if (player != null && player.isCreative()) {
			return event;
		}

		// is it a real server?
		if (server != null && !server.isSinglePlayer() && player != null) {

			if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(player.getName()) != null) {
				// player is op
				return event;
			}

		}

		if (Config.allowPlaceBlock) {
			return event;
		}

		// after this point what we are saying is that Config.allowPlaceBlock is
		// false!!!

		if (event.isCancelable()) {
			event.setCanceled(true); // just try to be nice first
		} else {
			return null; // the bullheaded way to cancel the event
		}

		// player.entityDropItem(new ItemStack(Item.getItemFromBlock(block.getBlock())),
		// 1);

		return event; // yay, the proper way!

	}

	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public static PlayerInteractEvent escapingSpawn(PlayerInteractEvent event) {

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
				return event;
			}
			break;

		// Nether
		case -1:
			if (!Config.theNether) {
				return event;
			}
			break;

		// The End
		case 1:
			if (!Config.theEnd) {
				return event;
			}
			break;

		// Some other dimensions definitely return null
		default:
			return event;
		}

		if (px > wx + Config.spawnProtection || pz > wz + Config.spawnProtection || px < wx - Config.spawnProtection
				|| pz < wz - Config.spawnProtection) {
			// player is outside the spawn protected area
			return event;
		}

		// is the player in creative or an operator?
		if (player.isCreative()) {
			return event;
		}
		// is it a real server?
		if (server != null && !server.isSinglePlayer()) {
			if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(player.getName()) != null) {
				// player is op
				return event;
			}
		}

		if (block != null && block != player) { // it is a block and the block is not the player
			if (Config.debugMode)
				player.sendMessage(new TextComponentString("check interaction type"));

			// no left clicking!
			if (event instanceof PlayerInteractEvent.LeftClickBlock) {
				if (Config.debugMode)
					player.sendMessage(new TextComponentString("you're left clicking something"));
				if (event.isCancelable()) {
					event.setCanceled(true); // don't be harvesting circuits things that are being checked for below
					return event;
				} else {
					return null;
				}
			}

			// is it a button or lever?
			if (block.canProvidePower()) {
				// are you we allowed to do use them
				if (Config.debugMode)
					player.sendMessage(new TextComponentString("it provides power!"));
				if (Config.allowCircuits) {
					return event;
				} else {
					if (event.isCancelable()) {
						event.setCanceled(true);
					} else {
						return null;
					}
				}
			}

			// is the block a container?
			if (block.hasComparatorInputOverride()) {
				// are we allowed to use them?
				if (Config.debugMode)
					player.sendMessage(new TextComponentString("it's an inventory"));
				if (Config.allowContainers) {
					return event;
				} else {
					if (event.isCancelable()) {
						event.setCanceled(true);
					} else {
						return null;
					}
				}
			}

			// is it a door?
			if (block.getProperties().containsKey(OPEN)) {
				// are we allowed to use doors?
				if (Config.debugMode)
					player.sendMessage(new TextComponentString("this opens and closes"));

				if (Config.allowDoors) {
					return event;
				} else {
					if (event.isCancelable()) {
						event.setCanceled(true);
					} else {
						return null;
					}
				}
			}

			// is it some other block?
			if (event instanceof RightClickBlock) {
				// are we allowed to right click other blocks?
				if (Config.debugMode)
					player.sendMessage(new TextComponentString("you're right clicking it!"));

				if (Config.allowRightClickBlock) {
					return event;
				} else {
					if (event.isCancelable()) {
						event.setCanceled(true);
					} else {
						return null;
					}
				}
			}

			// is it an item in the hand?
			if (event instanceof RightClickItem) {
				// are we allowed to right click items in our hands?
				if (Config.debugMode)
					player.sendMessage(new TextComponentString("right clicking item in hand"));

				if (Config.allowRightClickItem) {
					return event;
				} else {
					if (event.isCancelable()) {
						event.setCanceled(true);
					} else {
						return null;
					}
				}
			}

			// if we got here then the event should be canceled
			if (event.isCancelable()) {
				event.setCanceled(true);
			} else {
				return null;
			}
		}

		// let something else process the event
		return event;

	}

}
