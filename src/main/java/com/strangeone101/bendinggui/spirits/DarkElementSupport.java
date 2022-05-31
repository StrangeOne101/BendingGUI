package com.strangeone101.bendinggui.spirits;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.api.ChooseSupport;
import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.api.ElementSupport;

import org.bukkit.Material;

public class DarkElementSupport implements ElementSupport, ChooseSupport {

    @Override
    public int getChooseMenuIndex() {
        return 16;
    }

    @Override
    public Element getElement() {
        return SpiritsSupport.DARK_SPIRIT;
    }

    @Override
    public String getLangChooseTitle() {
        return "&9Choose &3&lDark Spirit";
    }

    @Override
    public String getLangChooseLore() {
        return "&7A Dark Spirit is a spirit corrupted by dark and chaotic energy. " +
                "Because they are a being pure of energy, they are able to interact with the " +
                "world differently to other beings";
    }

    @Override
    public int getOrderIndex() {
        return ElementOrder.SPIRIT_DARK;
    }

    @Override
    public Material getSlotMaterial() {
        return Material.CYAN_STAINED_GLASS_PANE;
    }

    @Override
    public String getLangOverviewName() {
        return getColor().toString().replace('\u00A7', '&') + "Dark Spirit";
    }

    @Override
    public Material getAbilityMaterial() {
        return Material.BLACK_GLAZED_TERRACOTTA;
    }

    @Override
    public Material getChooseMaterial() {
        return Material.PURPLE_DYE;
    }

    @Override
    public String getFancyName() {
        return "Dark Spirit";
    }
}
