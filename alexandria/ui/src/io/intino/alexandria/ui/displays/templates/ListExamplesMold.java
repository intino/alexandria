package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.items.List1Mold;
import io.intino.alexandria.ui.displays.items.List2Mold;
import io.intino.alexandria.ui.displays.items.List3Mold;
import io.intino.alexandria.ui.displays.items.List4Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.Datasource;

public class ListExamplesMold extends AbstractListExamplesMold<UiFrameworkBox> {

    public ListExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        init(list1, Datasources.itemDatasource());
        init(list2, Datasources.itemDatasource());
        init(list3, Datasources.personDatasource());
        init(list4, Datasources.personDatasource());
    }

    private void init(io.intino.alexandria.ui.displays.components.List list, Datasource datasource) {
        list.source(datasource);
        list.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof List1Mold) ((List1Mold)event.component()).stamp.update(event.item());
        else if (event.component() instanceof List2Mold) ((List2Mold)event.component()).stamp.update(event.item());
        else if (event.component() instanceof List3Mold) ((List3Mold) event.component()).firstName.update(((Person) event.item()).firstName());
        else if (event.component() instanceof List4Mold) ((List4Mold) event.component()).firstName.update(((Person) event.item()).firstName());
    }

}