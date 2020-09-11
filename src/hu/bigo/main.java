package hu.bigo;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;


enum Mode{
	hp,random
}

public class main extends JavaPlugin implements Listener, CommandExecutor{
	HashMap<String, Boolean> isEmpty = new HashMap<String, Boolean>();
	int i;
	ArrayList<Integer> avilable = new ArrayList<Integer>();
	HashMap<String, Integer> itemlist = new HashMap<String, Integer>();
	int time;
	boolean hardcore = false;
	File cFile;
	World nether;
	Utf8YamlConfiguration sf;
	Mode mode;
	int randomNumber = 1;
	
	//public static Logger LOGGER = LogManager.getLogger(Main.class);
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		for(Player p : Bukkit.getOnlinePlayers())
			{
			//isEmpty.put(p.getName(), true);
			//Bukkit.getLogger().info(p.getName()+ " otto hozzadva!");
			}
		try {
			if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();
			this.cFile = new File(this.getDataFolder(), "config.yml");
			if(!this.cFile.exists()) {
				this.cFile.createNewFile();
				FileUtils.copyInputStreamToFile(this.getResource("config.yml"), this.cFile);
			}
			this.sf = new Utf8YamlConfiguration();
			this.sf.load(this.cFile);
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.time = this.getConfig().getInt("timeInSecs");
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public Utf8YamlConfiguration getConfig() {
		return this.sf;
	}
	
	@Override
	public void saveConfig() {
		try {
			this.sf.save(this.cFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void breaks(BlockBreakEvent e) {
		if(e.isDropItems()) {
			String name = e.getBlock().getType().getId()+":"+e.getBlock().getData();
			
			
			if(itemlist.get(name) != null) {
				int newid = itemlist.get(name);
				Material mt = Material.values()[newid];
				e.setDropItems(false);
				e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), (new ItemStack(mt, 1)));		
				}else {
				int randomnmb = new Random().nextInt(avilable.size());
				int newid = avilable.get(randomnmb);
				avilable.remove(randomnmb);
				itemlist.put(name, newid);
				//Bukkit.broadcastMessage(newid+"s");
				Material mt = Material.values()[newid];
				e.setDropItems(false);
				e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), (new ItemStack(mt, 1)));
		}
	}
	}
	
	 @EventHandler
	 public void gf(PlayerTeleportEvent e){
	/*	 if(e.getCause()==TeleportCause.NETHER_PORTAL && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world"))
		e.setTo(Bukkit.getWorld("world_nether").getSpawnLocation());
		 if(e.getCause()==TeleportCause.NETHER_PORTAL && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world_nether"))
		e.setTo(Bukkit.getWorld("world").getSpawnLocation());*/
	 }
	 
	 @EventHandler
	 public void dfg(PlayerJoinEvent e) {
		 if(isEmpty.get(e.getPlayer().getName()) != null && isEmpty.get(e.getPlayer().getName()) != false) {
			 return;
		 }
		 Player p = e.getPlayer();
		 if(mode == Mode.hp) {
		 p.setMaxHealth(10);
		 }
		 p.setHealth(10);
		 p.setFoodLevel(20);
		 p.getInventory().clear();
		 p.setHealth(10);
		 isEmpty.put(e.getPlayer().getName(), true);
	 }
	
	@Override
	public void reloadConfig() {
		try {
			this.sf.load(this.cFile);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDisable(){
		saveTime();
	}
	
	public String getFormattedTime() {
    	int hours = (int) time / 3600;
    	int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        
        String h = hours > 9 ? ""+hours : "0"+hours;
        String m = mins > 9 ? ""+mins : "0"+mins;
        String s = secs > 9 ? ""+secs : "0"+secs;
        return  "§b"+h+":"+m+":"+s;
	}
	
	public void startTime() {
	i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	    @Override
	    public void run() {
	    	time++;
	        for(Player p : Bukkit.getOnlinePlayers()) {
	        	String message = getFormattedTime();
	            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	        }
	    }
	}, 0L, 20L);
	}
	
