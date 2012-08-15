package net.zxq.rastrosgonegriefing.listeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class RBansPlayerListener implements Listener {
	private RBans plugin;
	List<String> bannedFromChat = new ArrayList<String>();
	public static Material[] tnt = { Material.TNT };
	public static Material[] bedrock = { Material.BEDROCK };
	
	Calendar Current = Calendar.getInstance();
    int MO = Current.get(Calendar.MONTH);
    int D = Current.get(Calendar.DAY_OF_MONTH);
    int H = Current.get(Calendar.HOUR_OF_DAY);
    int M = Current.get(Calendar.MINUTE);
    int S = Current.get(Calendar.SECOND);
    int Y = Current.get(Calendar.YEAR);

	public RBansPlayerListener(RBans plugin) {
		this.plugin = plugin;
	}

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		plugin.playersJoined.add(event.getPlayer().getDisplayName()
				+ " has joined the server using the ip " + player.getAddress());

		player.sendMessage(ChatColor.GOLD + "This server is running RBans v"
				+ plugin.getDescription().getVersion() + " by Rastro!");
		
		if(player.getName().equals("EpicBeast31"))
		{
			player.setDisplayName("[RDEV]EpicBeast31");
		}
	}

	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		plugin.brokenBlocks.add(event.getPlayer().getDisplayName()
				+ " destroyed " + block.getType() + " at " + "X:"
				+ block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
	}

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		Material block1 = event.getBlock().getType();
		Block block = event.getBlock();
		Player player = event.getPlayer();
		Inventory inv = player.getInventory();
		if (plugin.getServer().getPluginManager().getPlugin("BlockIt") == null) {
			// TNT NOTIFIER
			for (Material blocked : tnt) {
				if (blocked == block1) {
					if (player.isOp()) {

					} else {
						event.getBlock().setType(Material.AIR);
						plugin.placedBlocks.add(event.getPlayer()
								.getDisplayName()
								+ " tried placing TNT at X:"
								+ block.getX()
								+ " Y:"
								+ block.getY()
								+ " Z:"
								+ block.getZ());
						plugin.getServer().broadcastMessage(
								event.getPlayer().getDisplayName()
										+ " tried placing" + ChatColor.RED
										+ " TNT " + ChatColor.WHITE + "at "
										+ "X:" + block.getX() + " Y:"
										+ block.getY() + " Z:" + block.getZ());
						inv.remove(64);
						inv.remove(Material.TNT);
					}
				}
			}

			// BEDROCK NOTIFIER
			for (Material blocked : bedrock) {
				if (blocked == block1) {
					if (player.isOp()) {

					} else {
						event.getBlock().setType(Material.AIR);
						plugin.placedBlocks.add(event.getPlayer()
								.getDisplayName()
								+ " tried placing BEDROCK at X:"
								+ block.getX()
								+ " Y:" + block.getY() + " Z:" + block.getZ());
						plugin.getServer().broadcastMessage(
								event.getPlayer().getDisplayName()
										+ " tried placing " + ChatColor.RED
										+ " BEDROCK " + ChatColor.WHITE + "at "
										+ "X:" + block.getX() + " Y:"
										+ block.getY() + " Z:" + block.getZ());
					}
				}
			}
			plugin.placedBlocks.add("[" + D + "/" + Y + "]" + event.getPlayer().getDisplayName()
					+ " tried placing " + block.getType() + " at X:"
					+ block.getX() + " Y:" + block.getY() + " Z:"
					+ block.getZ());
		}
	}

	@EventHandler()
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String playerName = event.getPlayer().getName();
		Player player = event.getPlayer();

		plugin.playerChat.add(event.getPlayer().getDisplayName() + ": "
				+ event.getMessage().toString());

		if (plugin.mutedPlayers.contains(playerName)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED
					+ "[RBans] You have been muted. You will be able to chat with others when an admin un-mutes you.");
		}
	}

	private boolean permCheck(Player player, String permission) {
		if (player.isOp() || player.hasPermission(permission))
			return true;
		return false;
	}
}