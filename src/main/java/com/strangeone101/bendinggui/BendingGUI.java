package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.SubElement;
import com.projectkorra.projectkorra.ability.AbilityModuleManager;
import com.projectkorra.projectkorra.airbending.AirMethods;
import com.projectkorra.projectkorra.earthbending.EarthMethods;
import com.projectkorra.projectkorra.firebending.FireMethods;
import com.projectkorra.projectkorra.waterbending.WaterMethods;
import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import com.strangeone101.bendinggui.menus.MenuElementSelect;
import com.strangeone101.bendinggui.nms.INMSManager;
import com.strangeone101.bendinggui.nms.NMSManager_RC1;
import com.strangeone101.bendinggui.nms.NMSManager_RC2;
import com.strangeone101.bendinggui.nms.NMSManager_RC3;
import com.strangeone101.bendinggui.nms.NMSManager_RC4;
import com.strangeone101.bendinggui.nms.NMSManager_RC5;

public class BendingGUI extends JavaPlugin implements Listener
{
	public static boolean pageArrowMoveMouse = false;
	/**This is an option so little work has to be done if GeneralMethods.getBendingPlayer(...) supports OfflinePlayers
	 * in the future.*/
	public static boolean enableOfflinePlayers = false; 
	
	public static Logger log;
	
	public static BendingGUI INSTANCE;
	
	public static boolean loaded = false;

