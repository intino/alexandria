package io.intino.konos.server.activity.displays.elements;

import io.intino.konos.server.activity.displays.catalogs.model.events.OnClickRecord;
import io.intino.konos.server.activity.displays.elements.model.AbstractView;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.Toolbar;
import io.intino.konos.server.activity.displays.molds.model.Mold;

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
    List<String> clusters();
    Item target();
    E element();
    String emptyMessage();
}
