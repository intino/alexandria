package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.exceptions.*;
import io.intino.alexandria.*;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaCatalogNotifier;
import io.intino.alexandria.ui.displays.AlexandriaComponent;


public class AlexandriaCatalog extends AlexandriaComponent<AlexandriaCatalogNotifier> {
    private UiFrameworkBox box;

    public AlexandriaCatalog(UiFrameworkBox box) {
        super();
        this.box = box;
    }



}