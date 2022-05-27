package com.strangeone101.bendinggui;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.object.Preset;
import com.projectkorra.projectkorra.util.TimeUtil;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.config.ConfigLanguage;
import com.strangeone101.bendinggui.menus.MenuSelectPresets;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LangBuilder {

    private String key;
    private String value = "";

    public LangBuilder(String key) {
        this.key = key.trim();
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
        if (element == null) {
            this.value = value.replace("{element}", new LangBuilder("Display.Players.NonBender").toString())
                    .replace("{ELEMENT}", new LangBuilder("Display.Players.NonBender").toString().toUpperCase())
                    .replace("{elementcolor}", ChatColor.WHITE + "")
                    .replace("{bender}", new LangBuilder("Display.Players.NonBender").toString());
            return this;
        }

        String name = element.getName();
        if (BendingGUI.INSTANCE.getSupportedElements().contains(element)) {
            ElementSupport support = BendingGUI.INSTANCE.getSupportedElement(element);
            name = support.getFancyName();
        }

        this.value = value.replace("{element}", name).replace("{ELEMENT}", name.toUpperCase())
                .replace("{elementcolor}", BendingGUI.getColor(element).toString())
                .replace("{bender}", element.getType() == Element.ElementType.NO_SUFFIX ? "" : element.getType().getBender())
                .replace("{bending}", element.getType() == Element.ElementType.NO_SUFFIX ? new LangBuilder("Generic.NoElementBending").toString() : element.getType().getBending())
                .replace("{bend}", element.getType() == Element.ElementType.NO_SUFFIX ? "" : element.getType().getBend());
        return this;
    }

    public LangBuilder plural(String word) {
        this.value = value.replace("{s}", word.toLowerCase().endsWith("s") ? "" : "s");
        return this;
    }

    public LangBuilder version(String version) {
        this.value = value.replace("{version}", version).replace("{pkversion}", BendingGUI.PK_VERSION);
        return this;
    }

    public LangBuilder yourOrPlayer(OfflinePlayer target, OfflinePlayer controller) {
        if (target.equals(controller)) {
            this.value = value.replace("{player|your}", new LangBuilder("Generic.Your").toString())
                    .replace("{player|yourself}", new LangBuilder("Generic.Yourself").toString())
                    .replace("{player|you}", new LangBuilder("Generic.You").toString())
                    .replace("{they|you}", new LangBuilder("Generic.You").toString());
        } else {
            this.value = value.replace("{player|your}", target.getName() + "'" + (target.getName().endsWith("s") ? "" : "s"))
                    .replace("{player|yourself}", target.getName())
                    .replace("{player|you}", target.getName())
                    .replace("{they|you}", new LangBuilder("Generic.They").toString());;
        }

        return this;
    }

    public LangBuilder anOrA(String proceedingWord) {
        if (proceedingWord == null || proceedingWord.equals("")) proceedingWord = "null";
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

    public LangBuilder capitalizeFirst() {
        this.value = capitalizeWord(this.value);
        return this;
    }

    public LangBuilder capitalizeAll() {
        String[] words = this.value.split(" ");
        List<String> newWords = new ArrayList<>();
        for (String s : words) {
            newWords.add(capitalizeWord(s));
        }
        this.value = String.join(" ", newWords);
        return this;
    }

    public LangBuilder time(long time) {
        if (time < 0) this.value = this.value.replace("{time}", "0s");
        else this.value = this.value.replace("{time}", TimeUtil.formatTime(time));

        return this;
    }

    public LangBuilder preset(String name, Map<Integer, String> abilities) {
        this.value = this.value.replace("{preset}", name)
                .replace("{presetcolor}", MenuSelectPresets.getPresetElement(abilities).getColor().toString());

        for (int i = 1; i <= 9; i++) {
            if (abilities.containsKey(i)) {
                String ability = abilities.get(i);
                ChatColor color = Element.AVATAR.getColor();
                if (CoreAbility.getAbility(ability) != null) color = CoreAbility.getAbility(ability).getElement().getColor();

                this.value = this.value.replace("{slot" + i + "}", color + ability);
            } else {
                this.value = this.value.replace("{slot" + i + "}", new LangBuilder("Display.Presets.Preset.Empty").toString());
            }
        }
        return this;
    }

    private String capitalizeWord(String word) {
        String copy = ChatColor.stripColor(word.replace('&', '\u00A7'))
                .replace("\\n", "").replace("\n", "");
        int place = word.indexOf(copy.charAt(0));
        word = word.substring(0, place) + word.substring(place, place + 1).toUpperCase()
                + word.substring(place + 1);
        return word;
    }



    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', value.replace("\\n", "\n"));
    }


    public static LangBuilder getAbilityDescription(CoreAbility ability) {
        LangBuilder builder = new LangBuilder("Abilities." + ability.getName());
        if (ability instanceof ComboAbility) builder = new LangBuilder("Abilities.Combo-" + ability.getName());

        if (builder.value.equals("&c" + builder.key) || builder.value.equals(ability.getName() + " placeholder here")) builder = new LangBuilder("Display.Errors.NoAbilityDescription");
        return builder;
    }

    public String getKey() {
        return key;
    }
}
