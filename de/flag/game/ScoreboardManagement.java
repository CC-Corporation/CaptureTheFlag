package de.flag.game;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import de.flag.commands.SelectMap;
import de.flag.commands.StartCommand;
import de.flag.listener.MainListener;
import de.flag.main.Main;
import de.flag.shop.Coins;
import de.flag.utils.TitleAPI;

public class ScoreboardManagement {

	public static Scoreboard score;
	public static Team red, blue;

	@SuppressWarnings("deprecation")
	public static void start() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();

		score = manager.getNewScoreboard();
		red = score.registerNewTeam("red");
		blue = score.registerNewTeam("blue");

		red.setAllowFriendlyFire(false);
		red.setPrefix("§cRot : ");
		red.setNameTagVisibility(NameTagVisibility.ALWAYS);

		blue.setAllowFriendlyFire(false);
		blue.setPrefix("§9Blau : ");
		blue.setNameTagVisibility(NameTagVisibility.ALWAYS);

		for (OfflinePlayer p : red.getPlayers())
			red.removePlayer(p);
		for (OfflinePlayer p : blue.getPlayers())
			blue.removePlayer(p);
	}


	@SuppressWarnings("deprecation")
	public static void updatePlayer(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = (sb.getObjective("nix") != null ? sb.getObjective("nix")
				: sb.registerNewObjective("nix", "nix"));
		if(!Main.gameRunning) {
			obj.setDisplayName("§6§lCapture The Flag");
			obj.getScore(" ").setScore(7);
			obj.getScore("§7Name: §a" + p.getName()).setScore(6);
			obj.getScore("  ").setScore(5);
			obj.getScore("§7Team: " + (blue.hasPlayer(p) ? "§9Blau" : "§cRot")).setScore(4);
			obj.getScore("   ").setScore(3);
			if(Main.getCounter() <= 10 || StartCommand.used)
				obj.getScore("§7Map: §a" + Main.worlddata.getString("name")).setScore(2);
			else {

				if(SelectMap.mapUsed == false) {
				obj.getScore("§7Map: §aAbstimmung...").setScore(2);;
				}else {
					obj.getScore("§7Map: §a" + Main.worlddata.getString("name")).setScore(2);
				}
			}
				
		} else {
			
			obj.setDisplayName("§6§lCapture The Flag");
			obj.getScore(" ").setScore(10);
			obj.getScore("§7Team: " + (blue.hasPlayer(p) ? "§9Blau" : "§cRot")).setScore(9);
			obj.getScore("   ").setScore(8);
			obj.getScore("§8§lPunkte").setScore(7);
			obj.getScore("  §cRot:   §7" + Game.points_red).setScore(6);
			obj.getScore("  §9Blau:  §7" + Game.points_blue).setScore(5);
			obj.getScore("§5").setScore(4);
			obj.getScore("§7Kills: " + "§6§l" + MainListener.getPlayerKills(p)).setScore(3);
			obj.getScore("§a§b").setScore(2);
			obj.getScore("§7Coins: §6§l" + Coins.getCoins(p)).setScore(1);
		}
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(sb);
		
		TitleAPI.sendTablist(p, "§4Capture the Flag\n", "\n§2powered by\nDr__Dirty & CPU");
		
	}
	public static void updatePlayers() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			updatePlayer(p);
		}
	}

}
