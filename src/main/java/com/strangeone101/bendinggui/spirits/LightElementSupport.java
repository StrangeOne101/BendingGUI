package com.strangeone101.bendinggui.spirits;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.api.ChooseSupport;
import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.api.ElementSupport;
import me.numin.spirits.SpiritElement;
import org.bukkit.Material;

public class LightElementSupport implements ElementSupport, ChooseSupport {

    @Override
    public int getChooseMenuIndex() {
        return 10;
    }

    @Override
    public Element getElement() {
        return SpiritElement.LIGHT_SPIRIT;
    }

    @Override
    public String getLangChooseTitle() {
        return "&bChoose &2&lLIGHT SPIRIT";
    }

    @Override
    public String getLangChooseLore() {
        return "&7A Light Spirit is a spirit rich in energy that is perfectly balanced. " +
                "Because they are a being pure of energy, they are able to interact with the " +
                "world differently to other beings";
    }

    @Override
    public int getOrderIndex() {
        return ElementOrder.SPIRIT_LIGHT;
    }

    @Override
    public Material getSlotMaterial() {
        return Material.CYAN_STAINED_GLASS_PANE;
    }

    @Override
    public String getLangOverviewName() {
        return getColor().toString().replace('\u00A7', '&') + "Light Spirit";
    }

    @Override
    public Material getAbilityMaterial() {
        return Material.WHITE_GLAZED_TERRACOTTA;
    }

    @Override
    public Material getChooseMaterial() {
        return Material.WHITE_DYE;
    }

    @Override
    public String getFancyName() {
        return "Light Spirit";
    }
}
