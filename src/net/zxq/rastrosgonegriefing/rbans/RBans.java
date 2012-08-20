package net.zxq.rastrosgonegriefing.rbans;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import net.zxq.rastrosgonegriefing.commands.BanExecutor;
import net.zxq.rastrosgonegriefing.commands.JFrameExecutor;
import net.zxq.rastrosgonegriefing.commands.KickExecutor;
import net.zxq.rastrosgonegriefing.commands.MuteExecutor;
import net.zxq.rastrosgonegriefing.commands.PlayerInfoExecutor;
import net.zxq.rastrosgonegriefing.commands.RollbackBanExecutor;
import net.zxq.rastrosgonegriefing.commands.RollbackExecutor;
import net.zxq.rastrosgonegriefing.commands.TempBanExecutor;
import net.zxq.rastrosgonegriefing.commands.UnBanExecutor;
import net.zxq.rastrosgonegriefing.commands.UnMuteExecutor;
import net.zxq.rastrosgonegriefing.listeners.PlayerLoginListener;
import net.zxq.rastrosgonegriefing.listeners.RBansPlayerListener;
import net.zxq.rastrosgonegriefing.rframe.RFrame;
import net.zxq.rastrosgonegriefing.util.BlockDestroyListStore;
import net.zxq.rastrosgonegriefing.util.BlockPlacedListStore;
import net.zxq.rastrosgonegriefing.util.ListStore;
import net.zxq.rastrosgonegriefing.util.MutedPlayersListStore;
import net.zxq.rastrosgonegriefing.util.PlayerChatListStore;
import net.zxq.rastrosgonegriefing.util.PlayerJoinListStore;
import net.zxq.rastrosgonegriefing.util.RBPlayer;
import net.zxq.rastrosgonegriefing.util.TempBanListStore;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RBans extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	public static ListStore bannedPlayers;
	public BlockDestroyListStore brokenBlocks;
	public BlockPlacedListStore placedBlocks;
	public PlayerJoinListStore playersJoined;
	public PlayerChatListStore playerChat;
	public MutedPlayersListStore mutedPlayers;
	public TempBanListStore tban;
	protected UpdateChecker uC;
	public static HashMap<String, Long> banned = new HashMap<String, Long>();
	public static HashMap<String, RBPlayer> players = new HashMap<String, RBPlayer>();
	public static File file = new File("RBans.txt");
	
	@Override
	public void onEnable() {
		List<String> listTwo = new ArrayList<String>();
		
		this.uC = new UpdateChecker(this, "http://dev.bukkit.org/server-mods/rbans/files.rss");
		
		if(uC.updateNeeded())
		{
			log.warning("v" + this.uC.getVersion() + " is out! You are running v" + getDescription().getVersion());
			log.warning("Get it here: " + this.uC.getLink());
		}

		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		
		if(banned == null){
			banned = new HashMap<String, Long>();
		}

		this.bannedPlayers = new ListStore(new File(pluginFolder
				+ File.separator + "localbans.txt"));
		this.bannedPlayers.loadFile();

		this.brokenBlocks = new BlockDestroyListStore(new File(pluginFolder
				+ File.separator + "Destroyed Blocks Log.txt"));
		this.brokenBlocks.loadFile();

		this.placedBlocks = new BlockPlacedListStore(new File(pluginFolder
				+ File.separator + "Placed Blocks Log.txt"));
		this.placedBlocks.loadFile();

		this.playersJoined = new PlayerJoinListStore(new File(pluginFolder
				+ File.separator + "Joined Players Log.txt"));
		this.playersJoined.loadFile();

		this.playerChat = new PlayerChatListStore(new File(pluginFolder
				+ File.separator + "Player Chat Log.txt"));
		this.playerChat.loadFile();

		this.mutedPlayers = new MutedPlayersListStore(new File(pluginFolder
				+ File.separator + "Muted Players.txt"));
		this.mutedPlayers.loadFile();
		
		this.tban = new TempBanListStore(new File(pluginFolder
				+ File.separator + "Muted Players.txt"));
		this.tban.loadFile();

		this.getServer().getPluginManager()
				.registerEvents(new RBansPlayerListener(this), this);
		this.getServer().getPluginManager()
				.registerEvents(new PlayerLoginListener(this), this);
		log("Enabled");

		listTwo.add("#TRUE = ENABLED");
		listTwo.add("#FALSE = DISABLED");

		FileConfiguration config = getConfig();

		config.addDefault("Messages.LogSavedMessage", true);

		config.options().copyDefaults(true);
		saveConfig();

		Plugin bi = this.getServer().getPluginManager().getPlugin("BlockIt");

		if (bi == null) {
			log.warning("BlockIt not found. Using RBans block listener.");
		} else {
			log.info("BlockIt found. Disabling RBans block listener.");
		}

		if (getServer().getPluginManager().getPlugin("HawkEye") != null) {
			log.info("HawkEye found and will be used for rollback funtionality.");
			return;
		} else if (getServer().getPluginManager().getPlugin("SWatchDog") != null) {
			log.info("SWatchDog found and will be used for rollback funtionality.");
			return;
		} else if (getServer().getPluginManager().getPlugin("LogBlock") != null) {
			log.info("LogBlock found and will be used for rollback funtionality.");
			return;
		} else if (getServer().getPluginManager().getPlugin("CoreProtect") != null) {
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
		this.getCommand("tempban").setExecutor(new TempBanExecutor(this));
		this.getCommand("rframe").setExecutor(new JFrameExecutor(this));
		this.getCommand("info").setExecutor(new PlayerInfoExecutor(this));
		this.getCommand("rbans rb").setExecutor(new RollbackExecutor(this));

		this.getServer().getScheduler()
				.scheduleAsyncRepeatingTask(this, new Runnable() {
					public void run() {
						bannedPlayers.saveFile();
						brokenBlocks.saveFile();
						placedBlocks.saveFile();
						playersJoined.saveFile();
						playerChat.saveFile();
						mutedPlayers.saveFile();

						if (getConfig().getString("Messages.LogSavedMessage") == "true") {
							getServer().broadcastMessage(
									ChatColor.RED + "[RBans] All Logs Saved.");
						}
					}
				}, 240L, 800L);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
				log.info(ChatColor.BLUE + "No RBans file yet, creating one for you.");
			} catch (IOException e) {
				log.warning(e.getMessage());
			}
		}
		
		for(Player p : getServer().getOnlinePlayers()) {
			// add the online players
			players.put(p.getName(), new RBPlayer(this, p));
		}
		// add the console as GLPlayer
		ConsoleCommandSender console = this.getServer().getConsoleSender();
		players.put(console.getName(), new RBPlayer(this, console));
	}

	@Override
	public void onDisable() {
		players.clear();
		log("Disabled");

		bannedPlayers.saveFile();
		brokenBlocks.saveFile();
		placedBlocks.saveFile();
		playersJoined.saveFile();
		playerChat.saveFile();
		mutedPlayers.saveFile();
	}

	public void log(String msg) {
		this.log.info("[RBans] " + msg);
	}
}