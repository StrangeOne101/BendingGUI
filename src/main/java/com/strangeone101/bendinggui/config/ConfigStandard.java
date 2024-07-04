package com.strangeone101.bendinggui.config;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.Util;
import com.strangeone101.bendinggui.api.ChooseSupport;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.menus.MenuSelectPresets;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigStandard extends ConfigBase {


	private static final Material UNKNOWN_ELEMENT = Material.CARVED_PUMPKIN;
	private static ConfigStandard INSTANCE;

	private HashMap<Element, Material> abilityElementIcons = new HashMap<Element, Material>();
	private HashMap<Element, Material> chooseElementIcons = new HashMap<Element, Material>();

	private Material item;
	private boolean glow;

	private boolean adminAlerts;
	private boolean adminModelData;
	private boolean useItem;

	private int modelDataBase;

	private boolean destroyOnDeath;
	private boolean destroyOnThrow;
	private boolean destroyOnStore;
	private boolean destroyOnStoreChests;

	private boolean giveFirstJoin;
	private boolean giveChangeWorld;

	private boolean showSubElements;
	private int abilityTrim;
	private int elementTrim;
	private int overviewTrim;

	private boolean showChoosePromptOnFirstJoin;
	private boolean closeMenuOnChoose;
	private boolean hideUnusableElements;
	
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
		defaults.put("Admin.ShowModelData", false);
		defaults.put("Admin.BaseModelData", 11070);
		defaults.put("Admin.UniqueAbilityModelData", true);

		defaults.put("Item.Material", Material.COMPASS);
		defaults.put("Item.Glow", false);
		defaults.put("Item.Destroy.OnDeath", true);
		defaults.put("Item.Destroy.OnThrow", false);
		defaults.put("Item.Destroy.OnStoreInChests", false);
		defaults.put("Item.Destroy.OnStore", true);
		defaults.put("Item.Give.FirstJoin", true);
		defaults.put("Item.Give.ChangeWorld", false);
		defaults.put("Item.Enabled", true);

		defaults.put("Display.ShowSubElementsOnPlayer", false);
		defaults.put("Display.AbilityDescriptionTrim", 45);
		defaults.put("Display.PlayerOverviewTrim", 55);
		defaults.put("Display.ElementDescriptionTrim", 50);
		defaults.put("Display.ShowChoosePromptOnFirstJoin", false);
		defaults.put("Display.HideNonUsableElements", false);
		defaults.put("Display.CloseMenuOnChoose", false);

		for (Element customElement : BendingGUI.INSTANCE.getSupportedElements()) {
			ElementSupport support = BendingGUI.INSTANCE.getSupportedElement(customElement);

			String subOrNot = support.getElement() instanceof Element.SubElement ? "SubElement" : "Element";

			defaults.put("AbilityIcons." + subOrNot + "." + support.getElement().getName(), support.getAbilityMaterial());

			if (support instanceof ChooseSupport) {
				defaults.put("DisplayIcons." + support.getElement().getName(), ((ChooseSupport) support).getChooseMaterial());
			}
		}

		for (Element customElement : Element.getAddonElements()) {
			if (!defaults.containsKey("AbilityIcons.Element." + customElement.getName())) {
				defaults.put("AbilityIcons.Element." + customElement.getName(), Material.STONE);
			}
		}

		for (Element.SubElement customSub : Element.getAddonSubElements()) {
			if (!defaults.containsKey("AbilityIcons.SubElement." + customSub.getName())) {
				String mat = Material.GRANITE.name();
				if (defaults.containsKey("AbilityIcons.Element." + customSub.getParentElement().getName())) {
					mat = defaults.get("AbilityIcons.Element." + customSub.getParentElement().getName()).toString();
				}
				defaults.put("AbilityIcons.SubElement." + customSub.getName(), mat);
			}
		}

		return defaults;
	}

	@Override
	public void load() {
		if (YamlConfiguration.loadConfiguration(getFile()).contains("General.Description.Fire")) { //Old config
			BendingGUI.INSTANCE.getLogger().info("Old config detected! Moving it and generating the new one!");
			try {
				Files.move(this.getFile().toPath(), new File(this.getFile().getParent(), "config_old.yml").toPath(), StandardCopyOption.REPLACE_EXISTING);
				if (this.getFile().exists()) this.getFile().delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.load();

		for (Element element : Element.getAllElements()) {
			Material mat = getMaterial("AbilityIcons.Element." + element.getName());
			abilityElementIcons.put(element, mat == null ? UNKNOWN_ELEMENT : mat);

			mat = getMaterial("DisplayIcons." + element.getName());
			chooseElementIcons.put(element, mat == null ? UNKNOWN_ELEMENT : mat);
		}

		for (Element.SubElement element : Element.getAllSubElements()) {
			Material mat = getMaterial("AbilityIcons.SubElement." + element.getName());
			abilityElementIcons.put(element, mat == null ? UNKNOWN_ELEMENT : mat);
		}

		adminAlerts = getBoolean("Admin.Alerts");
		adminModelData = getBoolean("Admin.ShowModelData");
		modelDataBase = getInteger("Admin.ModelDataBase");
		useItem = getBoolean("Item.Enabled");

		item = getMaterial("Item.Material");
		if (item == null) item = Material.COMPASS;

		glow = getBoolean("Item.Glow");

		destroyOnDeath = getBoolean("Item.Destroy.OnDeath");
		destroyOnThrow = getBoolean("Item.Destroy.OnThrow");
		destroyOnStoreChests = getBoolean("Item.Destroy.OnStoreInChests");
		destroyOnStore = getBoolean("Item.Destroy.OnStore");

		giveFirstJoin = getBoolean("Item.Give.FirstJoin");
		giveChangeWorld = getBoolean("Item.Give.ChangeWorld");

		showSubElements = getBoolean("Display.ShowSubElementsOnPlayer");
		abilityTrim = getInteger("Display.AbilityDescriptionTrim");
		elementTrim = getInteger("Display.ElementDescriptionTrim");
		overviewTrim = getInteger("Display.PlayerOverviewTrim");
		showChoosePromptOnFirstJoin = getBoolean("Display.ShowChoosePromptOnFirstJoin");
		hideUnusableElements = getBoolean("Display.HideNonUsableElements");
		closeMenuOnChoose = getBoolean("Display.CloseMenuOnChoose");

		if (abilityTrim < 1) abilityTrim = 45;
		if (elementTrim < 1) elementTrim = 55;
		if (overviewTrim < 1) overviewTrim = 50;
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

	public boolean doGiveFirstJoin() {
		return giveFirstJoin;
	}

	public boolean doGiveChangeWorld() {
		return giveChangeWorld;
	}

	public boolean doShowChoosePromptOnFirstJoin() {
		return showChoosePromptOnFirstJoin;
	}

	public boolean doHideUnusableElements() {
		return hideUnusableElements;
	}

	public boolean doCloseMenuOnChoose() {
		return closeMenuOnChoose;
	}
}
