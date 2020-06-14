package de.flag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flag.game.Game;
import de.flag.game.ScoreboardManagement;

public class SetPointsCommand implements CommandExecutor{
	//   /setpoints <red | blue> <number>
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.setpoints")) {
				if (args.length == 2) {
					
					if(args[0].equalsIgnoreCase("red")) {
						
						try {
							
							int points = Integer.parseInt(args[1]);
							if(points >= 0 && points <= 3) {
								Game.points_red = points;
								p.sendMessage("§7Team §cRot §7hat nun §6" + points + " §7 Punkte.");
							}else {
								p.sendMessage("§cPlease specify a valid number!");
							}
													
						
						}catch(Exception e) {
							p.sendMessage("§cPlease specify a valid number!");
						}
						
					}else if(args[0].equalsIgnoreCase("blue")) {
						
						try {
							
							int points = Integer.parseInt(args[1]);
							if(points >= 0 && points <= 3) {
								Game.points_blue = points;
								p.sendMessage("§7Team §9Blau §7hat nun §6" + points + " §7 Punkte.");
							}else {
								p.sendMessage("§cPlease specify a valid number!");
							}
						
						}catch(Exception e) {
							p.sendMessage("§cPlease specify a valid number!");
						}
						
					}else {
						p.sendMessage("§cPlease specify a valid Team!");
					}
					
					if (Game.points_red >= 3)
						Game.endingState("red");
					if (Game.points_blue >= 3)
						Game.endingState("blue");
					for (Player player : Bukkit.getOnlinePlayers()) {
						ScoreboardManagement.updatePlayer(player);
					}
				}
			}else {
				p.sendMessage("§4You don't have the permission to execute this command.");
			}
		}
		
		return true;
	}

}
