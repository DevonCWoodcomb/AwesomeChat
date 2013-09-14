package net.playssa.awesomechat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class AwesomeChatListener implements Listener
{
	List<String> FirstWordCommands;
	List<String> SecondWordCommands;
	String[] mess;
	ScoreboardManager sbm;
	Scoreboard board;
	AwesomeChat plugin;
	static ArrayList<AwesomeChatPlayer> AwesomeChatPlayers;
	static ArrayList<AwesomeChatGroup> AwesomeChatGroups;
	
	public AwesomeChatListener(AwesomeChat plugin, List<String> first, List<String> second)
	{
		this.plugin = plugin;
		AwesomeChatPlayers = new ArrayList<AwesomeChatPlayer>();
		AwesomeChatGroups = new ArrayList<AwesomeChatGroup>();
		FirstWordCommands = first;
		SecondWordCommands = second;
		sbm = Bukkit.getScoreboardManager();
		board = sbm.getNewScoreboard();
		for(String s : AwesomeChat.permission.getGroups())
		{
			AwesomeChatGroup g = new AwesomeChatGroup(s);
			AwesomeChatGroups.add(g);
			Team team = board.registerNewTeam(s);
			g.setTeam(team);
		}

	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void CommandNicknameReplacer(PlayerCommandPreprocessEvent event)
	{
		//plugin.log.info("CommandPreprocessdoesnteveiojngse");
		String[] message = event.getMessage().split(" ");
		String cmd = message[0];
		if(message.length > 1)
		{
			for(String fwc : FirstWordCommands)
			{
				if(cmd.equalsIgnoreCase(fwc))
				{
					message[1] = AwesomeChatPlayerMatcher.getPlayer(message[1]);
					break;
				}
			}
		}
		
		if(message.length > 2)
		{
			for(String swc : SecondWordCommands)
			{
				if(cmd.equalsIgnoreCase(swc))
				{
					message[2] = AwesomeChatPlayerMatcher.getPlayer(message[2]);
					break;
				}
			}
		}
		
		StringBuilder bob = new StringBuilder();
		for(String m : message)
		{
			bob.append(m);
			bob.append(" ");
		}
		bob.deleteCharAt(bob.length()-1);
		event.setMessage(bob.toString());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void PlayerJoin(PlayerJoinEvent event)
	{
		ArrayList<AwesomeChatGroup> groups = getGroups(event.getPlayer());
		AwesomeChatPlayer acp = new AwesomeChatPlayer(event.getPlayer(), groups);
		AwesomeChatPlayers.add(acp);
		event.setJoinMessage(event.getPlayer().getDisplayName()+ChatColor.GREEN+" has logged in.");
		board.getTeam(groups.get(0).groupName).addPlayer(event.getPlayer());
		event.getPlayer().setScoreboard(board);
	}
	
	/*@EventHandler(priority = EventPriority.NORMAL)
	public void nametag(PlayerReceiveNameTagEvent event)
	{
		AwesomeChatPlayer acp = AwesomeChatPlayerMatcher.getACPByName(event.getNamedPlayer().getName());
		event.setTag(StringUtils.left(acp.color+event.getNamedPlayer().getName(),16));	
	}*/
	
	@EventHandler(priority = EventPriority.LOW)
	public void PlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(event.getPlayer().getDisplayName()+" "+ChatColor.RED+"has logged out.");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void PlayerDeath(PlayerDeathEvent event)
	{
		
		String mess = event.getDeathMessage();
		plugin.log.info("someone died: "+mess);
		mess = mess.replace("fell from a high place","flew too close to the sun");
		mess = mess.replace("went up in flames","flew too close to the sun");
		mess = mess.replace("burned to death","flew too close to the sun");
		mess = mess.replace("was burnt to a crisp whilst","flew too close to the sun");
		mess = mess.replace("walked into a fire whilst","flew too close to the sun");
		ArrayList<String> death = new ArrayList<String>((Arrays.asList(mess.split(" "))));
		death.set(0, event.getEntity().getDisplayName());
		int by = death.indexOf("by");
		plugin.log.info("by: "+by+" death length: "+death.size());
		if(by > 0 && death.size() > (by+1))
		{
			by++;
			String killer = death.get(by);
			if(!(killer.equalsIgnoreCase("enderman")||killer.equalsIgnoreCase("wolf")||killer.equalsIgnoreCase("zombie")||
					killer.equalsIgnoreCase("blaze")||killer.equalsIgnoreCase("cave")||killer.equalsIgnoreCase("creeper")||
					killer.equalsIgnoreCase("ghast")||killer.equalsIgnoreCase("magma")||killer.equalsIgnoreCase("silverfish")||
					killer.equalsIgnoreCase("skeleton")||killer.equalsIgnoreCase("slime")||killer.equalsIgnoreCase("spider")||
					killer.equalsIgnoreCase("witch")||killer.equalsIgnoreCase("wither")||killer.equalsIgnoreCase("zombie")||
					killer.equalsIgnoreCase("snow")||killer.equalsIgnoreCase("iron")||killer.equalsIgnoreCase("ender")||
					killer.equalsIgnoreCase("wither")||killer.equalsIgnoreCase("giant")))
			{
				death.set(by, AwesomeChatPlayerMatcher.getACP(killer).getDisplayName());
				StringBuilder bob = new StringBuilder();
				for(String s: death)
				{
					bob.append(s);
					bob.append(" ");
				}
				mess = bob.toString();
			}
		}
		event.setDeathMessage(mess);
		
	}
	
	
	public ArrayList<AwesomeChatGroup> getGroups(Player player)
	{
		int primaryIndex = 0;
		ArrayList<AwesomeChatGroup> playerGroups = new ArrayList<AwesomeChatGroup>();
		for(AwesomeChatGroup group : AwesomeChatGroups)
		{
			for(String s : AwesomeChat.permission.getPlayerGroups(player))
			{
				if(group.matches(s))
				{
					if(s.equals(AwesomeChat.permission.getPrimaryGroup(player)))
						primaryIndex = playerGroups.size();
					playerGroups.add(group);
				}
			}
		}
		if(primaryIndex>0)
		{
			Collections.swap(playerGroups, 0, primaryIndex);
		}
		plugin.getLogger().info("Groups: "+ playerGroups.toString());
		return playerGroups;
	}
	
	public AwesomeChatPlayer getPlayer(Player player)
	{
		for(AwesomeChatPlayer acp : AwesomeChatPlayers)
		{
			if(acp.player.equals(player))
				return acp;
		}
		return null;
	}
	
	public static String colorize(String s)
	{
	    if(s == null) 
	    	return null;
	    return s.replaceAll("&([0-9a-l])", "\u00A7$1");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		if(event.getPlayer().hasPermission("AwesomeChat.chatcolor"))
		{
			event.setMessage(colorize(event.getMessage()));
		}
		String name = event.getPlayer().getName();
		AwesomeChatPlayer acp = getPlayer(event.getPlayer());
		if(!plugin.chatting.contains(name))
		{
			if(plugin.isStickied(name))
			{
				mess = event.getMessage().split(" ");
				event.setCancelled(true);
				plugin.chatMessage(event.getPlayer(), mess, plugin.stickied.get(name));
				return;
			}
		}
		else
		{
			plugin.chatting.remove(name);
		}
		event.setFormat(acp.getChatFormat());		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		AwesomeChatPlayer acp = getACPlayer(event.getPlayer());
		if(acp != null)
			AwesomeChatPlayers.remove(acp);
	}

	private AwesomeChatPlayer getACPlayer(Player player)
	{
		for(AwesomeChatPlayer p : AwesomeChatPlayers)
		{
			if(p.player.equals(player))
				return p;
		}
		return null;
	}
		
}
