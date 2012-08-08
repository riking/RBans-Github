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

public class RBansPlayerListener extends RBans implements Listener
{
	public static Material[] tnt = {Material.TNT};
	public static Material[] bedrock = {Material.BEDROCK};
	
	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		this.playersJoined.add(event.getPlayer().getDisplayName() + " has joined the server using the ip " + player.getAddress());
		player.sendMessage(ChatColor.GREEN + "Welcome " + player.getDisplayName() + ". You are being logged by RBans for everything you do, so don't be bad!");
	}
	
	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event)
	{
			Block block = event.getBlock();
			
			this.brokenBlocks.add(event.getPlayer().getDisplayName() + " destroyed " + block.getType() + " at " + "X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
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
						this.placedBlocks.add(ChatColor.RED + event.getPlayer().getDisplayName() + ChatColor.WHITE + " tried placing TNT at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
						getServer().broadcastMessage(event.getPlayer().getDisplayName() + " tried placing" + ChatColor.RED + " TNT " + ChatColor.WHITE + "at " + "X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
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
						this.placedBlocks.add(ChatColor.RED + event.getPlayer().getDisplayName() + ChatColor.WHITE + " tried placing BEDROCK at X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
						getServer().broadcastMessage(event.getPlayer().getDisplayName() + " tried placing" + ChatColor.RED + " BEDROCK " + ChatColor.WHITE + "at " + "X:" + block.getX() + " Y:" + block.getY() + " Z:" + block.getZ());
					}
				}
			}
	}
	
	@EventHandler()
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.getPlayer();
		
		this.playerChat.add(event.getPlayer().getDisplayName() + ": " + event.getMessage().toString());
	}
	
	@SuppressWarnings("unused")
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
}
