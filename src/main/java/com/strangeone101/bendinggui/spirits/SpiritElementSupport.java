package com.strangeone101.bendinggui.spirits;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.api.ElementSupport;
import org.bukkit.Material;

public class SpiritElementSupport implements ElementSupport {

    @Override
    public Element getElement() {
        return SpiritsSupport.SPIRIT;
    }

    @Override
    public int getOrderIndex() {
        return ElementOrder.SPIRIT;
    }

    @Override
    public Material getSlotMaterial() {
        return Material.CYAN_STAINED_GLASS_PANE;
    }

    @Override
    public Material getAbilityMaterial() {
        return Material.BLUE_GLAZED_TERRACOTTA;
    }
}
