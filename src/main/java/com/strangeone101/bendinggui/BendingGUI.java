package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.config.ConfigLanguage;
import com.strangeone101.bendinggui.config.ConfigPresets;
import com.strangeone101.bendinggui.config.ConfigStandard;
import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

		new GuiCommand();

		new ConfigStandard();
		new ConfigLanguage();
		new ConfigPresets();

		new SpiritsSupport();

		//Reload configs AFTER 1 tick since there may be PK plugins that load after BendingGUI and we need them to be loaded
		Bukkit.getScheduler().runTaskLater(this, () -> {
			ConfigStandard.getInstance().load();
			ConfigLanguage.getInstance().load();
			//loadElementOrder();
			ElementOrder.sortOrder();
		}, 1L);

		log.log(Level.INFO, enabled ? "BendingGUI Fully Loaded!" : "BendingGUI loaded but not functional.");

		loaded = true;
	}

	public void reload()
	{
		ConfigStandard.getInstance().load();
		ConfigLanguage.getInstance().load();
		ConfigPresets.getInstance().load();
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

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getHolder() instanceof MenuBase) {
				p.closeInventory();
			}
		}
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
