package io.intino.alexandria.ui.services.push;

public class UICookie {
    private String name;
    private String value;
    private int maxAge;

    public UICookie(String name, String value, int maxAge) {
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public int maxAge() {
        return maxAge;
    }
}
