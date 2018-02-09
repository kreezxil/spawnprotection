package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.List;

import com.kreezcraft.spawnprotection.Config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandRadius extends CommandBase {

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return "radius";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "set range of the spawn protection in diameter from worldspawn";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add(null);
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		int intRadius=0;
		if (args.length < 1 || args[0].length() < 1 || args[1].length() < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
		} else {
			String action = args[0].toLowerCase();
			String radius = args[1].toLowerCase();
			try {
				intRadius = Integer.parseInt(radius);
			} catch (NumberFormatException e) {
				sender.sendMessage(new TextComponentString("Radius must be a whole number from 1 to infinity!"));
				return;
			}
			if (action != "radius" ) {
				sender.sendMessage(new TextComponentString(getUsage(sender)));
				return;
			}
		}
		if(intRadius<1) {
			sender.sendMessage(new TextComponentString("Radius must be a whole number from 1 to infinity!"));
			return;
		}
		Config.spawnProtection=intRadius;
		if(Config.cfg.hasChanged()) {
			Config.cfg.save();
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return CommandLib.checkPermission(server, sender);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
