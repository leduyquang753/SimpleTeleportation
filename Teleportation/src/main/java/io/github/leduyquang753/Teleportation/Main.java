package io.github.leduyquang753.Teleportation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.leduyquang753.Teleportation.TpRequest;

public class Main extends JavaPlugin {
	static Plugin plugin;
	public static boolean Nether = true;
	public static boolean TheEnd = true;
	public static boolean PlayerTp = true;
	public static boolean fairPlay = false;
	public static boolean allowTpInEnd = false;
	public static boolean mustRequest = true;
	public static long time = 0;
	public static int delay[] = {5, 5, 60, 5};
	public static int cancelType[] = {1, 1, 0, 2};
//  delay[] and cancelType[] are arrays which store delay time and teleportation cancel type.
//  In each array, the 4 integers act as {Home, Nether, The End, Player}.
//  In the array cancelType[]:   0 means NONE
//                               1 means MOVE
//                               2 means SNEAK
	public static int requestTimeout = 60;
	public static ArrayList<TpRequest> requests = new ArrayList<TpRequest>();
	public static ArrayList<PlayerTeleportation> tps = new ArrayList<PlayerTeleportation>();
	public static ArrayList<WorldTeleportation> wtps = new ArrayList<WorldTeleportation>();
	public static FileConfiguration locations = new YamlConfiguration();
	
	public static Plugin getInstance() {
		return plugin;
	}
	
	public static FileConfiguration getLocationsStatic() {
		return locations;
	}
	
	@Override
	public void onEnable() {
		plugin = this;
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
	            PlayerTp = plugin.getConfig().getBoolean("playerteleport");
	            mustRequest = plugin.getConfig().getBoolean("mustrequest");
	            delay[0] = plugin.getConfig().getInt("delay.home");
	            delay[1] = plugin.getConfig().getInt("delay.nether");
	            delay[2] = plugin.getConfig().getInt("delay.theend");
	            delay[3] = plugin.getConfig().getInt("delay.player");
	            
	            // Get the teleportation cancel type
	            cancelType[0] = checkCancelType("home");
	            cancelType[1] = checkCancelType("nether");
	            cancelType[2] = checkCancelType("theend");
	            cancelType[3] = checkCancelType("player");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		File locfile = new File(getDataFolder(), "locations.yml");
		try {
	        if (!getDataFolder().exists()) {
	            getDataFolder().mkdirs();
	        }
	        if (!locfile.exists()) {
	            getLogger().warning("The locations.yml file was not found. We are creating it...");
	            plugin.saveResource("locations.yml", true);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			locations.load(locfile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		getLogger().info("Registering events and executors...");
		Bukkit.getPluginCommand("tpa").setExecutor(new Commands(this));
		Bukkit.getPluginCommand("tpaccept").setExecutor(new Commands(this));
		Bukkit.getPluginCommand("tpcancel").setExecutor(new Commands(this));
		Bukkit.getPluginCommand("tpdeny").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("sethome").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("home").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("nether").setExecutor(new Commands(this));
	    Bukkit.getPluginCommand("theend").setExecutor(new Commands(this));
	    Functions.doTimeLoop();
	    Bukkit.getPluginManager().registerEvents(new Events(), this);
	    getLogger().info("The plugin has been successfully enabled.");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("The plugin has been disabled.");
		getLogger().info("Plugin by Le Duy Quang (github.com/leduyquang753).");
	}
	
	public int checkCancelType(String str) {
		switch (plugin.getConfig().getString("cancelevent." + str)) {
		case "NONE" : return 0;
		case "MOVE" : return 1;
		case "SNEAK": return 2;
		case "JUMP" : return 3;
		}
		getLogger().warning("The cancel event config '" + str + "' was not valid. The default MOVE will be used.");
		return 1;
	}
	
	public static void removeRequest(Player sender) {
		int index = -1;
		for (int i=0; i<requests.size(); i++) {
			if (requests.get(i).getSender() == sender) index=i;
		}
		if (index != -1) requests.remove(index);
	}
	
	public boolean doRequest(Player sender) {
		int index = -1;
		for (int i=0; i<requests.size(); i++) {
			if (requests.get(i).getSender() == sender) index=i;
		}
		if (index != -1) {
			TpRequest tp = requests.get(index);
			tps.add(new PlayerTeleportation(tp.getSender(), tp.getReceiver()));
			requests.remove(index);
			return false;
		}
		return true;
	}
	
	public static boolean checkPlayer(Player check) {
		for (int i=0; i<requests.size(); i++) {
			if (requests.get(i).getSender() == check) return true;
		}
		for (int i=0; i<tps.size(); i++) {
			if (tps.get(i).getTeleporter() == check) return true;
		}
		return false;
	}
	
	public FileConfiguration getLocations() {
		return locations;
	}
	
	public void saveLocations() {
		File file = new File(getDataFolder(), "locations.yml");
		try {
			locations.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean removeAllTp(Player sender) {
		int index = -1;
		int index2 = -1;
		for (int i=0; i<wtps.size(); i++)
			if (wtps.get(i).getTeleporter() == sender) index=i;
		for (int i=0; i<tps.size(); i++)
			if (tps.get(i).getTeleporter() == sender) index2=i;
		if (index != -1) { wtps.remove(index); return true; }
		if (index2 != -1) { tps.remove(index2); return true; }
		return false;
	}
	
	public static boolean removeTp(Player sender, int type) {
		if (type < 3) {
			for (WorldTeleportation wtp : wtps) if (wtp.getTeleporter() == sender && wtp.getType() == type) {
				wtps.remove(wtp); return true;
			}
		} else
			for (PlayerTeleportation tp : tps) if (tp.getTeleporter() == sender) {
				tps.remove(tp); return true;
			}
		return false;
	}
}
