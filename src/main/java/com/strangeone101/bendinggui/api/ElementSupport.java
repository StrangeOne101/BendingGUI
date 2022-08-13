package com.strangeone101.bendinggui.api;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.config.ConfigStandard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public interface ElementSupport {

    Element getElement();

    default Material getAbilityMaterial() {
        return ConfigStandard.getInstance().getAbilityIconMaterial(getElement());
    }

    default Material getSlotMaterial() {
        return Material.PINK_STAINED_GLASS_PANE;
    }

    int getOrderIndex();

    default ChatColor getColor() {
        return getElement().getColor();
    }

    default String getLangOverviewName() {
        return getColor().toString().replace('\u00A7', '&') + getElement().getName() + (getElement().getType() != null ? getElement().getType().getBender() : "");
    }

    default String getFancyName() {
        return "";
    }

}
