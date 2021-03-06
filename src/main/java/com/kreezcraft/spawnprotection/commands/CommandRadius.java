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

public class CommandRadius extends CommandBase {

	public CommandRadius() {
		aliases = Lists.newArrayList(SpawnProtection.MODID, "sp-radius", "sp-diameter");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "sp-radius";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/sp-radius <range>\n    set range of the spawn protection\n    in diameter from worldspawn";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		int intRadius = 0;
		if (args.length < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		
		String radius = args[0];

		try {
			intRadius = Integer.parseInt(radius);
		} catch (NumberFormatException e) {
			sender.sendMessage(new TextComponentString("Radius must be a whole number from 1 to infinity!"));
			return;
		}

		if (intRadius < 1) {
			sender.sendMessage(new TextComponentString("Radius must be a whole number from 1 to infinity!"));
			return;
		}
		Config.spawnProtection.set(intRadius);

		Config.cfg.save();

		sender.sendMessage(new TextComponentString("Radius set to " + intRadius));
		sender.sendMessage(new TextComponentString("Config updated"));
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
