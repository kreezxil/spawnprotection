package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kreezcraft.spawnprotection.Config;
import com.kreezcraft.spawnprotection.SpawnProtection;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandAllow extends CommandBase {

	public CommandAllow() {
		aliases = Lists.newArrayList(SpawnProtection.MODID, "allow","permit");
	}
	
	private final List<String> aliases;
	
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
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		if (args.length < 2) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		String action = args[0];
		String truth = args[1];

		List<String> options = Lists.newArrayList("circuits", "containers", "doors", "place", "block", "item");
		List<String> truths = Lists.newArrayList("true", "false");

		if (!options.contains(action)) {
			// if (action != "circuits" && action != "containers" && action != "doors" &&
			// action != "place"
			// && action != "block" && action != "item") {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		if (!truths.contains(truth)) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		boolean theTruth;
		if (truth.equalsIgnoreCase("true")) {
			theTruth = true;
		} else {
			theTruth = false;
		}

		if (action.equalsIgnoreCase("circuits")) {
			Config.allowCircuits = theTruth;
			Config.cfg.get(Config.CATEGORY_INTERACTION, "allowCircuits", theTruth); //i'd be happier if the function was "set"
		} else if (action.equalsIgnoreCase("containers")) {
			Config.allowContainers = theTruth;
			Config.cfg.get(Config.CATEGORY_INTERACTION, "containers", theTruth); //i'd be happier if the function was "set"
		} else if (action.equalsIgnoreCase("doors")) {
			Config.allowDoors = theTruth;
			Config.cfg.get(Config.CATEGORY_INTERACTION, "doors", theTruth); //i'd be happier if the function was "set"
		} else if (action.equalsIgnoreCase("place")) {
			Config.allowPlaceBlock = theTruth;
			Config.cfg.get(Config.CATEGORY_INTERACTION, "place", theTruth); //i'd be happier if the function was "set"
		} else if (action.equalsIgnoreCase("block")) {
			Config.allowRightClickBlock = theTruth;
			Config.cfg.get(Config.CATEGORY_INTERACTION, "block", theTruth); //i'd be happier if the function was "set"
		} else if (action.equalsIgnoreCase("item")) {
			Config.allowRightClickItem = theTruth;
			Config.cfg.get(Config.CATEGORY_INTERACTION, "item", theTruth); //i'd be happier if the function was "set"
		}

		if(Config.cfg.hasChanged()) {
			sender.sendMessage(new TextComponentString("config updated"));
			Config.cfg.save();	
		} else {
			sender.sendMessage(new TextComponentString("config not updated"));
		}
		
		return;
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		return Collections.emptyList();
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
