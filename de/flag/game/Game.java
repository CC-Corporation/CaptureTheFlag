package de.flag.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import de.flag.commands.Cancel;
import de.flag.commands.SelectMap;
import de.flag.commands.StartCommand;
import de.flag.listener.MainListener;
import de.flag.main.Main;
import de.flag.shop.Coins;
import de.flag.utils.Counter;
import de.flag.utils.CounterAction;
import de.flag.utils.ItemUtils;
import de.flag.utils.ParticleUtils;
import de.flag.utils.Time;
import de.flag.utils.TitleAPI;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Game implements Listener {
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void start() {
		SelectMap.mapUsed = false;
		FileConfiguration c = Main.getInstance().getConfig();

		ironlist = (ArrayList<Location>) c.get(Main.currentworld + "iron");
		goldlist = (ArrayList<Location>) c.get(Main.currentworld + "gold");

		points_red = 0;
		points_blue = 0;

		for (Entity e : Bukkit.getWorld(Main.currentworld).getEntities()) {
			if (e instanceof Item || e instanceof Projectile) {
				e.remove();
			}
		}
		
		spawnLocations.clear();
		
		Team red = ScoreboardManagement.red;
		Team blue = ScoreboardManagement.blue;

		redFlag = new Flag(Main.getInstance().getLocation("flag_red"));
		blueFlag = new Flag(Main.getInstance().getLocation("flag_blue"));

		redFlag.color = 1;
		redFlag.dyecolor = DyeColor.RED;

		blueFlag.color = 4;
		blueFlag.dyecolor = DyeColor.BLUE;

		redFlag.returned();
		blueFlag.returned();

		for (OfflinePlayer op : red.getPlayers()) {
			if (!op.isOnline())
				continue;
			Player p = (Player) op;
			p.teleport(Main.getInstance().getLocation("spawn_red"));
			p.getInventory().clear();
			Coins.registerPlayer(p);
			addItems(p);
			p.setPlayerListName("§c" + p.getName());
//			p.setCustomName("§c" + p.getName());
//			p.setCustomNameVisible(true);
			p.setGameMode(GameMode.SURVIVAL);
			
		}
		for (OfflinePlayer op : blue.getPlayers()) {
			if (!op.isOnline())
				continue;
			Player p = (Player) op;
			p.teleport(Main.getInstance().getLocation("spawn_blue"));
			p.getInventory().clear();
			Coins.registerPlayer(p);
			addItems(p);
			p.setPlayerListName("§9" + p.getName());
//			p.setCustomName("§9" + p.getName());
//			p.setCustomNameVisible(true);
			p.setGameMode(GameMode.SURVIVAL);
		
		}
		System.out.println(Main.currentworld);
		Location sR = new Location(Bukkit.getWorld(Main.currentworld), (double)pl.getLocation(Main.locationNames[0]).getBlockX(), (double)(pl.getLocation(Main.locationNames[0]).getBlockY() + 1), (double)pl.getLocation(Main.locationNames[0]).getBlockZ());
        Location sB = new Location(Bukkit.getWorld(Main.currentworld), (double)pl.getLocation(Main.locationNames[1]).getBlockX(), (double)(pl.getLocation(Main.locationNames[1]).getBlockY() + 1), (double)pl.getLocation(Main.locationNames[1]).getBlockZ());
        spawnLocations.add(pl.getLocation(Main.locationNames[0]));
        spawnLocations.add(sR);
        spawnLocations.add(pl.getLocation(Main.locationNames[1]));
        spawnLocations.add(sB);
        
        Main.gameRunning = true;
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (ScoreboardManagement.blue.hasPlayer(p) || ScoreboardManagement.red.hasPlayer(p)) {
				ScoreboardManagement.updatePlayer(p);
			}
		}
		        
		timerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new GameRunnable(), 30, 30);
		time();
	}

	public static int timerID = 0;
	
	static ArrayList<Location> spawnLocations = new ArrayList<Location>();
	static Main pl = Main.getInstance();
	
	public static Flag redFlag;
	public static Flag blueFlag;

	public static ArrayList<Location> ironlist = new ArrayList<Location>();
	public static ArrayList<Location> goldlist = new ArrayList<Location>();

	@SuppressWarnings("deprecation")
	private static void addItems(Player p) {
		ItemUtils.clearArmorSlots(p);
		p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		p.getInventory().setLeggings(ItemUtils.getColoredItemStack(new ItemStack(Material.LEATHER_LEGGINGS),
				(ScoreboardManagement.blue.hasPlayer(p) ? Color.fromRGB(0, 0, 255) : Color.fromRGB(255, 0, 0))));
		p.getInventory().setBoots(ItemUtils.getColoredItemStack(new ItemStack(Material.LEATHER_BOOTS),
				(ScoreboardManagement.blue.hasPlayer(p) ? Color.fromRGB(0, 0, 255) : Color.fromRGB(255, 0, 0))));

		p.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
		p.getInventory().setItem(1, new ItemStack(Material.STONE_PICKAXE));
	}

	@SuppressWarnings("deprecation")
	public static void respawnPlayer(Player p, boolean counter, String msg) {
		if (counter) {
			
			if(msg == null || msg == "" || msg.isEmpty())
				Bukkit.broadcastMessage(Main.PREFIX + p.getDisplayName() + "§7 ist gestorben!");
			else
				Bukkit.broadcastMessage(Main.PREFIX + msg);
			lostPlayer(p);
		}
		double coins2 = (double) Coins.getCoins(p) /2;
		double halfCoins = Math.ceil(coins2);
		Coins.changeCoins(p, (int) halfCoins - Coins.getCoins(p));
		
		ScoreboardManagement.updatePlayer(p);
		
		Team red = ScoreboardManagement.red;
		Team blue = ScoreboardManagement.blue;

		p.setGameMode(GameMode.SPECTATOR);
		for (PotionEffect e : p.getActivePotionEffects()) {
			p.removePotionEffect(e.getType());
		}
		if (counter) {
			new Counter(4, 20, new CounterAction() {

				@Override
				public void run(int count) {
					if (!Main.gameRunning)
						return;
					switch (count) {
					case 6:
						TitleAPI.sendTitel(p, "§7Respawn in §a5", "", 5, 13, 2);
						break;
					case 5:
						TitleAPI.sendTitel(p, "§7Respawn in §a4", "", 5, 13, 2);
						break;
					case 4:
						TitleAPI.sendTitel(p, "§7Respawn in §a3", "", 5, 13, 2);
						break;
					case 3:
						TitleAPI.sendTitel(p, "§7Respawn in §a2", "", 5, 13, 2);
						break;
					case 2:
						TitleAPI.sendTitel(p, "§7Respawn in §a1", "", 5, 13, 2);
						break;
					case 1:
						if (Main.gameRunning) {
							if (red.hasPlayer(p)) {
								p.teleport(Main.getInstance().getLocation("spawn_red"));
							} else if (blue.hasPlayer(p)) {
								p.teleport(Main.getInstance().getLocation("spawn_blue"));
							}
						}
						
						p.setFireTicks(0);
						p.setHealth(20);
						p.getInventory().clear();
						p.setExp(0);
						addItems(p);
						p.setGameMode(GameMode.SURVIVAL);
						break;
					}

				}
			});
		} else {
			if (red.hasPlayer(p)) {
				p.teleport(Main.getInstance().getLocation("spawn_red"));
			} else if (blue.hasPlayer(p)) {
				p.teleport(Main.getInstance().getLocation("spawn_blue"));
			}
			p.setFireTicks(0);
			p.setHealth(20);
			p.getInventory().clear();
			addItems(p);
			p.setGameMode(GameMode.SURVIVAL);
		}

	}

	private static ArrayList<Location> locs = new ArrayList<Location>();

	public static void resetMap(boolean respawn) {
		for (Location loc : locs) {
			loc.getWorld().getBlockAt(loc).setType(Material.AIR);
		}
		if (respawn) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				respawnPlayer(p, false, null);
				Coins.changeCoins(p, -100000000);
			}
		}

		for (Entity e : Bukkit.getWorld(Main.currentworld).getEntities()) {
			if(e instanceof Item || e instanceof Projectile)
			e.remove();
			
		}
		redFlag.returned();
		blueFlag.returned();
		mine.clear();
		locs.clear();
		ScoreboardManagement.updatePlayers();
	}

	public static void endingState(String team) {
		Main.gameRunning = false;
		Main.endingstate = true;
		Bukkit.getScheduler().cancelTask(timerID);
		Bukkit.getScheduler().cancelTask(timeID);
		resetMap(true);
		Main.setCounter(60);
		spawnLocations.clear();
		
		String s = "";
		if (team.equalsIgnoreCase("red")) {
			s = "§7Team §cRot §7hat das Spiel gewonnen!";
		} else if (team.equalsIgnoreCase("blue")) {
			s = "§7Team §9Blau §7hat das Spiel gewonnen!";
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setGameMode(GameMode.SURVIVAL);
			p.teleport((Location) Main.getInstance().getConfig().get("lobby"));
			TitleAPI.sendTitel(p, s, "", 10, 60, 30);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 2f, 1f);
            p.sendMessage(" \n" + Main.PREFIX + "§7Runden Statistiken:\n ");
            p.sendMessage("   §7Dauer: §a" + Time.toTime(getTime()));
            p.sendMessage("   §7Kills: §a" + MainListener.getPlayerKills(p));
            p.sendMessage("   §7Deaths: §a" + getPlayerDeaths(p));
            if (getPlayerDeaths(p) == 0) {
                p.sendMessage("   §7K/D: §azu gut, um sie hier anzuzeigen\n ");
            }else {
                double kD1 = MainListener.getPlayerKills(p) / (double)getPlayerDeaths(p) * 10.0;
                int kD2 = (int)kD1;
                double kD3 = kD2 / 10.0;
                if (kD3 == (int)kD3) {
                    p.sendMessage("   §7K/D: §a" + (int)kD3 + "\n ");
                }else {
                    p.sendMessage("   §7K/D: §a" + kD3 + "\n ");
                }
            }
		}
		deathCounter.clear();
		MainListener.getKillCounter().clear();
		
		new Counter(16, 20, new CounterAction() {

			@Override
			public void run(int count) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if((count-1) == 10 || (count-1) <= 5) {
						 p.playSound(p.getLocation(), Sound.NOTE_BASS, 2.0f, 1.0f);
						p.sendMessage(Main.PREFIX + "Der Server restartet in §a" + (count - 1) + " §7Sekunden!");
					}else if ((count-1) == 15) {
						p.sendMessage(Main.PREFIX + "Der Server restartet in §a" + (count - 1) + " §7Sekunden!");
					}
				}
				if (count <= 1) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.kickPlayer("§aDer Server restartet!");
					}
					Main.endingstate = false;
					
				}

			}
		});

	}

	public static void lostPlayer(Player p) {
		if (!Main.gameRunning)
			return;
		if (blueFlag.holder != null && blueFlag.holder.getName().equalsIgnoreCase(p.getName())) {
			blueFlag.returned();
			Bukkit.broadcastMessage(Main.PREFIX + "§7Die §9blaue §7Flagge wurde zurückgesetzt!");
		} else if (redFlag.holder != null && redFlag.holder.getName().equalsIgnoreCase(p.getName())) {
			redFlag.returned();
			Bukkit.broadcastMessage(Main.PREFIX + "§7Die §crote §7Flagge wurde zurückgesetzt!");
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		lostPlayer(p);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e) {
		Team red = ScoreboardManagement.red;
		Team blue = ScoreboardManagement.blue;
		Player p = e.getPlayer();

		if (Main.gameRunning) {
			if (p.getGameMode() != GameMode.SURVIVAL)
				return;
			if ((e.getTo().getX() != e.getFrom().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) && teleportingTeams.containsKey(getTeam(p)) && teleportingTeams.get(getTeam(p)) == p) {
                teleportingTeams.remove(getTeam(p));
            }
			if (red.hasPlayer(p)) {
				if (blueFlag.loc.distance(p.getLocation()) < 0.6 && blueFlag.isHome) {
					blueFlag.catched(p);
					Bukkit.broadcastMessage(Main.PREFIX + "§c" + p.getName() + "§7 hat die §9blaue §7Flagge!");
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 2f, 1f);
				} else if (redFlag.loc.distance(p.getLocation()) < 6 && redFlag.isHome && p == blueFlag.holder) {
					points_red += 1;
					if (points_red >= 3)
						endingState("red");
					else {
						resetMap(true);
						for(Player p2 : Bukkit.getOnlinePlayers()) {
							p2.playSound(p2.getLocation(), Sound.WITHER_DEATH, 3f, 1f);
							TitleAPI.sendTitel(p2, "§7Team §cRot §7hat einen Punkt gemacht", "§8Die nächste Runde beginnt", 4, 40, 14);
						}						
					}

					ScoreboardManagement.updatePlayers();
				}

			} else if (blue.hasPlayer(p)) {
				if (redFlag.loc.distance(p.getLocation()) < 0.6 && redFlag.isHome) {
					redFlag.catched(p);
					Bukkit.broadcastMessage(Main.PREFIX + "§9" + p.getName() + "§7 hat die §crote §7Flagge!");
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 2f, 1f);
				} else if (blueFlag.loc.distance(p.getLocation()) < 6 && blueFlag.isHome && p == redFlag.holder) {
					
					points_blue += 1;
					if (points_blue >= 3)
						endingState("blue");
					else {
						resetMap(true);
						for(Player p2 : Bukkit.getOnlinePlayers()) {
							p2.playSound(p2.getLocation(), Sound.WITHER_DEATH, 3f, 1f);
							TitleAPI.sendTitel(p2, "§7Team §9Blau §7hat einen Punkt gemacht", "§8Die nächste Runde beginnt", 4, 16, 14);
						}
					}

					ScoreboardManagement.updatePlayers();
				}
			}
		}
		if ((p.getLocation().getBlock().getType() == Material.LAVA
				|| p.getLocation().getBlock().getType() == Material.STATIONARY_LAVA)
				&& p.getGameMode() == GameMode.SURVIVAL) {
			int deaths = getPlayerDeaths(p);
            deathCounter.put(p, deaths + 1);
			respawnPlayer(p, true, p.getDisplayName() + " ist in Lava gefallen!");
		} else if (p.getLocation().getBlock().getType() == Material.GOLD_PLATE
				&& p.getGameMode() == GameMode.SURVIVAL) {
			p.setVelocity(p.getLocation().getDirection().multiply(3).setY(1));
		} else if (p.getLocation().getBlock().getType() == Material.STONE_PLATE) {
			if (mine.containsKey(p.getLocation().getBlock().getLocation())) {
				String s = mine.get(p.getLocation().getBlock().getLocation());
				if ((s.equalsIgnoreCase("red") && blue.hasPlayer(p))
						|| (s.equalsIgnoreCase("blue") && red.hasPlayer(p))) {
					p.getLocation().getWorld().getBlockAt(p.getLocation()).setType(Material.AIR);
					mine.remove(p.getLocation().getBlock().getLocation());
					p.getLocation().getWorld().playSound(p.getLocation(), Sound.EXPLODE, 3f, 0.5f);
					p.getLocation().getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 1, 500);
					int deaths = getPlayerDeaths(p);
	                deathCounter.put(p, deaths + 1);
					respawnPlayer(p, true, p.getDisplayName() + " §7 wurde von einer Mine in die Luft gesprengt!");
				}
			}
		}

		if (p.getLocation().getY() < 1 && p.getGameMode() == GameMode.SURVIVAL && Main.gameRunning) {
			int deaths = getPlayerDeaths(p);
            deathCounter.put(p, deaths + 1);
			respawnPlayer(p, true, p.getDisplayName() + " §7ist ins Void gefallen!");
			if(p.getKiller() != null) {
				Player killer = p.getKiller();
				int kills = MainListener.getKillCounter().get(killer);
				MainListener.getKillCounter().put(killer, kills + 1);
				ScoreboardManagement.updatePlayer(killer);
				
			}
		}
	}

	public static HashMap<Location, String> mine = new HashMap<Location, String>();

	public static int points_red, points_blue;

	@EventHandler
	public void damage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();

		if (p.getGameMode() == GameMode.SURVIVAL && Main.gameRunning)
			if (p.getHealth() - e.getFinalDamage() <= 0) {
				e.setCancelled(true);
				p.getLocation().getWorld().playSound(p.getLocation(), Sound.ZOMBIE_WOODBREAK, 3f, 1f);
				p.getLocation().getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
				if(e.getCause() == DamageCause.ENTITY_ATTACK && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
					Player killer = (Player) ((EntityDamageByEntityEvent) e).getDamager();
								
					int kills = MainListener.getPlayerKills(killer);
					MainListener.killCounter.put(killer, kills + 1);
					ScoreboardManagement.updatePlayer(killer);
					
					int deaths = getPlayerDeaths(p);
	                deathCounter.put(p, deaths + 1);
					respawnPlayer(p, true, p.getDisplayName() + " §7wurde von " + killer.getDisplayName() + " getötet!");
				
				}else if(e.getCause() == DamageCause.PROJECTILE && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
					
					Projectile entity = (Projectile) ((EntityDamageByEntityEvent) e).getDamager();
					Player killer = (Player) entity.getShooter();
								
					int kills = MainListener.getPlayerKills(killer);
					MainListener.killCounter.put(killer, kills + 1);
					ScoreboardManagement.updatePlayer(killer);
					
					int deaths = getPlayerDeaths(p);
	                deathCounter.put(p, deaths + 1);
					respawnPlayer(p, true, p.getDisplayName() + " §7wurde von " + killer.getDisplayName() + " getötet!");
				} else {
					int deaths = getPlayerDeaths(p);
	                deathCounter.put(p, deaths + 1);
					respawnPlayer(p, true, null);
				}
				
			}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void DamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player player = (Player) e.getEntity();
			Player killer = (Player) e.getDamager();
						
			if(ScoreboardManagement.blue.hasPlayer(player) && ScoreboardManagement.blue.hasPlayer(killer)) {
				e.setCancelled(true);
			} else if(ScoreboardManagement.red.hasPlayer(player) && ScoreboardManagement.red.hasPlayer(killer)) {
				e.setCancelled(true);
			}
		}else if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
			Player player = (Player) e.getEntity();
			Player killer = (Player) ((Projectile)e.getDamager()).getShooter();
			if(ScoreboardManagement.blue.hasPlayer(player) && ScoreboardManagement.blue.hasPlayer(killer)) {
				e.setCancelled(true);
			} else if(ScoreboardManagement.red.hasPlayer(player) && ScoreboardManagement.red.hasPlayer(killer)) {
				e.setCancelled(true);
			}
		}
	}
	

	@EventHandler
	public void onWindowClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (p.getGameMode() != GameMode.SURVIVAL)
				return;
			if (!Main.gameRunning) {
				e.setCancelled(true);
				return;
			}
			if(e.getCurrentItem() != null) {	
				if (e.getCurrentItem().getType() == Material.BANNER) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (!Main.gameRunning)
			return;
		if (e.getItemDrop().getItemStack().getType() == Material.BANNER) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SURVIVAL)
			locs.add(e.getBlock().getLocation());
		Block b = e.getBlock();
		Location l = new Location(p.getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY()-1, b.getLocation().getBlockZ());
		if (l.getBlock().getType() == Material.STANDING_BANNER) {
			e.setCancelled(true);
		}
		Location block = b.getLocation();
	    	for (final Location loc : spawnLocations) {
	    		if (block.getBlockX() == loc.getBlockX() && block.getBlockY() == loc.getBlockY() && block.getBlockZ() == loc.getBlockZ()) {
	    			e.setCancelled(true);
	    			p.sendMessage(Main.PREFIX + "§7§cDu auf dem Spawn keine Blöcke setzen!");
	    		}
	    	}
		if (b.getType() == Material.WOOD_PLATE) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (b.getLocation().getBlock().getType() == Material.WOOD_PLATE) {
						b.getLocation().getBlock().setType(Material.STONE_PLATE);
						mine.put(b.getLocation(), ScoreboardManagement.blue.hasPlayer(p) ? "blue" : "red");
					}
				}
			}, 100);
		} else if(b.getType() == Material.TNT) {
			b.setType(Material.AIR);
            Entity tnt = p.getWorld().spawn(b.getLocation().add(0.5,0.1, 0.5), TNTPrimed.class);
            ((TNTPrimed)tnt).setFuseTicks(100);
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		e.setCancelled(true);
		for(Block b : e.blockList()) {
			if(locs.contains(b.getLocation()) && b.getType() != Material.STONE_PLATE) {
				b.setType(Material.AIR);
				locs.remove(b.getLocation());
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (locs.contains(e.getBlock().getLocation())) {
			locs.remove(e.getBlock().getLocation());
			e.setCancelled(false);
			if (mine.containsKey(e.getBlock().getLocation()))
				mine.remove(e.getBlock().getLocation());
			Block b = e.getBlock();
			if (b.getType() == Material.STONE_PLATE) {
				e.setCancelled(true);
				b.setType(Material.AIR);

			} else if (b.getType() == Material.WOOD_PLATE) {

				e.setCancelled(true);
				b.setType(Material.AIR);
				b.getLocation().getWorld().dropItemNaturally(b.getLocation(),
						ItemUtils.getItemWithName(Material.WOOD_PLATE, "§4Tret Mine (nicht scharf)"));
			}

		} else {
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL)
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemCollect(PlayerPickupItemEvent e) {
		Item i = e.getItem();
		Player p = e.getPlayer();
		if (i.getItemStack().getType() == Material.IRON_INGOT) {
			e.setCancelled(true);
			Coins.changeCoins(p, i.getItemStack().getAmount());
			ScoreboardManagement.updatePlayer(p);
			p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 3f, 1f);
			i.remove();
		} else if (i.getItemStack().getType() == Material.GOLD_INGOT) {
			e.setCancelled(true);
			Coins.changeCoins(p, i.getItemStack().getAmount() * 5);
			ScoreboardManagement.updatePlayer(p);
			p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 3f, 1f);
			i.remove();
		}
	}

	@EventHandler
	public void Hook(PlayerFishEvent e) {
		Player p = e.getPlayer();
		FishHook hook = e.getHook();
		if (hook.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR
				&& p.getItemInHand().getItemMeta().getDisplayName() == "§2Grappling Hook") {
			Location pLocation = p.getLocation();
			Location hookLocation = hook.getLocation();

			Vector vector = p.getVelocity();
			double distance = pLocation.distance(hookLocation);
			vector.setX(1.0D * (distance / 8.0D) * (hookLocation.getX() - pLocation.getX()) / distance);
			vector.setY((hookLocation.getY() - pLocation.getY()) / 10.0D + 0.7D);
			vector.setZ(1.0D * (distance / 8.0D) * (hookLocation.getZ() - pLocation.getZ()) / distance);
			p.setVelocity(vector);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 3.0F, 2.0F);
		}
	}
	
	static HashMap<Team, Player> teleportingTeams = new HashMap<Team, Player>();
	private static int tpTime = 25;
	static int tpRed = -tpTime;
	static int tpBlue = -tpTime;
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void Teleporter(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		ItemStack teleporter = ItemUtils.getItemWithName(Material.SULPHUR, "§4Flag Teleporter");
		if (Main.gameRunning && p.getGameMode() == GameMode.SURVIVAL) {
			if(item != null) {
				if (item.getType() == Material.SULPHUR && item.getItemMeta().getDisplayName().equalsIgnoreCase("§4Flag Teleporter")) {
					if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if(teleportingTeams.containsKey(getTeam(p))) {
							if (teleportingTeams.get(getTeam(p)) == p) {
			                    p.sendMessage(Main.PREFIX + "§cDu kannst dieses Item nur einmal benutzen");
			                }else {
			                	p.sendMessage(Main.PREFIX + "§cNur ein Mitglied deines Teams kann dieses Item nutzen.");
			            	}
							return;
						}
						if(ScoreboardManagement.red.hasPlayer(p)) {
							if (blueFlag.holder == p) {
								p.sendMessage(Main.PREFIX + "§cDieses Item kannst du nicht benutzen, wenn du die gegnerische Flagge hast!");
								return;
							}
							if(!(getTime() - tpRed >= tpTime)) {
								p.sendMessage(Main.PREFIX + "§cEs dauert noch §4" + (tpTime - (getTime() - tpRed)) + " Sekunden §cbis dein Tam den Teleporter wieder nutzen kann!");
								return;
							}
						}else if (ScoreboardManagement.blue.hasPlayer(p)) {
							if (redFlag.holder == p){
								p.sendMessage(Main.PREFIX + "§cDieses Item kannst du nicht benutzen, wenn du die gegnerische Flagge hast!");
								return;
							}
							if(!(getTime() - tpBlue >= tpTime)) {
								p.sendMessage(Main.PREFIX + "§cEs dauert noch §4" + (tpTime - (getTime() - tpBlue)) + " Sekunden §cbis dein Tam den Teleporter wieder nutzen kann!");
								return;
							}
						}
						
						teleportingTeams.put(getTeam(p), p);
						
						p.getInventory().removeItem(teleporter);
						ParticleUtils.drawCircle(EnumParticle.TOWN_AURA, p.getLocation(), (float) 1.5);
												
						TeleportingProcess tp = new TeleportingProcess(p);
						tp.teleport();
						
					}
				}
			}
		}
	}
	
	
	private static int taskID;
	private static boolean counterRuns = false;
	
	@SuppressWarnings("deprecation")
	public static void tryToStartGame() {
		FileConfiguration cfg = Main.getInstance().getConfig();
		if(Bukkit.getOnlinePlayers().toArray().length >= cfg.getInt("minPlayers")) {
			
			if(counterRuns == false) {
				counterRuns = true;
				if(!StartCommand.used)
					if(ScoreboardManagement.red.getPlayers().toArray().length > 0 && ScoreboardManagement.blue.getPlayers().toArray().length > 0) 
						Bukkit.broadcastMessage(Main.PREFIX + "§7Das Spiel startet in §660 §7Sekunden.");
							taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)Main.getInstance(), (Runnable)new Runnable() {
				                boolean message = true;
				                
				                @Override
				                public void run() {
				                    if (!StartCommand.used) {
				                        if (ScoreboardManagement.red.getPlayers().toArray().length > 0 && ScoreboardManagement.blue.getPlayers().toArray().length > 0) {
				                            if (!Cancel.cancel) {
				                                if (Main.getCounter() <= 0) {
				                                    start();
				                                    StartCommand.used = false;
				                                    Main.setCounter(60);
				                                    Bukkit.broadcastMessage(Main.PREFIX + "§7§aDas Spiel startet jetzt!");
				                                    Bukkit.getScheduler().cancelTask(taskID);
				                                }
				                                else if (Main.getCounter() <= 10) {
				                                    if (Main.getCounter() == 10) {
				                                        Bukkit.broadcastMessage(Main.PREFIX + "§7§7Das Spiel startet in §6" + Main.getCounter() + " §7Sekunden.");
				                                        for (final Player p : Bukkit.getOnlinePlayers()) {
				                                            p.playSound(p.getLocation(), Sound.NOTE_BASS, 2.0f, 1.0f);
				                                        }
				                                        MapVoting.endVoting();
				                                    }
				                                    for (final Player p : Bukkit.getOnlinePlayers()) {
				                                        TitleAPI.sendTitel(p, "§7Das Spiel startet in §a" + Main.getCounter(), "", 5, 12, 3);
				                                    }
				                                    if (Main.getCounter() <= 5) {
				                                        for (final Player p : Bukkit.getOnlinePlayers()) {
				                                            p.playSound(p.getLocation(), Sound.NOTE_BASS, 2.0f, 1.0f);
				                                        }
				                                    }
				                                }else {
				                                    switch (Main.getCounter()) {
				                                        case 20: case 30: case 40: case 50: {
				                                            for (final Player p : Bukkit.getOnlinePlayers()) {
				                                                p.playSound(p.getLocation(), Sound.NOTE_BASS, 2.0f, 1.0f);
				                                            }
				                                            Bukkit.broadcastMessage(Main.PREFIX + "§7§7Das Spiel startet in §6" + Main.getCounter() + " §7Sekunden.");
				                                            break;
				                                        }
				                                    }
				                                }
				                            }else {
				                                this.message = false;
				                                Main.setCounter(60);
				                                counterRuns = false;
				                                Bukkit.broadcastMessage(Main.PREFIX + "§cDer Countdown wurde abgebrochen.");
				                                Bukkit.getScheduler().cancelTask(taskID);
				                            }
				                        }else if (this.message) {
				                            Bukkit.broadcastMessage(Main.PREFIX + "§7Jedes Team muss mindestens ein Mitglied haben, damit das Spiel gestartet werden kann.");
				                            this.message = false;
				                            Main.setCounter(60);
				                            counterRuns = false;
				                            Bukkit.getScheduler().cancelTask(taskID);
				                        }
				                        Main.setCounter(Main.getCounter() - 1);
				                    }else {
				                        Main.setCounter(60);
				                        counterRuns = false;
				                        System.out.println("Der Countdown wurde abgebrochen.");
				                        Bukkit.getScheduler().cancelTask(taskID);
				                    }
				                }
				            }, 0L, 20L);
				
			}
		
			
		}	
	}
	
	@SuppressWarnings("deprecation")
	public static Team getTeam(Player p) {
		if(ScoreboardManagement.red.hasPlayer(p)) {
			return ScoreboardManagement.red;
		}else if (ScoreboardManagement.blue.hasPlayer(p)) {
			return ScoreboardManagement.blue;
		}else {
			return null;
		}
	}
	private static HashMap<Player, Integer> deathCounter = new HashMap<Player, Integer>();
	
	public static int getPlayerDeaths(final Player p) {
        if (!deathCounter.containsKey(p)) {
            deathCounter.put(p, 0);
        }
        return deathCounter.get(p);
    }
	
	private static int time = 0;
	static int timeID;
	public static void time() {
	      timeID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	                if (Main.gameRunning) {
	                   time++;
	                }
	            }
	        }, 0L, 20L);
	    }
	public static int getTime() {
        return time;
    }
}
