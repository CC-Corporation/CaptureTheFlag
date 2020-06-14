package de.flag.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.flag.commands.SelectMap;
import de.flag.main.Main;
import de.flag.utils.ItemUtils;
import de.flag.utils.StringData;

public class MapVoting implements Listener {
	
	private static HashMap<Player, Integer> votes = new HashMap<>();
	
	
	public void openMapVoting(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9*2,"§6Map Voting");
		FileConfiguration cfg = Main.getInstance().getConfig();
		for(String s : cfg.getStringList("worlds")) {
			StringData data = new StringData();
			data.load(s);
			ItemStack stack;
			try {
//				stack = new ItemStack(Integer.parseInt(data.getString("display")));
				String str = data.getString("display");
				String[] string = str.split("_");
				int mat = Integer.parseInt(string[0]);
				int id = Integer.parseInt(string[1]);
				stack = ItemUtils.getItemWithID(mat, id);
			} catch(Exception ex) {
				stack = new ItemStack(Material.PAPER);
			}
			
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName("§a" + data.getString("name"));
			
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§7Autor: " + data.getString("autor"));
			
			meta.setLore(lore);
			
			stack.setItemMeta(meta);
			inv.addItem(stack);
		}
		p.openInventory(inv);
		
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (Main.gameRunning) {
			return;
		}
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getItemInHand().getType() == Material.PAPER && p.getItemInHand().getItemMeta().getDisplayName().contentEquals("§6Map Voting")) { 
				openMapVoting(p);
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (Main.gameRunning) {
			return;
		}
//		if(e.getAction() == InventoryAction.PICKUP_SOME) {
			
			if(e.getInventory().getName().equalsIgnoreCase("§6Map Voting")) {
				e.setCancelled(true);
				if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					int slot = e.getSlot();
					
					if(votes.containsKey(p)) {
						
						votes.replace(p, slot);
						
						
					}else {
						
						votes.put(p, slot);
					}
					StringData data = new StringData();
					data.load(Main.getInstance().getConfig().getStringList("worlds").get(slot));
					p.sendMessage(Main.PREFIX + "§7Du hast für die Map §a" + data.getString("name") + " §7abgestimmt.");
					p.closeInventory();
					
				}
			}
//		}
			
	}
	
	public static void endVoting() {
		if(SelectMap.mapUsed == false) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				ItemStack air = new ItemStack(Material.AIR);
				p.getInventory().setItem(8, air);
				if(p.getOpenInventory().getTitle().contentEquals("§6Map Voting"))
					p.closeInventory();			
			}
			
			HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
			
			for(int a : votes.values()) {
				
				if(!values.containsKey(a)) {
					
					values.put(a, 1);
					
				}else {
					
					int value = values.get(a);
					values.replace(a, value + 1);
					
				}
				
			}
			FileConfiguration cfg = Main.getInstance().getConfig();
			int key = new Random().nextInt(cfg.getStringList("worlds").size());
			int value = 0;
			
			for (Entry<Integer, Integer> e : values.entrySet()) {
				
				if (e.getValue() > value) {
					
					value = e.getValue();
					key = e.getKey();
					
				}
				
			}
			
			StringData data = new StringData();
			data.load(Main.getInstance().getConfig().getStringList("worlds").get(key));
			Main.currentworld = data.getString("world");
			Main.worlddata = data;
			ScoreboardManagement.updatePlayers();
			votes.clear();
			values.clear();
		}
	}
		
}