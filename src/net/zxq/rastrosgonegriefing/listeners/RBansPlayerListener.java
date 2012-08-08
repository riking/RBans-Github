package net.zxq.rastrosgonegriefing.listeners;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class RBansPlayerListener extends RBans implements Listener {

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.playersJoined.add(event.getPlayer().getDisplayName()
				+ " has joined the server using the ip " + event.getPlayer().getAddress());
		event.getPlayer().sendMessage(ChatColor.GREEN
				+ "Welcome "
				+ event.getPlayer().getDisplayName()
				+ ". You are being logged by RBans for everything you do, so don't be bad!");
	}

	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();

		this.brokenBlocks.add(event.getPlayer().getDisplayName()
				+ " destroyed " + block.getType() + " at " + "X:"
				+ block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
	}

	public void log(Block block, Player player) {
		this.placedBlocks.add(player.getName() + " tried to place a "
				+ block.getType().toString().toLowerCase() + " at "
				+ block.getX() + " , " + block.getY() + " , " + block.getZ());
	}

	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getPlayer() == null || event.getBlock() == null) {
			return;
		}
		if (event.getPlayer().isOp()
				|| event.getPlayer().hasPermission("bypass permission")) {
			return;
		}
		if (event.getBlock().getType() == Material.TNT) {
			event.setCancelled(true);
			log(event.getBlock(), event.getPlayer());
		} else if (event.getBlock().getType() == Material.BEDROCK) {
			event.setCancelled(true);
			log(event.getBlock(), event.getPlayer());
		} else if (event.getBlock().getType() == Material.LAVA_BUCKET) {
			event.setCancelled(true);
			log(event.getBlock(), event.getPlayer());
		}
	}

	@EventHandler()
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		this.playerChat.add(event.getPlayer().getDisplayName() + ": "
				+ event.getMessage().toString());
	}

	@SuppressWarnings("unused")
	private boolean permCheck(Player player, String permission) {
		if (player.isOp() || player.hasPermission(permission))
			return true;
		return false;
	}
}
