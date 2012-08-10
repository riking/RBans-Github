package net.zxq.rastrosgonegriefing.rbans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker
{
	private RBans plugin;
	private URL filesFeed;
	
	private String version;
	private String link;
	
	public UpdateChecker(RBans plugin, String url)
	{
		this.plugin = plugin;
		
		try {
			this.filesFeed = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateNeeded()
	{		
		try {
			InputStream input = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			
			plugin.log.info(this.version + this.link);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getVersion()
	{
		return this.version;
	}
	
	public String getLink()
	{
		return this.link;
	}
}