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

public class SetSpawner implements TabExecutor{

	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.setspawner")) {
				if(args.length != 0) {
						FileConfiguration c = Main.getInstance().getConfig();
						Location loc = p.getLocation();
						
						if(args[0].equalsIgnoreCase("gold")) {
							if(!c.contains(loc.getWorld().getName() + "gold")) {
								c.set(loc.getWorld().getName() + "gold", new ArrayList<Location>());
								Main.getInstance().saveConfig();						
							}
							FileConfiguration cS = Main.getInstance().getConfig();
							List<Location> locs = (List<Location>) cS.get(loc.getWorld().getName() + "gold");
							locs.add(loc);
							cS.set(loc.getWorld().getName() + "gold", locs);
							
							Main.getInstance().saveConfig();
							p.sendMessage(Main.PREFIX + "§7 Du hast einen §aGoldspawner gesetzt!");
							
						} else if(args[0].equalsIgnoreCase("iron")) {
							if(!c.contains(loc.getWorld().getName() + "iron")) {
								c.set(loc.getWorld().getName() + "iron", new ArrayList<Location>());
								Main.getInstance().saveConfig();
							}
							
							FileConfiguration cS = Main.getInstance().getConfig();
							List<Location> locs = (List<Location>) cS.get(loc.getWorld().getName() + "iron");
							locs.add(loc);
							cS.set(loc.getWorld().getName() + "iron", locs);
							
							Main.getInstance().saveConfig();
							p.sendMessage(Main.PREFIX + "§7 Du hast einen §aEisenspawner gesetzt!");
							
						} else {
							p.sendMessage("§c/setspawner <iron|gold>");
						}
					} else {
						p.sendMessage("§c/setspawner <iron|gold>");
					}				
				
			}else {
				p.sendMessage("§4You don't have ther permission to execute this command.");
			}
		}
		return true;
	}
	
	private ArrayList<String> str = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		str.clear();
		str.add("iron");
		str.add("gold");
		return str;
	} 

}
