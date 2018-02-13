package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
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
	public int compareTo(ICommand p_compareTo_1_) {
		return super.compareTo(p_compareTo_1_);
	}

	@Override
	@Nonnull
	public String getName() {
		return "allow";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/allow <circuits|containers|doors|place|block|item> <true|false>\n"
				+ "    enables or disables the types of actions\n" + "    circuits=redstone, buttons, levers\n"
				+ "    containers=inventory objects\n" + "    doors=doors & trapdoors\n" + "    place=block placement\n"
				+ "    block=block interaction\n" + "    item=item interaction aka your hand item";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("permit");
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		if (args.length < 2) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		boolean theTruth = false;

		List<String> options = Lists.newArrayList("circuits", "containers", "doors", "place", "block", "item");
		List<String> truths = Lists.newArrayList("true", "false");

		if (!options.contains(args[0].toLowerCase())) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		if (!truths.contains(args[1].toLowerCase())) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		sender.sendMessage(new TextComponentString("Action:[" + args[0].toLowerCase() + "]"));
		sender.sendMessage(new TextComponentString(" Truth:[" + args[1].toLowerCase() + "]"));

		if (args[1].equalsIgnoreCase("true")) {
			theTruth = true;
		} else {
			theTruth = false;
		}

		if (args[0].equalsIgnoreCase("circuits")) {
			Config.allowCircuits = theTruth;
			sender.sendMessage(new TextComponentString(args[0] + " set to " + theTruth));
		} else if (args[0].equalsIgnoreCase("containers")) {
			sender.sendMessage(new TextComponentString(args[0] + " set to " + theTruth));
			Config.allowContainers = theTruth;
		} else if (args[0].equalsIgnoreCase("doors")) {
			sender.sendMessage(new TextComponentString(args[0] + " set to " + theTruth));
			Config.allowDoors = theTruth;
		} else if (args[0].equalsIgnoreCase("place")) {
			sender.sendMessage(new TextComponentString(args[0] + " set to " + theTruth));
			Config.allowPlaceBlock = theTruth;
		} else if (args[0].equalsIgnoreCase("block")) {
			sender.sendMessage(new TextComponentString(args[0] + " set to " + theTruth));
			Config.allowRightClickBlock = theTruth;
		} else if (args[0].equalsIgnoreCase("item")) {
			sender.sendMessage(new TextComponentString(args[0] + " set to " + theTruth));
			Config.allowRightClickItem = theTruth;
		}

		Config.saveConfig();

		return;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return CommandLib.checkPermission(server, sender);
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		return null;
	}

}
