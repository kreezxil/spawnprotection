package com.kreezcraft.spawnprotection;

import org.apache.logging.log4j.Logger;

import com.kreezcraft.spawnprotection.commands.CommandAllow;
import com.kreezcraft.spawnprotection.commands.CommandDimension;
import com.kreezcraft.spawnprotection.commands.CommandRadius;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SpawnProtection.MODID, version = SpawnProtection.VERSION, name = SpawnProtection.NAME, acceptableRemoteVersions = "*")
public class SpawnProtection {
	public static final String MODID = "spawnprotection";
	public static final String NAME = "No Op Spawn Protection";
	public static final String VERSION = "@VERSION@";

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

	/*
	 * I'm having problems using the commands to force the values to update that
	 * control the mod, its as if the config loads from the file in a static form
	 * and becomes unmutable so what then is cfg.save() even good for if it won't
	 * overwrite the config file. I'll have to research this later. For now, the mod
	 * is working much better than it was before.
	 * 
	 * Kreezxil 2/9/2018
	 */
	// @Mod.EventHandler
	// public void serverLoad(FMLServerStartingEvent event) {
	//
	// event.registerServerCommand(new CommandAllow());
	// event.registerServerCommand(new CommandDimension());
	// event.registerServerCommand(new CommandRadius());
	// }

}
