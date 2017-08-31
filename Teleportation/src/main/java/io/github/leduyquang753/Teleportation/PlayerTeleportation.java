package io.github.leduyquang753.Teleportation;

import org.bukkit.entity.Player;

public class PlayerTeleportation {
	Player teleper;
	Player receivr;
	long eventTime;
	public PlayerTeleportation(Player teleporter, Player receiver) {
		teleper = teleporter;
		receivr = receiver;
		eventTime = Main.time + Main.delay[4];
	}
	
	public Player getTeleporter() {
		return teleper;
	}
	
	public Player getReceiver() {
		return receivr;
	}
	
	public void commence() {
		if (eventTime == Main.time) {
			teleper.teleport(receivr);
			Main.removeAllTp(teleper);
		}
	}
}
