package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.GroupingToolbar1Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class GroupingToolbarExamplesMold extends AbstractGroupingToolbarExamplesMold<UiFrameworkBox> {

    public GroupingToolbarExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
    }

    @Override
    public void refresh() {
        super.refresh();
        list1.reload();
    }

    private void onAddItem(AddCollectionItemEvent event) {
        GroupingToolbar1Mold component = event.component();
        Person person = event.item();
        component.firstName1.value(person.firstName());
        component.gender1.value(person.gender().name());
        component.age1.value(String.valueOf(person.age()));
    }
}