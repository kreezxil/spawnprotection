package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kreezcraft.spawnprotection.Config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandDimension extends CommandBase {

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return "dimension";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<end|nether|overworld> <true|false>";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("dim");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1 || args[0].length() < 1 || args[1].length() < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
		} else {
			String dim = args[0].toLowerCase();
			String truth = args[1].toLowerCase();
			if (dim != "end" && dim != "nether" && dim != "overworld") {
				sender.sendMessage(new TextComponentString(getUsage(sender)));
				return;
			}
			if (truth != "true" && truth != "false") {
				sender.sendMessage(new TextComponentString(getUsage(sender)));
				return;
			}
			if (dim == "end") {
				if (truth == "true") {
					Config.theEnd = true;
				} else {
					Config.theEnd = false;
				}
			}
			if (dim == "overworld") {
				if (truth == "true") {
					Config.overWorld = true;
				} else {
					Config.overWorld = false;
				}
			}
			if (dim == "nether") {
				if (truth == "true") {
					Config.theNether = true;
				} else {
					Config.theNether = false;
				}
			}
		}
		if (Config.cfg.hasChanged()) {
			Config.cfg.save();
		}
		return;
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
