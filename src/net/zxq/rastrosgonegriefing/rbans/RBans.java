package net.zxq.rastrosgonegriefing.rbans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import net.zxq.rastrosgonegriefing.commands.BanExecutor;
import net.zxq.rastrosgonegriefing.commands.KickExecutor;
import net.zxq.rastrosgonegriefing.commands.MuteExecutor;
import net.zxq.rastrosgonegriefing.commands.RollbackBanExecutor;
import net.zxq.rastrosgonegriefing.commands.UnBanExecutor;
import net.zxq.rastrosgonegriefing.commands.UnMuteExecutor;
import net.zxq.rastrosgonegriefing.listeners.PlayerLoginListener;
import net.zxq.rastrosgonegriefing.listeners.RBansPlayerListener;
import net.zxq.rastrosgonegriefing.util.BlockDestroyListStore;
import net.zxq.rastrosgonegriefing.util.BlockPlacedListStore;
import net.zxq.rastrosgonegriefing.util.ListStore;
import net.zxq.rastrosgonegriefing.util.MutedPlayersListStore;
import net.zxq.rastrosgonegriefing.util.PlayerChatListStore;
import net.zxq.rastrosgonegriefing.util.PlayerJoinListStore;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RBans extends JavaPlugin
{
	public Logger log = Logger.getLogger("Minecraft");
	public ListStore bannedPlayers;
	public BlockDestroyListStore brokenBlocks;
	public BlockPlacedListStore placedBlocks;
	public PlayerJoinListStore playersJoined;
	public PlayerChatListStore playerChat;
	public MutedPlayersListStore mutedPlayers;
	protected UpdateChecker updateChecker;
	
	@Override
	public void onEnable()
	{
		List<String> listTwo = new ArrayList<String>();
		
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
		
		this.mutedPlayers = new MutedPlayersListStore(new File(pluginFolder + File.separator + "Muted Players.txt"));
		this.mutedPlayers.loadFile();
		
		this.getServer().getPluginManager().registerEvents(new RBansPlayerListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
		log("Enabled");
		
		listTwo.add("#TRUE = ENABLED");
		listTwo.add("#FALSE = DISABLED");
		
		FileConfiguration config = getConfig();
		
		config.addDefault("Messages.LogSavedMessage", true);
		
		config.options().copyDefaults(true);
		saveConfig();
		
		Plugin bi = this.getServer().getPluginManager().getPlugin("BlockIt");
		
		if(bi == null)
		{
			log.warning("BlockIt not found. Using RBans block listener.");
		}else{
			log.info("BlockIt found. Disabling RBans block listener.");
		}
		
		if(getServer().getPluginManager().getPlugin("HawkEye") != null)
		{
			log.info("HawkEye found and will be used for rollback funtionality.");
			return;
		} else if(getServer().getPluginManager().getPlugin("SWatchDog") != null)
		{
			log.info("SWatchDog found and will be used for rollback funtionality.");
			return;
		} else if(getServer().getPluginManager().getPlugin("LogBlock") != null)
		{
			log.info("LogBlock found and will be used for rollback funtionality.");
			return;
		} else if(getServer().getPluginManager().getPlugin("CoreProtect") != null)
		{
			log.info("CoreProtect found and will be used for rollback funtionality.");
			return;
		} else {
			log.warning("No compatible rollback plugins found. Rollback funtionality now being disabled.");
		}
		
		this.getCommand("ban").setExecutor(new BanExecutor(this));
		this.getCommand("unban").setExecutor(new UnBanExecutor(this));
		this.getCommand("kick").setExecutor(new KickExecutor(this));
		this.getCommand("rollbackban").setExecutor(new RollbackBanExecutor(this));
		this.getCommand("mute").setExecutor(new MuteExecutor(this));
		this.getCommand("unmute").setExecutor(new UnMuteExecutor(this));
		
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{				
				bannedPlayers.saveFile();
				brokenBlocks.saveFile();
				placedBlocks.saveFile();
				playersJoined.saveFile();
				playerChat.saveFile();
				mutedPlayers.saveFile();
				
				if(getConfig().getString("Messages.LogSavedMessage") == "true")
				{
					getServer().broadcastMessage(ChatColor.RED + "[RBans] All Logs Saved.");
				}
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
		mutedPlayers.saveFile();
	}
	
	public void log(String msg)
	{
		this.log.info("[RBans] " + msg);
	}
}