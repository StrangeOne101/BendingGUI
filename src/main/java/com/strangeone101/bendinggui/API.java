package com.strangeone101.bendinggui;

import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;

import java.util.ArrayList;
import java.util.List;

public class API {

    public static void registerElementSupport(ElementSupport elementSupport) {
        BendingGUI.SUPPORTED_ELEMENTS.remove(elementSupport.getElement());

        BendingGUI.SUPPORTED_ELEMENTS.put(elementSupport.getElement(), elementSupport);

        //Don't sort if it's just us adding the spirit elements as we will sort properly in the next tick
        if (!SpiritsSupport.isSpiritElement(elementSupport.getElement())) {
            ElementOrder.sortOrder();
        }

    }
}
