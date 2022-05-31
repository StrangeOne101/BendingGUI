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

import java.util.ArrayList;
import java.util.List;

public class SpiritsSupport {

    public static Element SPIRIT;
    public static Element LIGHT_SPIRIT;
    public static Element DARK_SPIRIT;

    public SpiritsSupport() {
        if (isEnabled() && checkSpiritClasspath()) {
            API.registerElementSupport(new SpiritElementSupport());
            API.registerElementSupport(new LightElementSupport());
            API.registerElementSupport(new DarkElementSupport());

            iHateSpirits();
        }
    }

    /**
     * Is the Spirits plugin enabled?
     * @return True if it is enabled
     */
    public static boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Spirits");
    }

    /**
     * Is the given element LIGHT_SPIRIT, DARK_SPIRIT or SPIRIT?
     * @param element The element
     * @return True if true
     */
    public static boolean isSpiritElement(Element element) {
        return isEnabled() && (element == SPIRIT || element == LIGHT_SPIRIT || element == DARK_SPIRIT);
    }

    /**
     * Gives the light/dark spirit element, as well as the neutral spirit element if the player
     * does not already have it
     * @param lightOrDark The light or dark element to remove
     * @param player The player to remove on
     * @param sender The player that caused this change
     * @param choose Whether this is for choosing an element or adding
     */
    public static void giveElement(Element lightOrDark, BendingPlayer player, Player sender, boolean choose) {
        if (choose) {
            player.getElements().clear();
        }

        if (!player.hasElement(SPIRIT)) {
            player.addElement(SPIRIT);
            PlayerChangeElementEvent event = new PlayerChangeElementEvent(sender, player.getPlayer(), SPIRIT,
                    choose ? PlayerChangeElementEvent.Result.CHOOSE : PlayerChangeElementEvent.Result.ADD);
            Bukkit.getPluginManager().callEvent(event);
        }

        player.addElement(lightOrDark);
        PlayerChangeElementEvent event = new PlayerChangeElementEvent(sender, player.getPlayer(), lightOrDark,
                choose ? PlayerChangeElementEvent.Result.CHOOSE : PlayerChangeElementEvent.Result.ADD);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Removes the light/dark spirit element, as well as the neutral spirit element if the player
     * does not have any other spirit elements
     * @param lightOrDark The light or dark element to remove
     * @param player The player to remove on
     * @param sender The player that caused this change
     */
    public static void removeElement(Element lightOrDark, BendingPlayer player, Player sender) {
        PlayerChangeElementEvent event = new PlayerChangeElementEvent(sender, player.getPlayer(), lightOrDark, PlayerChangeElementEvent.Result.REMOVE);
        player.getElements().remove(lightOrDark);
        Bukkit.getPluginManager().callEvent(event);

        for (Element element : player.getElements()) {
            if (isSpiritElement(element) && element != SPIRIT) return;
        }

        if (player.getElements().remove(SPIRIT)) {
            event = new PlayerChangeElementEvent(sender, player.getPlayer(), lightOrDark, PlayerChangeElementEvent.Result.REMOVE);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    /**
     * Checks if the player is the avatar. This is a hotfix so players with both
     * a spirit element and a light/dark spirit element don't appear as the avatar
     * because they "have more than one element"
     * @param player The player to check
     * @return True if they are the avatar
     */
    public static boolean isAvatar(BendingPlayer player) {
        if (player.getPlayer().hasPermission("bending.avatar")) return true;
        if (player.getElements().size() > 1) {
            if (isEnabled()) {
                List<Element> clonedElements = new ArrayList<>(player.getElements());
                clonedElements.remove(SPIRIT);

                if (clonedElements.size() == 2 && player.hasElement(LIGHT_SPIRIT) && player.hasElement(DARK_SPIRIT)) return false;

                return clonedElements.size() > 1;
            }
            return true;
        }
        return false;
    }

    private static void iHateSpirits() {
        //If there are NO spirit perms defined on the server, we are gonna define them because the Spirits plugin DOESNT
        if (Bukkit.getPluginManager().getPermission("bending.darkspirit") == null) {
            BendingGUI.log.info("Injecting custom spirit permissions");
            Permission base = Bukkit.getPluginManager().getPermission("bending.player");

            Permission light = defineDefaultPerm("bending.lightspirit", "Allows the player to become a light spirit");
            Permission dark = defineDefaultPerm("bending.darkspirit", "Allows the player to become a dark spirit");
            Permission neutral = defineDefaultPerm("bending.neutralspirit", "Allows the player to become a spirit");

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
                        if (coreAbility.getElement() == SPIRIT) perm.addParent(neutralPassive, true);
                        else if (coreAbility.getElement() == LIGHT_SPIRIT) perm.addParent(lightPassive, true);
                        else if (coreAbility.getElement() == DARK_SPIRIT) perm.addParent(darkPassive, true);
                    } else if (coreAbility instanceof ComboAbility) {
                        if (coreAbility.getElement() == SPIRIT) perm.addParent(neutralCombo, true);
                        else if (coreAbility.getElement() == LIGHT_SPIRIT) perm.addParent(lightCombo, true);
                        else if (coreAbility.getElement() == DARK_SPIRIT) perm.addParent(darkCombo, true);
                    } else {
                        if (coreAbility.getElement() == SPIRIT) perm.addParent(neutral, true);
                        else if (coreAbility.getElement() == LIGHT_SPIRIT) perm.addParent(light, true);
                        else if (coreAbility.getElement() == DARK_SPIRIT) perm.addParent(dark, true);
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

    private static boolean checkSpiritClasspath() {
        try {
            Class.forName("me.xnuminousx.spirits.elements.SpiritElement");
            SPIRIT = SpiritElement.SPIRIT;
            LIGHT_SPIRIT = SpiritElement.LIGHT_SPIRIT;
            DARK_SPIRIT = SpiritElement.DARK_SPIRIT;
            return true;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("me.numin.spirits.utilities.SpiritElement");
                SPIRIT = me.numin.spirits.utilities.SpiritElement.SPIRIT;
                LIGHT_SPIRIT = me.numin.spirits.utilities.SpiritElement.LIGHT_SPIRIT;
                DARK_SPIRIT = me.numin.spirits.utilities.SpiritElement.DARK_SPIRIT;
            } catch (ClassNotFoundException e1) {
                try {
                    Class.forName("me.numin.spirits.SpiritElement");
                    BendingGUI.log.severe("Wrong Spirits version! You are using the one from GitHub, which has a different classpath!");
                    BendingGUI.log.severe("You MUST be using Spirits BETA-1.0.13! 1.0.14 or higher WILL NOT WORK!");
                    e1.printStackTrace();
                } catch (ClassNotFoundException ignored) {}
            }

        }
        return false;
    }

}
