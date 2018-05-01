package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Toolbar;
import io.intino.konos.alexandria.ui.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.ui.model.Mold;

import java.util.List;

public interface AlexandriaElementViewDefinition<E extends Element> {
    String name();
    String label();
    String type();
    <V extends View> V raw();
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
