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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandDisplay extends CommandBase {

	public CommandDisplay() {
		aliases = Lists.newArrayList(SpawnProtection.MODID, "sp-display", "sp-show", "sp-view");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "sp-display";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/sp-display <all|worlds|protection|interaction>\n    show the config or parts thereof";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	private void printMsg(ICommandSender sender, String msg) {
		sender.sendMessage(new TextComponentString(msg));
	}

	private void showWorlds(ICommandSender sender) {
		printMsg(sender, "Worlds Spawn Protection");
		printMsg(sender, "=======================");
		printMsg(sender, "OverWorld: " + Config.overWorld.getBoolean());
		printMsg(sender, "Nether: " + Config.theNether.getBoolean());
		printMsg(sender, "TheEnd: " + Config.theEnd.getBoolean());
		printMsg(sender, " ");
	}

	private void showProtection(ICommandSender sender) {
		printMsg(sender, "Spawn Protection");
		printMsg(sender, "================");
		printMsg(sender, "Radius: " + Config.spawnProtection.getInt());
		printMsg(sender, "Debug Mode: " + Config.debugMode.getBoolean());
		printMsg(sender, "Ignore Ops: " + Config.ignoreOp.getBoolean());
		printMsg(sender, " ");
	}

	private void showInteractions(ICommandSender sender) {
		printMsg(sender, "Interaction Control");
		printMsg(sender, "===================");
		printMsg(sender, "Circuits: " + Config.allowCircuits.getBoolean());
		printMsg(sender, "Containers: " + Config.allowContainers.getBoolean());
		printMsg(sender, "Doors: " + Config.allowDoors.getBoolean());
		printMsg(sender, "Place Blocks: " + Config.allowPlaceBlock.getBoolean());
		printMsg(sender, "RightClickBlocks: " + Config.allowRightClickBlock.getBoolean());
		printMsg(sender, "RightClickItems: " + Config.allowRightClickItem.getBoolean());
		printMsg(sender, " ");
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		boolean theTruth;
		if (args.length < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		String action = args[0];

		if (action.equalsIgnoreCase("all")) {
			showWorlds(sender);
			showProtection(sender);
			showInteractions(sender);
		} else if (action.equalsIgnoreCase("worlds")) {
			showWorlds(sender);
		} else if (action.equalsIgnoreCase("protection")) {
			showProtection(sender);
		} else if (action.equalsIgnoreCase("interaction")) {
			showInteractions(sender);
		} else {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
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
