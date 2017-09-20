package com.kreezcraft.spawnprotection.commands.spawnprotectSubCommands;

import java.util.List;

import com.kreezcraft.spawnprotection.Config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandRadius extends CommandBase {

	@Override
	public String getName() {
		return "radius";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return getName()+" <integer>\n"+
	           " in range 0 thru 10000";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(server!=null && 
		   !server.isSinglePlayer() && 
		   server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName())==null) {
			throw new CommandException("You are not authorized, please either be in SinglePlayer or be Opped.\n");
		}
		
		int radius;
		
		if(args.length < 1) {
			throw new CommandException("Not enough args.\n" + TextFormatting.RED + getUsage(sender));
		}
		
		try {
			radius = Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			throw new CommandException("Argument not a whole number, please try again!\n" + TextFormatting.RED + getUsage(sender));
		}
		if(radius < 0 || radius > 10000) {
			throw new CommandException("Argument given is outside the allowed range.\n" + TextFormatting.RED + getUsage(sender));
		}
		
		Config.spawnProtection = radius;
		
		//print confirmation
		sender.sendMessage(
				new TextComponentString(
						TextFormatting.AQUA + 
						"Spawn protection radius set to " + radius + ". " + 
						TextFormatting.RED + "Successfully!"
				)
		);

		Config.cfg.save();
	}

}
