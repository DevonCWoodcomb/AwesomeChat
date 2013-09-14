package net.playssa.awesomechat;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class AwesomeChat extends JavaPlugin
{
	public static AwesomeChat plugin;
	public static Permission permission;
	static boolean timestamp;
	static DateFormat time;
	AwesomeChatListener ACL;
	AwesomeChatCommand ACC;
	ArrayList<String> words;
	Random rand;
	HashMap<String, String> stickied;
	ArrayList<String> chatting;
	List<String> first;
	List<String> second;
	Logger log;
	public void onEnable()
	{
		this.saveDefaultConfig();
		timestamp = this.getConfig().getBoolean("Timestamp");
		first = this.getConfig().getStringList("First");
		second = this.getConfig().getStringList("Second");
		log = Bukkit.getLogger();
		/*log.info("First:");
		for(String s : first)
		{
			log.info(s);
		}
		log.info("Second:");
		for(String s : second)
		{
			log.info(s);
		}*/
		time = new SimpleDateFormat("hh:mm");
		File file = new File(this.getDataFolder() + File.separator + "Players");
		file.mkdirs();
		file = new File(this.getDataFolder() + File.separator + "Groups");
		file.mkdirs();
		stickied = new HashMap<String,String>();
		chatting = new ArrayList<String>();
		plugin = this;
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null)
            permission = permissionProvider.getProvider();
        ACL = new AwesomeChatListener(this, first, second);
        ACC = new AwesomeChatCommand(this);
		getServer().getPluginManager().registerEvents(ACL, this);
		getCommand("acreload").setExecutor(ACC);
		getCommand("nick").setExecutor(ACC);
		getCommand("color").setExecutor(ACC);
		getCommand("rcolor").setExecutor(ACC);
		getCommand("rhcolor").setExecutor(ACC);
		getCommand("rh1").setExecutor(ACC);
		getCommand("rh2").setExecutor(ACC);
		getCommand("rprefix").setExecutor(ACC);
		getCommand("rsuffix").setExecutor(ACC);
		getCommand("admin").setExecutor(ACC);
		getCommand("mod").setExecutor(ACC);
		getCommand("builder").setExecutor(ACC);
		getCommand("guide").setExecutor(ACC);
		getCommand("chat").setExecutor(ACC);
	}
	public void onDisable()
	{
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("chat"))
		{				
			if(sender instanceof ConsoleCommandSender)
			{
				sender.sendMessage("wtf bro? Console can't do this shit!");
				return true;
			}
			else
			{
				StringBuilder message = new StringBuilder();
				for(String arg: args)
				{
					if(arg.equalsIgnoreCase("-s"))
					{
						stickied.remove(sender.getName());
						sender.sendMessage("You are now talking in general chat!");
					}
					else
					{
						message.append(arg+" ");
					}
				}
				chatting.add(sender.getName());
				((Player)sender).chat(message.toString());
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("admin"))
		{
			return chatMessage(sender, args, "admin");
		}
		else if(cmd.getName().equalsIgnoreCase("mod"))
		{
			return chatMessage(sender, args, "mod");
		}
		else if(cmd.getName().equalsIgnoreCase("builder"))
		{
			return chatMessage(sender, args, "builder");
		}
		else if(cmd.getName().equalsIgnoreCase("guide"))
		{
			return chatMessage(sender, args, "guide");
		}
		return false;
	}	
	
	public String randomWord()
	{
		Random rand = new Random();
		int randomint = rand.nextInt(words.size());
		return words.get(randomint);
	}
	public boolean isStickied(String name)
	{
		return stickied.containsKey(name);
	}
	public boolean chatMessage(CommandSender sender, String[] args, String chat)
	{
		if(sender.hasPermission("awesomechat."+chat+"chat"))
		{
			if(args.length<1)
			{
				return false;
			}
			StringBuilder bob = new StringBuilder();
			for(String s : args)
			{
				if(s.equalsIgnoreCase("-s"))
				{
					stickied.put(sender.getName(),chat);
					sender.sendMessage("You are now talking in "+chat+" chat!");			
				}
				else
				{
					bob.append(s+" ");
				}
			}
			if(bob.length()==0)
				return true;
			String name;
			if(sender instanceof ConsoleCommandSender)
			{
				name = ChatColor.DARK_GREEN+"CaptainAwesome(Console)";
			}
			else
			{
				name = ((Player)sender).getDisplayName();
			}
			
			for(Player pl : Bukkit.getServer().getOnlinePlayers())
			{
				if (pl.hasPermission("awesomechat."+chat+"chat"))
				{
					if(chat.equalsIgnoreCase("guide"))
					{
						pl.sendMessage(ChatColor.YELLOW + "<<<"+ChatColor.LIGHT_PURPLE +"GuideChat"+ChatColor.YELLOW + ">>> " 
							+ChatColor.RESET + name +ChatColor.RESET+": "+bob);
					}
					else if(chat.equalsIgnoreCase("mod"))
					{
						pl.sendMessage(ChatColor.YELLOW + "<<<"+ChatColor.RED +"ModChat"+ChatColor.YELLOW + ">>> " 
								+ChatColor.RESET + name +ChatColor.RESET+": "+bob);
					}
					else if(chat.equalsIgnoreCase("builder"))
					{
						pl.sendMessage(ChatColor.YELLOW + "<<<"+ChatColor.AQUA +"BuilderChat"+ChatColor.YELLOW + ">>> " 
								+ChatColor.RESET + name +ChatColor.RESET+": "+bob);
					}
					else if(chat.equalsIgnoreCase("admin"))
					{
						pl.sendMessage(ChatColor.YELLOW + "<<<"+ChatColor.GREEN +"AdminChat"+ChatColor.YELLOW + ">>> " 
								+ChatColor.RESET + name +ChatColor.RESET+": "+bob);
					}
				}
			}
			return true;
		}
		return false;
	}
	
}
