package de.flag.shop;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.flag.game.ScoreboardManagement;
import de.flag.main.Main;
import de.flag.utils.ItemUtils;

public class ItemShop {

	Player p;

	public ItemShop(Player p) {
		this.p = p;
	}

	public ArrayList<ShopSite> sites = new ArrayList<ShopSite>();

	public void addSite(ShopSite site) {
		sites.add(site);
	}

	public int selected = 0;

	@SuppressWarnings("deprecation")
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(null, 36, "§6Shop");

		for (int i = 0; i < sites.size(); i++) {
			ShopSite site = sites.get(i);
			inv.setItem(27 + i, site.getDisplay());
		}
		ShopSite site = sites.get(selected);
		for (int i = 0; i < site.getPatterns().size(); i++) {
			Pattern pattern = site.getPatterns().get(i);
			inv.setItem(i, pattern.getDisplay());
			inv.setItem(9 + i, ItemUtils.getRandomNBT(new ItemStack(Material.GOLD_INGOT, pattern.getCost()), "§6Kosten: " + pattern.getCost()));
		}
		for (int i = 0; i < 9; i++) {
			if (i == selected)
				inv.setItem(18 + i, new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 0, (byte) 5));
			else
				inv.setItem(18 + i, new ItemStack(Material.STAINED_GLASS_PANE));
		}
		for (int j = 0; j < 36; ++j) {
            ItemStack item = inv.getItem(j);
            ItemStack glassPane = new ItemStack(ItemUtils.getItemWithID(160, 7));
            ItemMeta glass = glassPane.getItemMeta();
            glass.setDisplayName(" ");
            glassPane.setItemMeta(glass);
            if (item == null) {
                inv.setItem(j, glassPane);
            }
            else if (item.getType() == Material.AIR) {
                inv.setItem(j, glassPane);
            }
		}
		return inv;
	}

	public void onWindowClick(int slot, int hotbarButton, InventoryAction action) {
		ShopSite site = sites.get(selected);
		
		if ((slot > 8 && slot < 18) || (slot > 26))
			slot -= 9;
		if (slot < 18) {
			if (slot <= site.getPatterns().size()-1) {
				if (Main.gameRunning) {
					if(action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.HOTBAR_MOVE_AND_READD && action != InventoryAction.HOTBAR_SWAP) {
						if(Coins.hasEnough(p, site.getPatterns().get(slot).getCost())) {
												
							site.getPatterns().get(slot).action(p);				
							Coins.changeCoins(p, -(site.getPatterns().get(slot).getCost()));
							ScoreboardManagement.updatePlayer(p);
							p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
							
						} else {
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2f, 1f);
							p.sendMessage(Main.PREFIX + "§cDas kannst du dir nicht leisten!");
						}
						
					}else if(action == InventoryAction.HOTBAR_MOVE_AND_READD) {
						if(hotbarButton >= 0 && hotbarButton <= 8) {
							if (site.getPatterns().get(slot).getDisplay().getType() != null) {
								Material mat = site.getPatterns().get(slot).getDisplay().getType();
								String siteName = site.getDisplay().getItemMeta().getDisplayName();
								if(siteName.equalsIgnoreCase("§6Blöcke") || mat == Material.STONE_PLATE || mat == Material.SULPHUR || mat == Material.WEB || mat == Material.ARROW || mat == Material.TNT) {
									double cost = (double) site.getPatterns().get(slot).getCost();
									int amount = site.getPatterns().get(slot).getDisplay().getAmount();
									int price = (int) (cost/(double)amount*64);
									if(Coins.hasEnough(p, price)) {
										p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.5f, 1f);
										for(int i = 0; i < (int) ((double) 64 / (double) amount); i++) {
											
											site.getPatterns().get(slot).action(p, hotbarButton);
											
										}
										Coins.changeCoins(p, -(price));
									}else if(Coins.getCoins(p) >= cost) {
										int count = (int) (Math.floor(Coins.getCoins(p)/cost));
										int price2 = (int) (count*cost);
										p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.5f, 1f);
										for(int i = 0; i < count; i++) {
											
											site.getPatterns().get(slot).action(p, hotbarButton);
											
										}
										Coins.changeCoins(p, -(price2));
									}else{
										p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1f);
										p.sendMessage(Main.PREFIX + "§cDas kannst du dir nicht leisten!");
									}
									ScoreboardManagement.updatePlayer(p);
								}else if(Coins.hasEnough(p, site.getPatterns().get(slot).getCost())) {
									
									site.getPatterns().get(slot).action(p, hotbarButton);		
									Coins.changeCoins(p, -(site.getPatterns().get(slot).getCost()));
									ScoreboardManagement.updatePlayer(p);
									p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
									
								} else {
									p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2f, 1f);
									p.sendMessage(Main.PREFIX + "§cDas kannst du dir nicht leisten!");
								}
							}
														
						}
						
					}else if(action == InventoryAction.HOTBAR_SWAP) {
						
						if(hotbarButton >= 0 && hotbarButton <= 8) {
							if (site.getPatterns().get(slot).getDisplay().getType() != null) {
								Material mat = site.getPatterns().get(slot).getDisplay().getType();
								String siteName = site.getDisplay().getItemMeta().getDisplayName();
								if(siteName.equalsIgnoreCase("§6Blöcke") || mat == Material.STONE_PLATE || mat == Material.SULPHUR || mat == Material.WEB || mat == Material.ARROW || mat == Material.TNT) {
									double cost = (double) site.getPatterns().get(slot).getCost();
									int amount = site.getPatterns().get(slot).getDisplay().getAmount();
									int price = (int) (cost/(double)amount*64);
									if(Coins.hasEnough(p, price)) {
										p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.5f, 1f);
										for(int i = 0; i < (int) ((double) 64 / (double) amount); i++) {
											
											site.getPatterns().get(slot).action(p, hotbarButton);		
											
										}
										Coins.changeCoins(p, -(price));
									}else if(Coins.getCoins(p) >= cost) {
										int count = (int) (Math.floor(Coins.getCoins(p)/cost));
										int price2 = (int) (count*cost);
										p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.5f, 1f);
										for(int i = 0; i < count; i++) {
											
											site.getPatterns().get(slot).action(p, hotbarButton);
											
										}
										Coins.changeCoins(p, -(price2));
									}else{
										p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1f);
										p.sendMessage(Main.PREFIX + "§cDas kannst du dir nicht leisten!");
									}
									ScoreboardManagement.updatePlayer(p);
								}else if(Coins.hasEnough(p, site.getPatterns().get(slot).getCost())) {
									
									site.getPatterns().get(slot).action(p, hotbarButton);		
									Coins.changeCoins(p, -(site.getPatterns().get(slot).getCost()));
									ScoreboardManagement.updatePlayer(p);
									p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
									
								} else {
									p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2f, 1f);
									p.sendMessage(Main.PREFIX + "§cDas kannst du dir nicht leisten!");
								}
							}
														
						}
						
					}else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY){
						if (site.getPatterns().get(slot).getDisplay().getType() != null) {
							Material mat = site.getPatterns().get(slot).getDisplay().getType();
							String siteName = site.getDisplay().getItemMeta().getDisplayName();
							if(siteName.equalsIgnoreCase("§6Blöcke") || mat == Material.STONE_PLATE || mat == Material.SULPHUR || mat == Material.WEB || mat == Material.ARROW || mat == Material.TNT) {
								double cost = (double) site.getPatterns().get(slot).getCost();
								int amount = site.getPatterns().get(slot).getDisplay().getAmount();
								int price = (int) (cost/(double)amount*64);
								if(Coins.hasEnough(p, price)) {
									p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.5f, 1f);
									for(int i = 0; i < (int) ((double) 64 / (double) amount); i++) {
										
										site.getPatterns().get(slot).action(p);		
										
									}
									Coins.changeCoins(p, -(price));
								}else if(Coins.getCoins(p) >= cost) {
									int count = (int) (Math.floor(Coins.getCoins(p)/cost));
									int price2 = (int) (count*cost);
									p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.5f, 1f);
									for(int i = 0; i < count; i++) {
										
										site.getPatterns().get(slot).action(p);
										
									}
									Coins.changeCoins(p, -(price2));
								}else{
									p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2.5f, 1f);
									p.sendMessage(Main.PREFIX + "§cDas kannst du dir nicht leisten!");
								}
								ScoreboardManagement.updatePlayer(p);
							}
						}
					}
				}else {
					p.playSound(p.getLocation(), Sound.ANVIL_LAND, 2f, 1f);
					p.sendMessage(Main.PREFIX + "§cDer Shop kann nur genutzt werden, wenn das Spiel läuft!");
					p.closeInventory();
				}
			}
		} else if (slot - 18 <= sites.size()-1) {
			selected = slot - 18;
			p.playSound(this.p.getLocation(), Sound.CLICK, 1.5f, 1.0f);
			updateInventory();
		}
		
	}

	public void updateInventory() {
		p.openInventory(getInventory());
		ShopListener.players.put(p, this);
	}

}
