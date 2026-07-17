package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.SearchBox1Mold;
import io.intino.alexandria.ui.displays.items.SearchBox2Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class SearchBoxExamplesMold extends AbstractSearchBoxExamplesMold<AlexandriaUiBox> {

    public SearchBoxExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
        list2.source(Datasources.personDatasource());
        list2.onAddItem(this::onAddItem2);
    }

    @Override
    public void refresh() {
        super.refresh();
        list1.reload();
    }

    private void onAddItem(AddCollectionItemEvent event) {
        if (event.component() instanceof SearchBox1Mold) {
            SearchBox1Mold component = event.component();
            Person person = event.item();
            component.firstName1.value(person.firstName());
            component.gender1.value(person.gender().name());
            component.age1.value(String.valueOf(person.age()));
        }
    }

    private void onAddItem2(AddCollectionItemEvent event) {
        if (event.component() instanceof SearchBox2Mold) {
            SearchBox2Mold component = event.component();
            Person person = event.item();
            component.firstName2.value(person.firstName());
            component.gender2.value(person.gender().name());
            component.age2.value(String.valueOf(person.age()));
        }
    }

}