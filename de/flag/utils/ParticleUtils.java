package de.flag.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class ParticleUtils {
	public static void drawCircle(EnumParticle part, Location loc, float radius) {
		for (double t = 0; t < 50; t += 0.5) {
			float x = radius * (float) Math.sin(t);
			float z = radius * (float) Math.cos(t);

			PacketPlayOutWorldParticles p = new PacketPlayOutWorldParticles(part, true, (float) loc.getX() + x,
					(float) loc.getY(), (float) loc.getZ() + z, 0, 0, 0, 0, 1, 0);
			for(Player p2 : Bukkit.getOnlinePlayers()) {
				((CraftPlayer)p2).getHandle().playerConnection.sendPacket(p);
			}
		}

	}

}
