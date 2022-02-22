package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.schemas.DynamicTableVisibleColumn;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.rows.DynamicTable1Row;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        dynamicTable1.visibleColumns(visibleColumns());
        dynamicTable1.showZeros(true);
        dynamicTable1.showPercentages(true);
    }

    private List<DynamicTableVisibleColumn> visibleColumns() {
        return Arrays.asList(
            new DynamicTableVisibleColumn().name("clientes (%)").visible(true),
            new DynamicTableVisibleColumn().name("adeudos").visible(false),
            new DynamicTableVisibleColumn().name("kwh").visible(true),
            new DynamicTableVisibleColumn().name("importe").visible(true),
            new DynamicTableVisibleColumn().name("iva").visible(true),
            new DynamicTableVisibleColumn().name("dap").visible(true),
            new DynamicTableVisibleColumn().name("clientes").visible(false),
            new DynamicTableVisibleColumn().name("adeudos").visible(true),
            new DynamicTableVisibleColumn().name("importe").visible(true),
            new DynamicTableVisibleColumn().name("clientes").visible(true),
            new DynamicTableVisibleColumn().name("adeudos").visible(true),
            new DynamicTableVisibleColumn().name("importe").visible(true)
        );
    }

}