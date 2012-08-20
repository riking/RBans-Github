package net.zxq.rastrosgonegriefing.listeners;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class PlayerLoginListener extends RBans implements Listener {
	/**
	 * -Removed unneeded variable
	 * -Used proper method to enforce ban
	 * @param event
	 */
	private RBans plugin;
	
	public PlayerLoginListener(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerLogin(PlayerLoginEvent event) {
		String playerName = event.getPlayer().getName();		
		if(plugin.bannedPlayers.contains(playerName))
		{
			event.setKickMessage("You have been banned.");
			event.setResult(Result.KICK_BANNED);
		}
		
		if(plugin.tban.contains(playerName))
		{
			event.disallow(Result.KICK_BANNED, "You have been banned.");
		}
	}
}