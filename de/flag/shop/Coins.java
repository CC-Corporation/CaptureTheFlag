package de.flag.shop;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;

public class Coins {
	public static void resetAll() {

		Set<Player> players = coins.keySet();
		coins.clear();

		for (Player p : players) {
			coins.put(p, 0);
		}
	}

	public static void registerPlayer(Player p) {
		coins.put(p, 0);
	}

	public static boolean hasEnough(Player p, int coin) {
		if (coins.containsKey(p)) {
			if (coins.get(p) >= coin) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static void changeCoins(Player p, int value) {
		if (coins.containsKey(p)) {
			int i = coins.get(p);
			coins.remove(p);
			if (i + value < 0)
				coins.put(p, 0);
			else
				coins.put(p, i + value);
		}
	}

	public static Integer getCoins(Player p) {
		if (coins.containsKey(p)) {
			return coins.get(p);
		} else {
			return 0;
		}
	}

	private static HashMap<Player, Integer> coins = new HashMap<Player, Integer>();

}
