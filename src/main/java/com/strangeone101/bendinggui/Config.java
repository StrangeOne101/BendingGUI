package com.strangeone101.bendinggui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.FileUtil;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;

public class Config
{
	//public static List<SubElement> subElementsEnableIcons = Arrays.asList(new SubElement[] {SubElement.Lavabending, SubElement.Icebending});
	public static HashMap<Element, Material> elementIcons = new HashMap<Element, Material>();
	public static Material avatarIcon = Material.BEACON;

	public static boolean hasBlueFire = Element.getElement("BlueFire") != null;

	public static String fireDesc = "Firebenders can create fire with their bare fists. They are more prone to fire based damage and in some cases are completely fire resistant. Some skilled firebenders can even create lightning, making them very dangerous.";
	public static String airDesc = "Airbenders can manipulate air at will, making them extremely fast and agile and have a powerful connection with spirits. They can also jump higher, run faster and they take no fall damage when they hit the ground.";
	public static String waterDesc = "Waterbenders can manipulate anything with water in them, including ice, plants and of course, water. They can freeze and thaw ice at will, and can also swim extremely fast in water.";
	public static String earthDesc = "Earthbenders can manipulate almost anything natrual from the earth. Earthebenders take no fall damage while landing on blocks they can bend and some skilled benders can even bend metal.";
	public static String chiDesc = "Chiblocking isn't stricly an element but it is a form of art that makes the user faster and more agile than other benders. Chiblockers can paralyze other benders, take their bending away and can fight extremely well.";

	public static boolean guiRequireItem = true;
	public static ItemStack guiItem = new ItemStack(Material.COMPASS);
	public static boolean guiItemEnchanted = false;

	public static boolean alerts = true;
	public static boolean quickSnoop = true;

	public static boolean showSubElementsOnUser = false;
	public static String getGiveMessage = "Right click the compass to configure your bending!";

	public static void load()
	{
		elementIcons.put(Element.ICE, Material.ICE);
		elementIcons.put(Element.LAVA, Material.ORANGE_TERRACOTTA);
		elementIcons.put(Element.BLOOD, Material.RED_TERRACOTTA);
		elementIcons.put(Element.METAL, Material.IRON_BLOCK);
		elementIcons.put(Element.LIGHTNING, Material.NETHERRACK);
		elementIcons.put(Element.COMBUSTION, Material.NETHERRACK);
		if (hasBlueFire) elementIcons.put(Element.BLUE_FIRE, Material.NETHERRACK);
		elementIcons.put(Element.FLIGHT, Material.QUARTZ_BLOCK);
		elementIcons.put(Element.HEALING, Material.BLUE_TERRACOTTA);
		elementIcons.put(Element.PLANT, Material.OAK_LEAVES);
		elementIcons.put(Element.SAND, Material.SAND);
		elementIcons.put(Element.SPIRITUAL, Material.QUARTZ_BLOCK);

		elementIcons.put(Element.AIR, Material.QUARTZ_BLOCK);
		elementIcons.put(Element.WATER, Material.BLUE_TERRACOTTA);
		elementIcons.put(Element.FIRE, Material.NETHERRACK);
		elementIcons.put(Element.EARTH, Material.GRASS_BLOCK);
		elementIcons.put(Element.CHI, Material.YELLOW_TERRACOTTA);

		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(BendingGUI.INSTANCE.getDataFolder(), "config.yml"));

			for (Element e : Element.getSubElements())
			{
				String stringicon = config.getString("Icons.SubElements." + e.getName(), elementIcons.get(e).toString());
				Material icon = loadMaterial(stringicon);
				elementIcons.put(e, icon);
			}

			for (Element e1 : Element.getMainElements())
			{
				String stringicon = config.getString("Icons.Elements." + e1.getName(), elementIcons.get(e1).toString());
				Material icon = loadMaterial(stringicon);
				elementIcons.put(e1, icon);
			}
			avatarIcon = loadMaterial(config.getString("Icons.Elements.Avatar", avatarIcon.toString()));

