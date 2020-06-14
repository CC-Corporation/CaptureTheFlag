package de.flag.utils;

import org.bukkit.Bukkit;

import de.flag.main.Main;

public class Counter {
	public Counter(int repeat, int delay, CounterAction action) {
		counter = repeat;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(counter <=0) {
					Bukkit.getScheduler().cancelTask(id);
					return;
				}
				action.run(counter);
				counter--;	
			}
		}, delay, delay);
	}
	int counter;
	int id;

}
