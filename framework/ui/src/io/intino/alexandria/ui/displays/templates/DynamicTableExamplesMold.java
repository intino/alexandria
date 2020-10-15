package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.rows.DynamicTable1Row;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class DynamicTableExamplesMold extends AbstractDynamicTableExamplesMold<UiFrameworkBox> {

    public DynamicTableExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        dynamicTable1.source(Datasources.dynamicTablePersonDatasource());
        dynamicTable1.onAddItem(e -> {
            Person person = e.item();
            DynamicTable1Row row = e.component();
            row.dynFirstNameItem.firstName.value(person.firstName());
            row.dynLastNameItem.lastName.value(person.lastName());
        });
        dynamicTable1.dimension("dimension1");
        dynamicTable1.drill("drill1");
        dynamicTable1.reload();
    }
}