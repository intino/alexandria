package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.items.GroupBoxMold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class GroupBoxExamplesMold extends AbstractGroupBoxExamplesMold<UiFrameworkBox> {

    public GroupBoxExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddItemEvent event) {
        if (event.component() instanceof GroupBoxMold) ((GroupBoxMold) event.component()).firstName.update(((Person) event.item()).firstName());
    }
}