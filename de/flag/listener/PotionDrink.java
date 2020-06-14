package de.flag.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

public class PotionDrink implements Listener {
		
	@EventHandler
	public void onPlayer(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		
		if(item.getType() == Material.POTION) {
			e.setCancelled(true);
			Potion potion = Potion.fromItemStack(item);
			p.addPotionEffects(potion.getEffects());
			p.setItemInHand(new ItemStack(Material.AIR));
		}
	}
	
}
