package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.Map1Mold;
import io.intino.alexandria.ui.displays.items.Map2Mold;
import io.intino.alexandria.ui.displays.items.Map3Mold;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.datasource.MapDatasource;

public class MapExamplesMold extends AbstractMapExamplesMold<AlexandriaUiBox> {

    public MapExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        init(map1, Datasources.mapDatasource());
        init(map2, Datasources.clusterDatasource());
        init(map3, Datasources.heatDatasource());
    }

    @Override
    public void refresh() {
        super.refresh();
        map1.reload();
        map2.reload();
        map3.reload();
    }

    private void init(io.intino.alexandria.ui.displays.components.Map map, MapDatasource datasource) {
        map.source(datasource);
        map.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddCollectionItemEvent event) {
        if (event.component() instanceof Map1Mold) ((Map1Mold)event.component()).stamp.item(event.item());
        else if (event.component() instanceof Map2Mold) ((Map2Mold)event.component()).stamp.item(event.item());
        else if (event.component() instanceof Map3Mold) ((Map3Mold)event.component()).stamp.item(event.item());
    }

}