package io.intino.konos.alexandria.activity.utils;

public class NumberUtil {
    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
