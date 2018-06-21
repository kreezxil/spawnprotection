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

public class CommandIgnoreOp extends CommandBase {

	public CommandIgnoreOp() {
		aliases = Lists.newArrayList(SpawnProtection.MODID, "sp-ignore", "sp-ignore_op", "sp-ignoreop");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "sp-ignore";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/sp-ignore_op <true|false>\n    should we ignore the op privilege?";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		boolean theTruth;
		if (args.length < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		String truth = args[0];

		if (truth.equalsIgnoreCase("true")) {
			theTruth = true;
		} else if (truth.equalsIgnoreCase("false")) {
			theTruth = false;
		} else {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		sender.sendMessage(new TextComponentString("Ignoring Op Priveleges is " + theTruth));
		ProtectionConfig.protection.ignoreOp = theTruth;
		ProtectionConfig.saveCfg();
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
		if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName()) != null) {
			return true; // ops can use the command
		}
		return false;
	}

}
