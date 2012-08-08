package net.zxq.rastrosgonegriefing.rbans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.zxq.rastrosgonegriefing.commands.BanExecutor;
import net.zxq.rastrosgonegriefing.commands.KickExecutor;
import net.zxq.rastrosgonegriefing.commands.RollbackBanExecutor;
import net.zxq.rastrosgonegriefing.commands.UnBanExecutor;
import net.zxq.rastrosgonegriefing.listeners.PlayerLoginListener;
import net.zxq.rastrosgonegriefing.listeners.RBansPlayerListener;
import net.zxq.rastrosgonegriefing.util.BlockDestroyListStore;
import net.zxq.rastrosgonegriefing.util.BlockPlacedListStore;
import net.zxq.rastrosgonegriefing.util.ListStore;
import net.zxq.rastrosgonegriefing.util.PlayerChatListStore;
import net.zxq.rastrosgonegriefing.util.PlayerJoinListStore;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RBans extends JavaPlugin
{
	public Logger log = Logger.getLogger("Minecraft");
	public ListStore bannedPlayers;
	public BlockDestroyListStore brokenBlocks;
	public BlockPlacedListStore placedBlocks;
	public PlayerJoinListStore playersJoined;
	public PlayerChatListStore playerChat;
	protected UpdateChecker updateChecker;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		/*
		this.updateChecker = new UpdateChecker(this, "http://rastrosgonegriefing.zxq.net/mcplugins/rbans/rss/rss.rss");
		if(this.updateChecker.updateNeeded())
		{
			log("A new version is available: " + this.updateChecker.getVersion());
			log("Get it from: " + this.updateChecker.getLink());
		}
		*/
		
		/* MAKES CONFIG
		getConfig().options().copyDefaults(true);
		saveConfig();
		*/
		
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		this.bannedPlayers = new ListStore(new File(pluginFolder + File.separator + "localbans.txt"));
		this.bannedPlayers.loadFile();
		
		/* LOG NAMES USING CONFIG.YML
		this.brokenBlocks = new BlockDestroyListStore(new File(pluginFolder + File.separator + getConfig().getString("destroyed-blocks-log-file-name") + ".txt"));
		this.brokenBlocks.loadFile();
		
		this.placedBlocks = new BlockPlacedListStore(new File(pluginFolder + File.separator + getConfig().getString("placed-blocks-log-file-name") + ".txt"));
		this.placedBlocks.loadFile();
		
		this.playersJoined = new PlayerJoinListStore(new File(pluginFolder + File.separator + getConfig().getString("joined-players-log-file-name") + ".txt"));
		this.playersJoined.loadFile();
		
		this.playerChat = new PlayerChatListStore(new File(pluginFolder + File.separator + getConfig().getString("player-chat-log-file-name") + ".txt"));
		this.playerChat.loadFile();
		*/
		
		this.brokenBlocks = new BlockDestroyListStore(new File(pluginFolder + File.separator + "Destroyed Blocks Log.txt"));
		this.brokenBlocks.loadFile();
		
		this.placedBlocks = new BlockPlacedListStore(new File(pluginFolder + File.separator + "Placed Blocks Log.txt"));
		this.placedBlocks.loadFile();
		
		this.playersJoined = new PlayerJoinListStore(new File(pluginFolder + File.separator + "Joined Players Log.txt"));
		this.playersJoined.loadFile();
		
		this.playerChat = new PlayerChatListStore(new File(pluginFolder + File.separator + "Player Chat Log.txt"));
		this.playerChat.loadFile();
		
		this.getServer().getPluginManager().registerEvents(new RBansPlayerListener(this), this);
		log("Enabled");
		
		this.getCommand("ban").setExecutor(new BanExecutor());
		this.getCommand("unban").setExecutor(new UnBanExecutor(this));
		this.getCommand("kick").setExecutor(new KickExecutor(this));
		this.getCommand("rollbackban").setExecutor(new RollbackBanExecutor(this));
		
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				bannedPlayers.saveFile();
				brokenBlocks.saveFile();
				placedBlocks.saveFile();
				playersJoined.saveFile();
				playerChat.saveFile();
				getServer().broadcastMessage("Files Saved.");
			}
		}, 240L, 800L);
	}
	
	@Override
	public void onDisable()
	{
		log("Disabled");
		
		this.bannedPlayers.saveFile();
		this.brokenBlocks.saveFile();
		this.placedBlocks.saveFile();
		this.playersJoined.saveFile();
		this.playerChat.saveFile();
	}
	
	public void log(String msg)
	{
		this.log.info("[RBans] " + msg);
	}
}