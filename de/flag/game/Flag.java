package de.flag.game;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Flag {
	public Flag(Location loc) {
		this.loc = loc;
		returned();
	}
	public Location loc;
	public boolean isHome = true;
	public Player holder = null;
	
	@SuppressWarnings("deprecation")
	public void catched(Player p) {
		isHome = false;
		holder = p;
		loc.getBlock().setType(Material.AIR);
		ItemStack item = new ItemStack(Material.BANNER,1,(short)0,(byte)color);
		holder.getInventory().setHelmet(item);
	}
	
	@SuppressWarnings("deprecation")
	public void returned() {
		isHome = true;
		
		loc.getBlock().setType(Material.STANDING_BANNER);
		
		Banner b = (Banner) loc.getBlock().getState();
		b.setBaseColor(dyecolor);
		b.update();
		
		ItemStack item = new ItemStack(Material.AIR);
		if(holder != null)
			holder.getInventory().setHelmet(item);
		holder = null;
		loc.getBlock().setData((byte) 2);
		
		Location block = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY() + 1.0, this.loc.getZ());
        block.getBlock().setType(Material.AIR);
	}
	
	public byte color = 0;
	public DyeColor dyecolor = DyeColor.BLACK;
	

}
