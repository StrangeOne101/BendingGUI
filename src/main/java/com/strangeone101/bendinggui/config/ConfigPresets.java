package com.strangeone101.bendinggui.config;

import com.projectkorra.projectkorra.object.Preset;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.menus.MenuSelectPresets;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigPresets extends ConfigBase {

	private static ConfigPresets INSTANCE;

	private Map<String, List<Material>> keywords = new HashMap<>();
	private Map<String, Material> global = new HashMap<>();


	public ConfigPresets() {
		super("presets.yml");
		
		this.load();
		
		INSTANCE = this;
	}

	public static ConfigPresets getInstance() {
		return INSTANCE;
	}

	@Override
	public Map<String, Object> addDefaults() {
		HashMap<String, Object> defaults = new HashMap<>();

		for (String keyword : MenuSelectPresets.getKeyedDefaults().keySet()) {
			String materials = listToString(MenuSelectPresets.getKeyedDefaults().get(keyword));

			defaults.put("PresetKeywords." + keyword, materials);
		}

		defaults.put("GlobalPresetIcons.example", Material.NETHER_STAR);

		return defaults;
	}

	@Override
	public void load() {
		config = YamlConfiguration.loadConfiguration(getFile());

		Map<String, Object> defaults = addDefaults();
		for (String key : defaults.keySet()) {
			Object o = defaults.get(key);
			if (o instanceof Material) {
				o = ((Material) o).name(); //Convert to string
			} else if (o instanceof ItemStack) {
				o = ((ItemStack)o).getType().toString(); //Convert to string from ItemStack
			}
			config.addDefault(key, o);
		}

		try {
			config.options().copyDefaults(!getFile().exists());
			config.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		getKeywords().clear();
		getGlobal().clear();

		for (String keyword : config.getConfigurationSection("PresetKeywords").getKeys(false)) {
			String materials = config.getString("PresetKeywords." + keyword);
			getKeywords().put(keyword, stringToList(materials));
		}

		for (String globalPreset : config.getConfigurationSection("GlobalPresetIcons").getKeys(false)) {
			String mat = config.getString("GlobalPresetIcons." + globalPreset);

			if (!Preset.externalPresetExists(globalPreset)) {
				BendingGUI.log.warning("Invalid global preset name: " + globalPreset);
			}

			getGlobal().put(globalPreset.toLowerCase(Locale.ROOT), getMaterialFromString(mat));
		}
	}

	private String listToString(List<Material> materials) {
		return materials.stream().map(Enum::name).collect(Collectors.joining(", "));
	}

	private List<Material> stringToList(String string) {
		return Arrays.stream(string.replace(" ", "").split(",")).map(this::getMaterialFromString).collect(Collectors.toList());
	}

	private Material getMaterialFromString(String material) {
		Material m = Material.getMaterial(material);
		if (m == null) m = Material.GLASS;
		return m;
	}

	public Map<String, List<Material>> getKeywords() {
		return keywords;
	}

	public Map<String, Material> getGlobal() {
		return global;
	}
}
