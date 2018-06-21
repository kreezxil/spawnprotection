package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kreezcraft.spawnprotection.ProtectionConfig;
import com.kreezcraft.spawnprotection.SpawnProtection;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;

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
		return "/sp-display <all|worlds|protection|interaction>\n    show the ProtectionConfig or parts thereof";
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
		
		DimensionType dimension = null;
		
		printMsg(sender, "Worlds Spawn Protection");
		printMsg(sender, "=======================");
		for (int dim : ProtectionConfig.protection.allowedDimensions ) {
			try {
				if(dimension.getById(dim)!=null) {
					printMsg(sender, dimension.getById(dim).getName() + ": enabled");
				}
			} catch(Exception e) {
				printMsg(sender, dim + " : does not exist!");
			}
		}
		printMsg(sender, " ");
	}

	private void showProtection(ICommandSender sender) {
		printMsg(sender, "Spawn Protection");
		printMsg(sender, "================");
		printMsg(sender, "Radius: " + ProtectionConfig.protection.spawnProtect);
		printMsg(sender, "Debug Mode: " + ProtectionConfig.protection.debugMode);
		printMsg(sender, "Ignore Ops: " + ProtectionConfig.protection.ignoreOp);
		printMsg(sender, " ");
	}

	private void showInteractions(ICommandSender sender) {
		printMsg(sender, "Interaction Control");
		printMsg(sender, "===================");
		printMsg(sender, "Circuits: " + ProtectionConfig.interaction.allowCircuits);
		printMsg(sender, "Containers: " + ProtectionConfig.interaction.allowContainers);
		printMsg(sender, "Doors: " + ProtectionConfig.interaction.allowDoors);
		printMsg(sender, "Place Blocks: " + ProtectionConfig.interaction.allowPlaceBlock);
		printMsg(sender, "RightClickBlocks: " + ProtectionConfig.interaction.allowRightClickBlock);
		printMsg(sender, "RightClickItems: " + ProtectionConfig.interaction.allowRightClickItem);
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
