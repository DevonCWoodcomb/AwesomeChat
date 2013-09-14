package net.playssa.awesomechat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AwesomeChatCommand implements CommandExecutor
{
	AwesomeChat plugin;
	public AwesomeChatCommand(AwesomeChat plugin)
	{
		this.plugin = plugin;
	}
	
	public ChatColor getColor(String arg)
	{
		if(arg.equalsIgnoreCase("gold") || arg.equalsIgnoreCase("6"))
		{
			return ChatColor.GOLD;
		}
		if(arg.equalsIgnoreCase("yellow") || arg.equalsIgnoreCase("e"))
		{
			return ChatColor.YELLOW;
		}
		if(arg.equalsIgnoreCase("darkgreen") || arg.equalsIgnoreCase("2"))
		{
			return ChatColor.DARK_GREEN;
		}
		if(arg.equalsIgnoreCase("green") || arg.equalsIgnoreCase("a"))
		{
			return ChatColor.GREEN;
		}
		if(arg.equalsIgnoreCase("aqua") || arg.equalsIgnoreCase("b"))
		{
			return ChatColor.AQUA;
		}
		if(arg.equalsIgnoreCase("darkaqua") || arg.equalsIgnoreCase("3"))
		{
			return ChatColor.DARK_AQUA;
		}
		if(arg.equalsIgnoreCase("blue") || arg.equalsIgnoreCase("9"))
		{
			return ChatColor.BLUE;
		}
		if(arg.equalsIgnoreCase("lightpurple") || arg.equalsIgnoreCase("pink") || arg.equalsIgnoreCase("d"))
		{
			return ChatColor.LIGHT_PURPLE;
		}
		if(arg.equalsIgnoreCase("purple") || arg.equalsIgnoreCase("5"))
		{
			return ChatColor.DARK_PURPLE;
		}
		if(arg.equalsIgnoreCase("white") || arg.equalsIgnoreCase("f"))
		{
			return ChatColor.WHITE;
		}
		if(arg.equalsIgnoreCase("gray") || arg.equalsIgnoreCase("7"))
		{
			return ChatColor.GRAY;
		}	
		if(arg.equalsIgnoreCase("red") || arg.equalsIgnoreCase("c"))
		{
			return ChatColor.RED;
		}
		if(arg.equalsIgnoreCase("darkred") || arg.equalsIgnoreCase("4"))
		{
			return ChatColor.DARK_RED;
		}	
		
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("chat"))
	    {
			if ((sender instanceof ConsoleCommandSender))
			{
				sender.sendMessage("wtf bro? Console can't do this shit!");
				return true;
			}

			StringBuilder message = new StringBuilder();
			for (String arg : args)
			{
				if (arg.equalsIgnoreCase("-s"))
				{
					plugin.stickied.remove(sender.getName());
					sender.sendMessage("You are now talking in general chat!");
				}
				else
				{
					message.append(arg + " ");
				}
			}
			plugin.chatting.add(sender.getName());
			((Player)sender).chat(message.toString());

			return true;
	    }
	    if (cmd.getName().equalsIgnoreCase("admin"))
	    {
	    	return chatMessage(sender, args, "admin");
	    }
	    if (cmd.getName().equalsIgnoreCase("mod"))
	    {
	    	return chatMessage(sender, args, "mod");
	    }
	    if (cmd.getName().equalsIgnoreCase("builder"))
	    {
	    	return chatMessage(sender, args, "builder");
	    }
	    if (cmd.getName().equalsIgnoreCase("guide"))
	    {
	    	return chatMessage(sender, args, "guide");
	    }
		if(cmd.getName().equalsIgnoreCase("acreload"))
		{
			if(sender.hasPermission("AwesomeChat.reload"))
			{
				plugin.reloadConfig();
				AwesomeChat.timestamp = plugin.getConfig().getBoolean("Timestamp");
				plugin.ACL.FirstWordCommands = plugin.getConfig().getStringList("First");
				plugin.ACL.SecondWordCommands = plugin.getConfig().getStringList("Second");
				for(AwesomeChatGroup group : AwesomeChatListener.AwesomeChatGroups)
				{
					group.reloadFile();
				}
				for(AwesomeChatPlayer acp : AwesomeChatListener.AwesomeChatPlayers)
				{
					Player temp = Bukkit.getPlayer(acp.getName());
					plugin.ACL.board.getPlayerTeam(temp).removePlayer(temp);
					acp.setGroups(plugin.ACL.getGroups(temp));
					acp.reloadFile();
					plugin.ACL.board.getTeam(acp.groups.get(0).groupName).addPlayer(temp);
					temp.setScoreboard(plugin.ACL.board);
					acp.player.setDisplayName(acp.displayName);
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("nick"))
		{
			if(sender.hasPermission("AwesomeChat.nick"))
			{
				if(args.length < 1)
				{
					sender.sendMessage(ChatColor.RED+"Not enough args. Please use the correct format.");
				}
				else if(args.length > 1)
				{
					if(sender.hasPermission("AwesomeChat.nick.other"))
					{
						AwesomeChatPlayer target = AwesomeChatPlayerMatcher.getACP(args[1]);
						if(target != null)
						{
							target.setNickname(args[0]);
							target.reloadFile();
						}
						else
						{
							sender.sendMessage("Player not found!");
						}
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"You don't have permission for that.");
						return true;
					}
				}
				else
				{
					AwesomeChatPlayer target = AwesomeChatPlayerMatcher.getACPByName(sender.getName());
					long timeSinceChange = System.currentTimeMillis();
					timeSinceChange -= target.getLastNickChange();
					if((timeSinceChange > 259200000L) || sender.hasPermission("AwesomeChat.nick.nolimit"))
					{
						target.setNickname(args[0]);
						target.setLastNickChange();
						target.reloadFile();
					}
				}
					
			}
			else
			{
				sender.sendMessage(ChatColor.RED+"You don't have permission for that.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("color"))
		{
			if(sender.hasPermission("AwesomeChat.color"))
			{
				if(args.length < 1)
				{
					sender.sendMessage(ChatColor.RED+"Not enough args. Please use the correct format.");
				}
				else if(args.length > 1)
				{
					if(sender.hasPermission("AwesomeChat.color.other"))
					{
						AwesomeChatPlayer target = AwesomeChatPlayerMatcher.getACP(args[1]);
						if(target != null)
						{
							ChatColor changeTo = getColor(args[0]);
							if(changeTo != null)
							{
								target.setColor(changeTo);
								target.player.sendMessage("Color changed to "+changeTo.toString());
								target.reloadFile();
							}
							else
							{
								sender.sendMessage(ChatColor.RED+"Not a valid color, please try again");
							}
						}
						else
						{
							sender.sendMessage("Player not found!");
						}
						
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"You don't have permission for that.");
						return true;
					}
				}
				else
				{
					AwesomeChatPlayer target = AwesomeChatPlayerMatcher.getACPByName(sender.getName());
					long timeSinceChange = System.currentTimeMillis();
					timeSinceChange -= target.getLastColorChange();
					if((timeSinceChange > 259200000L) || sender.hasPermission("AwesomeChat.color.nolimit"))
					{
						ChatColor changeTo = getColor(args[0]);
						if(changeTo != null)
						{
							target.setColor(changeTo);
							target.setLastColorChange();
							target.reloadFile();
						}
						else
						{
							sender.sendMessage(ChatColor.RED+"Not a valid color, please try again");
						}
					}
				}
					
			}
			else
			{
				sender.sendMessage(ChatColor.RED+"You don't have permission for that.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("rcolor"))
		{
			if(sender.hasPermission("AwesomeChat.rcolor"))
			{
				
			}
		}
		else if(cmd.getName().equalsIgnoreCase("rhcolor"))
		{
			if(sender.hasPermission("AwesomeChat.rhcolor"))
			{
				
			}
		}
		else if(cmd.getName().equalsIgnoreCase("rh1"))
		{
			if(sender.hasPermission("AwesomeChat.rh"))
			{
				
			}
		}
		else if(cmd.getName().equalsIgnoreCase("rh2"))
		{
			if(sender.hasPermission("AwesomeChat.rh"))
			{
				
			}
		}
		else if(cmd.getName().equalsIgnoreCase("rprefix"))
		{
			if(sender.hasPermission("AwesomeChat.rprefix"))
			{
				
			}
		}
		else if(cmd.getName().equalsIgnoreCase("rsuffix"))
		{
			if(sender.hasPermission("AwesomeChat.rsuffix"))
			{
				
			}
		}
		return true;
	}

	
	public boolean chatMessage(CommandSender sender, String[] args, String chat) 
	{
	    if (sender.hasPermission("awesomechat." + chat + "chat"))
	    {
	    	if (args.length < 1)
	    	{
	    		return false;
	    	}
	    	StringBuilder bob = new StringBuilder();
	    	for (String s : args)
	    	{
	    		if (s.equalsIgnoreCase("-s"))
	    		{
	    			plugin.stickied.put(sender.getName(), chat);
	    			sender.sendMessage("You are now talking in " + chat + " chat!");
	    		}
	    		else
	    		{
	    			bob.append(s + " ");
	    		}
	    	}
	    	if (bob.length() == 0)
	    		return true;
	    	String name;
	    	if ((sender instanceof ConsoleCommandSender))
	    	{
	    		name = ChatColor.DARK_GREEN + "CaptainAwesome(Console)";
	    	}
	    	else
	    	{
	    		name = ((Player)sender).getDisplayName();
	    	}

	    	for (Player pl : Bukkit.getServer().getOnlinePlayers())
	    	{
	    		if (pl.hasPermission("awesomechat." + chat + "chat"))
	    		{
	    			if (chat.equalsIgnoreCase("guide"))
	    			{
	    				pl.sendMessage(ChatColor.YELLOW + "<<<" + ChatColor.LIGHT_PURPLE + "GuideChat" + ChatColor.YELLOW + ">>> " + 
	    						ChatColor.RESET + name + ChatColor.RESET + ": " + bob);
	    			}
	    			else if (chat.equalsIgnoreCase("mod"))
	    			{
	    				pl.sendMessage(ChatColor.YELLOW + "<<<" + ChatColor.DARK_RED + "ModChat" + ChatColor.YELLOW + ">>> " + 
	    						ChatColor.RESET + name + ChatColor.RESET + ": " + bob);
	    			}
	    			else if (chat.equalsIgnoreCase("builder"))
	    			{
	    				pl.sendMessage(ChatColor.YELLOW + "<<<" + ChatColor.AQUA + "BuilderChat" + ChatColor.YELLOW + ">>> " + 
	    						ChatColor.RESET + name + ChatColor.RESET + ": " + bob);
	    			}
	    			else if (chat.equalsIgnoreCase("admin"))
	    			{
	    				pl.sendMessage(ChatColor.YELLOW + "<<<" + ChatColor.GREEN + "AdminChat" + ChatColor.YELLOW + ">>> " + 
	    						ChatColor.RESET + name + ChatColor.RESET + ": " + bob);
	    			}
	    		}
	    	}
	    	return true;
	    }
	    return false;
	}
	public boolean isStickied(String name) 
	{
		return plugin.stickied.containsKey(name);
	}
}
