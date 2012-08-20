package net.zxq.rastrosgonegriefing.commands;

import net.zxq.rastrosgonegriefing.rbans.RBans;
import net.zxq.rastrosgonegriefing.rbans.UpdateChecker;
import net.zxq.rastrosgonegriefing.rframe.RFrame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JFrameExecutor extends RBans implements CommandExecutor
{
	private RBans plugin;
	private RFrame rframe;
	
	public JFrameExecutor(RBans plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			if(!permCheck((Player)sender, "rbans.rframe"))
			{
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
				return true;
			}
		}
		
		RFrame rf = new RFrame();
		rf.setVisible(true);
		
		sender.sendMessage(ChatColor.GREEN + "RFrame has been opened.");
		
		return true;
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}	
}
