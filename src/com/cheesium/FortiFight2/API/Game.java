package com.cheesium.FortiFight2.API;

import java.util.HashMap;

import net.milkycraft.Scheduler.PlayerTimer;
import net.milkycraft.Scheduler.Time;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.cheesium.FortiFight2.Database.Backend;

public class Game {
	
	public static HashMap<String, Time> gameTimers = new HashMap<String, Time>();
	
	public static String getTimeLeft(String GameID, Time time) {
		int i = PlayerTimer.getRemainingTime(GameID, time);
		int remainder = i % 3600, minutes = remainder / 60, seconds = remainder % 60;
		if(minutes == 0) {
			return ChatColor.GOLD + String.valueOf(seconds) + " seconds";
		}
		return ChatColor.GOLD + String.valueOf(minutes) + " minutes and " + seconds + " seconds";
	}

	public static void createGame(String GameID) {
		Backend.createGame(GameID);
	}

	public static void setMaxPlayers(String GameID, int maxPlayers) {
		Backend.setMaxPlayers(GameID, maxPlayers);
	}
	
	public static void deleteGame(String GameID) {
		Backend.deleteGame(GameID);
	}
	
	public static boolean doesGameIDExist(String GameID) {
		return Backend.doesGameIDExist(GameID);
	}

	public static int getMaxPlayers(String GameID) {
		return Backend.getMaxPlayers(GameID);
	}

	public static void addPlayer(String GameID) {
		Backend.addPlayer(GameID);
	}

	public static void removePlayer(String GameID) {
		Backend.removePlayer(GameID);
	}

	public static int getPlayers(String GameID) {
		return Backend.getPlayers(GameID);
	}

	public static Location getLocation1(String GameID) {
		return Backend.getLocation1(GameID);
	}

	public static Location getLocation2(String GameID) {
		return Backend.getLocation2(GameID);
	}

	public static void setLocation1(String GameID, String loc) {
		Backend.setLocation1(GameID, loc);
	}

	public static void setLocation2(String GameID, String loc) {
		Backend.setLocation2(GameID, loc);
	}
	
	public static boolean isLocation1Set(String GameID) {
		return Backend.isLocation1Set(GameID);
	}
	
	public static boolean isLocation2Set(String GameID) {
		return Backend.isLocation2Set(GameID);
	}
	
	public static Cuboid getCuboid(String GameID) {
		return new Cuboid(getLocation1(GameID), getLocation2(GameID));
	}
}
