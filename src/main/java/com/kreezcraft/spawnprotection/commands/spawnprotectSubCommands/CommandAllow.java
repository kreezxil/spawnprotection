package com.kreezcraft.spawnprotection.commands.spawnprotectSubCommands;

import java.awt.TextComponent;
import java.util.List;

import com.kreezcraft.spawnprotection.Config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandAllow extends CommandBase {

	@Override
	public String getName() {
		return "allow";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return getName()+" <circuits|containers|doors>\n"+
			   "  circuits (buttons, levers, pressure plates)\n" +
			   "  containers (chests, furnaces, machines, ...)\n" +
			   "  doors (doors, trap doors, gates)\n";
		
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(server!=null && 
		   !server.isSinglePlayer() && 
		   server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName())==null) {
					throw new CommandException("You are not authorized, please either be in SinglePlayer or be Opped.\n");
		}
		
		//not a dedicated server, do the command
		if(args.length < 2) {
			throw new CommandException("You need at least two arguments.\n" + TextFormatting.RED + getUsage(sender));
		}
		
		boolean allow;
		String temp = args[args.length-1].toLowerCase(); //the last bit
		switch(temp) {
		case "true": 
			allow = true;
			break;
		case "false":
			allow = false;
			break;
		default:
			throw new CommandException(
					"Expected 'true' or 'false' ... received ... " +
					args[args.length] + "!\n" +
					TextFormatting.RED + getUsage(sender)
					);
		
		}
		
		String command = args[0].toLowerCase();
		
		switch(command) {
		case "doors":
			Config.allowDoors = allow;
			break;
		case "containers":
			Config.allowContainers = allow;
			break;
		case "circuits":
			Config.allowCircuits = allow;
			break;
		default:
			throw new CommandException("Expected one of 'doors,containers, or circuits' ... received " +
									   args[0] + "!\n" + TextFormatting.RED + getUsage(sender));
		}
		
		//print confirmation
		sender.sendMessage(
				new TextComponentString(
						TextFormatting.AQUA + 
						"Can players use " + command + " set to " + allow + 
						TextFormatting.RED + " Successfully!"
				)
		);

		Config.cfg.save();
	}

	

}
