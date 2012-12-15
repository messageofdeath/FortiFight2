package com.cheesium.FortiFight2.API;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cheesium.FortiFight2.FortiFight;

public class Engine {
	
	public static File file;
	public static FileConfiguration config;
	public static FortiFight plugin = FortiFight.plugin;
	
	public static void onStartUp() {
		file = new File(plugin.getDataFolder(), "config.yml");
		try {
			// *** Config ***
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				copy(plugin.getResource("config.yml"), file);
			}
			config = new YamlConfiguration();
			config.load(file);
		}catch(Exception e) {}
	}
	
	public static void log(Level level, String log) {
		FortiFight.log(level, log);
	}
	
	private static void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}