			fireDesc = config.getString("General.Description.Fire", fireDesc);
			waterDesc = config.getString("General.Description.Water", waterDesc);
			earthDesc = config.getString("General.Description.Earth", earthDesc);
			airDesc = config.getString("General.Description.Air", airDesc);
			chiDesc = config.getString("General.Description.Chi", chiDesc);

			alerts = config.getBoolean("Admin.Alerts", alerts);
			quickSnoop = config.getBoolean("Admin.QuickSnoop", quickSnoop);

			guiRequireItem = config.getBoolean("Gui.RequireItem", guiRequireItem);
			guiItemEnchanted = config.getBoolean("Gui.Item.EnchantedGlow", guiItemEnchanted);
			Material mat = Material.getMaterial(config.getString("Gui.Item.Material", guiItem.getType().toString()));
			getGiveMessage = config.getString("Gui.GiveMessage", getGiveMessage);
//			int damage = config.getInt("Gui.Item.Damage");
			if (mat != null && mat != Material.AIR)
			{
				guiItem = new ItemStack(mat, 1/*, (short) damage*/);
				if (guiItemEnchanted)
				{
					BendingGUI.addGlow(guiItem);
				}
			}

			showSubElementsOnUser = config.getBoolean("Gui.UserIcon.ShowSubelements", showSubElementsOnUser);
		}
		catch (FileNotFoundException e) {}
		catch (IOException e)
		{
			BendingGUI.log.warning("Warning: Something went wrong while the config was loading!");
		} catch (InvalidConfigurationException e)
		{
			BendingGUI.log.warning("Warning: Config file invalid! Backing up old one and generating default one...");
			File file = new File(BendingGUI.INSTANCE.getDataFolder(), "config.yml");
			FileUtil.copy(file, new File(BendingGUI.INSTANCE.getDataFolder(), "config-backup.yml"));
			file.delete();
		}

		for (Element e1 : elementIcons.keySet())
		{
			if (e1 instanceof SubElement) config.set("Icons.SubElements." + e1.getName(), elementIcons.get(e1).toString());
			else config.set("Icons.Elements." + e1.getName(), elementIcons.get(e1).toString());
		}
		config.set("Icons.Elements.Avatar", avatarIcon.toString());

		config.set("General.Description.Fire", fireDesc);
		config.set("General.Description.Water", waterDesc);
		config.set("General.Description.Earth", earthDesc);
		config.set("General.Description.Air", airDesc);
		config.set("General.Description.Chi", chiDesc);

		config.set("Gui.Item.Material", guiItem.getType().toString());
//		config.set("Gui.Item.Damage", guiItem.getDurability());
		config.set("Gui.Item.EnchantedGlow", guiItemEnchanted);
		config.set("Gui.RequireItem", guiRequireItem);
		config.set("Gui.GiveMessage", getGiveMessage);

		config.set("Admin.Alerts", alerts);
		config.set("Admin.QuickSnoop", quickSnoop);

		config.set("Gui.UserIcon.ShowSubelements", showSubElementsOnUser);

		try
		{
			config.save(new File(BendingGUI.INSTANCE.getDataFolder(), "config.yml"));
		}
		catch (IOException e1)
		{
			BendingGUI.log.warning("Failed to save config file!");
			e1.printStackTrace();
		}
	}
	
	/*protected static List<String> getEnabledIcons()
	{
		List<String> l1 = new ArrayList<String>();
		for (SubElement e : subElementsEnableIcons)
		{
			if (e.toString().toLowerCase().endsWith("bending"))
			{
				l1.add(e.toString().toUpperCase().replaceAll("BENDING", ""));
			}
			else
			{
				l1.add(e.toString().toUpperCase());
			}
		}
		return l1;
	}*/

	/**null-safe Material loader from String*/
	protected static Material loadMaterial(String materialString)
	{
		if (materialString == null || materialString.trim().isEmpty()) return Material.STONE;
		Material material = Material.getMaterial(materialString);
		return material != null ? material : Material.STONE;
	}
}
