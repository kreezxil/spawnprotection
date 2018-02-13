package com.kreezcraft.spawnprotection;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static Configuration cfg = ServerProxy.config;

    private static String CATEGORY_PROTECTION = "protection";
    private static String CATEGORY_DIMENSIONS = "dimensions";
    private static String CATEGORY_INTERACTION = "interaction";
   
    public static boolean debugMode = false;
    
    public static int spawnProtection = 16; //default value
    public static boolean overWorld = true;
    public static boolean theNether = false;
    public static boolean theEnd = false;
    
    public static boolean allowDoors = true;
    public static boolean allowCircuits = true;
    public static boolean allowContainers = true;
    public static boolean allowRightClickBlock = true;
    public static boolean allowRightClickItem = true;
    public static boolean allowPlaceBlock = true;

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
        spawnProtection = cfg.getInt("spawn_protection", CATEGORY_PROTECTION, 16, 0, 10000, "Set op-less spawn protection radius with a range of 0 thru 10,000");
        debugMode = cfg.getBoolean("debugMode", CATEGORY_PROTECTION, debugMode, "If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod");
        //The dimensions. By default the Nether and The End are false.
        cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Dimension Configuration");
        overWorld = cfg.getBoolean("OverWorld", CATEGORY_DIMENSIONS, overWorld, "If true the Overworld spawn will be protected");
        theNether = cfg.getBoolean("theNether", CATEGORY_DIMENSIONS, theNether, "If true the Nether spawn will be protected");
        theEnd = cfg.getBoolean("theEnd", CATEGORY_DIMENSIONS, theEnd, "If true The End spawn will be protected");
        
        //is player allowed to fiddle with things
        cfg.addCustomCategoryComment(CATEGORY_INTERACTION, "Interaction Configuration");
        allowDoors = cfg.getBoolean("allowDoors", CATEGORY_INTERACTION, allowDoors, "If true the player can open doors, trap doors, and gates.");
        allowCircuits = cfg.getBoolean("allowCircuits", CATEGORY_INTERACTION, allowCircuits, "If true the player can press buttons & flip levers.");
        allowContainers = cfg.getBoolean("allowContainers", CATEGORY_INTERACTION, allowContainers, "If true the player can open items that are also containers or give a valid output when combined with comparators.");
        allowRightClickBlock = cfg.getBoolean("allowRightClickBlock", CATEGORY_INTERACTION, allowRightClickBlock, "If true the player can right click on blocks in the area for interaction.");
        allowRightClickItem = cfg.getBoolean("allowRightClickItem", CATEGORY_INTERACTION, allowRightClickItem, "If true the player can right click on items in their hand for interaction.");
        allowPlaceBlock = cfg.getBoolean("allowPlaceBlock", CATEGORY_INTERACTION, allowPlaceBlock, "If true the player can place blocks. If false they can not and further it will eat their blocks. You might want to warn them about that. Also I'm working on it.");
        if(cfg.hasChanged()) {
        	cfg.save();
        }
    }



}