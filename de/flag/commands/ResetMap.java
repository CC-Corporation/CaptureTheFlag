package de.flag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flag.game.Game;
import de.flag.main.Main;

public class ResetMap implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("ctf.resetmap")) {
				Game.resetMap(false);
				p.sendMessage(Main.PREFIX + "Die Map wurde resetet!");
			} else {
				p.sendMessage(Main.PREFIX + "§cDafür hast du keine Berechtigung!");
			}
		}
		return true;
	}

}
