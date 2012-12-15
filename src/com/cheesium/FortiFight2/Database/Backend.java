package com.cheesium.FortiFight2.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import lib.PatPeter.SQLibrary.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.cheesium.FortiFight2.API.Engine;
import com.cheesium.FortiFight2.API.Game;
import com.cheesium.FortiFight2.API.LocationSerializer;

public class Backend {
	
	private static MySQL mysql;
	
	public static void init() {
		mysql = new MySQL(Bukkit.getLogger(), "[FortiFight]", Engine.config.getString("Database.Host"), Engine.config.getString("Database.Port")
				, Engine.config.getString("Database.Database"), Engine.config.getString("Database.User"), Engine.config.getString("Database.Pass"));
		try {
			if(!mysql.checkTable("FortiFight_Players")) {
				Engine.log(Level.SEVERE, "The table 'FortiFight_Players' was not found!");
				mysql.query("CREATE TABLE FortiFight_Players (GameID VARCHAR(50), Name VARCHAR(20))");
				Engine.log(Level.INFO, "The table 'FortiFight_Players' was created");
			}
			if(!mysql.checkTable("FortiFight_Games")) {
				Engine.log(Level.SEVERE, "The table 'FortiFight_Games' was not found!");
				mysql.query("CREATE TABLE FortiFight_Games (GameID VARCHAR(50), Loc1 VARCHAR(100), Loc2 VARCHAR(100), AmountofPlayers INT, MaxPlayers INT)");
				Engine.log(Level.INFO, "The table 'FortiFight_Games' was created");
			}
			if(!mysql.checkTable("FortiFight_Blocks")) {
				Engine.log(Level.SEVERE, "The table 'FortiFight_Blocks' was not found!");
				mysql.query("CREATE TABLE FortiFight_Blocks (GameID VARCHAR(50), Name VARCHAR(20), Loc VARCHAR(100))");
				Engine.log(Level.INFO, "The table 'FortiFight_Blocks' was created");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// *************************************************** PLAYERS ********************************************************************
	
	public static String getGameID(String name) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Players WHERE Name = "+name);
			if(rs.first()) {
				return rs.getString("GameID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addToImplemented(String name, String GameID) {
		try {
			PreparedStatement prep = mysql.prepare("INSERT INTO FortiFight_Players (GameID, Name) VALUES (?, ?)");
			prep.setString(1, GameID);
			prep.setString(2, name);
			prep.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setGameID(String name, String GameID) {
		try {
			mysql.query("UPDATE FortiFight_Players SET GameID = '"+GameID+"' WHERE Name = "+name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isImplemented(String name) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Players WHERE Name = "+name);
			if(rs.first()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// *************************************************** BLOCKS ********************************************************************
	
	public static void addBlockToPlayer(String GameID, String name, String loc) {
		try {
			PreparedStatement prep = mysql.prepare("INSERT INTO FortiFight_Blocks (GameID, Name, Loc) VALUES (?, ?, ?)");
			prep.setString(1, GameID);
			prep.setString(2, name);
			prep.setString(3, loc);
			prep.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static void removeBlockFromPlayer(String GameID, String name, String loc) {
		try {
			PreparedStatement prep = mysql.prepare("DELETE FROM FortiFight_Blocks WHERE GameID = ? AND Name = ? AND Loc = ?");
			prep.setString(1, GameID);
			prep.setString(2, name);
			prep.setString(3, loc);
			prep.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkBlock(String GameID, String loc) {
		try {
			PreparedStatement prep = mysql.prepare("Select * FROM FortiFight_Blocks WHERE GameID = ? AND Loc = ?");
			prep.setString(1, GameID);
			prep.setString(2, loc);
			ResultSet rs = prep.executeQuery();
			if(rs.first()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean checkIfHeOwnsIt(String GameID, String name, String loc) {
		try {
			PreparedStatement prep = mysql.prepare("Select * FROM FortiFight_Blocks WHERE GameID = ? AND Name = ? AND Loc = ?");
			prep.setString(1, GameID);
			prep.setString(2, name);
			prep.setString(3, loc);
			ResultSet rs = prep.executeQuery();
			if(rs.first()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// *************************************************** GAME ********************************************************************
	//(GameID VARCHAR(50), Loc1 VARCHAR(100), Loc2 VARCHAR(100), AmountofPlayers INT, MaxPlayers INT)
	public static void createGame(String GameID) {
		try {
			PreparedStatement prep = mysql.prepare("INSERT INTO FortiFight_Games (GameID, AmountofPlayers, MaxPlayers) VALUES (?,?,?)");
			prep.setString(1, GameID);
			prep.setInt(2, 0);
			prep.setInt(3, 10);
			prep.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteGame(String GameID) {
		try {
			mysql.query("DELETE * FROM FortiFight_Games WHERE GameID = '"+GameID+"'");
			mysql.query("DELETE * FROM FortiFight_Blocks WHERE GameID = '"+GameID+"'");
			mysql.query("DELETE * FROM FortiFight_Players WHERE GameID = '"+GameID+"'");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean doesGameIDExist(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				int i = rs.getInt("AmountofPlayers");
				i = i - 1;
				mysql.query("Update FortiFight_Games SET AmountofPlayers = '"+i+"'WHERE GameID = " + GameID);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void setMaxPlayers(String GameID, int AmountofPlayers) {
		try {
			mysql.query("Update FortiFight_Games SET MaxPlayers = '"+AmountofPlayers+"'WHERE GameID = " + GameID);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getMaxPlayers(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				return rs.getInt("MaxPlayers");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void addPlayer(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				int i = rs.getInt("AmountofPlayers");
				i = i + 1;
				mysql.query("Update FortiFight_Games SET AmountofPlayers = '"+i+"'WHERE GameID = " + GameID);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static void removePlayer(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				int i = rs.getInt("AmountofPlayers");
				i = i - 1;
				mysql.query("Update FortiFight_Games SET AmountofPlayers = '"+i+"'WHERE GameID = " + GameID);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getPlayers(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				return rs.getInt("AmountofPlayers");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Location getLocation1(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				LocationSerializer loc = new LocationSerializer(rs.getString("Loc1"));
				return loc.getSimpleLocation();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Location getLocation2(String GameID) {
		try {
			ResultSet rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				LocationSerializer loc = new LocationSerializer(rs.getString("Loc2"));
				return loc.getSimpleLocation();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isLocation1Set(String GameID) {
		ResultSet rs;
		try {
			rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				if(!rs.getString("Loc1").equals("") || rs.getString("Loc1") != null) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isLocation2Set(String GameID) {
		ResultSet rs;
		try {
			rs = mysql.query("Select * FROM FortiFight_Games WHERE GameID = " + GameID);
			if(rs.first()) {
				if(!rs.getString("Loc2").equals("") || rs.getString("Loc2") != null) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void setLocation1(String GameID, String loc) {
		try {
			mysql.query("UPDATE FortiFight_Games SET Loc1 '"+loc+"' WHERE GameID = " + GameID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setLocation2(String GameID, String loc) {
		try {
			mysql.query("UPDATE FortiFight_Games SET Loc2 '"+loc+"' WHERE GameID = " + GameID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
