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
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (this.bannedPlayers.contains(event.getPlayer().getName())) {
			event.disallow(Result.KICK_BANNED, "You have been banned from "
					+ getServer().getName() + ".");
		}
	}
}