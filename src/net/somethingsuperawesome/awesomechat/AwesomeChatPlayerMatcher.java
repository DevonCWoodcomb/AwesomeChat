package net.somethingsuperawesome.awesomechat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AwesomeChatPlayerMatcher
{
	public static String getPlayer(String name)
	{
		for (Player player: Bukkit.getServer().getOnlinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
				return player.getName();
		}
		AwesomeChatPlayer temp = getACPByNick(name);
		String nickname = "";
		if(temp != null)
		{
			nickname = temp.name;
		}
		
		if(nickname.length() > 0)
		{
			for (Player player: Bukkit.getServer().getOnlinePlayers())
			{
				if (player.getName().equalsIgnoreCase(nickname))
					return player.getName();
			}
		}
		
		for (Player player: Bukkit.getServer().getOnlinePlayers())
		{
			if (player.getName().toLowerCase().contains(name.toLowerCase()))
				return player.getName();
		}
				
		if(nickname.length() > 0)
		{
			for (Player player: Bukkit.getServer().getOnlinePlayers())
			{
				if (player.getName().toLowerCase().contains(nickname.toLowerCase()))
					return player.getName();
			}
		}
		return name;
	}
	
	public static AwesomeChatPlayer getACP(String name)
	{
		AwesomeChatPlayer acp;
		acp = getACPByName(name);
		if(acp == null)
			acp = getACPByNick(name);
		return acp;
	}
	
	public static AwesomeChatPlayer getACPByNick(String nick)
	{
		for(AwesomeChatPlayer acp : AwesomeChatListener.AwesomeChatPlayers)
		{
			if(acp.nickname.equalsIgnoreCase(nick))
				return acp;
		}
		for(AwesomeChatPlayer acp : AwesomeChatListener.AwesomeChatPlayers)
		{
			if(acp.nickname.toLowerCase().contains(nick.toLowerCase()))
				return acp;
		}
		return null;
	}
	
	public static AwesomeChatPlayer getACPByName(String nick)
	{
		for(AwesomeChatPlayer acp : AwesomeChatListener.AwesomeChatPlayers)
		{
			if(acp.name.equalsIgnoreCase(nick))
				return acp;
		}
		for(AwesomeChatPlayer acp : AwesomeChatListener.AwesomeChatPlayers)
		{
			if(acp.name.toLowerCase().contains(nick.toLowerCase()))
				return acp;
		}
		return null;
	}
}
