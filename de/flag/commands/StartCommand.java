package de.flag.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.flag.game.Game;
import de.flag.game.ScoreboardManagement;
import de.flag.main.Main;
import de.flag.utils.Counter;
import de.flag.utils.CounterAction;
import de.flag.utils.ItemUtils;
import de.flag.utils.StringData;
import de.flag.utils.TitleAPI;

public class StartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.start")) {
				if(!Main.gameRunning && !used) {
					used = true;
					
					if(SelectMap.mapUsed == false) {	
						
						FileConfiguration cfg = Main.getInstance().getConfig();
						int worlds = cfg.getStringList("worlds").size();
						
						int slot;
						
						if(worlds > 1) {
							slot = new Random().nextInt(worlds - 1);
						}else {
							slot = 0;
						}
						StringData data = new StringData();
						data.load(Main.getInstance().getConfig().getStringList("worlds").get(slot));
						Main.currentworld = data.getString("world");
						Main.worlddata = data;
						
						ScoreboardManagement.updatePlayers();
					}
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getInventory().remove(Material.PAPER);
                    }
                    ItemStack item = ItemUtils.getItemWithName(Material.PAPER, "§2Map Auswahl");
                    p.getInventory().setItem(4, item);
                    Bukkit.broadcastMessage(Main.PREFIX + "§7§7Das Spiel startet in §610 §7Sekunden.");
                    for (Player player2 : Bukkit.getOnlinePlayers()) {
                        player2.playSound(player2.getLocation(), Sound.NOTE_BASS, 2.0f, 1.0f);
                    }
						
						new Counter(11, 20, new CounterAction() {
						
						@Override
						public void run(int count) {
							if(count <= 1) {
								Game.start();
								used = false;
								Bukkit.broadcastMessage(Main.PREFIX + "§aDas Spiel startet jetzt!");
								
							} else {
								for(Player p : Bukkit.getOnlinePlayers()) {
									TitleAPI.sendTitel(p, "§7Das Spiel startet in §a" + (count-1), "", 5,	12, 3);
								}
								if (count <= 6) {
                                    for (final Player player : Bukkit.getOnlinePlayers()) {
                                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 2.0f, 1.0f);
                                    }
                                }
							}
						}
					});
				} else {
					p.sendMessage(Main.PREFIX + "§4Das Spiel läuft bereits!");
				}
			} else {
				p.sendMessage(Main.PREFIX + "§cDafür hast du keine Berechtigung!");
			}
		}
		return true;
	}
	public static boolean used = false;
	

}
