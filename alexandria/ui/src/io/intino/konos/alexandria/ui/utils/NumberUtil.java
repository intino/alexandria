package io.intino.konos.alexandria.ui.utils;

public class NumberUtil {
    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
