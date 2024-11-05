package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.List1Mold;
import io.intino.alexandria.ui.displays.items.List3Mold;
import io.intino.alexandria.ui.displays.items.List4Mold;
import io.intino.alexandria.ui.displays.items.List5Mold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

public class ListExamplesMold extends AbstractListExamplesMold<AlexandriaUiBox> {

    public ListExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        init(list1, Datasources.itemDatasource());
        init(list3, Datasources.personDatasource());
        init(list4, Datasources.personDatasource());
        init(list5, Datasources.personDatasource());
        add.onExecute((event) -> add.notifyUser("add item", UserMessage.Type.Info));
        remove.onExecute((event) -> remove.notifyUser("remove " + event.selection().size() + " items"));
    }

    @Override
    public void refresh() {
        super.refresh();
        list1.reload();
        list3.reload();
        list4.reload();
        list5.reload();
    }

    private void init(io.intino.alexandria.ui.displays.components.List list, PageDatasource datasource) {
        list.source(datasource);
        list.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddCollectionItemEvent event) {
        if (event.component() instanceof List1Mold) ((List1Mold)event.component()).stamp.item(event.item());
        else if (event.component() instanceof List3Mold) ((List3Mold) event.component()).firstName.value(((Person) event.item()).firstName());
        else if (event.component() instanceof List4Mold) ((List4Mold) event.component()).firstName.value(((Person) event.item()).firstName());
        else if (event.component() instanceof List5Mold) ((List5Mold) event.component()).firstName.value(((Person) event.item()).firstName());
    }

}