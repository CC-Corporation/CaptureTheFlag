package de.flag.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.flag.main.Main;
import de.flag.utils.StringData;

public class RegisterWorldsCommand implements TabExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("ctf.registerworld")) {
				if (args.length == 3) {
					Location loc = p.getLocation();
					FileConfiguration cfg = Main.getInstance().getConfig();

					List<String> worlds = cfg.getStringList("worlds");

					for (String s : worlds) {
						StringData data = new StringData();
						data.load(s);
						if (data.getString("world").equalsIgnoreCase(loc.getWorld().getName())) {
							p.sendMessage(Main.PREFIX + "§cDie Welt '" + loc.getWorld().getName()
									+ "' wurde bereits hinzugefügt!");
							return false;
						}
					}
					StringData data = new StringData();
					data.addString("world", loc.getWorld().getName());
					String arg3;
					if(args[2].contains(":")) {
						
						String arg2 = args[2];
						arg3 = arg2.replace(":", "_");
											
					}else {
						arg3 = args[2] + "_0";
					}
					
					data.addString("name", args[0]);
					data.addString("autor", args[1]);
					data.addString("display", arg3);
					
					worlds.add(data.save());
					
					cfg.set("worlds", worlds);
					Main.getInstance().saveConfig();
					p.sendMessage(Main.PREFIX + "Du hast die Welt §a'" + loc.getWorld().getName() + "' §7hinzugefügt!");

				} else {
					p.sendMessage(Main.PREFIX + "§c/registerworld <DisplayName> <Author> <DisplayItemID>");
				}
			} else {
				p.sendMessage(Main.PREFIX + "§cDafür hast du keine Berechtigung!");
			}

		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("§b<DisplayName> <Author> <DisplayItemID>");
		return null;
	}
}
