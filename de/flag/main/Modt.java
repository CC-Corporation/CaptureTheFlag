package de.flag.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Modt implements Listener {
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		
		FileConfiguration cfg = Main.getInstance().getConfig();
		int maxPlayers = cfg.getInt("maxPlayers");
		e.setMaxPlayers(maxPlayers);
		e.setMotd(Main.getModt());
		
	}
		
}
