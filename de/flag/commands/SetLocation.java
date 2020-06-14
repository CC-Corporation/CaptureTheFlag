package de.flag.commands;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.flag.main.Main;

public class SetLocation implements TabExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.setlocation")) {
				if(args.length > 0) {
					FileConfiguration cfg = Main.getInstance().getConfig();
					Location player = p.getLocation();
					Location loc = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw(), 0);
					cfg.set(p.getLocation().getWorld().getName() + args[0], loc);
					Main.getInstance().saveConfig();
					p.sendMessage(Main.PREFIX + "Location §a" + args[0] + "§7 wurde bei deiner Position gesetzt!");
				} else {
					p.sendMessage("§c/setloc <name>");
				}
			} else {
				p.sendMessage(Main.PREFIX + "§cDafür hast du keine Berechtigung!");
			}
		}
		return true;
	}
	
	private ArrayList<String> str = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		str.clear();
		for (String name : Main.locationNames) {
			str.add(name);
		}
		return str;
	}

}
