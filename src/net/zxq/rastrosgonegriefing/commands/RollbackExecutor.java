package net.zxq.rastrosgonegriefing.commands;

import java.util.ArrayList;

import net.zxq.rastrosgonegriefing.actions.RollbackAction;
import net.zxq.rastrosgonegriefing.actions.WorldEditFilterAction;
import net.zxq.rastrosgonegriefing.rbans.RBans;
import net.zxq.rastrosgonegriefing.util.ArgumentParser;
import net.zxq.rastrosgonegriefing.util.RBPlayer;
import net.zxq.rastrosgonegriefing.util.SearchTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RollbackExecutor extends RBans implements CommandExecutor {
	private RBans plugin;
	private String noPermsMsg = ChatColor.DARK_RED + "I am sorry, but i cannot let you do that! You don't have permission.";

	public RollbackExecutor(RBans plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rbans")) {
			if(args[0].equalsIgnoreCase("rb")) {
				if(sender.isOp() || permCheck((Player)sender, "rbans.rollbackban")) {
					if(args[1].equalsIgnoreCase("we")) {
						if(sender instanceof Player) {
							ArrayList<String> arguments = new ArrayList<String>();
							RBPlayer player = RBPlayer.getGLPlayer(sender);

							if(player.isDoingRollback()) {
								player.getPlayer().sendMessage(ChatColor.YELLOW + "[RBans] You are already doing a rollback, you can't have multiple rollbacks at the time.");
								return true;
							} else {
								for(int i = 0; i < args.length; i++) {
									if(i >= 2) {
										arguments.add(args[i]);
									}
								}

								ArgumentParser parser = new ArgumentParser(arguments);

								// check if the parser throwed any errors
								if(parser.error) {
									player.print(ChatColor.DARK_RED + "Sorry, an error occured. Please check if you formatted the arguments right.");
								} else {
									new SearchTask(player, new WorldEditFilterAction(player), parser.getResult());
								}

								return true;
							}
						}
					}
				}
			}
		}
		return true;
	}

	private boolean permCheck(Player player, String permission) {
		if (player.isOp() || player.hasPermission(permission))
			return true;
		return false;
	}
}
