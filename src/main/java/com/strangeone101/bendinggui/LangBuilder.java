package com.strangeone101.bendinggui;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.strangeone101.bendinggui.config.ConfigLanguage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class LangBuilder {

    private String key;
    private String value = "";

    public LangBuilder(String key) {
        this.key = key;
        this.value = ConfigLanguage.getInstance().getString(key);
        if (value == null) value = "&c" + key;
    }

    public LangBuilder player(OfflinePlayer player) {
        this.value = value.replace("{player}", player.getName());
        return this;
    }

    public LangBuilder slot(int slot) {
        this.value = value.replace("{slot}", slot + "");
        return this;
    }

    public LangBuilder page(int current, int max) {
        this.value = value.replace("{current}", current + "").replace("{max}", max + "");
        return this;
    }

    public LangBuilder ability(CoreAbility ability) {
        this.value = value.replace("{ability}", ability.getName()).replace("{abilitycolor}", ability.getElement().getColor().toString());
        return this;
    }

    public LangBuilder element(Element element) {
        this.value = value.replace("{element}", element.getName()).replace("{ELEMENT}", element.getName().toUpperCase())
                .replace("{elementcolor}", element.getColor().toString())
                .replace("{bender}", element.getType().getBender())
                .replace("{bending}", element.getType().getBending())
                .replace("{bend}", element.getType().getBend());
        return this;
    }

    public LangBuilder plural(String word) {
        this.value = value.replace("{s}", word.toLowerCase().endsWith("s") ? "" : "s");
        return this;
    }


    public LangBuilder yourOrPlayer(OfflinePlayer target, OfflinePlayer controller) {
        if (target == controller) {
            this.value = value.replace("{player|your}", new LangBuilder("Generic.Your").toString())
                    .replace("{player|yourself}", new LangBuilder("Generic.Yourself").toString())
                    .replace("{player|you}", new LangBuilder("Generic.You").toString());
        } else {
            this.value = value.replace("{player|your}", target.getName())
                    .replace("{player|yourself}", target.getName())
                    .replace("{player|you}", target.getName());
        }

        return this;
    }

    public LangBuilder anOrA(String proceedingWord) {
        proceedingWord = proceedingWord.toLowerCase();
        if (proceedingWord.charAt(0) == 'a' || proceedingWord.charAt(0) == 'e' || proceedingWord.charAt(0) == 'i' ||
                proceedingWord.charAt(0) == 'o' || proceedingWord.charAt(0) == 'u') {
            this.value = value.replace("{a}", new LangBuilder("Generic.An").toString());
        } else {
            this.value = value.replace("{a}", new LangBuilder("Generic.A").toString());
        }

        return this;
    }

    public LangBuilder list(String... list) {
        String key = "Generic.List" + list.length;
        LangBuilder innerList = new LangBuilder(key);
        for (int i = 0; i < list.length; i++) {
            innerList.value = innerList.value.replaceFirst("\\{item}", list[i]);
        }
        this.value = this.value.replace("{list}", innerList.toString());
        return this;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', value.replace("\\n", "\n"));
    }


    public static LangBuilder getAbilityDescription(CoreAbility ability) {
        LangBuilder builder = new LangBuilder("Abilities." + ability.getName());
        if (ability instanceof ComboAbility) builder = new LangBuilder("Abilities.Combo-" + ability.getName());

        if (builder.value.equals("&c" + builder.key)) builder = new LangBuilder("Display.Errors.NoAbilityDescription");
        return builder;
    }

    public String getKey() {
        return key;
    }
}
