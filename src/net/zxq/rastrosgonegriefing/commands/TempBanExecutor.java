package net.zxq.rastrosgonegriefing.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempBanExecutor extends RBans implements CommandExecutor
{
	private RBans plugin;
	
	public TempBanExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(!permCheck((Player)sender, "rbans.tempban"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
			
			if(args.length != 1)
			{
				sender.sendMessage(ChatColor.RED + "Usage: /tempban <player> <time>");
				return true;
			}
			
			Player tban = plugin.getServer().getPlayer(args[0]);
			if(tban != null)
			{
				tban.kickPlayer("You have been tempbanned for " + args[1] + "seconds.");
				plugin.tban.add(args[0] + args[1]);
			}
			
			sender.sendMessage(ChatColor.GREEN + args[0] + " has been tempbanned for " + "" + ".");
			plugin.tban.saveFile();
		}
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
}