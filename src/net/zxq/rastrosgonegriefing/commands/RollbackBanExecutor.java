package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.earth2me.essentials.EssentialsPlayerListener;
import com.earth2me.essentials.commands.EssentialsCommand;

public class RollbackBanExecutor implements CommandExecutor
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
			sender.sendMessage(ChatColor.RED + "EX: /rollbackban Rastro");
			return true;
		}
		
		Player ban = plugin.getServer().getPlayer(args[0]);
		plugin.bannedPlayers.add(args[0]); //ADDING TO BANNED PLAYERS TXT FILE
		
		//SENDING BAN CRAP
		if(ban != null)
		{
			ban.setBanned(true);
			ban.kickPlayer("You have been banned from " + plugin.getServer().getName() + ".");
		}
		
		Bukkit.dispatchCommand(sender, "sudo " + sender.getName() + " co rollback u:" + args[0] + " t:9h");
		plugin.getServer().broadcastMessage(ChatColor.RED + args[0] + " has been banned and rolledback by " + sender.getName());
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
	
}