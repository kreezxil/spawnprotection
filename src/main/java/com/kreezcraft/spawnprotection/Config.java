package com.kreezcraft.spawnprotection;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

	public static Configuration cfg = ServerProxy.config;

	public static String CATEGORY_PROTECTION = "protection";
	public static String CATEGORY_DIMENSIONS = "dimensions";
	public static String CATEGORY_INTERACTION = "interaction";

	// public static boolean debugMode;
	//
	// public static int spawnProtection; //default value
	// public static boolean overWorld;
	// public static boolean theNether;
	// public static boolean theEnd;
	//
	// public static boolean allowDoors;
	// public static boolean allowCircuits;
	// public static boolean allowContainers;
	// public static boolean allowRightClickBlock;
	// public static boolean allowRightClickItem;
	// public static boolean allowPlaceBlock;

	public static Property debugMode, spawnProtection, overWorld, theNether, theEnd, allowDoors, allowCircuits,
			allowContainers, allowRightClickBlock, allowRightClickItem, allowPlaceBlock,ignoreOp;

	// Call this from CommonProxy.preInit(). It will create our config if it doesn't
	// exist yet and read the values if it does exist.
	public static void readConfig() {
		try {
			cfg.load();
			initGeneralConfig(cfg);
		} catch (Exception e1) {
			SpawnProtection.logger.log(Level.ERROR, "Problem loading config file!", e1);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	private static void initGeneralConfig(Configuration cfg) {

		cfg.addCustomCategoryComment(CATEGORY_PROTECTION, "Protection Configuration");
		// spawnProtection = cfg.getInt("spawn_protection", CATEGORY_PROTECTION, 16, 0,
		// 10000, "Set op-less spawn protection radius with a range of 0 thru 10,000");
		// debugMode = cfg.getBoolean("debugMode", CATEGORY_PROTECTION, false, "If true
		// it will print messages to the player based on what you are doing in the
		// protected zone, useful for helping Kreezxil debug the mod");

		spawnProtection = cfg.get(CATEGORY_PROTECTION, "spawn_protection", 16,
				"Set op-less spawn protection radius with a range of 0 thru 10,000", 0, 10000);
		debugMode = cfg.get(CATEGORY_PROTECTION, "debugMode", false,
				"If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod");
		ignoreOp = cfg.get(CATEGORY_PROTECTION, "ignoreOp", false,"If true ops privileges will be ignored, useful for testing of the commands without going to survival");

		// The dimensions. By default the Nether and The End are false.
		cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Dimension Configuration");
		overWorld = cfg.get(CATEGORY_DIMENSIONS, "OverWorld", true, "If true the Overworld spawn will be protected");
		theNether = cfg.get(CATEGORY_DIMENSIONS, "theNether", false, "If true the Nether spawn will be protected");
		theEnd = cfg.get(CATEGORY_DIMENSIONS, "theEnd", false, "If true The End spawn will be protected");

		// is player allowed to fiddle with things
		cfg.addCustomCategoryComment(CATEGORY_INTERACTION, "Interaction Configuration");
		allowDoors = cfg.get(CATEGORY_INTERACTION, "allowDoors", true,
				"If true the player can open doors, trap doors, and gates.");
		allowCircuits = cfg.get(CATEGORY_INTERACTION, "allowCircuits", true,
				"If true the player can press buttons & flip levers.");
		allowContainers = cfg.get(CATEGORY_INTERACTION, "allowContainers", true,
				"If true the player can open items that are also containers or give a valid output when combined with comparators.");
		allowRightClickBlock = cfg.get(CATEGORY_INTERACTION, "allowRightClickBlock", true,
				"If true the player can right click on blocks in the area for interaction.");
		allowRightClickItem = cfg.get(CATEGORY_INTERACTION, "allowRightClickItem", true,
				"If true the player can right click on items in the area for interaction.");
		allowPlaceBlock = cfg.get(CATEGORY_INTERACTION, "allowPlaceBlock", true,
				"If true the player can place blocks. If false they can not and further it will eat their blocks. You might want to warn them about that. Also I'm working on it.");
	}

}