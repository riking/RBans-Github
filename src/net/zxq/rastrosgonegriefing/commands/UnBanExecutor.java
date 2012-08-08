package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBanExecutor implements CommandExecutor
{
	private RBans plugin;
	
	public UnBanExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(!permCheck((Player)sender, "rbans.unban"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
		}
		
		if(args.length != 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /unban <player>");
			return true;
		}
		
		Player unban = this.plugin.getServer().getPlayer(args[0]);
		this.plugin.bannedPlayers.remove(args[0]);
		if(unban != null)
		{
			unban.setBanned(false);
		}
		sender.sendMessage(ChatColor.GREEN + args[0] + " has been pardoned from " + this.plugin.getServer().getName() + " and will be removed from the list on next server reload.");
		
		Bukkit.dispatchCommand(sender, "pardon " + args[0]);
		//Bukkit.dispatchCommand(sender, "unban " + args[0]);
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
	
}