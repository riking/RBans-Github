package net.zxq.rastrosgonegriefing.listeners;

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
import org.bukkit.plugin.Plugin;

public class RBansPlayerListener implements Listener
{
	private RBans plugin;
	public static Material[] tnt = {Material.TNT};
	public static Material[] bedrock = {Material.BEDROCK};
	
	public RBansPlayerListener(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		plugin.playersJoined.add(event.getPlayer().getDisplayName() + " has joined the server using the ip " + player.getAddress());
		
		player.sendMessage(ChatColor.GOLD + "This server is running RBans v" + plugin.getDescription().getVersion() + " by Rastro!");
	}
	
	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event)
	{
			Block block = event.getBlock();
			Player player = event.getPlayer();
			
			plugin.brokenBlocks.add(event.getPlayer().getDisplayName() + " destroyed " + block.getType() + " at " + "X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
	}
	
	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event)
	{
			Material block1 = event.getBlock().getType();
			Block block = event.getBlock();
			Player player = event.getPlayer();
			
			//TNT NOTIFIER
			for(Material blocked : tnt)
			{
				if(blocked == block1)
				{
					if(player.isOp())
					{
						
					}else{
						event.getBlock().setType(Material.AIR);
						plugin.placedBlocks.add(event.getPlayer().getDisplayName() + " tried placing TNT at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
						plugin.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " tried placing" + ChatColor.RED + " TNT " + ChatColor.WHITE + "at " + "X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
					}
				}
			}
			
			//BEDROCK NOTIFIER
			for(Material blocked : bedrock)
			{
				if(blocked == block1)
				{
					if(player.isOp())
					{
						
					}else{
						event.getBlock().setType(Material.AIR);
						plugin.placedBlocks.add(event.getPlayer().getDisplayName() + " tried placing BEDROCK at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
						plugin.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " tried placing " + ChatColor.RED + " BEDROCK " + ChatColor.WHITE + "at " + "X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
					}
				}
			}
			
			plugin.placedBlocks.add(event.getPlayer().getDisplayName() + " tried placing " + block.getType() + " at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
	}
	
	@EventHandler()
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		
		plugin.playerChat.add(event.getPlayer().getDisplayName() + ": " + event.getMessage().toString());
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
}