	private void saveTime(){
		this.getConfig().set("timeInSecs", time);
		this.saveConfig();
	}
	
	public void pauseTime() {
		Bukkit.getScheduler().cancelTask(i);
	}
	
	public void regenWorld(String w, Environment en) {
	    World world = Bukkit.getWorld(w);
	 
	    if (world != null) {
	    	sendLobby();
	        Bukkit.unloadWorld(world, false);
	        File del = new File(w);
	   
	        if (del.exists()) {
	            try {
	            	deleteWorld(del);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    WorldCreator worldc = new WorldCreator(w);
	    worldc.environment(en);
	    worldc.generateStructures(true);
	    Bukkit.createWorld(worldc);
	    Bukkit.getWorlds().add(world);
	}
	

	/*Handler handler = new Handler() {
	    @Override
	    public void publish(LogRecord record) {
	        String message = record.getMessage();
	        Bukkit.broadcastMessage(message);
	    }

	    @Override
	    public void flush() {

	    }

	    @Override
	    public void close() throws SecurityException {
	    }
	};*/
	
    @EventHandler
    public void onKill(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
        	Player p = (Player) e.getEntity();
            if (p.getLastDamage() >= p.getHealth()) {
            	Bukkit.broadcastMessage(p.getLastDamage()+" "+p.getHealth());
        		p.setHealth(20);
        		p.setGameMode(GameMode.SPECTATOR);
        		Bukkit.getScheduler().cancelTask(i);
                Bukkit.broadcastMessage("§c[Challange] §6A Challange végetért! §f"+p.getName()+" §6meghalt! \nIdő: "+getFormattedTime());
            	}
            }
    }
	
	public boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	
	public void sendLobby() {
		for(Player p : Bukkit.getOnlinePlayers()) {
		try {
			ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(bytearray);
	        out.writeUTF("Connect");
	        out.writeUTF("loading");
	        p.sendPluginMessage(this, "BungeeCord", bytearray.toByteArray());
			}catch(Exception e) {
					p.sendMessage("uno problemo senior otto!");
			}
		}
	}
	
	@Override
	 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("mode")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("random")) {
					mode = Mode.random;
					sender.sendMessage("§aok! random");
				}else if(args[0].equalsIgnoreCase("hp")) {
					mode = Mode.hp;
					sender.sendMessage("§aok! hp");
				}
			}
		}
		if(command.getName().equalsIgnoreCase("goto")) {
			if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) for(World w : Bukkit.getWorlds())sender.sendMessage(" - " +w.getName()+" ("+w.getEnvironment()+")");
			else if(args[0].equalsIgnoreCase("who")) for(Player p : Bukkit.getOnlinePlayers()) sender.sendMessage(""+p.getName()+" - "+p.getLocation().getWorld().getName());
			else for(Player p : Bukkit.getOnlinePlayers()) p.teleport(getServer().getWorld(args[0]).getSpawnLocation());
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("create")) {
				Environment env = Environment.NORMAL;
				if(args[2].equalsIgnoreCase("nether")) {
					env = Environment.NETHER;
				}
				sender.sendMessage(args[1]+" §6világ (re)generálás folyamatban...");
				regenWorld(args[1], env);
				sender.sendMessage("§aok!");
			}
		}
		}
		if(command.getName().equalsIgnoreCase("reset")) {
			time = 0;
			List<String> newHelyek = this.getConfig().getStringList("Helyek");
			newHelyek.clear();
			this.getConfig().set("Helyek", newHelyek);
			saveTime();
			Bukkit.getScheduler().cancelTask(i);
			if(args.length == 1) {
			sender.sendMessage("§6Sima világ regenerálás folyamatban...");
			regenWorld("world_nether", Environment.NETHER);
			regenWorld("world", Environment.NORMAL);
			for(Player p : Bukkit.getOnlinePlayers()) {
				try {
					ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(bytearray);
			        out.writeUTF("Connect");
			        out.writeUTF("loading");
			        ((Player) sender).sendPluginMessage(this, "BungeeCord", bytearray.toByteArray());
			        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			            public void run()
			            {
			            	Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "stop");
			            }
			        }, 60L);
			        
					}catch(Exception e) {
						sender.sendMessage("uno problemo senior otto!");
					}
			}
			}
		/*	((Player) sender).kickPlayer("A szerver újraépült! Lépj vissza!");
			getServer().dispatchCommand(getServer().getConsoleSender(), "stop");*/
		}
		if(command.getName().equalsIgnoreCase("addquest")) {
			Player p = (Player) sender;
			List<String> newHelyek = this.getConfig().getStringList("Quests");
			String finals = "";
			for(int i = 0; i<args.length;i++) finals += args[i]+" ";
			newHelyek.add(finals);
			this.getConfig().set("Quests", newHelyek);
			this.saveConfig();
			this.reloadConfig();
			p.sendMessage("§6"+args[0]+" §ahozzáadva!");
		}
		if(command.getName().equalsIgnoreCase("hely")) {
			Player p = (Player) sender;
			String hely = (" ("+args[0]+" - "+p.getLocation().getBlockX()+"/"
			+p.getLocation().getBlockY()+"/"+p.getLocation().getBlockZ()+") ");
			List<String> newHelyek = this.getConfig().getStringList("Helyek");
			newHelyek.add(hely);
			this.getConfig().set("Helyek", newHelyek);
			this.saveConfig();
			this.reloadConfig();
			p.sendMessage("§6"+args[0]+" §alerakva!");
		}
		else if(command.getName().equalsIgnoreCase("helyek")) {
			String allHely = "";
			for(String s : this.getConfig().getStringList("Helyek")) {
				allHely += "§3|§f"+s+"§3|";
			}
			sender.sendMessage(allHely);
		}
		else if(command.getName().equalsIgnoreCase("done")) {
			int id = Integer.parseInt(args[0]);
			String newText = "#"+this.getConfig().getStringList("Quests").get(id);
			List<String> old = this.getConfig().getStringList("Quests");
			old.set(id, newText);
			this.getConfig().set("Quests", old);
			this.saveConfig();
			this.reloadConfig();
			sender.sendMessage("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n");
			((Player) sender).chat("/feladatok");
		}
		else if(command.getName().equalsIgnoreCase("undone")) {
			int id = Integer.parseInt(args[0]);
			String newText = this.getConfig().getStringList("Quests").get(id).replaceAll("#", "");
			List<String> old = this.getConfig().getStringList("Quests");
			old.set(id, newText);
			this.getConfig().set("Quests", old);
			this.saveConfig();
			this.reloadConfig();
			sender.sendMessage("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n");
			((Player) sender).chat("/feladatok");
		}
		else if(command.getName().equalsIgnoreCase("start")) {
			startTime();
			if(mode == Mode.random) {
				randomNumber = (new Random()).nextInt(Material.values().length);
				for(int i = 0; i<Material.values().length;i++) {
					avilable.add(i);
				}
				Bukkit.broadcastMessage(avilable.get(44)+" ----------------------------");
			}
			sender.sendMessage("§cIndul az idő!");
		}
		else if(command.getName().equalsIgnoreCase("pause")) {
			pauseTime();
			saveTime();
			sender.sendMessage("§cSzünetel az idő!");
		}
		else if(command.getName().equalsIgnoreCase("feladatok")) {
			int iter = 0;
			for(String s : this.getConfig().getStringList("Quests")) {
				if(s.startsWith("#")) {
					TextComponent message = new TextComponent("§a"+s.replaceAll("#", ""));
					message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/undone "+iter ) );
					message.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("§l§2Kész!").create()));
					sender.spigot().sendMessage(message);
					iter++;
					continue;
				}
				TextComponent message = new TextComponent("§c"+s);
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/done "+iter ) );
				message.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("§l§8Nem Kész!").create()));
				sender.spigot().sendMessage(message);
				iter++;
			}
		}
		return false;
	}
	
}
