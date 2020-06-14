package de.flag.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.flag.main.Main;
import de.flag.utils.StringData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("ctf.lobbyspawn") || p.hasPermission("ctf.setlocation")) {
			
		}else {
			
		}
		if (Main.b) {
			if(p.hasPermission("ctf.lobbyspawn")) {
				TextComponent lobby = new TextComponent();
				lobby.setText(Main.PREFIX + "§cDer §4Lobby Spawnpunkt §cwurde noch nicht gesetzt!");
				lobby.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/setlobbyspawn"));
				lobby.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bSetzte den Lobbyspawn!").create()));
				
				p.spigot().sendMessage(lobby);
			}
		}
		
		if(p.hasPermission("ctf.registerworld")) {
			List<String> worlds = Main.getInstance().getConfig().getStringList("worlds");
			
			if (worlds.size() == 0) {
				
				TextComponent text = new TextComponent();
				text.setText(Main.PREFIX + "§cBitte füge eine Welt hinzu! ");
				TextComponent tc = new TextComponent();
				tc.setText("§4(/registerworld)");
				tc.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/registerworld"));
				tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDu musst dich erst in die richtige Welt teleportieren!\n§b(/mv tp [name] [world])").create()));
				text.addExtra(tc);
	//			TextComponent msg = new TextComponent();
	//			msg.setText("§4(/registerworld)");
				
	//			p.sendMessage(PREFIX + "§cEs braucht mindestens eine Welt zur Ausführung des Plugins.");
				p.spigot().sendMessage(text);
				
				return;
			}
			
			StringData data = new StringData();
			data.load(worlds.get(0));
			
			
			
			HashMap<String, String> locations = new HashMap<>();
			FileConfiguration c = Main.getInstance().getConfig();
			String msg = "";
			for (String s : worlds) {	
				for (String s2 : Main.locationNames) {
					StringData data2 = new StringData();
					data2.load(s);
					if (c.contains(data2.getString("world") + s2))
						continue;
					String world = data2.getString("world");
					msg += world + ": " + s2 + " ; ";
					locations.put(world, s2);
				}
			}
			if (!msg.isEmpty()) {
				
				TextComponent txt = new TextComponent();
				txt.setText(Main.PREFIX + "§4Folgende Locations wurden noch nicht gesetzt: \n");
				for (String s : worlds) {
					StringData data2 = new StringData();
					data2.load(s);
					for (String s2 : Main.locationNames) {
						if (c.contains(data2.getString("world") + s2))
							continue;
						String world = data2.getString("world");
						Main.msg += world + ": " + s2 + " ; ";
						locations.put(world, s2);
						
											
						TextComponent w = new TextComponent();
						w.setText("§c§l" + world + ":");
						String command = "/mv tp " + p.getName() + " " + world;
						w.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
						w.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bTeleportiere dich zur Welt!").create()));
						txt.addExtra(w);
						
						TextComponent l = new TextComponent();
						l.setText(" §c§o" + s2 + "\n");
						String setLoc = "/setloc " + s2;
						l.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, setLoc));
						l.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bSetzte die Location!").create()));
						txt.addExtra(l);
					}
				}
				p.spigot().sendMessage(txt);
			}
			locations.clear();
			worlds.clear();
		}
	}
	
//	@EventHandler
//	public void onPlayerLogin(PlayerLoginEvent e) {
//		Player p = e.getPlayer();
//		if(p.hasPermission("ctf.lobbyspawn") || p.hasPermission("ctf.setlocation")) {
//			
//		}else {
//			e.disallow(Result.KICK_OTHER, "§cDieser Server muss noch eingerichtet werden.\n\n§cFrage einen Server Adminsrator, ob er dies tut!\n ");
//		}
//	}
}
