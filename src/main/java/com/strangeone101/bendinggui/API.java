package com.strangeone101.bendinggui;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.api.ChooseSupport;
import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import com.strangeone101.bendinggui.menus.MenuEditElements;
import com.strangeone101.bendinggui.menus.MenuPlayers;
import com.strangeone101.bendinggui.menus.MenuSelectElement;
import com.strangeone101.bendinggui.menus.MenuSelectPresets;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;

import java.util.function.Function;


public class API {

    /**
     * Register a custom element to be supported by BendingGUI.
     * If you want it to appear on choose/edit/add element menus,
     * make the class also implement {@link ChooseSupport}
     * @param elementSupport The element support to add
     */
    public static void registerElementSupport(ElementSupport elementSupport) {
        BendingGUI.SUPPORTED_ELEMENTS.remove(elementSupport.getElement());

        BendingGUI.SUPPORTED_ELEMENTS.put(elementSupport.getElement(), elementSupport);

        //Don't sort if it's just us adding the spirit elements as we will sort properly in the next tick
        if (!SpiritsSupport.isSpiritElement(elementSupport.getElement())) {
            ElementOrder.sortOrder();
        }
    }

    /**
     * Change the element order by setting where in the order the provided
     * element should be
     * @param element The element to custom order
     * @param placement The placement of the element
     */
    public static void customOrderElement(Element element, int placement) {
        ElementOrder.setCustomElementOrder(element, placement);
        ElementOrder.sortOrder();
    }

    /**
     * Adds a custom menu item to the provided menu.
     * @param clazz The class of the menu to add the menu item to
     * @param itemFunction The function that gets the menu item. Has access to the current menu
     * @param index The index for the item to be on. Use negatives to go in reverse order (-1 is the last slot)
     */
    public static <T extends MenuBase> void addCustomMenuIcon(Class<T> clazz, Function<T, MenuItem> itemFunction, int index) {
        if (clazz.equals(MenuBendingOptions.class)) MenuBendingOptions.CUSTOM_ICONS.put(index, (Function<MenuBendingOptions, MenuItem>) itemFunction);
        else if (clazz.equals(MenuEditElements.class)) MenuEditElements.CUSTOM_ICONS.put(index, (Function<MenuEditElements, MenuItem>) itemFunction);
        else if (clazz.equals(MenuSelectElement.class)) MenuSelectElement.CUSTOM_ICONS.put(index, (Function<MenuSelectElement, MenuItem>) itemFunction);
        else if (clazz.equals(MenuPlayers.class)) MenuPlayers.CUSTOM_ICONS.put(index, (Function<MenuPlayers, MenuItem>) itemFunction);
        else if (clazz.equals(MenuSelectPresets.class)) MenuSelectPresets.CUSTOM_ICONS.put(index, (Function<MenuSelectPresets, MenuItem>) itemFunction);
        else throw new UnsupportedOperationException("Cannot add menu item to class " + clazz.toString());
    }
}
