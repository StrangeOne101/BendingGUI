package com.strangeone101.bendinggui.spirits;

import org.bukkit.Bukkit;

public class SpiritsSupport {

    public SpiritsSupport() {
        if (isEnabled()) {

        }
    }

    public static boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Spirits");
    }

    private void registerSpiritStuff() {

    }

}
