package de.flag.shop;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class ShopSite {
	ItemStack display;
	
	public ShopSite(ItemStack display) {
		this.display = display;
	}
	
	public ArrayList<Pattern> patterns = new ArrayList<Pattern>();
	
	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
	}

	public ItemStack getDisplay() {
		return display;
	}

	public void setDisplay(ItemStack display) {
		this.display = display;
	}

	public ArrayList<Pattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(ArrayList<Pattern> patterns) {
		this.patterns = patterns;
	}

}
