package de.flag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.flag.main.Main;

public class SetLobbySpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.setlobby")) {
				FileConfiguration c = Main.getInstance().getConfig();
				c.set("lobby", p.getLocation());
				Main.getInstance().saveConfig();
				
				p.sendMessage(Main.PREFIX + "Du hast den §aSpawnpunkt §7erfolgreich gesetzt!");
			} else {
				p.sendMessage(Main.PREFIX + "§cDafür hast du keine Berechtigung!");
			}
		}
		return true;
	}
	
}
