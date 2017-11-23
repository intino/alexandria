package io.intino.konos.alexandria.activity.model.layout.options;

import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.layout.ElementOption;

public class Options extends ElementOption {
    private Loader<String> label;
    private Loader<String> icon;
    private Loader<Integer> bubble;
    private ElementRender render;

    public String label(Element element, Object object) {
        return label != null ? label.value(element, object) : element.label();
    }

    public Options label(Loader<String> loader) {
        this.label = loader;
        return this;
    }

    public String icon(Element element, Object object) {
        return icon != null ? icon.value(element, object) : null;
    }

    public Options icon(Loader<String> loader) {
        this.icon = loader;
        return this;
    }

    public int bubble(Element element, Object object) {
        return bubble != null ? bubble.value(element, object) : -1;
    }

    public Options bubble(Loader<Integer> loader) {
        this.bubble = loader;
        return this;
    }

    public ElementRender render() {
        return render;
    }

    public Options render(ElementRender render) {
        this.render = render;
        this.render.option(this);
        return this;
    }

    public interface Loader<O> {
        O value(Element element, Object object);
    }

}
