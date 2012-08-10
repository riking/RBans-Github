package net.zxq.rastrosgonegriefing.rbans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

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
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RBans extends JavaPlugin
{
	public Logger log = Logger.getLogger("Minecraft");
	public ListStore bannedPlayers;
	public BlockDestroyListStore brokenBlocks;
	public BlockPlacedListStore placedBlocks;
	public PlayerJoinListStore playersJoined;
	public PlayerChatListStore playerChat;
	public ListStore bannedPlayersbackup;
	public BlockDestroyListStore brokenBlocksbackup;
	public BlockPlacedListStore placedBlocksbackup;
	public PlayerJoinListStore playersJoinedbackup;
	public PlayerChatListStore playerChatbackup;
	protected UpdateChecker updateChecker;
	private double newVersion;
	private double currentVersion;
	
	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		
		currentVersion = Double.valueOf(getDescription().getVersion().split("-")[0].replaceFirst("\\.", ""));
		
		/*
		this.updateChecker = new UpdateChecker(this, "http://rastrosgonegriefing.zxq.net/mcplugins/rbans/rss/rss.rss");
		if(this.updateChecker.updateNeeded())
		{
			log("A new version is available: " + this.updateChecker.getVersion());
			log("Get it from: " + this.updateChecker.getLink());
		}
		*/
		
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		
		this.bannedPlayers = new ListStore(new File(pluginFolder + File.separator + "localbans.txt"));
		this.bannedPlayers.loadFile();
		
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
				try{
					newVersion = updateCheck(currentVersion);
					if(newVersion > currentVersion)
					{
						log.warning("RBans " + newVersion + " is out! You are running version " + currentVersion);
						log.warning("Update RBans at http://dev.bukkit.org/server-mods/rbans/");
					}
				} catch(Exception e) {
					//ignore exceptions
				}
				
				bannedPlayers.saveFile();
				brokenBlocks.saveFile();
				placedBlocks.saveFile();
				playersJoined.saveFile();
				playerChat.saveFile();
				
				//"rbans.skiplogmessage"
				getServer().broadcastMessage(ChatColor.RED + "[RBans] All Logs Saved.");
			}
		}, 240L, 800L);
	}
	
	public double updateCheck(double currentVersion) throws Exception{
		String pluginUrlString = "http://dev.bukkit.org/server-mods/rbans/files.rss";
		try{
			URL url = new URL(pluginUrlString);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("item");
			Node firstNode = nodes.item(0);
			if(firstNode.getNodeType() == 1){
				Element firstElement = (Element)firstNode;
				NodeList firstElementTagName = firstElement.getElementsByTagName("title");
				Element firstNameElement = (Element) firstElementTagName.item(0);
				NodeList firstNodes = firstNameElement.getChildNodes();
				return Double.valueOf(firstNodes.item(0).getNodeValue().replace("RBans", "").replaceFirst(".", "").trim());
			}
		}
		catch (Exception localException) {	
		}
		return currentVersion;
	}
	
	@Override
	public void onDisable()
	{
		log("Disabled");
		
		bannedPlayers.saveFile();
		brokenBlocks.saveFile();
		placedBlocks.saveFile();
		playersJoined.saveFile();
		playerChat.saveFile();
		bannedPlayersbackup.saveFile();
		brokenBlocksbackup.saveFile();
		placedBlocksbackup.saveFile();
		playersJoinedbackup.saveFile();
		playerChatbackup.saveFile();
	}
	
	public void log(String msg)
	{
		this.log.info("[RBans] " + msg);
	}
	
	private boolean permCheck(Player player, String permission)
	{
		if(player.isOp() || player.hasPermission(permission)) return true;
		return false;
	}
}