package de.flag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.flag.main.Main;

public class PingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 0) {
				
				int playerping = ((CraftPlayer) p).getHandle().ping;
	            p.sendMessage(Main.PREFIX + "§aDein Ping: §e" +  String.valueOf(playerping) + "ms");
	            				
			}else {
				p.sendMessage("§cPlease use §4/ping§c.");
			}
		}
		return true;
	}
	
	
	
}
