package net.playssa.awesomechat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Team;

public class AwesomeChatGroup
{
	protected String groupName, prefix, suffix;
	protected ChatColor color, highlightColor;
	protected String hChar1, hChar2;
	private FileConfiguration groupData = null;
	private File groupDataFile = null;
	private Team team;
	
	public AwesomeChatGroup(String groupName)
	{
		this.groupName = groupName;
		groupDataFile = new File(AwesomeChat.plugin.getDataFolder(),"Groups" + File.separator + groupName + ".yml");
		try
		{
			if(groupDataFile.createNewFile())
			{
				groupData = YamlConfiguration.loadConfiguration(groupDataFile);
				groupData.set("Prefix", "");
				groupData.set("Suffix", "");
				groupData.set("Color", ChatColor.WHITE.name());
				groupData.set("HighlightColor", ChatColor.WHITE.name());
				groupData.set("HighlightChar1", "");
				groupData.set("HighlightChar2", "");
				groupData.save(groupDataFile);
			}
			else
			{
				groupData = YamlConfiguration.loadConfiguration(groupDataFile);
			}
				reloadFile();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void reloadFile()
	{
		groupData = YamlConfiguration.loadConfiguration(groupDataFile);
		this.prefix = colorize(groupData.getString("Prefix"));
		this.suffix = colorize(groupData.getString("Suffix"));
		this.color = ChatColor.valueOf(groupData.getString("Color"));
		this.highlightColor = ChatColor.valueOf(groupData.getString("HighlightColor"));
		this.hChar1 = colorize(groupData.getString("HighlightChar1"));
		this.hChar2 = colorize(groupData.getString("HighlightChar2"));
		if(team != null)
		{
			this.team.setPrefix(StringUtils.left((getPrefix()+" "),16));
			this.team.setDisplayName(StringUtils.left((getPrefix()+" "),16));
		}
		
	}
	
	public boolean matches(String groupName)
	{
		return this.groupName.equals(groupName);
	}
	
	public String toString()
	{
		return groupName;
	}
	
	public String getPrefix()
	{
		if(prefix.length()<1)
			return "";
		return highlightColor + hChar1 + color + prefix + highlightColor + hChar2;
	}
	public String getSuffix()
	{
		if(suffix.length()<1)
			return "";
		return highlightColor + hChar1 + color + suffix + highlightColor + hChar2;
	}
	public void setTeam(Team team)
	{
		this.team = team;
		this.team.setPrefix(StringUtils.left((hChar1+color+prefix+ChatColor.RESET+hChar2+" "),16));
		this.team.setDisplayName(StringUtils.left((hChar1+color+prefix+ChatColor.RESET+hChar2+" "),16));
	}
	public static String colorize(String s)
	{
	    if(s == null) 
	    	return null;
	    return s.replaceAll("&([0-9a-l])", "\u00A7$1");
	}
		
}