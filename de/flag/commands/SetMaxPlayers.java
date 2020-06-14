package de.flag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.flag.main.Main;

public class SetMaxPlayers implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.maxplayers")) {
				
				if(args.length == 1) {
					
					try {
						int players = Integer.parseInt(args[0]);
						double test = (double) players/2;
						int test2 = players/2;
												
						if(test2 == test && players >= 2) {
							
							FileConfiguration cfg = Main.getInstance().getConfig();
							cfg.set("maxPlayers", players);
							cfg.set("teamSize", test2);
							Main.getInstance().saveConfig();
							
							p.sendMessage(Main.PREFIX + "§aDu hast die maximale Spieleranzahl erfolgreich verändert.");
							
						}else {
							p.sendMessage("§cPlease specify a valid number!");
						}
						
						
					}catch(Exception e) {
						p.sendMessage("§cPlease specify a valid number!");
					}
					
				}else {
					p.sendMessage("§cPlease use /setmaxplayers <number>!");
				}
				
			}else {
				p.sendMessage("§4You don't have the permission to execute this command.");
			}
			
		}
		
		return true;
	}
	
	
	
}
