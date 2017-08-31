package io.github.leduyquang753.Teleportation;

import org.bukkit.entity.Player;

public class TpRequest {
	Player sder, receivr;
	long time;
	public TpRequest(Player sender, Player receiver) {
		sder = sender;
		receivr = receiver;
		time = Main.time + Main.requestTimeout;
	}
	
	public Player getSender() {
		return sder;
	}
	
	public Player getReceiver() {
		return receivr;
	}
	
	public long getTimeOut() {
		return time;
	}
}
