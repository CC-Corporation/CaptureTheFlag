package de.flag.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopPattern extends Pattern{
	
	ItemStack display;
	
	int cost;

	ItemStack output;
	
	public ShopPattern(ItemStack display, int cost, ItemStack output) {
		super(display, cost);
		this.display = display;
		this.cost = cost;
		this.output = output;
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
	@Override
	public void action(Player p) {
		p.getInventory().addItem(output);
	}
	
	@Override
	public void action(Player p, int outputSlot) {
		ItemStack stack = p.getInventory().getItem(outputSlot);
		p.getInventory().setItem(outputSlot, output);
		if(stack != null) {
			if(stack.getType() != Material.AIR) {
				p.getInventory().addItem(stack);
			}
		}
	}

}
