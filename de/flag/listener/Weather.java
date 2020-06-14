package de.flag.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather implements Listener {
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		
		e.setCancelled(true);
		
	}
	
}
