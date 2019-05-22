package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.items.SearchBox1Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class SearchBoxExamplesMold extends AbstractSearchBoxExamplesMold<UiFrameworkBox> {

    public SearchBoxExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof SearchBox1Mold) {
            SearchBox1Mold component = event.component();
            Person person = event.item();
            component.firstName1.update(person.firstName());
            component.gender1.update(person.gender().name());
            component.age1.update(String.valueOf(person.age()));
        }
    }

}