package net.zxq.rastrosgonegriefing.listeners;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class PlayerLoginListener implements Listener
{
	private RBans plugin;
	public PlayerLoginListener(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		String playername = event.getPlayer().getName();
		
		if(plugin.bannedPlayers.contains(playername))
		{
			event.setKickMessage("You have been banned from " + plugin.getServer().getName() + ".");
			event.setResult(Result.KICK_BANNED);
		}
	}
}