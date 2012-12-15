package com.cheesium.FortiFight2;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.cheesium.FortiFight2.API.Engine;
import com.cheesium.FortiFight2.Commands.CommandListener;

public class FortiFight extends JavaPlugin {
	
	public static String prefix;
	public static PluginDescriptionFile file;
	public static FortiFight plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		file = getDescription();
		prefix = "[" + file.getName() + "] " +  file.getVersion() + ": ";
		Engine.onStartUp();
		getCommand("fort").setExecutor(new CommandListener());
		log(Level.INFO, "is now enabled!");
	}
	
	@Override
	public void onDisable() {
	}
	
	public static void log(Level level, String log) {
		Bukkit.getLogger().log(level, prefix + log);
	}
}
