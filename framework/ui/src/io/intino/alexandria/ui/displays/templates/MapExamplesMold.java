package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class MapExamplesMold extends AbstractMapExamplesMold<UiFrameworkBox> {

    public MapExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        map1.source(Datasources.locatedPersonDatasource());
        map2.source(Datasources.locatedPersonDatasource());
    }

}