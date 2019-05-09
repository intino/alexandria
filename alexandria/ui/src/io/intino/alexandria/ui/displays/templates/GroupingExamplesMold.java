package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.items.Grouping1Mold;
import io.intino.alexandria.ui.displays.items.Grouping2Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class GroupingExamplesMold extends AbstractGroupingExamplesMold<UiFrameworkBox> {

    public GroupingExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
        list2.source(Datasources.personDatasource());
        list2.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof Grouping1Mold) {
            Grouping1Mold component = event.component();
            Person person = event.item();
            component.firstName1.update(person.firstName());
            component.gender1.update(person.gender().name());
            component.age1.update(String.valueOf(person.age()));
        }
        else if (event.component() instanceof Grouping2Mold) {
            Grouping2Mold component = event.component();
            Person person = event.item();
            component.firstName2.update(person.firstName());
            component.gender2.update(person.gender().name());
            component.age2.update(String.valueOf(person.age()));
        }
    }

}