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
            component.table11Mold.firstName.value(person.firstName());
            component.table11Mold.firstName.onChange(e -> logInfo(person, e.value()));
            component.table12Mold.lastName.value(person.lastName());
            component.table12Mold.lastName.onChange(e -> logInfo(person, e.value()));
        }
        else if (event.component() instanceof Table2Row) {
            Table2Row component = event.component();
            component.table21Mold.firstName.value(person.firstName());
            component.table22Mold.lastName.value(person.lastName());
        }
    }

    private void logInfo(Person person, String info) {
        System.out.println(String.format("ha cambiado el valor del campo %s a %s", person.firstName(), info));
    }

}