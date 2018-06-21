package com.kreezcraft.spawnprotection;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@Config(modid = SpawnProtection.MODID, category = "")
public class ProtectionConfig {

	@Config.Comment({ "Protection Configuration" })
	@Config.Name("General Protection")
	public static Protection protection = new Protection();

	public static class Protection {
		@Config.Comment({ "The range from 0 to 10000 for spawn protection." })
		@Config.Name("Spawn Protection")
		@Config.RangeInt(min = 0, max = 10000)
		public int spawnProtect = 16;

		@Config.Comment({
				"If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod" })
		@Config.Name("Debug Mode")
		public boolean debugMode = false;

		@Config.Comment({
				"If true ops privileges will be ignored, useful for testing of the commands without going to survival" })
		@Config.Name("Ignore Operators")
		public boolean ignoreOp = false;

		@Config.Comment({ "Dimensions to Protect", "-1 is the Nether", "0 is the Overworld", "1 is The End" })
		@Config.Name("Allowed Dimensions")
		public List<Integer> allowedDimensions = Lists.newArrayList(1);

	}

	@Config.Comment({ "Interaction Configuration" })
	@Config.Name("Granular Control")
	public static Interaction interaction = new Interaction();

	public static class Interaction {

		@Config.Comment({ "If true the player can open doors, trap doors, and gates." })
		@Config.Name("Allow Doors")
		public boolean allowDoors = true;

		@Config.Comment({ "If true the player can press buttons & flip levers." })
		@Config.Name("Allow Circuits")
		public boolean allowCircuits = true;

		@Config.Comment({
				"If true the player can open items that are also containers or give a valid output when combined with comparators." })
		@Config.Name("Allow Containers/Inventory")
		public boolean allowContainers = true;

		@Config.Comment({ "If true the player can right click on blocks in the area for interaction." })
		@Config.Name("Allow Right Click Block")
		public boolean allowRightClickBlock = true;

		@Config.Comment({ "If true the player can right click on items in the area for interaction." })
		@Config.Name("Allow Right Click Item")
		public boolean allowRightClickItem = true;

		@Config.Comment({
				"If true the player can place blocks. If false they can not and further it will eat their blocks. You might want to warn them about that. Also I'm working on it." })
		@Config.Name("Allow Place Block")
		public boolean allowPlaceBlock = true;
	}

	@SubscribeEvent
	public static void onProtectionConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(SpawnProtection.MODID)) {
			ConfigManager.sync(SpawnProtection.MODID, Config.Type.INSTANCE);
		}
	}

	public static void saveCfg() {
		ConfigManager.sync(SpawnProtection.MODID, Config.Type.INSTANCE);
	}
}