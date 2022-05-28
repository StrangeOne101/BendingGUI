package com.strangeone101.bendinggui.spirits;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import com.strangeone101.bendinggui.API;
import com.strangeone101.bendinggui.BendingGUI;
import me.xnuminousx.spirits.elements.SpiritElement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Locale;

public class SpiritsSupport {

    public SpiritsSupport() {
        if (isEnabled()) {
            API.registerElementSupport(new SpiritElementSupport());
            API.registerElementSupport(new LightElementSupport());
            API.registerElementSupport(new DarkElementSupport());

            iHateSpirits();
        }
    }

    public static boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Spirits");
    }

    public static boolean isSpiritElement(Element element) {
        return isEnabled() && (element == SpiritElement.SPIRIT || element == SpiritElement.LIGHT_SPIRIT || element == SpiritElement.DARK_SPIRIT);
    }

    public static void giveElement(Element lightOrDark, BendingPlayer player, Player sender, boolean choose) {
        if (choose) {
            player.getElements().clear();
        }

        if (!player.hasElement(SpiritElement.SPIRIT)) {
            player.addElement(SpiritElement.SPIRIT);
            PlayerChangeElementEvent event = new PlayerChangeElementEvent(sender, player.getPlayer(), SpiritElement.SPIRIT,
                    choose ? PlayerChangeElementEvent.Result.CHOOSE : PlayerChangeElementEvent.Result.ADD);
            Bukkit.getPluginManager().callEvent(event);
        }

        player.addElement(lightOrDark);
        PlayerChangeElementEvent event = new PlayerChangeElementEvent(sender, player.getPlayer(), lightOrDark,
                choose ? PlayerChangeElementEvent.Result.CHOOSE : PlayerChangeElementEvent.Result.ADD);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void removeElement(Element lightOrDark, BendingPlayer player, Player sender) {
        PlayerChangeElementEvent event = new PlayerChangeElementEvent(sender, player.getPlayer(), lightOrDark, PlayerChangeElementEvent.Result.REMOVE);
        Bukkit.getPluginManager().callEvent(event);
        player.getElements().remove(lightOrDark);

        for (Element element : player.getElements()) {
            if (isSpiritElement(element) && element != SpiritElement.SPIRIT) return;
        }

        if (player.getElements().contains(SpiritElement.SPIRIT)) {
            event = new PlayerChangeElementEvent(sender, player.getPlayer(), lightOrDark, PlayerChangeElementEvent.Result.REMOVE);
            Bukkit.getPluginManager().callEvent(event);
        }
        player.getElements().remove(SpiritElement.SPIRIT);
    }

    private static void iHateSpirits() {
        //If there are NO spirit perms defined on the server, we are gonna define them because the Spirits plugin DOESNT
        if (Bukkit.getPluginManager().getPermission("bending.darkspirit") == null) {
            BendingGUI.log.info("Injecting custom spirit permissions");
            Permission base = Bukkit.getPluginManager().getPermission("bending.player");

            Permission light = defineDefaultPerm("bending.lightspirit", "Allows the player to become a light spirit");
            Permission dark = defineDefaultPerm("bending.darkspirit", "Allows the player to become a dark spirit");
            Permission neutral = defineDefaultPerm("bending.spirit", "Allows the player to become a spirit");

            Permission lightPassive = defineDefaultPerm("bending.lightspirit.passive", "Allows the player to use light spirit passives");
            Permission darkPassive = defineDefaultPerm("bending.darkspirit.passive", "Allows the player to use light dark spirit passives");
            Permission neutralPassive = defineDefaultPerm("bending.spirit.passive", "Allows the player to use light spirit passives");

            Permission lightCombo = defineDefaultPerm("bending.ability.LightSpiritCombo", "Allows the player to use light spirit combos");
            Permission darkCombo = defineDefaultPerm("bending.ability.DarkSpiritCombo", "Allows the player to use light dark spirit combos");
            Permission neutralCombo = defineDefaultPerm("bending.ability.SpiritCombo", "Allows the player to use light spirit combos");

            light.addParent(base, true);
            dark.addParent(base, true);
            neutral.addParent(base, true);

            lightPassive.addParent(light, true);
            darkPassive.addParent(dark, true);
            neutralPassive.addParent(neutral, true);

            lightCombo.addParent(light, true);
            darkCombo.addParent(dark, true);
            neutralCombo.addParent(neutral, true);

            for (CoreAbility coreAbility : CoreAbility.getAbilities()) {
                if (isSpiritElement(coreAbility.getElement())) {
                    Permission perm = defineDefaultPerm("bending.ability." + coreAbility.getName(), "Allows the player to use " + coreAbility.getName());
                    if (coreAbility instanceof PassiveAbility) {
                        if (coreAbility.getElement() == SpiritElement.SPIRIT) perm.addParent(neutralPassive, true);
                        else if (coreAbility.getElement() == SpiritElement.LIGHT_SPIRIT) perm.addParent(lightPassive, true);
                        else if (coreAbility.getElement() == SpiritElement.DARK_SPIRIT) perm.addParent(darkPassive, true);
                    } else if (coreAbility instanceof ComboAbility) {
                        if (coreAbility.getElement() == SpiritElement.SPIRIT) perm.addParent(neutralCombo, true);
                        else if (coreAbility.getElement() == SpiritElement.LIGHT_SPIRIT) perm.addParent(lightCombo, true);
                        else if (coreAbility.getElement() == SpiritElement.DARK_SPIRIT) perm.addParent(darkCombo, true);
                    } else {
                        if (coreAbility.getElement() == SpiritElement.SPIRIT) perm.addParent(neutral, true);
                        else if (coreAbility.getElement() == SpiritElement.LIGHT_SPIRIT) perm.addParent(light, true);
                        else if (coreAbility.getElement() == SpiritElement.DARK_SPIRIT) perm.addParent(dark, true);
                    }
                }
            }
        }
    }

    private static Permission defineDefaultPerm(String key, String description) {
        if (Bukkit.getPluginManager().getPermission(key) != null) return Bukkit.getPluginManager().getPermission(key);
        Permission permission = new Permission(key);
        permission.setDescription(description);
        Bukkit.getPluginManager().addPermission(permission);
        return permission;
    }

}
