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
if(lastBit == "true")
			dimensionState = true;
else if(lastBit == "false")
			dimensionState = false;
else
			throw new CommandException("Last argument should be true or false.\n" + TextFormatting.RED + getUsage(sender));
	

		String[] dimension = (String[]) Arrays.copyOfRange(args, 0, args.length-1); //grab the first part to concatenated
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < dimension.length; i++) {
			strBuilder.append(dimension[i]);
			strBuilder.append(" ");
		}
		String whichDimension = strBuilder.toString().toLowerCase();
		
		if(whichDimension== "overworld")
			Config.overWorld = dimensionState;
		else if(whichDimension == "nether")
			Config.theNether = dimensionState;
		else if(whichDimension == "the end")
			Config.theEnd = dimensionState;
		else
			throw new CommandException("That was not a dimension you area allowed to change or no argument given!\n" + TextFormatting.RED + getUsage(sender));
		

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
