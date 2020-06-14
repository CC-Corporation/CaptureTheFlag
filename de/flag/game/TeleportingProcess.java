package de.flag.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.flag.main.Main;
import de.flag.utils.ItemUtils;
import de.flag.utils.ParticleUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class TeleportingProcess {
	int taskID2;
	Player p;
	public TeleportingProcess(Player p) {
		
		this.p = p;
		
	}
	public void teleport() {
		ItemStack teleporter = ItemUtils.getItemWithName(Material.SULPHUR, "§4Flag Teleporter");
		taskID2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			int counter = 0;
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(counter < 5) {
					if(Game.teleportingTeams.containsKey(Game.getTeam(p))) {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 2f, 0);
						int t = (5 - counter);
						p.sendMessage(Main.PREFIX + "§a" + t);
					}else {
						Bukkit.getScheduler().cancelTask(taskID2);
						p.sendMessage(Main.PREFIX + "§cDu darfst dich nicht bewegen!");
						Game.teleportingTeams.remove(Game.getTeam(p));
						counter = 0;
						p.getInventory().addItem(teleporter);
						Bukkit.getScheduler().cancelTask(taskID2);
						return;
						
					}
					
				}else {
					counter = 0;
					if(ScoreboardManagement.red.hasPlayer(p)){
						Player target = Game.redFlag.holder;
						if(target != null) {
							p.teleport(target);
						}else {
							Location flag = Game.redFlag.loc;
							p.teleport(flag);
						}
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
						Game.teleportingTeams.remove(Game.getTeam(p));
						Game.tpRed = Game.getTime();
						Bukkit.getScheduler().cancelTask(taskID2);
						
					}else if(ScoreboardManagement.blue.hasPlayer(p)){
						Player target = Game.blueFlag.holder;
						if(target != null) {
							p.teleport(target);
							
						}else {
							Location flag = Game.blueFlag.loc;												
							p.teleport(flag);
						}
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
						Game.teleportingTeams.remove(Game.getTeam(p));
						Game.tpBlue = Game.getTime();
						Bukkit.getScheduler().cancelTask(taskID2);;
						
					}
					Bukkit.getScheduler().cancelTask(taskID2);
				}
				for(int i = 0; i <= counter; i++) {
					ParticleUtils.drawCircle(EnumParticle.TOWN_AURA, p.getLocation().add(0, ((double)i)/2-0.25, 0), (float) 1.5);
					ParticleUtils.drawCircle(EnumParticle.TOWN_AURA, p.getLocation().add(0, ((double)i)/2, 0), (float) 1.5);
				}
				counter++;
			}
			
		}, 0, 20);
	}
	
}
