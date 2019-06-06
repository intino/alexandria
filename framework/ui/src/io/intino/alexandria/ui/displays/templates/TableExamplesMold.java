package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.rows.Table1Row;
import io.intino.alexandria.ui.displays.rows.Table2Row;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

public class TableExamplesMold extends AbstractTableExamplesMold<AlexandriaUiBox> {

    public TableExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        init(table1, Datasources.personDatasource());
        init(table2, Datasources.personDatasource());
    }

    private void init(io.intino.alexandria.ui.displays.components.Table table, PageDatasource datasource) {
        table.source(datasource);
        table.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddItemEvent event) {
        Person person = event.item();

        if (event.component() instanceof Table1Row) {
            Table1Row component = event.component();
            component.table11Mold.firstName.update(person.firstName());
            component.table12Mold.lastName.update(person.lastName());
        }
        else if (event.component() instanceof Table2Row) {
            Table2Row component = event.component();
            component.table21Mold.firstName.update(person.firstName());
            component.table22Mold.lastName.update(person.lastName());
        }
    }

}