package de.flag.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.flag.game.ScoreboardManagement;
import de.flag.main.Main;
import de.flag.utils.ItemUtils;
import de.flag.utils.StringData;

public class SelectMap implements CommandExecutor, Listener{
	
	public static boolean mapUsed = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("ctf.selectmap")) {
				if(Main.gameRunning) {
					p.sendMessage(Main.PREFIX + "§cDie Map kann nicht geändert werden, da das Spiel gerade läuft!");
					return false;
				}
				mapUsed = true;
				Inventory inv = Bukkit.createInventory(null, 9*2,"§6Maps");
				FileConfiguration cfg = Main.getInstance().getConfig();
				for(String s : cfg.getStringList("worlds")) {
					StringData data = new StringData();
					data.load(s);
					ItemStack stack;
					try {
//						stack = new ItemStack(Integer.parseInt(data.getString("display")));
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
			} else {
				p.sendMessage(Main.PREFIX + "§cDafür hast du keine Berechtigung!");
			}
		}
		return true;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			try {
				if(e.getClickedInventory().getName().equalsIgnoreCase("§6Maps")) {
					if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
						StringData data = new StringData();
						data.load(Main.getInstance().getConfig().getStringList("worlds").get(e.getSlot()));
						Main.currentworld = data.getString("world");
						Main.worlddata = data;
						p.closeInventory();
						p.sendMessage(Main.PREFIX + "Die aktuelle Map ist jetzt §a" + data.getString("name"));
						ScoreboardManagement.updatePlayers();
					}
				}
			} catch(Exception ex) {
			}
		}
	}

}