	public static ItemStack getGuiItem()
	{
		ItemStack stack = Config.guiItem;
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Configure Bending");
		meta.setLore(Arrays.asList(new String[] {ChatColor.GRAY + "Right click to configure your bending!"}));
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack getAdminGuiItem(Player player)
	{
		ItemStack stack = Config.guiItem;
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Configure Bending (" + player.getName() + ")");
		meta.setLore(Arrays.asList(new String[] {ChatColor.GRAY + "Right click to configure " + player.getName() + "'s bending!"}));
		stack.setItemMeta(meta);
		return stack;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemRightClick(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getItem() != null && e.getItem().isSimilar(getGuiItem()))
			{
				MenuBendingOptions menu = new MenuBendingOptions(e.getPlayer());
				menu.openMenu(e.getPlayer());
			}
			/*else if (e.getItem() != null && e.getItem().isSimilar(getAdminGuiItem(null)))
			{
				MenuBendingOptions menu = new MenuBendingOptions(Bukkit.getOfflinePlayer(e.getItem().getItemMeta().getDisplayName().split("\\(")[1].split("\\")[0]));
				menu.openMenu(e.getPlayer());
			}*/
		}		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMenuItemClicked(InventoryClickEvent event) 
	{
		try
		{
			Inventory inventory = event.getInventory();
	        if (inventory.getHolder() instanceof MenuBase) 
	        {
	            MenuBase menu = (MenuBase) inventory.getHolder();
	            if (event.getWhoClicked() instanceof Player) 
	            {
	                Player player = (Player) event.getWhoClicked();
	                if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) 
	                {
	                	int index = event.getRawSlot();
	                    if (index < inventory.getSize()) 
	                    {
	                    	if (event.getCursor().getType() != Material.AIR)
	                    	{
	                    		event.setCancelled(true);
	                    		menu.closeMenu(player);
	                    		player.sendMessage(ChatColor.RED + "The bending gui cannot be tampered with!");
	                    	}
	                    	MenuItem item = menu.getMenuItem(index);
	                    	if (item != null)
	                    	{
	                    		item.isShiftClicked = event.isShiftClick();
	                    		item.onClick(player);
	                    		event.setCancelled(true);
	                    	}
	                    }
	                    else
	                    {
	                    	menu.setLastClickedSlot(index);
	                    }
	                }
	            }
	        }
		}
		catch (Exception e)
		{
			event.getWhoClicked().closeInventory();
			event.getWhoClicked().sendMessage(ChatColor.RED + "An error occured while processing the clickevent. Please report this to your admin or the plugin developer!");
		}
    }
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if (!loaded || !this.isEnabled()) return false;
		try
		{
			if (command.getName().equalsIgnoreCase("gui") || command.getName().equalsIgnoreCase("bg") || command.getName().equalsIgnoreCase("bendinggui") || command.getName().equalsIgnoreCase("bendgui"))
			{
				if (!(sender instanceof Player) && args.length == 0)
				{
					sender.sendMessage("Only players can run this command!");
					return false;
				}
				
				if (!sender.hasPermission("bendinggui.command"))
				{
					sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
					return true;
				}
				Player player = (Player) sender;
				if (args.length == 0)
				{
					if (Config.guiRequireItem)
					{
						if (player.getInventory().contains(getGuiItem()))
						{
							player.getInventory().remove(getGuiItem());
						}
						player.getInventory().addItem(getGuiItem());
						player.sendMessage(ChatColor.GREEN + Config.getGiveMessage);
					}
					else
					{
						MenuBendingOptions menu = new MenuBendingOptions(player);
						menu.openMenu(player);
					}
					/*MenuBendingOptions menu = new MenuBendingOptions(player);
					menu.openMenu(player);*/
					return true;
				}
				else if (args[0].equalsIgnoreCase("choose") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("ch"))
				{
					if (args.length == 2)
					{
						if (player.hasPermission("bending.admin.choose"))
						{
							Player playero = Bukkit.getPlayer(args[1]);
							if (playero != null)
							{
								MenuElementSelect menu = new MenuElementSelect(playero);
								menu.openMenu(player);
							}
							else
							{
								player.sendMessage(ChatColor.RED + "Error while finding player!");
							}
						}
						else
						{
							player.sendMessage(ChatColor.RED + "You don't have permission to choose other players element!");
						}
						return true;
					}
					if (!(sender instanceof Player))
					{
						sender.sendMessage("Only players can run this command!");
						return false;
					}
					if (GeneralMethods.getBendingPlayer(player.getName()).getElements().isEmpty() || player.hasPermission("bending.command.rechoose"))
					{
						MenuElementSelect menu = new MenuElementSelect(player);
						menu.openMenu(player);
					}
					else if (!player.hasPermission("bending.admin.rechoose"))
					{
						player.sendMessage(ChatColor.RED + "You have already chosen an element!");
					}
				}
				else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v") || args[0].equalsIgnoreCase("ver"))
				{
					sender.sendMessage(ChatColor.YELLOW + "BendingGUI is version " + getDescription().getVersion() + ", running on ProjectKorra " + ProjectKorra.plugin.getDescription().getVersion());
				}
				else
				{
					Player playero = Bukkit.getPlayer(args[1]);
					if (playero == null && player.hasPermission("bendinggui.admin"))
					{
						sender.sendMessage(ChatColor.RED + "Error while finding player!");
						return true;
					}
					else if (playero != null && !player.hasPermission("bendinggui.admin"))
					{
						player.sendMessage(ChatColor.RED + "You don't have permission to edit other players bending!");
					}
					else if (playero == null)
					{
						sender.sendMessage(ChatColor.RED + "Command usage is /gui or /gui <choose/version>");
					}
					else
					{
						MenuBendingOptions menu = new MenuBendingOptions(playero);
						menu.openMenu(player);
					}
					return true;
					
				}
				return true;
			}
		}
		catch (Exception e)
		{
			sender.sendMessage(ChatColor.RED + "BendingGUI appears to be broken at the moment. Please report this to your server admin or to the plugin developer!");
			log.severe("Something went wrong!");
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public void onEnable() 
	{
		INSTANCE = this;
		log = getLogger();
		if (Bukkit.getPluginManager().getPlugin("ProjectKorra") == null || !Bukkit.getPluginManager().getPlugin("ProjectKorra").isEnabled())
		{
			log.severe("ProjectKorra plugin not installed! This plugin is completely useless without it!");
			this.setEnabled(false);
			return;
		}
		
		
		
		
		if (getNMSManager() == null)
		{
			log.severe("This plugin is not compatible with your version of Spigot/Bukkit! Please update it or inform the plugin developer about this!");
			this.setEnabled(false);
			try {
				throw new ExceptionInInitializerError("Plugin incompatible with version :" + Bukkit.getBukkitVersion() + ". Must update!");
			} catch (ExceptionInInitializerError e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (!checkVersion())
		{
			this.setEnabled(false);
			return;
		}
		
		getServer().getPluginManager().registerEvents(this, this);
		Config.load();
		Descriptions.load();
		Descriptions.save();
		
		log.log(Level.INFO, "BendingGUI Fully Loaded!");
		
		loaded = true;
	}
	
	public boolean checkVersion()
	{
		String version = ProjectKorra.plugin.getDescription().getVersion();
		String varg1 = version.split(" ")[0];
		if (varg1.startsWith("1.6.0"))
		{
			log.severe("BendingGUI does not support version ProjectKorra 1.6.0! Please upgrade to version 1.7.0 or higher!");
			return false;
		}
		else if (!varg1.startsWith("1.7."))
		{
			log.warning("The version of ProjectKorra installed is not fully supported yet! Do not be surprised if something breaks!");
		}
		else if (version.toLowerCase().contains("beta"))
		{
			try
			{
				int betav = Integer.parseInt(version.split(" ", 3)[2]);
				if (betav <= 12)
				{
					log.severe("This version of BendingGUI is made for ProjectKorra 1.7.0 Beta 13 or higher!");
					return false;
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				log.warning("Unknown beta build of ProjectKorra detected. Support for this version is not guaranteed.");
			}
			catch (NumberFormatException e)
			{
				log.warning("Unknown beta build of ProjectKorra detected. Support for this version is not guaranteed.");
			}
			
		}
		return true;
	}
	
	/**Makes a description list for items. Use BendingGUI.getDescriptions now instead!*/
	@Deprecated
	public List<String> formatDescription(String string, ChatColor color)
	{
		List<String> s = new ArrayList<String>();
		for (String s1 : string.split("\n"))
		{
			if (s1.equals("")) s.add("");
			else s.add(color + s1 + "\n");
		}
		return s;
	}
	/**Makes a description list for items.*/
	public static List<String> getDescriptions(String description, ChatColor color, int length)
	{
		Pattern p = Pattern.compile("\\G\\s*(.{1,"+length+"})(?=\\s|$)", Pattern.DOTALL);
		Matcher m = p.matcher(description);
		List<String> l = new ArrayList<String>();
		while (m.find())
		{
			l.add(color + m.group(1));
		}
		return l;
	}
	
	public static Element getAbilityElement(String ability)
	{
		if (AbilityModuleManager.airbendingabilities.contains(ability)) return Element.Air;
		if (AbilityModuleManager.waterbendingabilities.contains(ability)) return Element.Water;
		if (AbilityModuleManager.firebendingabilities.contains(ability)) return Element.Fire;
		if (AbilityModuleManager.earthbendingabilities.contains(ability)) return Element.Earth;
		if (AbilityModuleManager.chiabilities.contains(ability)) return Element.Chi;
		return null;
	}
	
	@Deprecated
	/**Use getSubElement(...) instead!\n\nReturns if an ability is the sub element provided*/
	public static boolean isSubElement(String ability, SubElement element)
	{
		if (element == SubElement.Bloodbending) return AbilityModuleManager.bloodabilities.contains(ability);
		else if (element == SubElement.Combustion) return AbilityModuleManager.combustionabilities.contains(ability);
		else if (element == SubElement.Flight) return AbilityModuleManager.flightabilities.contains(ability);
		else if (element == SubElement.Healing) return AbilityModuleManager.healingabilities.contains(ability);
		else if (element == SubElement.Icebending) return AbilityModuleManager.iceabilities.contains(ability);
		else if (element == SubElement.Lavabending) return AbilityModuleManager.lavaabilities.contains(ability);
		else if (element == SubElement.Lightning) return AbilityModuleManager.lightningabilities.contains(ability);
		else if (element == SubElement.Metalbending) return AbilityModuleManager.metalabilities.contains(ability);
		else if (element == SubElement.Plantbending) return AbilityModuleManager.plantabilities.contains(ability);
		else if (element == SubElement.Sandbending) return AbilityModuleManager.sandabilities.contains(ability);
		else if (element == SubElement.SpiritualProjection) return AbilityModuleManager.spiritualprojectionabilities.contains(ability);
		return false;
	}
	
	public static SubElement getSubElement(String ability)
	{
		if (AbilityModuleManager.bloodabilities.contains(ability)) return SubElement.Bloodbending;
		else if (AbilityModuleManager.combustionabilities.contains(ability)) return SubElement.Combustion;
		else if (AbilityModuleManager.flightabilities.contains(ability)) return SubElement.Flight;
		else if (AbilityModuleManager.healingabilities.contains(ability)) return SubElement.Healing;
		else if (AbilityModuleManager.iceabilities.contains(ability)) return SubElement.Icebending;
		else if (AbilityModuleManager.lavaabilities.contains(ability)) return SubElement.Lavabending;
		else if (AbilityModuleManager.lightningabilities.contains(ability)) return SubElement.Lightning;
		else if (AbilityModuleManager.metalabilities.contains(ability)) return SubElement.Metalbending;
		else if (AbilityModuleManager.plantabilities.contains(ability)) return SubElement.Plantbending;
		else if (AbilityModuleManager.sandabilities.contains(ability)) return SubElement.Sandbending;
		else if (AbilityModuleManager.spiritualprojectionabilities.contains(ability)) return SubElement.SpiritualProjection;
		return null;
	}
	
	public String makeListFancy(List<String> list)
	{
		String s = "";
		for (int i = 0; i < list.size(); i++)
		{
			if (i == 0) {s = list.get(0);}
			else if (i == list.size() - 1) {s = s + " and " + list.get(i);}
			else {s = s + ", " + list.get(i);}
		}
		return s;
	}
	
	/**Credit to the Spigot Wiki for this solution. Returns the correct NMSManager 
	 * for the version you are using. This means all versions of spigot (1.8) are 
	 * compatible.*/
	public static INMSManager getNMSManager()
	{
	    try 
	    {
	        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
	        if (version.equals("v1_8_R1")) 
		    {
		        return new NMSManager_RC1();
		    } 
		    else if (version.equals("v1_8_R2")) 
		    {
		        return new NMSManager_RC2();
		    }
		    else if (version.equals("v1_8_R3")) 
		    {
		    	return new NMSManager_RC3();
		    }
		    else if (version.equals("v1_8_R4")) 
		    {
		    	return new NMSManager_RC4();
		    }
		    else if (version.equals("v1_8_R5")) 
		    {
		    	return new NMSManager_RC5();
		    }
	    } 
	    catch (ArrayIndexOutOfBoundsException e) 
	    {
	        return null;
	    }
	    return null;
	}
	
	/**Returns if a player can bind a move. This needs to be used because PK's has flaws.*/
	public static boolean canBind(Player p, String ability)
	{
		if (!EarthMethods.canLavabend(p) && EarthMethods.isLavabendingAbility(ability)) return false;
		else if (!EarthMethods.canMetalbend(p) && EarthMethods.isMetalbendingAbility(ability)) return false;
		else if (!EarthMethods.canSandbend(p) && EarthMethods.isSandbendingAbility(ability)) return false;
		else if (!AirMethods.canAirFlight(p) && AirMethods.isFlightAbility(ability)) return false;
		else if (!AirMethods.canUseSpiritualProjection(p) && AirMethods.isSpiritualProjectionAbility(ability)) return false;
		else if (!FireMethods.canCombustionbend(p) && FireMethods.isCombustionbendingAbility(ability)) return false;
		else if (!FireMethods.canLightningbend(p) && FireMethods.isLightningbendingAbility(ability)) return false;
		else if (!WaterMethods.canBloodbend(p) && WaterMethods.isBloodbendingAbility(ability)) return false;
		else if (!WaterMethods.canIcebend(p) && WaterMethods.isIcebendingAbility(ability)) return false;
		else if (!WaterMethods.canWaterHeal(p) && WaterMethods.isHealingAbility(ability)) return false;
		else if (!WaterMethods.canPlantbend(p) && WaterMethods.isPlantbendingAbility(ability)) return false;
		else if (!GeneralMethods.canBind(p.getName(), ability)) return false;
		return true;
	}
	
	
	@EventHandler
	public void onClose(InventoryCloseEvent e)
	{
		if (e.getInventory() instanceof MenuBendingOptions)
		{
			MenuBendingOptions menu = (MenuBendingOptions) e.getInventory();
			if (DynamicUpdater.players.containsKey(menu.getMenuPlayer().getUniqueId()) && DynamicUpdater.players.get(menu.getOpenPlayer().getUniqueId()).contains(menu.getOpenPlayer()))
			{
				DynamicUpdater.players.get(menu.getOpenPlayer().getUniqueId()).remove(menu.getOpenPlayer());
			}
		}
	}
}
