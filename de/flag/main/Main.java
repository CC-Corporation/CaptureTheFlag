package de.flag.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.flag.commands.Cancel;
import de.flag.commands.GamemodeCommand;
import de.flag.commands.PingCommand;
import de.flag.commands.RegisterWorldsCommand;
import de.flag.commands.ResetMap;
import de.flag.commands.SeeDeathsCommand;
import de.flag.commands.SelectMap;
import de.flag.commands.SetCoinsCommand;
import de.flag.commands.SetLobbySpawn;
import de.flag.commands.SetLocation;
import de.flag.commands.SetMaxPlayers;
import de.flag.commands.SetPointsCommand;
import de.flag.commands.SetSpawner;
import de.flag.commands.StartCommand;
import de.flag.game.Game;
import de.flag.game.MapVoting;
import de.flag.game.ScoreboardManagement;
import de.flag.listener.ItemClickListener;
import de.flag.listener.MainListener;
import de.flag.listener.PlayerJoin;
import de.flag.listener.PotionDrink;
import de.flag.listener.Weather;
import de.flag.shop.ShopListener;
import de.flag.utils.StringData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {

	public Main() {
		instance = this;
	}
	
	private static final int MAX_PLAYERS = 4;
	private static final int MIN_PLAYERS = 2;
	private static final String MODT = "§4§l                       Capture the Flag §7[§e1.8§7]\n§r §7§kgg§r                          §aby Dr__Dirty & CPU                          §7§kgg§r ";
	public static final String PREFIX = "§6§lGame§r§e » §7";
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		gameRunning = false;
		endingstate = false;

		getCommand("setloc").setExecutor(new SetLocation()); // loads only this command to register missing Locations!
		getCommand("registerworld").setExecutor(new RegisterWorldsCommand());
		getCommand("setlobbyspawn").setExecutor(new SetLobbySpawn());
		getCommand("gm").setExecutor(new GamemodeCommand());
		
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		
		createDefaultConfig();

		List<String> worlds = getConfig().getStringList("worlds");
		if (worlds.size() == 0) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("ctf.registerworld")) {
					TextComponent text = new TextComponent();
					text.setText(PREFIX + "§cBitte füge eine Welt hinzu! ");
					TextComponent tc = new TextComponent();
					tc.setText("§4(/registerworld)");
					tc.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/registerworld"));
					tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDu musst dich erst in die richtige Welt teleportieren!\n§b(/mv tp [name] [world])").create()));
					text.addExtra(tc);

					p.spigot().sendMessage(text);
				}
			}
			
			return;
		}

		StringData data = new StringData();
		data.load(worlds.get(0));
		
		currentworld = data.getString("world");
		worlddata = data;
		
		
		
		
		HashMap<String, String> locations = new HashMap<>();
		FileConfiguration c = getConfig();
		
		
		if (!c.contains("lobby")) {
			b = true;
		}
		
		
		
		if (b) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("ctf.setlobby")) {
	//				p.sendMessage(PREFIX + "§cDer Lobby Spawnpunkt wurde noch nicht gesetzt!");
					
					TextComponent lobby = new TextComponent();
					lobby.setText(PREFIX + "§cDer §4Lobby Spawnpunkt §cwurde noch nicht gesetzt!");
					lobby.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/setlobbyspawn"));
					lobby.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bSetzte den Lobbyspawn!").create()));
					
					p.spigot().sendMessage(lobby);
				}
				
			}
			return;
		}
				
		TextComponent txt = new TextComponent();
		txt.setText(PREFIX + "§4Folgende Locations wurden noch nicht gesetzt: \n");
		for(Player p : Bukkit.getOnlinePlayers()) {
			for (String s : worlds) {
				StringData data2 = new StringData();
				data2.load(s);
				for (String s2 : locationNames) {
					if (c.contains(data2.getString("world") + s2))
						continue;
					String world = data2.getString("world");
					msg += world + ": " + s2 + " ; ";
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
			if(!msg.isEmpty())
				if(p.hasPermission("ctf.setlocation"))
					p.spigot().sendMessage(txt);
			if (b) {
				if(p.hasPermission("ctf.setlobby")) {
					TextComponent lS = new TextComponent();
					lS.setText(PREFIX + "§cDer §4Lobby Spawnpunkt §cwurde noch nicht gesetzt!");
					lS.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/setlobbyspawn"));
					lS.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDu musst dich erst in die richtige Welt teleportieren!\\n§b(/mv tp [name] [world])").create()));
					
					p.spigot().sendMessage(lS);
				}
			}
		}
	

		
		// continue if all Locations are set
		ScoreboardManagement.start();

		// LISTENER
		getServer().getPluginManager().registerEvents(new MainListener(), this);
		getServer().getPluginManager().registerEvents(new ItemClickListener(), this);
		getServer().getPluginManager().registerEvents(new Game(), this);
		getServer().getPluginManager().registerEvents(new ShopListener(), this);
		getServer().getPluginManager().registerEvents(new SelectMap(), this);
		getServer().getPluginManager().registerEvents(new Weather(), this);
		getServer().getPluginManager().registerEvents(new MapVoting(), this);
		getServer().getPluginManager().registerEvents(new Modt(), this);
		getServer().getPluginManager().registerEvents(new PotionDrink(), this);

		// COMMANDS
		getCommand("map").setExecutor(new SelectMap());
		getCommand("start").setExecutor(new StartCommand());
		getCommand("resetmap").setExecutor(new ResetMap());
		getCommand("setspawner").setExecutor(new SetSpawner());
		getCommand("setcoins").setExecutor(new SetCoinsCommand());
		getCommand("setpoints").setExecutor(new SetPointsCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("cancel").setExecutor(new Cancel());
		getCommand("setmaxplayers").setExecutor(new SetMaxPlayers());
		getCommand("deaths").setExecutor(new SeeDeathsCommand());
				
	}

	public Location getLocation(String name) {
		return (Location) getConfig().get(currentworld + name);
	}
	
	public static String msg = "";
	public static boolean b = false;
	
	public static String currentworld;
	public static StringData worlddata = new StringData();

	public static String[] locationNames = new String[] { "spawn_red", "spawn_blue", "flag_red", "flag_blue" };

	public static boolean gameRunning = false;
	public static boolean endingstate = false;

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public void createDefaultConfig() {
		FileConfiguration cfg = getInstance().getConfig();
		if (cfg.contains("worlds")) {
			return;
		}
		cfg.set("maxPlayers", MAX_PLAYERS);
		cfg.set("minPlayers", MIN_PLAYERS);
		cfg.set("teamSize", MAX_PLAYERS/2);
		cfg.set("worlds", new ArrayList<String>());
		
		saveConfig();
		
	}
	private static int startCounter = 60;
	
	public static int getCounter() {
		return startCounter;
	}
	public static void setCounter(int value) {
		startCounter = value;
	}
	
	public static String getModt() {
		return MODT;
	}
}
