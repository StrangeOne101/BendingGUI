package com.strangeone101.bendinggui.api;

import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;
import me.numin.spirits.SpiritElement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

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

    static {


    }

    private void sortOrder() {
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

        List<Element> customElements = Arrays.asList(Element.getAddonElements());
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
                ORDER.put(customElement, behind(lastBehind, customElement));
            }
        }
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
}
