package de.flag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flag.game.Game;
import de.flag.main.Main;

public class SeeDeathsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Main.gameRunning) {
				if(args.length == 0) {
					int deaths = Game.getPlayerDeaths(p);
					
					if(deaths >= 0) {
						p.sendMessage(Main.PREFIX + "Du bist §6noch nie §7 in dieser Runde gestorben.");
					}else if (deaths == 1) {
						p.sendMessage(Main.PREFIX + "Du bist §6einmal §7 in dieser Runde gestorben.");
					}else {
						p.sendMessage(Main.PREFIX + "Du bist §6" + Game.getPlayerDeaths(p) + "-mal §7in dieser Runde gestorben.");
					}
				}else {
					p.sendMessage("§cPlease use /deaths");
				}
			}else {
				p.sendMessage("§cDieser Befehl kann nur ausgeführt werden, wenn das Spiel läuft.");
			}
			
		}
		
		return true;
	}
	
	
	
}
