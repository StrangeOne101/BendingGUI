package com.strangeone101.bendinggui.spirits;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.API;
import me.numin.spirits.SpiritElement;
import org.bukkit.Bukkit;

public class SpiritsSupport {

    public SpiritsSupport() {
        if (isEnabled()) {
            API.registerElementSupport(new SpiritElementSupport());
            API.registerElementSupport(new LightElementSupport());
            API.registerElementSupport(new DarkElementSupport());
        }
    }

    public static boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Spirits");
    }

    private void registerSpiritStuff() {

    }

    public static boolean isSpiritElement(Element element) {
        return isEnabled() && (element == SpiritElement.SPIRIT || element == SpiritElement.LIGHT_SPIRIT || element == SpiritElement.DARK_SPIRIT);
    }

    public static void giveElement(Element lightOrDark, BendingPlayer player) {
        if (!player.hasElement(SpiritElement.SPIRIT)) {
            player.addElement(SpiritElement.SPIRIT);
        }

        player.addElement(lightOrDark);
    }

    public static void removeElement(Element lightOrDark, BendingPlayer player) {
        player.getElements().remove(lightOrDark);

        for (Element element : player.getElements()) {
            if (isSpiritElement(element)) return;
        }

        player.getElements().remove(SpiritElement.SPIRIT);
    }



}
