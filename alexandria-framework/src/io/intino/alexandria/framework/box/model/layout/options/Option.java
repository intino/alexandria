package io.intino.alexandria.framework.box.model.layout.options;

import io.intino.alexandria.framework.box.model.ElementRender;
import io.intino.alexandria.framework.box.model.layout.ElementOption;

public class Option extends ElementOption {
    private String label;
    private String icon = null;
    private int bubble = -1;
    private ElementRender render;

    public String label() {
        return label;
    }

    public Option label(String label) {
        this.label = label;
        return this;
    }

    public String icon() {
        return icon;
    }

    public Option icon(String icon) {
        this.icon = icon;
        return this;
    }

    public int bubble() {
        return bubble;
    }

    public Option bubble(int bubble) {
        this.bubble = bubble;
        return this;
    }

    public ElementRender render() {
        return render;
    }

    public Option render(ElementRender render) {
        this.render = render;
        return this;
    }
}
