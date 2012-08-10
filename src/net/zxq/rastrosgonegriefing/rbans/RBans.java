package net.zxq.rastrosgonegriefing.rbans;

import java.io.File;
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
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

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
	
	@Override
	public void onEnable()
	{		
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
		this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
		log("Enabled");
		
		this.getCommand("ban").setExecutor(new BanExecutor(this));
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
				
				//"rbans.skiplogmessage"
				getServer().broadcastMessage(ChatColor.RED + "[RBans] All Logs Saved.");
			}
		}, 240L, 800L);
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
		
		/*
		bannedPlayersbackup.saveFile();
		brokenBlocksbackup.saveFile();
		placedBlocksbackup.saveFile();
		playersJoinedbackup.saveFile();
		playerChatbackup.saveFile();
		*/
	}
	
	public void log(String msg)
	{
		this.log.info("[RBans] " + msg);
	}
}