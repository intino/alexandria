package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class Panels<B extends Box> extends AbstractPanels<B> {
    private Panel selected;

    public Panels(B box) {
        super(box);
    }

    public void linkTo(Menu menu) {
        menu.onSelect(event -> select(event.option()));
    }

    public void select(String name) {
        Panel panel = panel(name);
        if (panel == null) return;
        if (selected != null && selected.name().equals(panel.name())) return;
        this.selected = panel;
        notifier.refreshSelected(name);
    }

    private Panel panel(String name) {
        return this.children(Panel.class).stream().filter(p -> p.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}