package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Arrays;
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

public class CommandDimension extends CommandBase {

	public CommandDimension() {
		aliases = Lists.newArrayList(SpawnProtection.MODID, "sp-dim");
	}
	
	private final List<String> aliases;
	
	@Override
	@Nonnull
	public String getName() {
		return "sp-dimension";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "/sp-dimension <end|nether|overworld> <true|false>\n" + "    determines if spawn protection is\n"
				+ "    enabled or not for the dimension.";
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
		String dim = args[0];
		String truth = args[1];

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
				Config.theEnd.set(true);
			} else {
				Config.theEnd.set(false);
			}
		}
		if (dim.equalsIgnoreCase("overworld")) {
			if (truth.equalsIgnoreCase("true")) {
				Config.overWorld.set(true);
			} else {
				Config.overWorld.set(false);
			}
		}
		if (dim.equalsIgnoreCase("nether")) {
			if (truth.equalsIgnoreCase("true")) {
				Config.theNether.set(true);
			} else {
				Config.theNether.set(false);
			}
		}

		if(Config.cfg.hasChanged()) {
			sender.sendMessage(new TextComponentString("Protection of "+dim+" set to "+truth));
			sender.sendMessage(new TextComponentString("Config updated"));
			Config.cfg.save();
		} else {
			sender.sendMessage(new TextComponentString("Config not updated"));
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
		if(server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName()) != null) {
			return true; //ops can use the command
		}
		return false;
	}

}
