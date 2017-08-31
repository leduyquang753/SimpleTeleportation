package io.github.leduyquang753.Teleportation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	private Main main;
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sethome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			if (player.getWorld() != Bukkit.getWorlds().get(0)) {
				sender.sendMessage(ChatColor.RED + "You must be in the overworld in order to set home.");
				return true;
			}
			final long x = player.getLocation().getBlockX();
			final long y = player.getLocation().getBlockY();
			final long z = player.getLocation().getBlockZ();
			final double yaw = player.getLocation().getYaw();
			final double pitch = player.getLocation().getPitch();
			FileConfiguration config = main.getLocations();
			config.set("locations." + player.getName() + ".x", x);
			config.set("locations." + player.getName() + ".y", y);
			config.set("locations." + player.getName() + ".z", z);
			config.set("locations." + player.getName() + ".yaw", yaw);
			config.set("locations." + player.getName() + ".pitch", pitch);
			main.saveLocations();
			sender.sendMessage(ChatColor.YELLOW + "Your home was set to:  " + x + "  " + y + "  " + z + "  " + yaw + "  " + pitch);
			main.getLogger().info(player.getName() + "'s home was set to:  " + x + "  " + y + "  " + z + "  " + yaw + "  " + pitch);
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("home")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!main.getLocations().contains("locations." + player.getName())) {
				player.sendMessage(ChatColor.RED + "You didn't set a home. Set one by doing " + ChatColor.YELLOW + "/sethome" + ChatColor.RED + " in the overworld.");
				return true;
			}
			if (player.getWorld() == Bukkit.getWorlds().get(2)) if (!Main.allowTpInEnd && (Main.fairPlay || !sender.isOp())) {
				player.sendMessage(ChatColor.RED + "You cannot teleport to home because you are in The End.");
				return true;
			}
			player.sendMessage(ChatColor.YELLOW + "You will teleport to home in " + Main.delay[0] + " seconds.");
			String msg = "";
			switch (Main.cancelType[0]) {
			case 1: msg = "Move"; break;
			case 2: msg = "Sneak"; break;
			case 3: msg = "Jump"; break;
			}
			Main.removeAllTp(player);
			if (msg != "") player.sendMessage(ChatColor.YELLOW + msg + " to cancel.");
			Main.wtps.add(new WorldTeleportation(player, 0));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("nether")) {
			if (!Main.Nether && (Main.fairPlay || !sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "This command is disabled.");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			if (player.getWorld() == Bukkit.getWorlds().get(2)) if (!Main.allowTpInEnd && (Main.fairPlay || !sender.isOp())) {
				player.sendMessage(ChatColor.RED + "You cannot teleport to the Nether because you are in The End.");
				return true;
			}
			player.sendMessage(ChatColor.YELLOW + "You will teleport to the Nether in " + Main.delay[1] + " seconds.");
			String msg = "";
			switch (Main.cancelType[1]) {
			case 1: msg = "Move"; break;
			case 2: msg = "Sneak"; break;
			case 3: msg = "Jump"; break;
			}
			if (msg != "") player.sendMessage(ChatColor.YELLOW + msg + " to cancel.");
			Main.wtps.add(new WorldTeleportation(player, 1));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("theend")) {
			if (!Main.TheEnd && (Main.fairPlay || !sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "This command is disabled.");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			player.sendMessage(ChatColor.YELLOW + "You will teleport to The End in " + Main.delay[2] + " seconds. Prepare yourself...");
			String msg = "";
			switch (Main.cancelType[2]) {
			case 1: msg = "Move"; break;
			case 2: msg = "Sneak"; break;
			case 3: msg = "Jump"; break;
			}
			Main.removeAllTp(player);
			if (msg != "") player.sendMessage(ChatColor.YELLOW + msg + " to cancel.");
			Main.wtps.add(new WorldTeleportation(player, 2));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (!Main.PlayerTp && (Main.fairPlay || !sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "This command is disabled.");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			if (player.getWorld() == Bukkit.getWorlds().get(2)) if (!Main.allowTpInEnd && (Main.fairPlay || !sender.isOp())) {
				player.sendMessage(ChatColor.RED + "You cannot teleport to a player because you are in The End.");
				return true;
			}
			
			if (Main.checkPlayer(player)) {
				player.sendMessage(ChatColor.RED + "You cannot do this because you are having another request. Cancel your request by doing " + ChatColor.YELLOW + "/tpcancel" + ChatColor.RED + ".");
				return true;
			}
			if (args.length == 0) return false;
			boolean exist = false;
			Player recvr = player;
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName() == args[0]) exist = true;
				recvr = p;
			}
			if (!exist) {
				player.sendMessage(ChatColor.RED + "The player isn't online now.");
				return true;
			}
			player.sendMessage(ChatColor.YELLOW + "You have sent a teleport request to " + ChatColor.BLUE + ". They have " + Main.requestTimeout + " seconds to accept.");
			recvr.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.YELLOW + " wants to teleport to you.");
			recvr.sendMessage(ChatColor.BLUE + "/tpaccept " + player.getName() + ChatColor.YELLOW + " to accept.");
			recvr.sendMessage(ChatColor.BLUE + "/tpdeny " + player.getName() + ChatColor.YELLOW + " to deny.");
			Main.requests.add(new TpRequest(player, recvr));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpcancel")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!Main.checkPlayer(player)) {
				player.sendMessage(ChatColor.RED + "You don't have any requests...");
				return true;
			}
			Main.removeRequest(player);
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpaccept")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			boolean exist = false;
			Player sder = player;
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName() == args[0]) exist = true;
				sder = p;
			}
			if (!exist) {
				player.sendMessage(ChatColor.RED + "The player isn't online now.");
				return true;
			}
			if (!Main.checkPlayer(sder)) {
				player.sendMessage(ChatColor.RED + "That player didn't send a request to you.");
			}
			player.sendMessage(ChatColor.YELLOW + "You have accepted the request from " + ChatColor.BLUE + sder.getName() + ChatColor.YELLOW + ".");
			sder.sendMessage(ChatColor.YELLOW + "You will teleport to " + ChatColor.BLUE + player.getName() + ChatColor.YELLOW + " in " + Main.delay[3] + " seconds.");
			String msg = "";
			switch (Main.cancelType[0]) {
			case 1: msg = "Move"; break;
			case 2: msg = "Sneak"; break;
			case 3: msg = "Jump"; break;
			}
			Main.removeAllTp(sder);
			if (msg != "") player.sendMessage(ChatColor.YELLOW + msg + " to cancel.");
			Main.tps.add(new PlayerTeleportation(sder, player));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpaccept")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			boolean exist = false;
			Player sder = player;
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName() == args[0]) exist = true;
				sder = p;
			}
			if (!exist) {
				player.sendMessage(ChatColor.RED + "The player isn't online now.");
				return true;
			}
			if (!Main.checkPlayer(sder)) {
				player.sendMessage(ChatColor.RED + "That player didn't send a request to you.");
			}
			player.sendMessage(ChatColor.YELLOW + "You have denied the request from " + ChatColor.BLUE + sder.getName() + ChatColor.YELLOW + ".");
			sder.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " has denied your request.");
			Main.removeRequest(sder);
			return true;
		}
		return false;
	}
}
