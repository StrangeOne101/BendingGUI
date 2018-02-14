package com.strangeone101.bendinggui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.FileUtil;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;

public class Config 
{
	//public static List<SubElement> subElementsEnableIcons = Arrays.asList(new SubElement[] {SubElement.Lavabending, SubElement.Icebending});
	public static HashMap<Element, MaterialData> elementIcons = new HashMap<Element, MaterialData>();
	public static MaterialData avatarIcon = new MaterialData(Material.BEACON);
	
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
	
	@SuppressWarnings({ "deprecation" })
	public static void load()
	{
		elementIcons.put(Element.ICE, new MaterialData(Material.ICE));
		elementIcons.put(Element.LAVA, new MaterialData(Material.STAINED_CLAY, (byte)1));
		elementIcons.put(Element.BLOOD, new MaterialData(Material.STAINED_CLAY, (byte)14));
		elementIcons.put(Element.METAL, new MaterialData(Material.IRON_BLOCK));
		elementIcons.put(Element.LIGHTNING, new MaterialData(Material.NETHERRACK));
		elementIcons.put(Element.COMBUSTION, new MaterialData(Material.NETHERRACK));
		elementIcons.put(Element.FLIGHT, new MaterialData(Material.QUARTZ_BLOCK));
		elementIcons.put(Element.HEALING, new MaterialData(Material.STAINED_CLAY, (byte)11));
		elementIcons.put(Element.PLANT, new MaterialData(Material.LEAVES));
		elementIcons.put(Element.SAND, new MaterialData(Material.SAND));
		elementIcons.put(Element.SPIRITUAL, new MaterialData(Material.QUARTZ_BLOCK));
		
		elementIcons.put(Element.AIR, new MaterialData(Material.QUARTZ_BLOCK));
		elementIcons.put(Element.WATER, new MaterialData(Material.STAINED_CLAY, (byte)11));
		elementIcons.put(Element.FIRE, new MaterialData(Material.NETHERRACK));
		elementIcons.put(Element.EARTH, new MaterialData(Material.GRASS));
		elementIcons.put(Element.CHI, new MaterialData(Material.STAINED_CLAY, (byte)4));
		
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(BendingGUI.INSTANCE.getDataFolder(), "config.yml"));
			
			for (Element e : Element.getSubElements())
			{
				String stringicon = config.getString("Icons.SubElements." + e.getName(), saveMaterialData(elementIcons.get(e)));
				MaterialData icon = loadMaterialData(stringicon);
				elementIcons.put(e, icon);
			}
			
			for (Element e1 : Element.getMainElements())
			{
				String stringicon = config.getString("Icons.Elements." + e1.getName(), saveMaterialData(elementIcons.get(e1)));
				MaterialData icon = loadMaterialData(stringicon);
				elementIcons.put(e1, icon);
			}
			avatarIcon = loadMaterialData(config.getString("Icons.Elements.Avatar", saveMaterialData(avatarIcon)));
			
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
			int damage = config.getInt("Gui.Item.Damage");
			if (mat != null && mat != Material.AIR)
			{
				guiItem = new ItemStack(mat, 1, (short) damage);
				if (guiItemEnchanted)
				{
					guiItem = BendingGUI.getNMSManager().addGlow(guiItem);
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
			if (e1 instanceof SubElement) config.set("Icons.SubElements." + e1.getName(), saveMaterialData(elementIcons.get(e1)));
			else config.set("Icons.Elements." + e1.getName(), saveMaterialData(elementIcons.get(e1)));
		}
		config.set("Icons.Elements.Avatar", saveMaterialData(avatarIcon));
		
		config.set("General.Description.Fire", fireDesc);
		config.set("General.Description.Water", waterDesc);
		config.set("General.Description.Earth", earthDesc);
		config.set("General.Description.Air", airDesc);
		config.set("General.Description.Chi", chiDesc);
		
		config.set("Gui.Item.Material", guiItem.getType().toString());
		config.set("Gui.Item.Damage", guiItem.getDurability());
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
	
	/**Makes MaterialData into a string*/
	@SuppressWarnings("deprecation")
	protected static String saveMaterialData(MaterialData data)
	{
		String s = data.getItemType().toString() + ":" + data.getData();
		return s.endsWith(":0") ? s.split("\\:")[0] : s;
	}
	
	/**Loads MaterialData from a string*/
	@SuppressWarnings("deprecation")
	protected static MaterialData loadMaterialData(String data)
	{
		if (data == null || data.equals("")) return new MaterialData(Material.STONE);
		Material material = Material.getMaterial(data.contains(":") ? data.split("\\:")[0] : data);
		byte b = data.contains(":") ? Byte.parseByte(data.split("\\:")[1]) : (byte)0;
		return new MaterialData(material, b);
	}
}
