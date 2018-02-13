package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Arrays;
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

public class CommandDimension extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "dimension";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/dimension <end|nether|overworld> <true|false>\n" + "    determines if spawn protection is\n"
				+ "    enabled or not for the dimension.";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("dim");
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		if (args.length < 2) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		String dim = args[0].toLowerCase();
		String truth = args[1].toLowerCase();

		List<String> dims = Lists.newArrayList("end", "nether", "overworld");
		List<String> truths = Lists.newArrayList("true", "false");

		if (!dims.contains(dim)) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		if (!truths.contains(truth)) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		if (dim.equalsIgnoreCase("end")) {
			if (truth.equalsIgnoreCase("true")) {
				sender.sendMessage(new TextComponentString(dim + " set to " + truth));
				Config.theEnd = true;
			} else {
				sender.sendMessage(new TextComponentString(dim + " set to " + truth));
				Config.theEnd = false;
			}
		}
		if (dim.equalsIgnoreCase("overworld")) {
			if (truth.equalsIgnoreCase("true")) {
				sender.sendMessage(new TextComponentString(dim + " set to " + truth));
				Config.overWorld = true;
			} else {
				sender.sendMessage(new TextComponentString(dim + " set to " + truth));
				Config.overWorld = false;
			}
		}
		if (dim.equalsIgnoreCase("nether")) {
			if (truth.equalsIgnoreCase("true")) {
				sender.sendMessage(new TextComponentString(dim + " set to " + truth));
				Config.theNether = true;
			} else {
				sender.sendMessage(new TextComponentString(dim + " set to " + truth));
				Config.theNether = false;
			}
		}

		Config.readConfig();

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
		return Collections.emptyList();
	}

}
