package io.github.leduyquang753.Teleportation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WorldTeleportation {
	Player teleporter;
	int type;
	long time;
	
	public WorldTeleportation(Player teleper, int type) {
		teleporter = teleper;
		this.type = type;
		time = Main.time + Main.delay[type];
	}
	
	public Player getTeleporter() {
		return teleporter;
	}
	
	public int getType() {
		return type;
	}
	
	public void commence() {
		if (Main.time == time) {
		switch (type) {
		case 0: 
			FileConfiguration config = Main.getLocationsStatic();
			final long x = config.getLong("locations." + teleporter.getName() + ".x");
			final long y = config.getLong("locations." + teleporter.getName() + ".y");
			final long z = config.getLong("locations." + teleporter.getName() + ".z");
			final double yaw = config.getDouble("locations." + teleporter.getName() + ".yaw");
			final double pitch = config.getDouble("locations." + teleporter.getName() + ".pitch");
			teleporter.teleport(new Location(Bukkit.getWorlds().get(0), x, y, z, (float) yaw, (float) pitch));
			break;
		case 1:
			Location loc = Bukkit.getWorlds().get(1).getSpawnLocation();
			teleporter.teleport(loc);
			break;
		case 2:
			Location loc2 = Bukkit.getWorlds().get(2).getSpawnLocation();
			teleporter.teleport(loc2);
			break;
		}
		Main.removeAllTp(teleporter);
		}
	}
}
