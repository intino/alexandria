package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.Dialog4ListMold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

public class DialogExamplesMold extends AbstractDialogExamplesMold<UiFrameworkBox> {

    public DialogExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        initDialog1();
        initDialog2();
        initDialog4();
    }

    private void initDialog1() {
//        dialog1.size("1000px", "300px");
        dialog1.onBeforeOpen((event) -> {
            field1.value("");
            field2.value("");
            return true;
        });
        acceptButton.onExecute((event -> {
            dialog1.close();
            String value1 = field1.value() != null && !field1.value().isEmpty() ? field1.value() : "(no value)";
            String value2 = field2.value() != null && !field2.value().isEmpty() ? field2.value() : "(no value)";
            String value = "field1: " + value1 + ", field2: " + value2;
            DialogExamplesMold.this.notifyUser("Se ha seleccionado " + value, UserMessage.Type.Info);
        }));
    }

    private void initDialog2() {
        dialog2.onSelect((event -> {
            String option = (String) event.selection().get(0);
            DialogExamplesMold.this.notifyUser("Se ha seleccionado la opción " + option, UserMessage.Type.Info);
        }));
    }

    private void initDialog4() {
        dialog4.onSelect((event -> {
            Person person = (Person) event.selection().get(0);
            DialogExamplesMold.this.notifyUser("Se ha seleccionado " + person.firstName(), UserMessage.Type.Info);
        }));
        list1.source(Datasources.personDatasource());
        list1.onAddItem(this::onAddItem);
    }

    private void onAddItem(AddCollectionItemEvent event) {
        if (event.component() instanceof Dialog4ListMold) ((Dialog4ListMold) event.component()).firstName.value(((Person) event.item()).firstName());
    }
}