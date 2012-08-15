package net.zxq.rastrosgonegriefing.commands;

import java.util.ArrayList;
import java.util.List;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteExecutor extends RBans implements CommandExecutor
{
	private RBans plugin;
	List<String> bannedFromChat = new ArrayList<String>();
	
	public MuteExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(!permCheck((Player)sender, "rbans.mute"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
		}
		
		if(args.length != 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /mute <player>");
			return true;
		}
		
		Player mute = plugin.getServer().getPlayer(args[0]);
		if(mute != null)
		{
				plugin.mutedPlayers.add(args[0]);
		}
		sender.sendMessage(ChatColor.GREEN + args[0] + " has been muted.");
		plugin.mutedPlayers.saveFile();
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
}