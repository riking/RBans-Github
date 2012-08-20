package net.zxq.rastrosgonegriefing.rframe;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.zxq.rastrosgonegriefing.rbans.RBans;

public class RFrame extends JFrame implements ActionListener
{
    private RBans plugin;
  
    public RFrame()  
    {
    	super("RBans Admin Frame");
    	setLookandFeel();
    	setSize(348, 128);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    	BorderLayout border = new BorderLayout();
    	setLayout(border);
    	setLocationRelativeTo(null);
    	
    	JButton banSomeone = new JButton("Ban Someone");
    	banSomeone.setToolTipText("Opens the frame where you choose who you want to ban.");
        add(banSomeone);
        
        banSomeone.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
		    	if(command.equals("Ban Someone"))
		    	{
		    		BanFrame bf = new BanFrame(plugin);
		    		bf.setVisible(true);
		    	}
			}
        });
        
        setVisible(true);
        
        this.plugin = plugin;
    }
    
    @Override
	public void actionPerformed(ActionEvent event) {
			
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
    	RFrame rf = new RFrame();
    }
}