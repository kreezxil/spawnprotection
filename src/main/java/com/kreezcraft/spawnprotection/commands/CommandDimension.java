package com.kreezcraft.spawnprotection.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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
		return "/sp-dimension <add|remove> <end|nether|overworld|id>\n" + "    determines if spawn protection is\n"
				+ "    enabled or not for the dimension.";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		if (args.length < 2) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}

		String action = args[0];
		String dim = args[1];

		int theDim = Integer.parseInt(dim);

		List<String> actions = Lists.newArrayList("add", "remove");

		if (dim.equals("end"))
			theDim = 1;
		else if (dim.equals("nether"))
			theDim = -1;
		else if (dim.equals("overworld"))
			theDim = 0;

		if (action.equals("add")) {
			if (!ProtectionConfig.protection.allowedDimensions.contains(theDim)) {
				sender.sendMessage(new TextComponentString("Dimension added to Protection List"));
				ProtectionConfig.protection.allowedDimensions.add(theDim);
			} else {
				sender.sendMessage(new TextComponentString("Dimension already in Protection List"));
			}

		} else if (action.equals("remove")) {
			if (!ProtectionConfig.protection.allowedDimensions.contains(theDim)) {
				sender.sendMessage(new TextComponentString("Dimension not in list, so you're good to go!"));
			} else {
				sender.sendMessage(new TextComponentString("Dimension removed from list."));
				Iterator itr = ProtectionConfig.protection.allowedDimensions.iterator();
				while (itr.hasNext()) {
					int i = (Integer) itr.next();
					if (itr.equals(theDim)) {
						itr.remove();
					}
				}
			}
		}
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
