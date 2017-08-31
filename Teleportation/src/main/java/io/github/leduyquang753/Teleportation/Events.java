package io.github.leduyquang753.Teleportation;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Events implements Listener {
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		for (int i = 0; i < 4; i++) if (Main.cancelType[i] == 1) if (Main.removeTp(event.getPlayer(), i))
			event.getPlayer().sendMessage(ChatColor.YELLOW + "You have cancelled the teleport.");
	}
	
	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		if (event.isSneaking()) for (int i = 0; i < 4; i++) if (Main.cancelType[i] == 2) if (Main.removeTp(event.getPlayer(), i))
			event.getPlayer().sendMessage(ChatColor.YELLOW + "You have cancelled the teleport.");
	}
}
