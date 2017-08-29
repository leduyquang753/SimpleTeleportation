package io.github.leduyquang753.Teleportation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
			FileConfiguration config = main.getConfig();
			config.set("locations." + player.getName() + ".x", x);
			config.set("locations." + player.getName() + ".y", y);
			config.set("locations." + player.getName() + ".z", z);
			config.set("locations." + player.getName() + ".yaw", yaw);
			config.set("locations." + player.getName() + ".pitch", pitch);
			main.saveConfig();
			sender.sendMessage(ChatColor.YELLOW + "Your home was set to:  " + x + "  " + y + "  " + z + "  " + yaw + "  " + pitch);
			main.getLogger().info(player.getName() + "''s home was set to:  " + x + "  " + y + "  " + z + "  " + yaw + "  " + pitch);
		}
		
		if (cmd.getName().equalsIgnoreCase("home")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can do this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!main.getConfig().contains(player.getName())) {
				player.sendMessage(ChatColor.RED + "You didn't set a home. Set one by doing " + ChatColor.YELLOW + "/sethome" + ChatColor.RED + " in the overworld.");
				return true;
			}
			if (player.getWorld() == Bukkit.getWorlds().get(2)) if (!Main.allowTpInEnd && (Main.fairPlay || !sender.isOp())) {
				player.sendMessage(ChatColor.RED + "You cannot teleport to home because you are in The End.");
				return true;
			}
			FileConfiguration config = main.getConfig();
			final long x = config.getLong("locations." + player.getName() + ".x");
			final long y = config.getLong("locations." + player.getName() + ".y");
			final long z = config.getLong("locations." + player.getName() + ".z");
			final double yaw = config.getDouble("locations." + player.getName() + ".yaw");
			final double pitch = config.getDouble("locations." + player.getName() + ".pitch");
			player.sendMessage(ChatColor.YELLOW + "You will teleport to home in 5 seconds.");
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				  public void run() {
				      player.teleport(new Location(Bukkit.getWorlds().get(0), x, y, z, (float) yaw, (float) pitch));
				  }
				}, 100L);
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
			player.sendMessage(ChatColor.YELLOW + "You will teleport to the Nether in 5 seconds.");
			Location loc = Bukkit.getWorlds().get(1).getSpawnLocation();
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				  public void run() {
				      player.teleport(loc);
				  }
				}, 100L);
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
			player.sendMessage(ChatColor.YELLOW + "You will teleport to The End in 1 minute. Prepare yourself...");
			Location loc = Bukkit.getWorlds().get(2).getSpawnLocation();
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				  public void run() {
				      player.teleport(loc);
				  }
				}, 1200L);
		}
		return false;
	}

}
