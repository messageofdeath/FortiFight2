package com.cheesium.FortiFight2.Database;

import com.cheesium.FortiFight2.API.Game;

public abstract class Backend {
	
	// ********************** Players ************************
	public abstract String getGameID(String name);
	
	public abstract void setGameID(String name);
	
	public abstract Game getGame(String name);
	
	public abstract void setGame(String name, String GameID);
	// ********************** Blocks *************************
	public abstract void addBlockToPlayer(String GameID, String name, String loc);
	
	public abstract void removeBlockFromPlayer(String GameID, String name, String loc);
	
	public abstract boolean checkIfHeOwnsIt(String GameID, String name, String loc);
	
	public abstract boolean isWithinAnotherBlock(String GameID, String name, String loc);
	// ********************** Game ***************************
}
