package com.kreezcraft.spawnprotection;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
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
			if (!Config.overWorld.getBoolean()) {
				return event;
			}
			break;

		// Nether
		case -1:
			if (!Config.theNether.getBoolean()) {
				return event;
			}
			break;

		// The End
		case 1:
			if (!Config.theEnd.getBoolean()) {
				return event;
			}
			break;

		// Some other dimensions definitely return null
		default:
			return event;
		}

		if (px > wx + Config.spawnProtection.getInt() || pz > wz + Config.spawnProtection.getInt()
				|| px < wx - Config.spawnProtection.getInt() || pz < wz - Config.spawnProtection.getInt()) {
			// player is outside the spawn protected area
			return event;
		}

		// if the ignoreOp is false then process the following block
		if (!Config.ignoreOp.getBoolean()) {
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
		}

		if (Config.allowPlaceBlock.getBoolean()) {

			if(Config.debugMode.getBoolean()) System.out.println("Allowing block placement");
			return event;
		} else if (!Config.allowPlaceBlock.getBoolean()) {
			if(Config.debugMode.getBoolean()) System.out.println("Canceling block placement");
			// after this point what we are saying is that Config.allowPlaceBlock is
			// false!!!

			if (event.isCancelable()) {
				event.setCanceled(true); // just try to be nice first
			} else {
				return null; // the bullheaded way to cancel the event
			}
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
		if(!isWorldProtected(world)) {
			return event;
		}

		if (px > wx + Config.spawnProtection.getInt() || pz > wz + Config.spawnProtection.getInt()
				|| px < wx - Config.spawnProtection.getInt() || pz < wz - Config.spawnProtection.getInt()) {
			// player is outside the spawn protected area
			return event;
		}

		// if ignoreOp is false, then process the next block
		if (!Config.ignoreOp.getBoolean()) {

			// is the player in creative or an operator?
			if (player.isCreative()) {
				return event;
			}

			// is it a real server?
			if (server == null)
				if(Config.debugMode.getBoolean()) player.sendMessage(str("server is null"));
			if (server.isSinglePlayer())
				if(Config.debugMode.getBoolean()) player.sendMessage(str("server is singleplayer"));

			if (server != null && !server.isSinglePlayer() && player != null) {

				if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(player.getName()) != null) {
					// player is op
					return event;
				}

			}
		}

	
		if (block != null && block != player) { // it is a block and the block is not the player
			// BEGIN - LEGIT BLOCK

			// no left clicking!
			if (event instanceof PlayerInteractEvent.LeftClickBlock) {
				if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Canceling left clicking of blocks"));
				if (event.isCancelable()) {
					event.setCanceled(true); // don't be harvesting circuits things that are being checked for below
					// return event;
				} else {
					return null;
				}
			}

			// is it a button or lever?
			if (block.canProvidePower()) {
				// are you we allowed to do use them
				if (Config.allowCircuits.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Allowing circuits"));
					return event;
				} else if (!Config.allowCircuits.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Canceling circuits"));
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
				if (Config.allowContainers.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Allowing container access"));
					return event;
				} else if (!Config.allowContainers.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Canceling container access"));
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

				if (Config.allowDoors.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Allowing open/close"));
					return event;
				} else if (!Config.allowDoors.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Canceling open/close"));
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
				if (Config.allowRightClickBlock.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Allowing block right clicking"));
					return event;
				} else if (!Config.allowRightClickBlock.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Canceling block right clicking"));
					if (event.isCancelable()) {
						event.setCanceled(true);
						// return event;
					} else {
						return null;
					}
				}
			}

			// is it an item in the hand?
			if (event instanceof RightClickItem) {
				// are we allowed to right click items in our hands?
				if (Config.allowRightClickItem.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Allowing item right click"));
					return event;
				} else if (!Config.allowRightClickItem.getBoolean()) {
					if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Canceling item right click"));
					if (event.isCancelable()) {
						event.setCanceled(true);
						// return event;
					} else {
						return null;
					}
				}
			}

			// villager?
			if (block instanceof EntityVillager) {
				if(Config.debugMode.getBoolean()) player.sendMessage(new TextComponentString("Allowing villager interaction"));
				return event;
			}

			// // if we got here then the event should be canceled
			// if (event.isCancelable()) {
			// event.setCanceled(true);
			// } else {
			// return null;
			// } else

		} // END - LEGIT BLOCK -

		// now return the event and let forge handle it from here based on what we did
		// or did not do to it
		return event;

	}

	private static ITextComponent str(String s) {
		return new TextComponentString(s);
	}
	
	private static Boolean isWorldProtected(World worldIn) {

		
		switch (worldIn.provider.getDimension()) {

		// overWorld
		case 0:
			return Config.overWorld.getBoolean();
		// Nether
		case -1:
			return Config.theNether.getBoolean();
					// The End
		case 1:
			return Config.theEnd.getBoolean();
		// Some other dimensions definitely return false
		default:
			return false;
		}
	}
}
