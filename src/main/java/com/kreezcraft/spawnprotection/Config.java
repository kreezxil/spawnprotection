package com.kreezcraft.spawnprotection;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String CATEGORY_PROTECTION = "protection";

    // This values below you can access elsewhere in your mod:
    public static int spawnProtection = 16; //default value

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
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
        cfg.addCustomCategoryComment(CATEGORY_PROTECTION, "Protection configuration");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        spawnProtection = cfg.getInt("spawn_protection", CATEGORY_PROTECTION, 16, 0, 10000, "Set op-less spawn protection radius with a range of 0 thru 10,000");
    }

}