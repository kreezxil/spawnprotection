package com.kreezcraft.spawnprotection.commands.spawnprotectSubCommands;

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
import scala.actors.threadpool.Arrays;

public class CommandDimension extends CommandBase {

	@Override
	public String getName() {
		return "dimension";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return getName()+" <overworld|nether|end>\n";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(server!=null && 
		   !server.isSinglePlayer() && 
		   server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName())==null) {
				throw new CommandException("You are not authorized, please either be in SinglePlayer or be Opped.\n");
		}

		if(args.length < 2) {
			throw new CommandException("You need at least two arguments.\n" + TextFormatting.RED + getUsage(sender));
		}

		boolean dimensionState;
		String lastBit = args[args.length-1].toLowerCase();
		switch(lastBit) {
		case "true":
			dimensionState = true;
			break;
		case "false":
			dimensionState = false;
			break;
		default:
			throw new CommandException("Last argument should be true or false.\n" + TextFormatting.RED + getUsage(sender));
		}

		String[] dimension = (String[]) Arrays.copyOfRange(args, 0, args.length-1); //grab the first part to concatenated
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < dimension.length; i++) {
			strBuilder.append(dimension[i]);
			strBuilder.append(" ");
		}
		String whichDimension = strBuilder.toString().toLowerCase();
		
		switch(whichDimension) {
		case "overworld":
		case "ov":
		case "ow":
			Config.overWorld = dimensionState;
			break;
		case "nether":
		case "the nether":
			Config.theNether = dimensionState;
			break;
		case "end":
		case "the end":
		case "ender":
			Config.theEnd = dimensionState;
			break;
		default:
			throw new CommandException("That was not a dimension you area allowed to change or no argument given!\n" + TextFormatting.RED + getUsage(sender));
		}

		//print confirmation
		sender.sendMessage(
				new TextComponentString(
						TextFormatting.AQUA + 
						"Spawn protection for " + whichDimension + " set to " + dimensionState + 
						TextFormatting.RED + " Successfully!"
				)
		);

		Config.cfg.save();
	
	
	}

	


}
