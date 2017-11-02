package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.framework.box.model.AbstractView;
import io.intino.konos.alexandria.framework.box.model.Element;
import io.intino.konos.alexandria.framework.box.model.Item;
import io.intino.konos.alexandria.framework.box.model.Toolbar;
import io.intino.konos.alexandria.framework.box.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.framework.box.model.Mold;

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
