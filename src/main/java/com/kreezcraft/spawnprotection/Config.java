package com.kreezcraft.spawnprotection;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static Configuration cfg = ServerProxy.config;

    private static final String CATEGORY_PROTECTION = "protection";
    private static final String CATEGORY_DIMENSIONS = "dimensions";
    private static final String CATEGORY_INTERACTION = "interaction";
   
    
    public static int spawnProtection; //default value
    public static boolean overWorld;
    public static boolean theNether;
    public static boolean theEnd;
    
    public static boolean allowDoors;
    public static boolean allowCircuits;
    public static boolean allowContainers;
    public static boolean allowRightClickBlock;
    public static boolean allowRightClickItem;

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
 
        //The dimensions. By default the Nether and The End are false.
        cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Dimension Configuration");
        overWorld = cfg.getBoolean("OverWorld", CATEGORY_DIMENSIONS, true, "If true the Overworld spawn will be protected");
        theNether = cfg.getBoolean("theNether", CATEGORY_DIMENSIONS, false, "If true the Nether spawn will be protected");
        theEnd = cfg.getBoolean("theEnd", CATEGORY_DIMENSIONS, false, "If true The End spawn will be protected");
        
        //is player allowed to fiddle with things
        cfg.addCustomCategoryComment(CATEGORY_INTERACTION, "Interaction Configuration");
        allowDoors = cfg.getBoolean("allowDoors", CATEGORY_INTERACTION, true, "If true the player can open doors, trap doors, and gates.");
        allowCircuits = cfg.getBoolean("allowCircuits", CATEGORY_INTERACTION, true, "If true the player can press buttons & flip levers.");
        allowContainers = cfg.getBoolean("allowContainers", CATEGORY_INTERACTION, true, "If true the player can open items that are also containers or give a valid output when combined with comparators.");
        allowRightClickBlock = cfg.getBoolean("allowRightClickBlock", CATEGORY_INTERACTION, true, "If true the player can right click on blocks in the area for interaction.");
        allowRightClickItem = cfg.getBoolean("allowRightClickItem", CATEGORY_INTERACTION, true, "If true the player can right click on items in their hand for interaction.");
    }

}