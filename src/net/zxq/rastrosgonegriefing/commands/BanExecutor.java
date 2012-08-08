package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanExecutor implements CommandExecutor
{
	private RBans plugin;
	
	public BanExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(!permCheck((Player)sender, "rbans.ban"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
		}
		
		if(args.length != 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /ban <player>");
			return true;
		}
		
		Player ban = this.plugin.getServer().getPlayer(args[0]);
		this.plugin.bannedPlayers.add(args[0]);
		if(ban != null)
		{
			ban.setBanned(true);
			ban.kickPlayer("You have been banned from " + this.plugin.getServer().getName() + ".");
		}
		sender.sendMessage(ChatColor.GREEN + args[0] + " has been banned from " + this.plugin.getServer().getName() + " and will be added from the list on next server reload.");
		Bukkit.dispatchCommand(sender, "ban " + args[0]);
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
	
}