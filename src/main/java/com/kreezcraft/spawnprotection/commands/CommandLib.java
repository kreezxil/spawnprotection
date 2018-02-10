package com.kreezcraft.spawnprotection.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandLib {
	

	public static boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender instanceof EntityPlayer && server != null && !server.isSinglePlayer()
				&& server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName()) != null) {
			// player is op
			return true;
		}
		return false;
	}

}
