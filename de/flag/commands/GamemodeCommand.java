package de.flag.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class GamemodeCommand implements TabExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if (player.hasPermission("ctf.gm")) {
			
				if (args.length == 0) {
				
					player.sendMessage("§cPlease use /gm <0 | 1 | 2 | 3> <player>!");
								
				}else if (args.length == 1) {
				
					changeGameMode(player, args[0]);
				
				}else if (args.length == 2) {
					Player target = Bukkit.getPlayer(args[1]);
				
					if (target != null) {
						changeGameMode(target, args[1]);
					}else {
						player.sendMessage("§cThe Player " + args[1] + " is not online.");
					}
				}else {
					player.sendMessage("§cPlease use /gm <0 | 1 | 2 | 3> <player>!");
				}
			}else {
				player.sendMessage("§4You don't have got the permission to execute this command!");
			}
			
		}else {
			if (args.length == 0 || args.length == 1) {
				
				sender.sendMessage("§cPlese use /gm <0 | 1 | 2 | 3> <player>!");
								
			}else if (args.length == 2) {
				Player target = Bukkit.getPlayer(args[1]);
				
				if (target != null) {
					changeGameMode(target, args[1]);
				}else {
					sender.sendMessage("§cThe Player " + args[1] + " is not online.");
				}
			}else {
				sender.sendMessage("§cPlease use /gm <0 | 1 | 2 | 3> <player>!");
			}
		}
		
		return true;
	}
	
	public void changeGameMode(Player player, String args) {
		
		if(args.contentEquals("0")) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("1")) {
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("2")) {
			player.setGameMode(GameMode.ADVENTURE);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("3")) {
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("s")) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("c")) {
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("survival")) {
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("creative")) {
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("adventure")) {
			player.setGameMode(GameMode.ADVENTURE);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else if(args.contentEquals("spectator")) {
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage("§2Your Gamemode has been changed!");
		}else {
			player.sendMessage("§cPlease use /gm <0 | 1 | 2 | 3> <player>!");
		
		}
	}
	
	ArrayList<String> str = new ArrayList<String>();

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		str.clear();
		if(args.length == 1) {
			str.add("0");
			str.add("1");
			str.add("2");
			str.add("3");
		}else if(args.length == 2) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				str.add(p.getName());
			}
		}
		return str;
	}
	
	
	
}
