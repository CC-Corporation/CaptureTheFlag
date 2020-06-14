package de.flag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flag.game.Game;
import de.flag.main.Main;

public class Cancel implements CommandExecutor {
	
	public static boolean cancel;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.cancel")) {
				
				if (args.length == 0) {
					if (cancel) {
						
						cancel = false;
						Game.tryToStartGame();
						
					}else {
						
						Main.setCounter(60);
						cancel = true;
						
					}
				}
				
			}else {
				p.sendMessage("§4You don't have the permission to execute this command.");
			}
		}
		
		
		return true;
	}

}
