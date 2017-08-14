package com.kreezcraft.spawnprotection;



import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SpawnProtection.MODID, version = SpawnProtection.VERSION, name = SpawnProtection.NAME)
public class SpawnProtection
{
    public static final String MODID = "spawnprotection";
    public static final String NAME = "Spawn Protection";
    public static final String VERSION = "1.11.2-1.3";
    
    @SidedProxy(clientSide = "com.kreezcraft.spawnprotection.ClientProxy", serverSide = "com.kreezcraft.spawnprotection.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static SpawnProtection instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
    
}
