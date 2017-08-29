package io.github.leduyquang753.Teleportation;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	Plugin plugin = this;
	public static boolean Nether = true;
	public static boolean TheEnd = true;
	public static boolean fairPlay = false;
	public static boolean allowTpInEnd = false;
	
	@Override
	public void onEnable() {
		getLogger().info("Loading config...");
		try {
	        if (!getDataFolder().exists()) {
	            getDataFolder().mkdirs();
	        }
	        File file = new File(getDataFolder(), "config.yml");
	        if (!file.exists()) {
	            getLogger().warning("The config.yml file was not found. We are creating it...");
	            saveDefaultConfig();
	        } else {
	            Nether = plugin.getConfig().getBoolean("nether");
	            TheEnd = plugin.getConfig().getBoolean("theend");
	            fairPlay = plugin.getConfig().getBoolean("fairplay");
	            allowTpInEnd = plugin.getConfig().getBoolean("allowtpinend");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		getLogger().info("Registering events and executors...");
	    Bukkit.getPluginCommand("sethome").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("home").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("nether").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("theend").setExecutor(new Commands(this));
	    getLogger().info("The plugin has been successfully enabled.");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("The plugin has been disabled.");
		getLogger().info("Plugin by Le Duy Quang (github.com/leduyquang753).");
	}
}
