package io.intino.konos.alexandria.ui.model.view.set.item;

import io.intino.konos.alexandria.ui.model.*;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.view.set.AbstractItem;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.List;

import static java.util.Collections.emptyList;

public class Items extends AbstractItem {
    private Loader<String> label;
    private Loader<String> icon;
    private Loader<Integer> bubble;
    private Source source = null;
    private View view;

    public String label(Element element, Object object) {
        return label != null ? label.value(element, object) : element.label();
    }

    public Items label(Loader<String> loader) {
        this.label = loader;
        return this;
    }

    public String icon(Element element, Object object) {
        return icon != null ? icon.value(element, object) : null;
    }

    public Items icon(Loader<String> loader) {
        this.icon = loader;
        return this;
    }

    public int bubble(Element element, Object object) {
        return bubble != null ? bubble.value(element, object) : -1;
    }

    public Items bubble(Loader<Integer> loader) {
        this.bubble = loader;
        return this;
    }

    public ItemList items(UISession session) {
        return new ItemList(source != null ? source.items(session) : emptyList());
    }

    public Items source(Source source) {
        this.source = source;
        return this;
    }

    public View view() {
        return view;
    }

    public Items view(ElementRender render) {
        this.view = view;
        return this;
    }

    public interface Loader<O> {
        O value(Element element, Object object);
    }

    public interface Source {
        List<Item> items(UISession session);
    }
}
