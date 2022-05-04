package com.strangeone101.bendinggui.config;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.Util;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigStandard extends ConfigBase {


	private static final Material UNKNOWN_ELEMENT = Material.CARVED_PUMPKIN;
	private static ConfigStandard INSTANCE;

	private HashMap<Element, Material> abilityElementIcons = new HashMap<Element, Material>();
	private HashMap<Element, Material> chooseElementIcons = new HashMap<Element, Material>();

	private Material item;
	private boolean glow;

	private boolean adminAlerts;
	private boolean quickSnoop;
	private boolean useItem;

	private boolean destroyOnDeath;
	private boolean destroyOnThrow;
	private boolean destroyOnStore;
	private boolean destroyOnStoreChests;

	private boolean showSubElements;
	private int abilityTrim;
	private int elementTrim;
	private int overviewTrim;
	
	public ConfigStandard() {
		super("config.yml");
		
		this.load();
		
		INSTANCE = this;
	}

	public static ConfigStandard getInstance() {
		return INSTANCE;
	}

	@Override
	public Map<String, Object> addDefaults() {
		HashMap<String, Object> defaults = new HashMap<>();

		defaults.put("AbilityIcons.SubElement.Ice", Material.ICE);
		defaults.put("AbilityIcons.SubElement.Lava", Material.ORANGE_TERRACOTTA);
		defaults.put("AbilityIcons.SubElement.Blood", Material.RED_TERRACOTTA);
		defaults.put("AbilityIcons.SubElement.Metal", Material.IRON_BLOCK);
		defaults.put("AbilityIcons.SubElement.Lightning", Material.NETHERRACK);
		defaults.put("AbilityIcons.SubElement.Combustion", Material.NETHERRACK);
		defaults.put("AbilityIcons.SubElement.BlueFire", Material.NETHERRACK);
		defaults.put("AbilityIcons.SubElement.Flight", Material.QUARTZ_BLOCK);
		defaults.put("AbilityIcons.SubElement.Healing", Material.BLUE_TERRACOTTA);
		defaults.put("AbilityIcons.SubElement.Plant", Material.OAK_LEAVES);
		defaults.put("AbilityIcons.SubElement.Sand", Material.SAND);
		defaults.put("AbilityIcons.SubElement.Spiritual", Material.QUARTZ_BLOCK);

		defaults.put("AbilityIcons.Element.Air", Material.QUARTZ_BLOCK);
		defaults.put("AbilityIcons.Element.Water", Material.BLUE_TERRACOTTA);
		defaults.put("AbilityIcons.Element.Fire", Material.NETHERRACK);
		defaults.put("AbilityIcons.Element.Earth", Material.GRASS_BLOCK);
		defaults.put("AbilityIcons.Element.Chi", Material.YELLOW_TERRACOTTA);

		defaults.put("AbilityIcons.Element.Avatar", Material.BEACON);

		defaults.put("DisplayIcons.Fire", Material.BLAZE_POWDER);
		defaults.put("DisplayIcons.Water", Material.WATER_BUCKET);
		defaults.put("DisplayIcons.Earth", Material.GRASS_BLOCK);
		defaults.put("DisplayIcons.Air", Material.STRING);
		defaults.put("DisplayIcons.Chi", Material.STICK);

		defaults.put("Admin.Alerts", true);
		defaults.put("Admin.QuickSnoop", true);

		defaults.put("Icon.Material", Material.COMPASS);
		defaults.put("Icon.Glow", false);
		defaults.put("Icon.Destroy.OnDeath", true);
		defaults.put("Icon.Destroy.OnThrow", false);
		defaults.put("Icon.Destroy.OnStoreInChests", false);
		defaults.put("Icon.Destroy.OnStore", true);


		defaults.put("UseItem", true);

		defaults.put("Display.ShowSubElementsOnPlayer", false);
		defaults.put("Display.AbilityDescriptionTrim", 45);
		defaults.put("Display.PlayerOverviewTrim", 55);
		defaults.put("Display.ElementDescriptionTrim", 55);

		return defaults;
	}

	@Override
	public void load() {
		if (YamlConfiguration.loadConfiguration(getFile()).contains("General.Description.Fire")) { //Old config
			BendingGUI.INSTANCE.getLogger().info("Old config detected! Moving it and generating the new one!");
			try {
				Files.move(this.getFile().toPath(), new File(this.getFile().getParent(), "config_old.yml").toPath(), StandardCopyOption.COPY_ATTRIBUTES);
				if (this.getFile().exists()) this.getFile().delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.load();

		for (Element element : Element.getAllElements()) {
			Material mat = getMaterial("AbilityIcons.Element." + element.getName());
			abilityElementIcons.put(element, mat == null ? UNKNOWN_ELEMENT : mat);

			mat = getMaterial("DisplayIcons.Element." + element.getName());
			chooseElementIcons.put(element, mat == null ? UNKNOWN_ELEMENT : mat);
		}

		for (Element.SubElement element : Element.getAllSubElements()) {
			Material mat = getMaterial("AbilityIcons.SubElement." + element.getName());
			abilityElementIcons.put(element, mat == null ? UNKNOWN_ELEMENT : mat);
		}

		adminAlerts = getBoolean("Admin.Alerts");
		useItem = getBoolean("UseItem");

		item = getMaterial("Icon.Material");
		if (item == null) item = Material.COMPASS;

		glow = getBoolean("Icon.Glow");

		destroyOnDeath = getBoolean("Icon.Destroy.OnDeath");
		destroyOnThrow = getBoolean("Icon.Destroy.OnThrow");
		destroyOnStoreChests = getBoolean("Icon.Destroy.OnStoreInChests");
		destroyOnStore = getBoolean("Icon.Destroy.OnStore");

		showSubElements = getBoolean("Display.ShowSubElementsOnPlayer");
		abilityTrim = getInteger("Display.AbilityDescriptionTrim");
		elementTrim = getInteger("ElementDescriptionTrim");
		overviewTrim = getInteger("Display.PlayerOverviewTrim");
	}

	public boolean hasAdminAlerts() {
		return adminAlerts;
	}

	public boolean doDestroyOnDeath() {
		return destroyOnDeath;
	}

	public boolean doDestroyOnStore() {
		return destroyOnStore;
	}

	public boolean doDestroyOnStoreInChests() {
		return destroyOnStoreChests;
	}

	public boolean doDestroyOnThrow() {
		return destroyOnThrow;
	}

	public Material getAbilityIconMaterial(Element element) {
		return abilityElementIcons.get(element);
	}

	public Material getChooseIconMaterial(Element element) {
		return chooseElementIcons.get(element);
	}

	public boolean doUseItem() {
		return useItem;
	}

	public ItemStack getItem() {
		ItemStack stack = new ItemStack(item);
		if (glow) Util.addGlow(stack);

		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(new LangBuilder("Item.Title").toString());
		meta.setLore(Arrays.asList(new LangBuilder("Item.Lore").toString().split("\\n")));
		meta.getPersistentDataContainer().set(BendingGUI.COMPASS, PersistentDataType.BYTE, (byte) 0);

		stack.setItemMeta(meta);
		return stack;
	}

	public boolean doShowSubElements() {
		return showSubElements;
	}

	public int getAbilityTrim() {
		return abilityTrim;
	}

	public int getElementTrim() {
		return elementTrim;
	}

	public int getOverviewTrim() {
		return overviewTrim;
	}
}
