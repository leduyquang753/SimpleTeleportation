package io.github.leduyquang753.Teleportation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Functions {
	private static boolean ok = true;
	
	public static void doTimeLoop() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			  public void run() {
				  Main.time++;
			      if (Main.requests.size()>0) for (TpRequest rq : Main.requests) {
			    	  if (Main.time == rq.getTimeOut()) {
			    		  rq.getSender().sendMessage(ChatColor.YELLOW + "The teleport request to " + ChatColor.BLUE + rq.getReceiver().getName() + ChatColor.YELLOW + " has expired.");
			    		  rq.getReceiver().sendMessage(ChatColor.YELLOW + "The teleport request from " + ChatColor.BLUE + rq.getSender().getName() + ChatColor.YELLOW + " has expired.");
			    		  Main.requests.remove(rq);
			    	  }
			    	  
			    	  if (!rq.getSender().isOnline()) {
			    		  rq.getReceiver().sendMessage(ChatColor.YELLOW + "The teleport request from " + ChatColor.BLUE + rq.getSender().getName() + ChatColor.YELLOW + " has cancelled because the requester has quit.");
			    		  Main.requests.remove(rq);
			    	  }
			    	  if (!rq.getReceiver().isOnline()) {
			    		  rq.getSender().sendMessage(ChatColor.YELLOW + "The teleport request to " + ChatColor.BLUE + rq.getReceiver().getName() + ChatColor.YELLOW + " has cancelled because the receiver has quit.");
			    		  Main.requests.remove(rq);
			    	  }
			      }
			      
			      if (Main.tps.size()>0) for (PlayerTeleportation tp : Main.tps) {
			    	  ok = true;
			    	  if (!tp.getTeleporter().isOnline()) {
			    		  tp.getReceiver().sendMessage(ChatColor.YELLOW + "The teleport from " + ChatColor.BLUE + tp.getTeleporter().getName() + ChatColor.YELLOW + " has cancelled because the requester has quit.");
			    		  Main.tps.remove(tp); ok = false;
			    	  }
			    	  if (!tp.getReceiver().isOnline()) {
			    		  tp.getTeleporter().sendMessage(ChatColor.YELLOW + "The teleport to " + ChatColor.BLUE + tp.getReceiver().getName() + ChatColor.YELLOW + " has cancelled because the receiver has quit.");
			    		  Main.tps.remove(tp); ok = false;
			    	  }
			    	  if (ok) tp.commence();
			      }
			      
			      if (Main.wtps.size()>0) for (WorldTeleportation wtp : Main.wtps) {
			    	  wtp.commence();
			      }
			  }
			}, 0L, 20L);
	}
}
