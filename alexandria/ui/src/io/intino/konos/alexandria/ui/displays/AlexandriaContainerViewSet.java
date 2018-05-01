package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaContainerViewSetNotifier;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.views.ContainerView;
import io.intino.konos.alexandria.ui.model.views.container.Container;
import io.intino.konos.alexandria.ui.model.views.container.SetContainer;
import io.intino.konos.alexandria.ui.model.views.set.AbstractItem;
import io.intino.konos.alexandria.ui.model.views.set.item.Group;
import io.intino.konos.alexandria.ui.model.views.set.item.Item;
import io.intino.konos.alexandria.ui.model.views.set.item.Items;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


public class AlexandriaContainerViewSet extends AlexandriaContainerView<AlexandriaContainerViewSetNotifier> {
    private Map<Class<? extends AbstractItem>, Function<AbstractItem, List<SetItem>>> setItemsProviders = new HashMap<>();
    private List<SetItem> items = new ArrayList<>();

    public AlexandriaContainerViewSet(Box box) {
        super(box);
        buildSetItemsProviders();
    }

    public void home() {
        if (items.size() <= 1) return;
        openElement(this.items.get(0).label(), id());
    }

    @Override
    protected void init() {
        super.init();
        sendItems();
    }

    public void openItem(String key) {
        openElement(itemWithKey(key).label());
    }

    public void refreshOpened(String key) {
        notifier.refreshOpened(itemWithKey(key).name());
    }

    public void sendItems(List<SetItem> items) {
        notifier.refreshItemList(items.stream().map(this::schemaItemOf).collect(toList()));
    }

    private SetItem itemWithKey(String key) {
        return items.stream().filter(i -> key.equals(i.name()) || key.equals(i.label())).findFirst().orElse(null);
    }

    private Reference schemaItemOf(SetItem item) {
        Reference result = ReferenceBuilder.build(item.name(), item.label());
        List<ReferenceProperty> referenceProperties = result.referencePropertyList();
        referenceProperties.add(new ReferenceProperty().name("type").value(item.type()));
        referenceProperties.add(new ReferenceProperty().name("group").value(item.group() != null ? item.group().label() : null));
        referenceProperties.add(new ReferenceProperty().name("group_opened").value(item.group() != null ? String.valueOf(item.group().mode() == Group.Mode.Expanded) : null));
        return result;
    }

    private List<SetItem> itemOf(AbstractItem item) {
        Container container = containerOf(item);
        if (container == null) return emptyList();
        return setItemsProviders.get(item.getClass()).apply(item);
    }

    private Container containerOf(AbstractItem item) {
        if (item instanceof io.intino.konos.alexandria.ui.model.views.set.item.Item) return ((io.intino.konos.alexandria.ui.model.views.set.item.Item) item).view().container();
        else if (item instanceof Items) return ((Items) item).view().container();
        return null;
    }

    private String labelOf(AbstractItem item, Element element, io.intino.konos.alexandria.ui.model.Item target) {

        if (item instanceof io.intino.konos.alexandria.ui.model.views.set.item.Item) return ((io.intino.konos.alexandria.ui.model.views.set.item.Item) item).label();
        else if (item instanceof Group) return ((Group) item).label();
        else if (item instanceof Items) return ((Items) item).label(element, target != null ? target.object() : null);

        return "no label";
    }

    private List<SetItem> items() {
        UISession session = session();
        return viewItems().filter(view ->!view.hidden(session)).map(this::itemOf).flatMap(Collection::stream).collect(toList());
    }

    private Stream<AbstractItem> viewItems() {
        ContainerView view = view();
        SetContainer container = view.container();
        return container.items().stream().map(v -> {
            if (v instanceof Group) return ((Group) v).items();
            return singletonList(v);
        }).flatMap(Collection::stream);
    }

    private void sendItems() {
        this.items = items();
        sendItems(items);
    }

    private void buildSetItemsProviders() {
        setItemsProviders.put(Group.class, this::setItemsFromGroup);
        setItemsProviders.put(Item.class, this::setItemsFromItem);
        setItemsProviders.put(Items.class, this::setItemsFromItems);
    }

    private List<SetItem> setItemsFromGroup(AbstractItem abstractItem) {
        return emptyList();
    }

    private List<SetItem> setItemsFromItem(AbstractItem abstractItem) {
        Item item = (Item) abstractItem;
        Container container = item.view().container();
        return singletonList(itemOf(abstractItem, container));
    }

    private List<SetItem> setItemsFromItems(AbstractItem abstractItem) {
        Items items = (Items) abstractItem;
        Container container = items.view().container();
        return items.items(session()).items().stream().map(i -> itemOf(abstractItem, container, i)).collect(toList());
    }

    private SetItem itemOf(AbstractItem item, Container container) {
        return itemOf(item, container, null);
    }

    private SetItem itemOf(AbstractItem item, Container container, io.intino.konos.alexandria.ui.model.Item target) {
        return new SetItem() {
            @Override
            public AbstractItem item() {
                return item;
            }

            @Override
            public String name() {
                return item.name();
            }

            @Override
            public String label() {
                return labelOf(item, element(), target);
            }

            @Override
            public String type() {
                return nameOf(container.displayTypeFor(element(), target));
            }

            @Override
            public Group group() {
                return item().owner();
            }

            @Override
            public Element element() {
                return container.element();
            }

            public io.intino.konos.alexandria.ui.model.Item target() {
                return target;
            }
        };
    }

    protected interface SetItem {
        String name();
        String label();
        String type();
        AbstractItem item();
        Group group();
        Element element();
        io.intino.konos.alexandria.ui.model.Item target();
    }

}