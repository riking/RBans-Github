package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerInfoExecutor extends RBans implements CommandExecutor
{
	private RBans plugin;
	
	public PlayerInfoExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(!permCheck((Player)sender, "rbans.playerinfo"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
		}
		
		if(args.length != 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /info <player>");
			return true;
		}
		
		Player info = plugin.getServer().getPlayer(args[0]);
		if(info != null)
		{
			sender.sendMessage("Name: " + info.getName());
			sender.sendMessage("Display Name: " + info.getDisplayName());
			sender.sendMessage("IP: " + info.getAddress());
			sender.sendMessage("Whitelisted: " + info.isWhitelisted());
			sender.sendMessage("EXP Level: " + info.getExpToLevel());
			sender.sendMessage("Health: " + info.getHealth());
			sender.sendMessage("OP: " + info.isOp());
			sender.sendMessage("Flying: " + info.isFlying());
		}
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
	
}
