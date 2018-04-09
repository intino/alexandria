package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.AbstractView;
import io.intino.konos.alexandria.activity.model.Element;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Toolbar;
import io.intino.konos.alexandria.activity.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.activity.model.Mold;

import java.util.List;

public interface ElementView<E extends Element> {
    String name();
    String label();
    String type();
    <V extends AbstractView> V raw();
    boolean embeddedElement();
    Toolbar toolbar();
    int width();
    Mold mold();
    OnClickRecord onClickRecordEvent();
    boolean canCreateClusters();
    boolean canSearch();
    boolean selectionEnabledByDefault();
    List<String> clusters();
    Item target();
    E element();
    String emptyMessage();
}
