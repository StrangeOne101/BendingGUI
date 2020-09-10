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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ProjectKorra;
import com.strangeone101.bendinggui.command.GuiCommand;

public class BendingGUI extends JavaPlugin
{
	public static boolean pageArrowMoveMouse = false;
	/**This is an option so little work has to be done if GeneralMethods.getBendingPlayer(...) supports OfflinePlayers
	 * in the future.*/
	public static boolean enableOfflinePlayers = false;

	public static Logger log;

	public static BendingGUI INSTANCE;

	public static boolean loaded = false;
	public static boolean enabled = true;
	//public static boolean jedcore = false;
	public static String versionInfo;

	public static List<Element> elementOrder;

	private static ItemStack GUI_ITEM;

	public static ItemStack getGuiItem()
	{
		return GUI_ITEM;
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!loaded || !this.isEnabled()) return false;
		try
		{
			if (command.getName().equalsIgnoreCase("gui") || command.getName().equalsIgnoreCase("bg") || command.getName().equalsIgnoreCase("bendinggui") || command.getName().equalsIgnoreCase("bendgui"))
			{
				return GuiCommand.executeCommand(sender, Arrays.asList(args));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "There is a problem with BendingGUI right now. Please report this to your admin or the plugin developer!");
		}

		return false;
	}

	@Override
	public void onEnable()
	{
		INSTANCE = this;
		log = getLogger();
		getServer().getPluginManager().registerEvents(new Listener(), this);
		if (Bukkit.getPluginManager().getPlugin("ProjectKorra") == null || !Bukkit.getPluginManager().getPlugin("ProjectKorra").isEnabled())
		{
			log.severe("ProjectKorra plugin not installed! This plugin is completely useless without it!");
			this.setEnabled(false);
			return;
		}

		ItemStack stack = Config.guiItem;
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Configure Bending");
		meta.setLore(Arrays.asList(new String[] {ChatColor.GRAY + "Right click to configure your bending!"}));
		stack.setItemMeta(meta);

		GUI_ITEM = stack;



		BendingBoard.checkPlugins();

		versionInfo = checkVersion();

		if (!versionInfo.equals(""))
		{
			String error = versionInfo;
			if (error.startsWith("!")) {
				log.severe(error.substring(1));
				enabled = false;
				try {
					throw new VersionIncompatibilityException();
				} catch (VersionIncompatibilityException e) {
					e.printStackTrace();
				}

			} else {
				log.warning(error);
			}
		}

		/**Loop through all combos and record elements for later use.*/
		/*Collection<ComboAbilityInfo> combos = ComboManager.getComboAbilities().values();
		for (ComboAbilityInfo combo : combos) 
		{
			Element elementmain = null;
			Element elementsub = null;
			Element elementFinal = Element.AVATAR;
			for (AbilityInformation abilityinfo : combo.getAbilities())
			{
				Element abilityelement = CoreAbility.getAbility(abilityinfo.getAbilityName()).getElement();
				SubElement sub = null;
				if (abilityelement instanceof SubElement)
				{
					sub = (SubElement) abilityelement;
					abilityelement = sub.getParentElement();
				}
				
				if (abilityelement == null) 
				{
					abilityelement = Element.AVATAR;
				}


				if (elementmain == null) elementmain = abilityelement;
				else if (elementmain != abilityelement) 
				{
					elementFinal = Element.AVATAR; 
					break;
				}
				else if (sub != null) 
				{
					elementsub = sub;
				}
			}
			
			if (elementFinal == null) elementFinal = elementmain;
			MenuCombos.comboElements.put(combo.getName(), elementFinal);
		}*/


		Config.load();
		Descriptions.load();
		Descriptions.save();
		loadElementOrder();
		new GuiCommand();

		log.log(Level.INFO, enabled ? "BendingGUI Fully Loaded!" : "BendingGUI loaded but functional.");

		loaded = true;
	}

	public void loadElementOrder()
	{
		elementOrder = new ArrayList<Element>();

		elementOrder.add(Element.AIR);    elementOrder.add(Element.FLIGHT);    elementOrder.add(Element.SPIRITUAL);
		if (Element.getAddonSubElements(Element.AIR).length > 0) elementOrder.addAll(Arrays.asList(Element.getAddonSubElements(Element.AIR)));
		elementOrder.add(Element.EARTH);  elementOrder.add(Element.SAND);       elementOrder.add(Element.METAL);    elementOrder.add(Element.LAVA);
		if (Element.getAddonSubElements(Element.EARTH).length > 0)elementOrder.addAll(Arrays.asList(Element.getAddonSubElements(Element.EARTH)));
		elementOrder.add(Element.FIRE);
		elementOrder.add(Element.LIGHTNING);
		elementOrder.add(Element.COMBUSTION);
		if (Config.hasBlueFire) elementOrder.add(Element.BLUE_FIRE);
		if (Element.getAddonSubElements(Element.FIRE).length > 0)elementOrder.addAll(Arrays.asList(Element.getAddonSubElements(Element.FIRE)));
		elementOrder.add(Element.WATER);  elementOrder.add(Element.ICE);     elementOrder.add(Element.HEALING);  elementOrder.add(Element.PLANT);     elementOrder.add(Element.BLOOD);
		if (Element.getAddonSubElements(Element.WATER).length > 0)elementOrder.addAll(Arrays.asList(Element.getAddonSubElements(Element.WATER)));
		elementOrder.add(Element.CHI);
		if (Element.getAddonSubElements(Element.CHI).length > 0)elementOrder.addAll(Arrays.asList(Element.getAddonSubElements(Element.CHI)));

		for (Element e : Element.getAddonElements())
		{
			if (e == null) continue;
			elementOrder.add(e);
			if (Element.getAddonSubElements(e).length > 0) elementOrder.addAll(Arrays.asList(Element.getAddonSubElements(e)));
		}

		elementOrder.add(Element.AVATAR);
		//log.info("Debug: " + makeListFancy(elementOrder));
	}

	public void reload()
	{
		Config.load();
		Descriptions.load();
		Descriptions.save();
		log.log(Level.INFO, "BendingGUI Reloaded!");
	}

	public String checkVersion()
	{
		String version = ProjectKorra.plugin.getDescription().getVersion();
		String varg1 = version.split(" ")[0];
		if (varg1.startsWith("1.4.0") || varg1.startsWith("1.5.0") || varg1.startsWith("1.6.0") || varg1.startsWith("1.7.0"))
		{
			return "!BendingGUI does not support version ProjectKorra " + varg1 + "! Please upgrade to version 1.8.9 or higher!";
		}
		else if (!varg1.startsWith("1.8.9") && !varg1.startsWith("1.9."))
		{
			return "The version of ProjectKorra installed is not fully supported yet! Do not be surprised if something breaks!";
		}
		else if (varg1.startsWith("1.8."))
		{
			if (version.toLowerCase().contains("beta"))
			{
				try
				{
					int betav = Integer.parseInt(version.split(" ", 3)[2]);
					if (betav <= 9)
					{
						return "!This version of BendingGUI is made for ProjectKorra 1.8.9 or ProjectKorra 1.9.0! The version you have will not work!";
					}
				}
				catch (IndexOutOfBoundsException e)
				{
					return "Unknown beta build of ProjectKorra detected. Support for this version is not guaranteed.";
				}
				catch (NumberFormatException e)
				{
					return "Unknown beta build of ProjectKorra detected. Support for this version is not guaranteed.";
				}
			} else if (!varg1.startsWith("1.8.9")) {
				return "The version of ProjectKorra installed is incompatible with BendingGUI. Please upgrade to 1.8.9 or 1.9.0";
			}
		} else if (varg1.startsWith("1.9.")) {
			if (!varg1.startsWith("1.9.0")) {
				return "This version of BendingGUI is made for ProjectKorra Core 1.8.9 or 1.9.0! You are running a higher or modded version which may not be fully supported yet.";
			}
		}
		else
		{
			return "This version of BendingGUI is made for ProjectKorra Core 1.8.9 or 1.9.0! You are running a higher or modded version which may not be fully supported yet.";
		}
		return "";
	}

	/**Makes a description list for items. Use BendingGUI.getDescriptions now instead!*/
	/*@Deprecated
	public List<String> formatDescription(String string, ChatColor color)
	{
		List<String> s = new ArrayList<String>();
		for (String s1 : string.split("\n"))
		{
			if (s1.equals("")) s.add("");
			else s.add(color + s1 + "\n");
		}
		return s;
	}*/

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

	public String makeListFancy(List<? extends Object> list)
	{
		String s = "";
		for (int i = 0; i < list.size(); i++)
		{
			if (i == 0) {s = list.get(0).toString();}
			else if (i == list.size() - 1) {s = s + " and " + list.get(i);}
			else {s = s + ", " + list.get(i);}
		}
		return s;
	}

	public static void addGlow(ItemStack itemStack)
	{
	    ItemMeta itemMeta = itemStack.getItemMeta();
	    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemMeta.addEnchant(Enchantment.LUCK, 1, true);
			itemStack.setItemMeta(itemMeta);
	}

	public static ChatColor getColor(Element element)
	{
		if (element instanceof SubElement) element = ((SubElement)element).getParentElement();
		if (element == Element.WATER) return ChatColor.BLUE;
		if (element == Element.CHI) return ChatColor.GOLD;
		if (element == Element.EARTH) return ChatColor.GREEN;
		if (element == Element.FIRE) return ChatColor.RED;
		if (element == Element.AIR) return ChatColor.GRAY;
		if (element == Element.AVATAR) return ChatColor.LIGHT_PURPLE;
		return element.getColor();
	}

	public static ChatColor getColorLight(Element element)
	{
		if (element instanceof SubElement) element = ((SubElement)element).getParentElement();
		if (element == Element.WATER) return ChatColor.BLUE;
		if (element == Element.CHI) return ChatColor.YELLOW;
		if (element == Element.EARTH) return ChatColor.GREEN;
		if (element == Element.FIRE) return ChatColor.RED;
		if (element == Element.AIR) return ChatColor.GRAY;
		if (element == Element.AVATAR) return ChatColor.LIGHT_PURPLE;
		return element.getColor();
	}

	public static ChatColor getColorDeep(Element element)
	{
		if (element instanceof SubElement) element = ((SubElement)element).getParentElement();
		if (element == Element.WATER) return ChatColor.DARK_BLUE;
		if (element == Element.CHI) return ChatColor.GOLD;
		if (element == Element.EARTH) return ChatColor.DARK_GREEN;
		if (element == Element.FIRE) return ChatColor.DARK_RED;
		if (element == Element.AIR) return ChatColor.GRAY;
		if (element == Element.AVATAR) return ChatColor.DARK_PURPLE;
		return element.getColor();
	}
}
