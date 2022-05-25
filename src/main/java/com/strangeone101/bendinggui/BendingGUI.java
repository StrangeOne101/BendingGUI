package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.config.ConfigLanguage;
import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

	protected static Map<Element, ElementSupport> SUPPORTED_ELEMENTS = new HashMap<>();

	public static Logger log;

	public static BendingGUI INSTANCE;

	public static boolean loaded = false;
	public static boolean enabled = true;
	public static String versionInfo;

	public static final String PK_VERSION = "1.9.3";

	@Deprecated
	public static List<Element> elementOrder;

	public static NamespacedKey COMPASS;

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
		COMPASS = new NamespacedKey(this, "item");
		getServer().getPluginManager().registerEvents(new Listener(), this);
		if (Bukkit.getPluginManager().getPlugin("ProjectKorra") == null || !Bukkit.getPluginManager().getPlugin("ProjectKorra").isEnabled())
		{
			log.severe("ProjectKorra plugin not installed! This plugin is completely useless without it!");
			this.setEnabled(false);
			return;
		}

		BendingBoard.checkPlugins();

		versionInfo = checkVersion();

		if (!versionInfo.equals(""))
		{
			String error = versionInfo;
			if (error.startsWith("!")) {
				log.severe(error.substring(1));
				enabled = false;
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


		//Descriptions.load();
		//Descriptions.save();
		new ConfigStandard();
		new ConfigLanguage();
		loadElementOrder();
		new GuiCommand();

		log.log(Level.INFO, enabled ? "BendingGUI Fully Loaded!" : "BendingGUI loaded but not functional.");

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
		elementOrder.add(Element.BLUE_FIRE);
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
		ConfigStandard.getInstance().load();
		ConfigLanguage.getInstance().load();
		log.log(Level.INFO, "BendingGUI Reloaded!");
	}

	public String checkVersion()
	{
		String version = ProjectKorra.plugin.getDescription().getVersion();
		String varg1 = version.split(" ")[0];
		if (varg1.startsWith("1.4.0") || varg1.startsWith("1.5.0") || varg1.startsWith("1.6.0") || varg1.startsWith("1.7.") || varg1.startsWith("1.8."))
		{
			return "!BendingGUI does not support version ProjectKorra " + varg1 + "! Please upgrade to version " + PK_VERSION + " or higher!";
		}
		else if (!varg1.startsWith(PK_VERSION) && !varg1.startsWith("1.9."))
		{
			return "This version of BendingGUI is made for ProjectKorra Core " + PK_VERSION + "! You are running a higher or modded version which may not be fully supported yet.";
		}
		return "";
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
		if (SUPPORTED_ELEMENTS.containsKey(element)) return SUPPORTED_ELEMENTS.get(element).getColor();
		return element.getColor();
	}

	public ElementSupport getSupportedElement(Element element) {
		return SUPPORTED_ELEMENTS.get(element);
	}

	public Set<Element> getSupportedElements() {
		return SUPPORTED_ELEMENTS.keySet();
	}
}
