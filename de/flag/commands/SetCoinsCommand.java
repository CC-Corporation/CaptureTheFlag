package de.flag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flag.game.ScoreboardManagement;
import de.flag.shop.Coins;

public class SetCoinsCommand implements CommandExecutor {
	
	//   /setcoins <player> <number>
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.setcoins")) {
				
				if(args.length == 1) {
					
					try {
						
						int coins = Integer.parseInt(args[0]);
						if(coins > 0 || coins < 1000000000) {
							
							Coins.changeCoins(p, coins - Coins.getCoins(p));
							ScoreboardManagement.updatePlayer(p);
						
						}else {
							p.sendMessage("§cPlease specify a valid number!");
						}
					
					}catch(Exception e) {
						p.sendMessage("§cPlease specify a valid number!");
					}
					
				}else if (args.length == 2) {
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null) {
						int coins = Integer.parseInt(args[1]);
						if(coins >= 0 || coins <= 1000000000) {
							
							Coins.changeCoins(target, coins - Coins.getCoins(p));
							ScoreboardManagement.updatePlayer(target);
						}else {
							p.sendMessage("§cPlease specify a valid number!");
						}
					}else {
						p.sendMessage("§cThe Player §6" + args[1] + " §c is not online.");
					}
					
				}else {
					
				}
				
			}else {
				p.sendMessage("§4You don't have the permission to execute this command.");
			}
		}
		
		return true;
	}

}
