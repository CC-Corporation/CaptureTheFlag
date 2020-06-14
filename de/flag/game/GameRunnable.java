package de.flag.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.flag.utils.ParticleUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class GameRunnable implements Runnable {

	@Override
	public void run() {
		if(goldCounter >= goldDelay) {
			if (Game.goldlist != null)
				for (Location loc : Game.goldlist) {
					loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT));
				}
			goldCounter=0;
		} else {
			goldCounter++;
		}

		if (Game.ironlist != null)
			for (Location loc : Game.ironlist) {
				loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.IRON_INGOT));
			}
		if(Game.blueFlag.holder != null) {
			ParticleUtils.drawCircle(EnumParticle.WATER_SPLASH, Game.blueFlag.holder.getLocation().add(0, 2, 0), 1);
			ParticleUtils.drawCircle(EnumParticle.WATER_SPLASH, Game.blueFlag.holder.getLocation().add(0, 2.3, 0), 1);
			ParticleUtils.drawCircle(EnumParticle.WATER_SPLASH, Game.blueFlag.holder.getLocation().add(0, 2.4, 0), 1);
		}
		if(Game.redFlag.holder != null) {
			ParticleUtils.drawCircle(EnumParticle.LAVA, Game.redFlag.holder.getLocation().add(0, 2, 0), 1);
		}
	}
	int goldCounter = 0;
	final int goldDelay = 10;
	
}
