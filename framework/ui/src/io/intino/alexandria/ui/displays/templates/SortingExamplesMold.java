package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.items.Sorting1Mold;
import io.intino.alexandria.ui.displays.items.Sorting2Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class SortingExamplesMold extends AbstractSortingExamplesMold<AlexandriaUiBox> {

    public SortingExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
        list2.source(Datasources.personDatasource());
		list2.onAddItem(this::onAddItem);
		selector.onSelect(event -> list2.sortings(event.selection()));
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof Sorting1Mold) {
            Sorting1Mold component = event.component();
            Person person = event.item();
            component.firstName1.value(person.firstName());
            component.gender1.value(person.gender().name());
            component.age1.value(String.valueOf(person.age()));
        }
		else if (event.component() instanceof Sorting2Mold) {
			Sorting2Mold component = event.component();
			Person person = event.item();
			component.firstName2.value(person.firstName());
			component.gender2.value(person.gender().name());
			component.age2.value(String.valueOf(person.age()));
		}
    }

}