package net.somethingsuperawesome.awesomechat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AwesomeChatPlayer
{
	protected Player player;
	protected ArrayList<AwesomeChatGroup> groups;
	@Getter
	protected String name, nickname, displayName;
	protected String prefix, suffix;
	protected ChatColor color, highlightColor;
	protected String hChar1, hChar2;
	@Getter
	protected long lastNickChange = 1, lastColorChange = 1;
	private FileConfiguration playerData = null;
	private File playerDataFile = null;
	
	public AwesomeChatPlayer(Player player, ArrayList<AwesomeChatGroup> groups)
	{
		this.player = player;
		this.groups = groups;
		this.name = player.getName();
		playerDataFile = new File(AwesomeChat.plugin.getDataFolder(),"Players" + File.separator + name + ".yml");
		try
		{
			if(playerDataFile.createNewFile())
			{
				playerData = YamlConfiguration.loadConfiguration(playerDataFile);
				playerData.set("Nickname", name);
				playerData.set("Prefix", "");
				playerData.set("Suffix", "");
				playerData.set("Color", ChatColor.WHITE.name());
				playerData.set("HighlightColor", ChatColor.WHITE.name());
				playerData.set("HighlightChar1", "");
				playerData.set("HighlightChar2", "");
				playerData.set("LastNickChange", 1);
				playerData.set("LastColorChange", 1);
				playerData.save(playerDataFile);
			}
			else
			{
				playerData = YamlConfiguration.loadConfiguration(playerDataFile);
			}
				reloadFile();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setGroups(ArrayList<AwesomeChatGroup> groups)
	{
		this.groups = groups;
	}
	
	public void setNickname(String nick)
	{
		nickname = nick;
		playerData.set("Nickname", nickname);
		lastNickChange = System.currentTimeMillis();
		playerData.set("LastNickChange", lastNickChange);
		try
		{
			playerData.save(playerDataFile);
			
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setColor(ChatColor c)
	{
		color = c;
		playerData.set("Color", color.name());
		try
		{
			playerData.save(playerDataFile);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setLastColorChange()
	{
		lastColorChange = System.currentTimeMillis();
		playerData.set("LastColorChange", lastColorChange);
		try
		{
			playerData.save(playerDataFile);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setLastNickChange()
	{
		lastNickChange = System.currentTimeMillis();
		playerData.set("LastNickChange", lastNickChange);
		try
		{
			playerData.save(playerDataFile);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reloadFile()
	{
		playerData = YamlConfiguration.loadConfiguration(playerDataFile);
		this.nickname = playerData.getString("Nickname");
		this.prefix = playerData.getString("Prefix");
		this.suffix = playerData.getString("Suffix");
		this.color = ChatColor.valueOf(playerData.getString("Color"));
		this.highlightColor = ChatColor.valueOf(playerData.getString("HighlightColor"));
		this.hChar1 = playerData.getString("HighlightChar1");
		this.hChar2 = playerData.getString("HighlightChar2");
		this.lastNickChange = playerData.getLong("LastNickChange");
		this.lastColorChange = playerData.getLong("LastColorChange");
		this.displayName = color + nickname + ChatColor.RESET;
		setDisplayName();
	}
	
	public String getChatFormat()
	{
		String format = "";
		if(AwesomeChat.timestamp)
		{
			format = ChatColor.GRAY + "[" + AwesomeChat.time.format(System.currentTimeMillis()) + "] ";
		}
		for(AwesomeChatGroup g : groups)
		{
			format += g.getPrefix();
		}
		format += getPrefix() + " %1$s" + getSuffix();
		for(AwesomeChatGroup g : groups)
		{
			format += g.getSuffix();
		}
		format += ChatColor.RESET + ": %2$s";
		return format;
	}
	
	public String getPrefix()
	{
		if(prefix.length()<1)
			return "";
		return highlightColor + hChar1 + color + prefix + highlightColor + hChar2+ ChatColor.RESET;
	}
	public String getSuffix()
	{
		if(suffix.length()<1)
			return "";
		return highlightColor + hChar1 + color + suffix + highlightColor + hChar2+ ChatColor.RESET;
	}
	
	public void setDisplayName()
	{
		player.setDisplayName(displayName);
		player.setPlayerListName(StringUtils.left(this.color+this.nickname, 16));
	}
}