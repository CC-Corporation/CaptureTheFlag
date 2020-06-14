package de.flag.shop;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import de.flag.utils.ItemUtils;

public class ShopListener implements Listener {
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.DROPPER) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			getDefaultShop(p);
		}
	}
	
	public static HashMap<Player, ItemShop> players = new HashMap<Player, ItemShop>();
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();
			if(players.containsKey(p)) {
				players.remove(p);
			}
			
		}
	}
	
	@EventHandler
	public void onWindowClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(players.containsKey(p)) {
				switch(e.getAction()) {
				case MOVE_TO_OTHER_INVENTORY : case COLLECT_TO_CURSOR : case CLONE_STACK :
					e.setCancelled(true);
					break;
				case PLACE_ALL : case PLACE_ONE : case PLACE_SOME:
					if(e.getClickedInventory().getName().equalsIgnoreCase("§6Shop"))
						e.setCancelled(true);
					break;
				case UNKNOWN : case NOTHING :
					e.setCancelled(true);
				default:
					break;
				}
				
				if(e.getClickedInventory() != null) {
					if(e.getClickedInventory().getName().equalsIgnoreCase("§6Shop")) {
						e.setCancelled(true);						
						players.get(p).onWindowClick(e.getSlot(),e.getHotbarButton(), e.getAction());
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public ItemShop getDefaultShop(Player p) {
		ItemShop shop = new ItemShop(p);
		
		ShopSite site1 = new ShopSite(ItemUtils.getItemWithName(Material.SANDSTONE, "§6Blöcke"));
		site1.addPattern(new ShopPattern(new ItemStack(Material.SANDSTONE,2), 1, new ItemStack(Material.SANDSTONE,2)));
		site1.addPattern(new ShopPattern(new ItemStack(Material.ENDER_STONE), 5, new ItemStack(Material.ENDER_STONE)));
		site1.addPattern(new ShopPattern(new ItemStack(Material.WOOD, 2,(short) 0, (byte) 5), 15, new ItemStack(Material.WOOD, 2,(short) 0, (byte) 5)));
		site1.addPattern(new ShopPattern(new ItemStack(Material.MONSTER_EGGS,3,(short) 0, (byte) 1), 20, new ItemStack(Material.MONSTER_EGGS,3,(short) 0, (byte) 1)));
		site1.addPattern(new ShopPattern(new ItemStack(Material.OBSIDIAN), 64, new ItemStack(Material.OBSIDIAN)));
		
		ShopSite site2 = new ShopSite(ItemUtils.getItemWithName(Material.IRON_SWORD, "§6Waffen"));
		site2.addPattern(new ShopPattern(ItemUtils.getItemWithName(Material.STICK, "§r§fKnockback Stick", Enchantment.KNOCKBACK, 1), 10, ItemUtils.getItemWithName(Material.STICK, "§6Knockback Stick", Enchantment.KNOCKBACK, 1)));
		site2.addPattern(new ShopPattern(ItemUtils.getItemWithEnchantment(Material.WOOD_SWORD, Enchantment.DAMAGE_ALL, 2), 35, ItemUtils.getItemWithEnchantment(Material.WOOD_SWORD, Enchantment.DAMAGE_ALL, 2)));
		site2.addPattern(new ShopPattern(new ItemStack(Material.IRON_SWORD), 25, new ItemStack(Material.IRON_SWORD)));
		site2.addPattern(new ShopPattern(new ItemStack(Material.DIAMOND_SWORD), 50, new ItemStack(Material.DIAMOND_SWORD)));
		site2.addPattern(new ShopPattern(new ItemStack(Material.BOW), 35, new ItemStack(Material.BOW)));
		site2.addPattern(new ShopPattern(new ItemStack(Material.ARROW,8), 8, new ItemStack(Material.ARROW, 8)));
		
		ShopSite site3 = new ShopSite(ItemUtils.getItemWithName(Material.IRON_PICKAXE, "§6Werkzeuge & Rüstung"));
		
		site3.addPattern(new ShopPattern(new ItemStack(Material.IRON_PICKAXE), 20, new ItemStack(Material.IRON_PICKAXE)));
		site3.addPattern(new ShopPattern(new ItemStack(Material.DIAMOND_PICKAXE), 40, new ItemStack(Material.DIAMOND_PICKAXE)));
		site3.addPattern(new ShopPattern(new ItemStack(Material.FISHING_ROD), 10, new ItemStack(Material.FISHING_ROD)));
		
		Pattern pIron = new Pattern(ItemUtils.getItemWithName(Material.IRON_CHESTPLATE, "§r§aRüstungsupgrade I"), 30);
		Pattern pGold = new Pattern(ItemUtils.getItemWithName(Material.DIAMOND_CHESTPLATE, "§r§aRüstungsupgrade II"), 60);
		
		pIron.setAction(new PlayerRunnable() {
			
			@Override
			public void run(Player p) {
				if(p.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE) {
					p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
				} else {
					Coins.changeCoins(p, pIron.cost);
					p.sendMessage("§cDu hast dieses Upgrade bereits!");
				}
					
			}
		});
		pGold.setAction(new PlayerRunnable() {
			
			@Override
			public void run(Player p) {
				if(p.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE || p.getInventory().getChestplate().getType() == Material.IRON_CHESTPLATE) {
					p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
				} else {
					Coins.changeCoins(p, pGold.cost);
					p.sendMessage("§cDu hast dieses Upgrade bereits!");
				}
					
			}
		});
		
		site3.addPattern(pIron);
		site3.addPattern(pGold);

		ShopSite site4 = new ShopSite(ItemUtils.getPotion(PotionType.INSTANT_HEAL, 1, "§6Effekte"));
		
		site4.addPattern(new ShopPattern(ItemUtils.getPotion(PotionType.INSTANT_HEAL, 1, "§r§dHeiltrank I"), 12, ItemUtils.getPotion(PotionType.INSTANT_HEAL, 1, "§r§dHeiltrank I")));
		site4.addPattern(new ShopPattern(ItemUtils.getPotion(PotionType.INSTANT_HEAL, 2, "§r§dHeiltrank II"), 20, ItemUtils.getPotion(PotionType.INSTANT_HEAL, 2, "§r§dHeiltrank II")));
		site4.addPattern(new ShopPattern(ItemUtils.getPotion(PotionType.SPEED, 1, "§r§dGeschwindigkeit I"), 16, ItemUtils.getPotion(PotionType.SPEED, 1, "§r§dGeschwindigkeit I")));
		site4.addPattern(new ShopPattern(ItemUtils.getPotion(PotionType.JUMP, 2, "§r§dJumpboost II"), 16, ItemUtils.getPotion(PotionType.JUMP, 2, "§r§dJumpboost II")));
		site4.addPattern(new ShopPattern(new ItemStack(Material.GOLDEN_APPLE), 15, new ItemStack(Material.GOLDEN_APPLE)));
		
		ShopSite site5 = new ShopSite(ItemUtils.getItemWithName(Material.TNT, "§6Sonstiges"));
		
		site5.addPattern(new ShopPattern(new ItemStack(Material.WEB), 12,new ItemStack(Material.WEB)));
		site5.addPattern(new ShopPattern(new ItemStack(Material.TNT), 32, new ItemStack(Material.TNT)));
		site5.addPattern(new ShopPattern(ItemUtils.getItemWithName(Material.STONE_PLATE, "§4Tret Mine"), 64, ItemUtils.getItemWithName(Material.WOOD_PLATE, "§4Tret Mine (nicht scharf)")));
		site5.addPattern(new ShopPattern(ItemUtils.getItemWithName(Material.FISHING_ROD, "§2Grappling Hook"), 40, ItemUtils.getItemWithName(Material.FISHING_ROD, "§2Grappling Hook")));
		site5.addPattern(new ShopPattern(ItemUtils.getItemWithName(Material.SULPHUR, "§4Flag Teleporter"), 52, ItemUtils.getItemWithName(Material.SULPHUR, "§4Flag Teleporter")));
		
		shop.addSite(site1);
		shop.addSite(site2);
		shop.addSite(site3);
		shop.addSite(site4);
		shop.addSite(site5);
		
		shop.updateInventory();
		
		return shop;
	}

}
