package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	@Nonnull
	public String getName() {
		return "radius";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/radius <range>\n    set range of the spawn protection\n    in diameter from worldspawn";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add(null);
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		int intRadius = 0;
		if (args.length < 2) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		String action = args[0].toLowerCase();
		String radius = args[1].toLowerCase();
		try {
			intRadius = Integer.parseInt(radius);
		} catch (NumberFormatException e) {
			sender.sendMessage(new TextComponentString("Radius must be a whole number from 1 to infinity!"));
			return;
		}
		if (action != "radius") {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		if (intRadius < 1) {
			sender.sendMessage(new TextComponentString("Radius must be a whole number from 1 to infinity!"));
			return;
		}
		Config.spawnProtection = intRadius;
		sender.sendMessage(new TextComponentString(action + " set to " + intRadius));

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
		return Collections.emptyList();
	}

}
