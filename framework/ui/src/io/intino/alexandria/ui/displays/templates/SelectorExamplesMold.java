package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.items.Selector8ListMold;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.documentation.model.Datasources;

import java.util.List;
import java.util.stream.Collectors;

public class SelectorExamplesMold extends AbstractSelectorExamplesMold<UiFrameworkBox> {

    public SelectorExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        selector1.onSelect(e -> notify(e.selection()));
        selector2.onSelect(e -> notify(e.selection()));
        selector3.onSelect(e -> notify(e.selection()));
        selector4.onSelect(e -> notify(e.selection()));
        selector5.select("option 3");
        selector5.onSelect(e -> notify(e.selection()));
        selector6.onSelect(e -> notify(e.selection()));
        selector7.onSelect(e -> notify(e.selection()));
        selector8.selector8List.source(Datasources.personDatasource());
        selector8.selector8List.onAddItem(this::onAddItem);
        selector8.onSelect(e -> notifyPerson(e.selection()));
        selector8.valueProvider(person -> ((Person)person).firstName());
    }

    private void notify(List<String> selection) {
        if (selection.size() <= 0) {
            selector1.notifyUser("no option selected", UserMessage.Type.Info);
            return;
        }
        selector1.notifyUser("Selection: " + String.join(", ", selection), UserMessage.Type.Info);
    }

    private void notifyPerson(List<Person> selection) {
        if (selection.size() <= 0) {
            selector1.notifyUser("no option selected", UserMessage.Type.Info);
            return;
        }
        selector1.notifyUser("Selection: " + selection.stream().map(Person::firstName).collect(Collectors.joining(", ")), UserMessage.Type.Info);
    }

    private void onAddItem(AddItemEvent event) {
        Selector8ListMold component = event.component();
        Person person = event.item();
        component.firstName.value(person.firstName());
    }

}