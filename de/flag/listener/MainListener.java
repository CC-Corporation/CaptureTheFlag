package de.flag.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import de.flag.game.Game;
import de.flag.game.ScoreboardManagement;
import de.flag.main.Main;
import de.flag.utils.ItemUtils;

public class MainListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SURVIVAL && !Main.gameRunning)
			e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setFireTicks(0);
		p.teleport((Location) Main.getInstance().getConfig().get("lobby"));
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setLevel(0);
		p.setExp(0);
		p.getInventory().clear();
		ItemUtils.clearArmorSlots(p);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}

		ItemStack team = new ItemStack(Material.BED);
		ItemMeta meta = team.getItemMeta();
		meta.setDisplayName("§6Teamauswahl");
		team.setItemMeta(meta);
		ItemStack voting = ItemUtils.getItemWithName(Material.PAPER, "§6Map Voting");
		
		p.getInventory().setItem(0,team);
		p.getInventory().setItem(1,voting);
		p.getInventory().setItem(8, ItemUtils.getBook("§4Anleitung", "§4Anleitung", "§aDr__Dirty & CPU", "§6§lSpiel\n\n§r§0Es gibt zwei Teams: §cROT §0und §9BLAU§0. Diese versuchen jeweils die gegnerische Flagge zu ihrer Eigenen zu bringen und somit einen Punkt zu erziehlen. Das Team, das zu erst drei Punkte hat gewinnt.", "§6§lShop\n\n§r§0Den Shop öffnet man, indem man einen Dropper rechtsclickt. Danach erscheint das Shop-Inventar, in dem man mit den Coins, die im Scoreboard angezeigt werden, Items kaufen kann. Coins bekommt man, wenn man Eisen- oder Goldbarren aufsammelt.", "§6§lLobbyfase\n\n§r§0Nach dem Joinen gelangt man in die Lobby. Hier hat man die Möglichkeit mit dem Bett-Item sein Team zu wechseln oder am Map Voting teilzunehmen. Das Map Voting endet zehn Sekunden bevor die Runde startet."));
		p.updateInventory();
		
		e.setJoinMessage(Main.PREFIX + "§a" + p.getName() + " §7will mitspielen!");
		
		Team blue = ScoreboardManagement.blue;
		Team red = ScoreboardManagement.red;
		
		if(red.getSize() > blue.getSize()) {
			
			if(p.getName().equals("Red_Head_CPU")) {
				OfflinePlayer player = (OfflinePlayer) red.getPlayers().toArray()[red.getSize()-1];
				blue.addPlayer(player);
				((Player) player).setDisplayName("§9" + player.getName() + "§7");
				((Player) player).setPlayerListName("§9" + player.getName());
				red.addPlayer(p);
				p.setDisplayName("§c" + p.getName() + "§7");
				p.setPlayerListName("§c" + p.getName());
				
			}else {
				blue.addPlayer(p);
				p.setDisplayName("§9" + p.getName() + "§7");
				p.setPlayerListName("§9" + p.getName());
			}
			
		} else {
		
			red.addPlayer(p);
			p.setDisplayName("§c" + p.getName() + "§7");
			p.setPlayerListName("§c" + p.getName());
			
		}
		ScoreboardManagement.updatePlayer(p);
			
		Game.tryToStartGame();
		
	}
	@EventHandler
	public void preJoin(PlayerLoginEvent e) {
		if(Main.gameRunning || Main.endingstate) {
			e.disallow(Result.KICK_OTHER, "§6Gerade läuft eine Runde!");
		} else if(Bukkit.getOnlinePlayers().size() >= Main.getInstance().getConfig().getInt("maxPlayers")) 
			e.disallow(Result.KICK_FULL, "§cDer Server ist voll!");
				
	}

	@EventHandler
	public void onHungerDeplete(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			p.setSaturation(20);
			p.setFoodLevel(20);
			p.updateInventory();
		}
		e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		ScoreboardManagement.blue.removePlayer(p);
		ScoreboardManagement.red.removePlayer(p);
		
		e.setQuitMessage(Main.PREFIX + "§a" + p.getName() + " §7will nicht mehr mitspielen!");
		if(Main.gameRunning) {
			if(ScoreboardManagement.blue.getPlayers().size() == 0) {
				Game.endingState("red");
			} else if(ScoreboardManagement.red.getPlayers().size() == 0) {
				Game.endingState("blue");
			}
		}

			
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!Main.gameRunning && p.getGameMode() == GameMode.SURVIVAL) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!Main.gameRunning && e.getEntityType() == EntityType.PLAYER) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(!Main.gameRunning && p.getGameMode() == GameMode.SURVIVAL) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onMessageSend(AsyncPlayerChatEvent e) {
		e.setFormat("§8<<§7" + e.getPlayer().getPlayerListName() + "§8>>§7 " + e.getMessage());
	}
	public static HashMap<Player, Integer> getKillCounter() {
		return killCounter;
	}
	
	
	public static HashMap<Player, Integer> killCounter = new HashMap<>();
	
	
	public static void setKillCounter(HashMap<Player, Integer> killCounter) {
		MainListener.killCounter = killCounter;
	}
	
	public static int getPlayerKills(Player p) {
		if(killCounter.containsKey(p)) {
			return killCounter.get(p);
		} else {
			killCounter.put(p, 0);
			return 0; 
		}
		 
	}
	
	@EventHandler
	public void onPlayerGetsXP(PlayerExpChangeEvent e) {
		e.setAmount(0);
	}
	
}
