package com.kreezcraft.spawnprotection.commands;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.kreezcraft.spawnprotection.commands.spawnprotectSubCommands.CommandAllow;
import com.kreezcraft.spawnprotection.commands.spawnprotectSubCommands.CommandDimension;
import com.kreezcraft.spawnprotection.commands.spawnprotectSubCommands.CommandRadius;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandSpawnProtect extends CommandTreeBase {
	
	public CommandSpawnProtect() {
		addSubcommand(new CommandRadius());
		addSubcommand(new CommandDimension());
		addSubcommand(new CommandAllow());
	}

	@Override
	public String getName() {
		return "spawnprotection";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		List<String> subCommands = Lists.newArrayList();
		for (ICommand subCommand : getSubCommands())
			subCommands.add(subCommand.getName());
		return "/"+getName()+" <" + Joiner.on("|").join(subCommands) + ">";
	}
	
}