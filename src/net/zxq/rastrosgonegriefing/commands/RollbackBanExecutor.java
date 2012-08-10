package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RollbackBanExecutor extends RBans implements CommandExecutor
{
	private RBans plugin;
	
	public RollbackBanExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(!permCheck((Player)sender, "rbans.rollbackban"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
		}
		
		if(args.length != 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /rollbackban <player>");
			return true;
		}
		
		Player ban = this.plugin.getServer().getPlayer(args[0]);
		this.plugin.bannedPlayers.add(args[0]); //ADDING TO BANNED PLAYERS TXT FILE
		
		if(plugin.getServer().getPluginManager().getPlugin("CoreProtect") != null) {
			Bukkit.dispatchCommand(sender, "co rollback u:" + args[0] + " t:9h");
			return true;
		} else if(plugin.getServer().getPluginManager().getPlugin("SWatchdog") != null) {
			Bukkit.dispatchCommand(sender, "wundo p:" + args[0] + " 9999 9999 0 0 0 0 0");
			return true;
		} else if(plugin.getServer().getPluginManager().getPlugin("HawkEye") != null) {
			Bukkit.dispatchCommand(sender, "hawk rollback " + args[0] + " a:* t: 9999y");
			return true;
		} else if(plugin.getServer().getPluginManager().getPlugin("LogBlock") != null) {
			Bukkit.dispatchCommand(sender, "lb rb player " + args[0] + " since 999y area 9999999");
			return true;
		}
		
		//SENDING BAN CRAP
		if(ban != null)
		{
			ban.setBanned(true);
			ban.kickPlayer("You have been banned and rolledback from " + this.plugin.getServer().getName() + ".");
		}
		this.plugin.getServer().broadcastMessage(ChatColor.RED + args[0] + " has been banned and rolledback by " + sender.getName());
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
	
}