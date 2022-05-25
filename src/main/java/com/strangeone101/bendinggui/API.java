package com.strangeone101.bendinggui;

import com.strangeone101.bendinggui.api.ElementSupport;

import java.util.ArrayList;
import java.util.List;

public class API {

    public static void registerElementSupport(ElementSupport elementSupport) {
        BendingGUI.SUPPORTED_ELEMENTS.remove(elementSupport.getElement());

        BendingGUI.SUPPORTED_ELEMENTS.put(elementSupport.getElement(), elementSupport);

    }
}
