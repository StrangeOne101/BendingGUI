package com.strangeone101.bendinggui.config;

import java.util.Map;

public class ConfigStandard extends ConfigBase {

	public static ConfigStandard INSTANCE;
	
	public ConfigStandard() {
		super("config.yml");
		
		this.load();
		
		INSTANCE = this;
	}

	@Override
	public Map<String, Object> addDefaults() {
		// TODO Auto-generated method stub
		return null;
	}

}
