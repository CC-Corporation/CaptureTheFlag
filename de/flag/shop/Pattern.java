package de.flag.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Pattern {
	
	ItemStack display;
	int cost;
	PlayerRunnable run;
	
	public Pattern(ItemStack display, int cost) {
		this.display = display;
		this.cost = cost;
	}
	public void setAction(PlayerRunnable run) {
		this.run = run;
	}
	public void action(Player p) {
		if(run != null)
			run.run(p);
	}
	public void action(Player p, int hotbarButton) {
		if(run != null)
			run.run(p);
	}
	public ItemStack getDisplay() {
		return display;
	}
	public void setDisplay(ItemStack display) {
		this.display = display;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

}
