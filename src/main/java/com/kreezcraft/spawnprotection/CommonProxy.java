package com.kreezcraft.spawnprotection;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
    	EntityPlayer player = event.getEntityPlayer();
    	World world = player.getEntityWorld();
    	int px,pz,wx,wz;
    	
    	px = player.getPosition().getX();
    	pz = player.getPosition().getZ();
    	wx = world.getSpawnPoint().getX();
    	wz = world.getSpawnPoint().getZ();
    	
    	if(px > wx + Config.spawnProtection ||
    	   pz > wz + Config.spawnProtection ||
    	   px < wx - Config.spawnProtection ||
    	   pz < wz - Config.spawnProtection) {
    		return event;
    	} else {
    		if(!player.isCreative() && event.isCancelable()) {
    			event.setCanceled(true);
    		}
    		return null;
    	}
   	}
}

