package io.intino.konos.alexandria.framework.box.model;

public class Desktop extends Element {
    private Layout layout;

    public enum Layout {
        Tab, Menu
    }

    public Layout layout() {
        return layout;
    }

    public Desktop layout(Layout layout) {
        this.layout = layout;
        return this;
    }

}
