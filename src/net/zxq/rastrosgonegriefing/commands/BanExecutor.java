package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanExecutor extends RBans implements CommandExecutor
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
		
		Player ban = plugin.getServer().getPlayer(args[0]);
		RBans.bannedPlayers.add(args[0]);
		if(ban != null)
		{
			//plugin.getServer().getPlayer(args[0]).setBanned(true);
			ban.kickPlayer("You have been banned from " + plugin.getServer().getServerName() + ".");
		}
		sender.sendMessage(ChatColor.GREEN + args[0] + " has been banned.");
		RBans.bannedPlayers.saveFile();
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
	
}
