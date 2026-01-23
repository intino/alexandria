package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.items.Selector11ListMold;
import io.intino.alexandria.ui.displays.items.Selector8ListMold;
import io.intino.alexandria.ui.displays.items.Selector9ListMold;
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
        selector8.selector8List.onAddItem(this::onAddItemSelector8);
        selector8.onSelect(e -> notifyPerson(e.selection()));
//        selector8.onOpen(e -> notifyUser("Open " + e.value(), UserMessage.Type.Info));
        selector8.valueProvider(person -> ((Person)person).firstName());
//        selector8.readonly(true);
//        selector8.selection(Datasources.personDatasource().items(0, 10, null, Collections.emptyList(), Collections.emptyList()).subList(0, 2));
        selector9.selector9List.source(Datasources.personDatasource());
        selector9.selector9List.onAddItem(this::onAddItemSelector9);
        selector9.onSelect(e -> notifyPerson(e.selection()));
        selector9.valueProvider(person -> ((Person)person).firstName());
		selector10.onSelect(e -> notify(e.selection()));
//		selector10.readonly(true);
//		selector10.selection("option 1", "option 2");
		selector11.selector11List.source(Datasources.personDatasource());
		selector11.selector11List.onAddItem(this::onAddItemSelector11);
		selector11.onSelect(e -> notifyPerson(e.selection()));
		selector11.valueProvider(person -> ((Person)person).firstName());
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

    private void onAddItemSelector8(AddCollectionItemEvent event) {
        Selector8ListMold component = event.component();
        Person person = event.item();
        component.firstName.value(person.firstName());
    }

    private void onAddItemSelector9(AddCollectionItemEvent event) {
        Selector9ListMold component = event.component();
        Person person = event.item();
        component.firstName.value(person.firstName());
    }

	private void onAddItemSelector11(AddCollectionItemEvent event) {
		Selector11ListMold component = event.component();
		Person person = event.item();
		component.firstName.value(person.firstName());
	}

}