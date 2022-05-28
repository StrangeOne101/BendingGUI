package com.strangeone101.bendinggui.api;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;
import me.xnuminousx.spirits.elements.SpiritElement;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ElementOrder {

    public static final int AIR = 100;
    public static final int SPIRITUAL_PROJECTION = 110;
    public static final int FLIGHT = 120;

    public static final int FIRE = 200;
    public static final int LIGHTNING = 210;
    public static final int COMBUSTION = 220;
    public static final int BLUE_FIRE = 230;

    public static final int EARTH = 300;
    public static final int SAND = 310;
    public static final int METAL = 320;
    public static final int LAVA = 330;

    public static final int WATER = 400;
    public static final int ICE = 410;
    public static final int HEALING = 420;
    public static final int PLANT = 430;
    public static final int BLOOD = 440;

    public static final int CHI = 500;

    public static final int SPIRIT = 600;
    public static final int SPIRIT_LIGHT = 650;
    public static final int SPIRIT_DARK = 700;

    public static final int AVATAR = 1000;

    private static LinkedHashMap<Element, Integer> ORDER = new LinkedHashMap<>();

    private static List<Element> PARENT_ELEMENTS = new ArrayList<>();
    private static List<Element.SubElement> SUBELEMENTS = new ArrayList<>();

    public static void sortOrder() {
        ORDER.clear();

        ORDER.put(Element.AIR, AIR);
        ORDER.put(Element.SPIRITUAL, SPIRITUAL_PROJECTION);
        ORDER.put(Element.FLIGHT, FLIGHT);
        ORDER.put(Element.FIRE, FIRE);
        ORDER.put(Element.LIGHTNING, LIGHTNING);
        ORDER.put(Element.COMBUSTION, COMBUSTION);
        ORDER.put(Element.BLUE_FIRE, BLUE_FIRE);
        ORDER.put(Element.EARTH, EARTH);
        ORDER.put(Element.SAND, SAND);
        ORDER.put(Element.METAL, METAL);
        ORDER.put(Element.LAVA, LAVA);
        ORDER.put(Element.WATER, WATER);
        ORDER.put(Element.ICE, ICE);
        ORDER.put(Element.HEALING, HEALING);
        ORDER.put(Element.PLANT, PLANT);
        ORDER.put(Element.BLOOD, BLOOD);
        ORDER.put(Element.CHI, CHI);
        ORDER.put(Element.AVATAR, AVATAR);

        //Add all custom subs for the main elements
        for (Element mainElement : new Element[] {Element.AIR, Element.EARTH, Element.FIRE, Element.WATER, Element.CHI}) {
            for (Element.SubElement airSub : Element.getAddonSubElements(mainElement)) {
                ORDER.put(airSub, behind(mainElement, airSub));
            }
        }

        List<Element> customElements = new ArrayList<>(Arrays.asList(Element.getAddonElements()));
        Element lastBehind = Element.CHI;

        //Add Spirit Elements
        if (SpiritsSupport.isEnabled()) {
            ORDER.put(SpiritElement.SPIRIT, SPIRIT);
            ORDER.put(SpiritElement.LIGHT_SPIRIT, SPIRIT_LIGHT);
            ORDER.put(SpiritElement.DARK_SPIRIT, SPIRIT_DARK);

            customElements.remove(SpiritElement.SPIRIT);
            customElements.remove(SpiritElement.LIGHT_SPIRIT);
            customElements.remove(SpiritElement.DARK_SPIRIT);

            lastBehind = SpiritElement.DARK_SPIRIT;
        }


        for (Element customElement : customElements) {
            ORDER.put(customElement, behind(lastBehind, customElement));

            for (Element.SubElement customElementSub : Element.getSubElements(customElement)) {
                ORDER.put(customElementSub, behind(lastBehind, customElement));
            }
        }

        ORDER = sortByValue(ORDER);

        PARENT_ELEMENTS = ORDER.keySet().stream().filter(e -> !(e instanceof Element.SubElement)).collect(Collectors.toUnmodifiableList());
        SUBELEMENTS = ORDER.keySet().stream().filter(e -> (e instanceof Element.SubElement)).map(e -> (Element.SubElement)e).collect(Collectors.toUnmodifiableList());
    }

    public static int between(int a, int b) {
        return (a + b) / 2;
    }

    public static int behind(Element behind, Element newElement) {
        if (!(behind instanceof Element.SubElement)) {
            if (newElement instanceof Element.SubElement) {
                int max = 0;

                for (Element.SubElement sub : Element.getSubElements(behind)) {
                    int temp = ORDER.get(sub);
                    if (temp > max) max = temp;
                }

                return max + 10;
            }

            int index = 0;

            for (Element e : ORDER.keySet()) {
                if (e == behind) {
                    index = -1;
                } else if (index == -1) {
                    return between(ORDER.get(behind), ORDER.get(e));
                }

                index++;
            }

            return between(ORDER.get(behind), ORDER.get(behind) + 500);
        }

        int index = 0;
        for (Element e : ORDER.keySet()) {
            if (e == behind) {
                index = -1;
            } else if (index == -1) {
                if (e instanceof Element.SubElement)
                    return between(ORDER.get(behind), ORDER.get(e));
                return ORDER.get(behind) + 10;
            }

            index++;
        }
        return between(ORDER.get(behind), ORDER.get(behind) + 500);
    }

    private static LinkedHashMap<Element, Integer> sortByValue(Map<Element, Integer> unsortMap) {
        List<Map.Entry<Element, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort(Map.Entry.comparingByValue());
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    /**
     * Get all elements on the server, sorted by the element order
     * @return The sorted elements
     */
    public static Set<Element> getOrder() {
        return ORDER.keySet();
    }

    /**
     * Get parent elements on the server, sorted by element order
     * @return The sorted parent elements
     */
    public static List<Element> getParentElements() {
        return PARENT_ELEMENTS;
    }

    /**
     * Get subelements on the server, sorted by element order
     * @return The sorted subelements
     */
    public static List<Element.SubElement> getSubelements() {
        return SUBELEMENTS;
    }
}
