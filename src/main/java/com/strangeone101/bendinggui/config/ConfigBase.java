package com.strangeone101.bendinggui.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.strangeone101.bendinggui.BendingGUI;

public abstract class ConfigBase {
	
	protected FileConfiguration config;
	private File file;
	private String name;
	
	public ConfigBase(String name) {
		this.name = name;
		this.file = new File(BendingGUI.INSTANCE.getDataFolder(), name);
	}
	
	/**
	 * Loads the config from file and loads the values
	 */
	public void load() {
		config = YamlConfiguration.loadConfiguration(file);
		
		Map<String, Object> defaults = addDefaults();
		for (String key : defaults.keySet()) {
			Object o = defaults.get(key);
			if (o instanceof Material) {
				o = ((Material) o).name(); //Convert to string
			} else if (o instanceof ItemStack) {
				o = ((ItemStack)o).getType().toString(); //Convert to string from ItemStack
			}
			config.addDefault(key, defaults.get(key));
		}
		
		try {
			config.options().copyDefaults(true);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get all the defaults of the config
	 * @return A map of all the defaults
	 */
	public abstract Map<String, Object> addDefaults();
	
	public String getFilename() {
		return name;
	}

	public File getFile() {
		return file;
	}

	public boolean getBoolean(String key) {
		return config.getBoolean(key);
	}
	
	public int getInteger(String key) {
		return config.getInt(key);
	}
	
	public long getLong(String key) {
		return config.getLong(key);
	}
	
	public String getString(String key) {
		return config.getString(key);
	}
	
	public List<String> getList(String key) {
		return config.getStringList(key);
	}
	
	public Material getMaterial(String key) {
		return Material.valueOf(config.getString(key));
	}
	
	public void set(String key, Object object) {
		config.set(key, object);
	}
	
	protected void addDefault(String key, Object object) {
		config.addDefault(key, object);
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
