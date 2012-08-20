package net.zxq.rastrosgonegriefing.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.zxq.rastrosgonegriefing.actions.RollbackAction;
import net.zxq.rastrosgonegriefing.actions.SearchAction;
import net.zxq.rastrosgonegriefing.actions.WorldEditFilterAction;
import net.zxq.rastrosgonegriefing.rbans.RBans;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class RBPlayer {
	World world;
	Player player;
	RBans plugin;
	CommandSender sender;
	Boolean worldedit;
	String[][] pages;
	public Integer rollbackTaskID;
	public Thread worldeditFilter;
	public Thread searchTask;
	public Thread rollbackThread;
	
	boolean isDoingRollback = false;
	boolean isSearching = false;
	boolean isUsingTool = false;
	public boolean we = false;
	public HashMap<Location, String> playersIgnitedTNT = new HashMap<Location, String>();
	public ArrayList<String> result = new ArrayList<String>();
	
	public RBPlayer(RBans plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
	}
	
	public RBPlayer(RBans plugin, CommandSender sender) {
		this.sender = sender;
		this.plugin = plugin;
	}
	
	public void search(boolean we, ArrayList<String> args) {
		String[] arg = new String[args.size()];
		for(int i = 0; i < args.size(); i++) {
			arg[i] = args.get(i);
		}

		new SearchTask(this, new SearchAction(this), arg);
	}

	public void rollback(boolean we, ArrayList<String> args) {
		String[] arg = new String[args.size()];
		for(int i = 0; i < args.size(); i++) {
			arg[i] = args.get(i);
		}

		if(we) {
			new SearchTask(this, new WorldEditFilterAction(this), arg);
		} else {
			new SearchTask(this, new RollbackAction(this), arg);
		}
	}

	public void print(String msg) {
		player.sendMessage(msg);
	}

	public void print(String[] msg) {
		if(msg != null) {
			for(int i = 0; i < msg.length; i++) {
				if(msg[i] != null) {
					player.sendMessage(msg[i]);
				}
			}
		}
	}

	public void print(ArrayList<String> msg) {
		if(msg != null) {
			for(int i = 0; i < msg.size(); i++) {
				if(msg.get(i) != null) {
					player.sendMessage(msg.get(i));
				}
			}
		}
	}

	public boolean teleport(Location to) {
		return player.teleport(to);
	}

	public RBans getGriefLog() {
		return plugin;
	}

	public World getWorld() {
		return player.getWorld();
	}

	public Server getServer() {
		return player.getServer();
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<String> getSearchResult() {
		return result;
	}

	public void setSearchResult(ArrayList<String> result) {
		this.result = result;
	}

	public Selection getWorldEditSelection() {
		WorldEditPlugin we = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
		return we.getSelection(player);
	}

	public Integer getRollbackTaskID() {
		return rollbackTaskID;
	}

	public Thread getSearchTask() {
		return searchTask;
	}

	public boolean isUsingTool() {
		return isUsingTool;
	}

	public void setUsingTool(boolean isUsingTool) {
		this.isUsingTool = isUsingTool;
	}

	public boolean isDoingRollback() {
		return isDoingRollback;
	}

	public void setDoingRollback(boolean rollback) {
		this.isDoingRollback = rollback;
	}

	public boolean isSearching() {
		return isSearching;
	}

	public void setSearching(boolean searching) {
		this.isSearching = searching;
	}

	public String[][] getPages() {
		return pages;
	}

	public void setPages(String[][] pages) {
		this.pages = pages;
	}

	public static RBPlayer getGLPlayer(Player p) {
		return RBans.players.get(p.getName());
	}

	public static RBPlayer getGLPlayer(CommandSender sender) {
		return RBans.players.get(sender.getName());
	}

	public static RBPlayer getGLPlayer(String p) {
		return RBans.players.get(p);
	}

	@Override
	public String toString() {
		return "{GLPlayer} name: " + player.getName() + " rollback: " + isDoingRollback + " search: " + isSearching;
	}

	/**
	 * This equals if the object is an instance of GLPlayer,
	 * if the hashcode of this GLPlayer == hashcode of the object,
	 * if the toString()'s are equal 
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RBPlayer)) {
			return false;
		} else {
			RBPlayer p = (RBPlayer) obj;
			if(this.hashCode() == p.hashCode()) {
				return true;
			} else if(this.toString().equals(p.toString())) {
				return true;
			}
			return super.equals(obj);
		}
	}
}