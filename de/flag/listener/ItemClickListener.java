package de.flag.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.flag.game.Game;
import de.flag.game.ScoreboardManagement;
import de.flag.main.Main;
import de.flag.utils.ItemUtils;
import de.flag.utils.StringData;

public class ItemClickListener implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(Main.gameRunning)
			return;
		ItemStack item = p.getItemInHand();
		int maxTeamSize = Main.getInstance().getConfig().getInt("teamSize");
		if(item.getType() == Material.AIR)
			return;
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(item.getItemMeta().getDisplayName() == "§6Teamauswahl") {
				Inventory inv = Bukkit.createInventory(null, 9, "§8Teamauswahl");
				
				//RED
				ItemStack red = new ItemStack(Material.WOOL, 1,(short) 0, (byte)14);
				ItemMeta redmeta = red.getItemMeta();
				redmeta.setDisplayName("§cTeam Rot");
				ArrayList<String> redlore = new ArrayList<String>();
				for(OfflinePlayer p2 : ScoreboardManagement.red.getPlayers())
					redlore.add("§7" + p2.getName());
				for(int i = 0; i < maxTeamSize - ScoreboardManagement.red.getPlayers().size();i++)
					redlore.add("§8-");
				redmeta.setLore(redlore);
				red.setItemMeta(redmeta);
				inv.addItem(red);
				
				//BLUE
				ItemStack blue = new ItemStack(Material.WOOL, 1,(short) 0, (byte)11);
				ItemMeta bluemeta = blue.getItemMeta();
				bluemeta.setDisplayName("§9Team Blau");
				ArrayList<String> bluelore = new ArrayList<String>();
				for(OfflinePlayer p2 : ScoreboardManagement.blue.getPlayers())
					bluelore.add("§7" + p2.getName());
				for(int i = 0; i < maxTeamSize - ScoreboardManagement.blue.getPlayers().size();i++)
					bluelore.add("§8-");
				bluemeta.setLore(bluelore);
				blue.setItemMeta(bluemeta);
				inv.addItem(blue);
				
				p.openInventory(inv);
				
			}else if (p.getInventory().getItemInHand().getType() == Material.PAPER && p.getInventory().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§2Map Auswahl")) {
                final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 18, "§6Maps");
                final FileConfiguration cfg = Main.getInstance().getConfig();
                
                for (final String s : cfg.getStringList("worlds")) {
                    final StringData data = new StringData();
                    data.load(s);
                    ItemStack stack;
                    try {
                        final String str = data.getString("display");
                        final String[] string = str.split("_");
                        final int mat = Integer.parseInt(string[0]);
                        final int id = Integer.parseInt(string[1]);
                        stack = ItemUtils.getItemWithID(mat, id);
                    }catch (Exception ex) {
                        stack = new ItemStack(Material.PAPER);
                    }
                    final ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName("§a" + data.getString("name"));
                    List<String> lore = new ArrayList<String>();
                    lore.add("§7Autor: " + data.getString("autor"));
                    meta.setLore(lore);
                    stack.setItemMeta(meta);
                    inv.addItem(stack);
                }
                p.openInventory(inv);
            } 
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		int maxTeamSize = Main.getInstance().getConfig().getInt("teamSize");
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(e.getSlot() >= 36 && e.getSlot() <= 39)
				e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if(item == null || item.getType() == Material.AIR)
				return;
			if (!Main.gameRunning && p.getGameMode() == GameMode.SURVIVAL) {
				e.setCancelled(true);
				if(item.getItemMeta().getDisplayName() == "§cTeam Rot") {
					if(!ScoreboardManagement.red.hasPlayer(p)) {
						if(ScoreboardManagement.red.getSize() < maxTeamSize) {
							ScoreboardManagement.red.addPlayer(p);
							p.setDisplayName("§c" + p.getName() + "§7");
							p.setPlayerListName("§c" + p.getName());
							p.sendMessage(Main.PREFIX + "Du bist jetzt in Team §cRot");
							Game.tryToStartGame();
						} else {
							p.sendMessage(Main.PREFIX + "§cDieses Team ist voll!");
						}
					}else {
						p.sendMessage(Main.PREFIX + "§7Du bist bereits in Team §cRot§7!");						
					}
					p.closeInventory();
					
				} else if(item.getItemMeta().getDisplayName() == "§9Team Blau") {
					if(!ScoreboardManagement.blue.hasPlayer(p)) {
						if(ScoreboardManagement.blue.getSize() < maxTeamSize) {
							ScoreboardManagement.blue.addPlayer(p);
							p.setDisplayName("§9" + p.getName() + "§7");
							p.setPlayerListName("§9" + p.getName());
							p.sendMessage(Main.PREFIX + "Du bist jetzt in Team §9Blau");
							Game.tryToStartGame();
						} else {
							p.sendMessage(Main.PREFIX + "Dieses Team ist voll!");
						}
					}else {
						p.sendMessage(Main.PREFIX + "§7Du bist bereits in Team §9Blau§7!");							
					}
					p.closeInventory();
					
				}
				ScoreboardManagement.updatePlayer(p);
				
				
			}
		}
	}

}
