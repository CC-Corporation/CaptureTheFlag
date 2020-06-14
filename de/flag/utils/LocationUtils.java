package de.flag.utils;

import org.bukkit.Location;

public class LocationUtils {
	
	public static boolean playerMoved(Location player, Location loc) {
		
		Location locA = player.add(0.5 , 0, 0.5);
		Location locB = player.subtract(0.5, 0, -0.5);
				
		boolean b = !isInArea(player, locA, locB);
		return b;
		
	}
	
	public static boolean isInArea(Location location, Location positionA, Location positionB) {
		
		double x = location.getX();
		double z = location.getZ();
		double aX = positionA.getX();
		double aZ = positionA.getZ();
		double bX = positionB.getX();
		double bZ = positionB.getZ();
		
		if (aX < bX) {
			
			if(aZ < bZ) {
				
				if(x >= aX && x <= bX && z >= aZ && z <= bZ) {
					return true;
				}else {
					return false;
				}
				
			}else if (aZ > bZ) {
				
				if(x >= aX && x <= bX && z <= aZ && z >= bZ) {
					return true;
				}else {
					return false;
				}
				
			}else {
				if(x == aX && z == aZ) {
					return true;
				}else {
					return false;
				}
			}
			
		}else if (aX > bX){
			
			if(aZ < bZ) {
				
				if(x <= aX && x >= bX && z >= aZ && z <= bZ) {
					return true;
				}else {
					return false;
				}
				
			}else if (aZ > bZ) {
				
				if(x <= aX && x >= bX && z <= aZ && z >= bZ) {
					return true;
				}else {
					return false;
				}
				
			}else {
				if(x == aX && z == aZ) {
					return true;
				}else {
					return false;
				}
			}
			
		}else {
			
			if(aZ < bZ) {
				
				if(x == aX && z >= aZ && z <= bZ) {
					return true;
				}else {
					return false;
				}
				
			}else if (aZ > bZ) {
				
				if(x == aX && z <= aZ && z >= bZ) {
					return true;
				}else {
					return false;
				}
				
			}else {
				
				if(x == aX && z == aZ) {
					return true;
				}else {
					return false;
				}
				
			}
		}
		
				
	}
}
