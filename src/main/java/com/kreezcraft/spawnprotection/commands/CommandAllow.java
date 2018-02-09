package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.List;

import com.kreezcraft.spawnprotection.Config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandAllow extends CommandBase {

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return "allow";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return " <circuits|containers|doors|place|block|item>";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("permit");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1 || args[0].length() < 1 || args[1].length() < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
		} else {
			String action = args[0].toLowerCase();
			String truth = args[1].toLowerCase();
			if (action != "circuits" || action != "containers" || action != "doors" || action != "place"
					|| action != "block" || action != "item") {
				sender.sendMessage(new TextComponentString(getUsage(sender)));
				return;
			}
			if (truth != "true" || truth != "false") {
				sender.sendMessage(new TextComponentString(getUsage(sender)));
				return;
			}
			boolean theTruth;
			if (truth == "true") {
				theTruth = true;
			} else {
				theTruth = false;
			}
			if (action == "circuits") {
				Config.allowCircuits = theTruth;
			} else if (action == "containers") {
				Config.allowContainers = theTruth;
			} else if (action == "doors") {
				Config.allowDoors = theTruth;
			} else if (action == "place") {
				Config.allowPlaceBlock = theTruth;
			} else if (action == "block") {
				Config.allowRightClickBlock = theTruth;
			} else if (action == "item") {
				Config.allowRightClickItem = theTruth;
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
