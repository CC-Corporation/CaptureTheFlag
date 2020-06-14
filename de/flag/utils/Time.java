package de.flag.utils;

public class Time {
	
	public static String toTime(int seconds) {
		String str1, str2, str3;
		str1 = "";
		str2 = "";
		str3 = "";
		int t = seconds;
		int minutes1 = t/60;
		int hours = (minutes1)/60;
		int minutes2 = minutes1 - hours*60;
		int secounds2 = t - (minutes2*60) - (hours*60*60);
		if(secounds2 < 10) {
			str1 = "0";
		}
		if(minutes2 < 10) {
			str2 = "0";
		}
		if(hours < 10) {
			str3 = "0";
		}
		String str = str3 + hours + ":" + str2 + minutes2 + ":" + str1 + secounds2;
		return str;
	}
	
}
