package net.zxq.rastrosgonegriefing.rframe;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.zxq.rastrosgonegriefing.commands.BanExecutor;
import net.zxq.rastrosgonegriefing.rbans.RBans;

public class BanFrame extends JFrame
{
	private static RBans plugin;
	  
    public BanFrame(final RBans plugin)  
    {
    	super("RBans Ban Frame");
    	setLookandFeel();
    	setSize(348, 128);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    	BorderLayout border = new BorderLayout();
    	setLayout(border);
    	setLocationRelativeTo(null);
    	
    	final JTextField playerToBan = new JTextField();
    	JButton ban = new JButton("BAN THEM!");
    	
    	add(playerToBan, BorderLayout.NORTH);
    	ban.setToolTipText("Bans the player you specified above.");
        add(ban);
        
        ban.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
					RBans.bannedPlayers.add(playerToBan.getText());
					//plugin.getServer().getPlayer(playerToBan.getText()).kickPlayer(playerToBan.getText());
					Bukkit.broadcastMessage(ChatColor.GREEN + playerToBan.getText() + " has been banned.");
					RBans.bannedPlayers.saveFile();
        }
       });
        
        setVisible(true);
        
        RFrame rf = new RFrame();
        rf.setVisible(false);
    }
    
    private void setLookandFeel()
    {
    	try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			//no-one cares about errors0
		}
    }
    
    public static void main(String[] args)
    {
    	new BanFrame(plugin);
    }
}