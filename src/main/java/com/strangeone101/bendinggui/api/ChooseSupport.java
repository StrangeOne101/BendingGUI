package com.strangeone101.bendinggui.api;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.Material;

public interface ChooseSupport {

    int getChooseMenuIndex();

    default Material getChooseMaterial() {
        return ConfigStandard.getInstance().getChooseIconMaterial(getElement());
    }

    Element getElement();

    String getLangChooseTitle();

    String getLangChooseLore();

}
