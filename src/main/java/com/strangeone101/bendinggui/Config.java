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

import com.projectkorra.ProjectKorra.Element;
import com.projectkorra.ProjectKorra.SubElement;

public class Config 
{
	//public static List<SubElement> subElementsEnableIcons = Arrays.asList(new SubElement[] {SubElement.Lavabending, SubElement.Icebending});
	public static HashMap<SubElement, MaterialData> subElementIcons = new HashMap<SubElement, MaterialData>();
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
	
	public static boolean showSubElementsOnUser = false;
	public static String getGiveMessage = "Right click the compass to configure your bending!";
	
	@SuppressWarnings({ "deprecation" })
	public static void load()
	{
		subElementIcons.put(SubElement.Icebending, new MaterialData(Material.ICE));
		subElementIcons.put(SubElement.Lavabending, new MaterialData(Material.STAINED_CLAY, (byte)1));
		subElementIcons.put(SubElement.Bloodbending, new MaterialData(Material.STAINED_CLAY, (byte)14));
		subElementIcons.put(SubElement.Metalbending, new MaterialData(Material.IRON_BLOCK));
		subElementIcons.put(SubElement.Lightning, new MaterialData(Material.NETHERRACK));
		subElementIcons.put(SubElement.Combustion, new MaterialData(Material.NETHERRACK));
		subElementIcons.put(SubElement.Flight, new MaterialData(Material.QUARTZ_BLOCK));
		subElementIcons.put(SubElement.Healing, new MaterialData(Material.STAINED_CLAY, (byte)11));
		subElementIcons.put(SubElement.Plantbending, new MaterialData(Material.LEAVES));
		subElementIcons.put(SubElement.Sandbending, new MaterialData(Material.SAND));
		subElementIcons.put(SubElement.SpiritualProjection, new MaterialData(Material.QUARTZ_BLOCK));
		
		elementIcons.put(Element.Air, new MaterialData(Material.QUARTZ_BLOCK));
		elementIcons.put(Element.Water, new MaterialData(Material.STAINED_CLAY, (byte)11));
		elementIcons.put(Element.Fire, new MaterialData(Material.NETHERRACK));
		elementIcons.put(Element.Earth, new MaterialData(Material.GRASS));
		elementIcons.put(Element.Chi, new MaterialData(Material.STAINED_CLAY, (byte)4));
		
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(BendingGUI.INSTANCE.getDataFolder(), "config.yml"));
			
			/*List<String> list1 = config.getStringList("Icons.EnabledSubElements");
			//List<SubElement> subelements = new ArrayList<SubElement>();
			for (String s : list1)
			{
				if (s == list1.get(0))
				{
					subElementsEnableIcons = new ArrayList<SubElement>();
				}
				if (s.equalsIgnoreCase("lava") || s.equalsIgnoreCase("lavabending")) subElementsEnableIcons.add(SubElement.Lavabending);
				else if (s.equalsIgnoreCase("ice") || s.equalsIgnoreCase("icebending")) subElementsEnableIcons.add(SubElement.Icebending);
				else if (s.equalsIgnoreCase("metal") || s.equalsIgnoreCase("metalbending")) subElementsEnableIcons.add(SubElement.Metalbending);
				else if (s.equalsIgnoreCase("plant") || s.equalsIgnoreCase("plantbending")) subElementsEnableIcons.add(SubElement.Plantbending);
				else if (s.equalsIgnoreCase("sand") || s.equalsIgnoreCase("sandbending")) subElementsEnableIcons.add(SubElement.Sandbending);
				else if (s.equalsIgnoreCase("blood") || s.equalsIgnoreCase("bloodbending")) subElementsEnableIcons.add(SubElement.Bloodbending);
				else if (s.equalsIgnoreCase("combustion")) subElementsEnableIcons.add(SubElement.Combustion);
				else if (s.equalsIgnoreCase("flight")) subElementsEnableIcons.add(SubElement.Flight);
				else if (s.equalsIgnoreCase("healing")) subElementsEnableIcons.add(SubElement.Healing);
				else if (s.equalsIgnoreCase("lightning")) subElementsEnableIcons.add(SubElement.Lightning);
				else if (s.equalsIgnoreCase("spirit")) subElementsEnableIcons.add(SubElement.SpiritualProjection);
			}*/
			
			for (SubElement e : SubElement.values())
			{
				String stringicon = config.getString("Icons.SubElements." + e.toString(), saveMaterialData(subElementIcons.get(e)));
				MaterialData icon = loadMaterialData(stringicon);
				subElementIcons.put(e, icon);
			}
			
			for (Element e1 : elementIcons.keySet())
			{
				String stringicon = config.getString("Icons.Elements." + e1.toString(), saveMaterialData(elementIcons.get(e1)));
				MaterialData icon = loadMaterialData(stringicon);
				elementIcons.put(e1, icon);
			}
			avatarIcon = loadMaterialData(config.getString("Icons.Elements.Avatar", saveMaterialData(avatarIcon)));
			
			fireDesc = config.getString("General.Description.Fire", fireDesc);
			waterDesc = config.getString("General.Description.Water", waterDesc);
			earthDesc = config.getString("General.Description.Earth", earthDesc);
			airDesc = config.getString("General.Description.Air", airDesc);
			chiDesc = config.getString("General.Description.Chi", chiDesc);
			
			guiRequireItem = config.getBoolean("Gui.RequireItem", guiRequireItem);
			//guiItemEnchanted = config.getBoolean("Gui.Item.EnchantedGlow", guiItemEnchanted);
			Material mat = Material.getMaterial(config.getString("Gui.Item.Material", guiItem.getType().toString()));
			getGiveMessage = config.getString("Gui.GiveMessage", getGiveMessage);
			int damage = config.getInt("Gui.Item.Damage");
			if (mat != null && mat != Material.AIR)
			{
				guiItem = new ItemStack(mat, 1, (short) damage);
				if (guiItemEnchanted)
				{
					guiItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
					guiItem.removeEnchantment(Enchantment.ARROW_INFINITE);
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
		
		//config.set("Icons.EnabledSubElements", getEnabledIcons());
		for (SubElement e : subElementIcons.keySet())
		{
			config.set("Icons.SubElements." + e.toString(), saveMaterialData(subElementIcons.get(e)));
		}
		for (Element e1 : elementIcons.keySet())
		{
			config.set("Icons.Elements." + e1.toString(), saveMaterialData(elementIcons.get(e1)));
		}
		config.set("Icons.Elements.Avatar", saveMaterialData(avatarIcon));
		
		config.set("General.Description.Fire", fireDesc);
		config.set("General.Description.Water", waterDesc);
		config.set("General.Description.Earth", earthDesc);
		config.set("General.Description.Air", airDesc);
		config.set("General.Description.Chi", chiDesc);
		
		config.set("Gui.Item.Material", guiItem.getType().toString());
		config.set("Gui.Item.Damage", guiItem.getDurability());
		//config.set("Gui.Item.EnchantedGlow", guiItemEnchanted);
		config.set("Gui.RequireItem", guiRequireItem);
		config.set("Gui.GiveMessage", getGiveMessage);
		
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
		Material material = Material.getMaterial(data.contains(":") ? data.split("\\:")[0] : data);
		byte b = data.contains(":") ? Byte.parseByte(data.split("\\:")[1]) : (byte)0;
		return new MaterialData(material, b);
	}
}
