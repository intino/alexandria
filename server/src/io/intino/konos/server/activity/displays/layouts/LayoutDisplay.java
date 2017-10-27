package io.intino.konos.server.activity.displays.layouts;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.catalogs.model.TemporalCatalog;
import io.intino.konos.server.activity.displays.elements.ElementStoreDisplay;
import io.intino.konos.server.activity.displays.elements.builders.ReferenceBuilder;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.ElementRender;
import io.intino.konos.server.activity.displays.elements.model.ItemList;
import io.intino.konos.server.activity.displays.elements.model.renders.*;
import io.intino.konos.server.activity.displays.layouts.builders.PlatformInfoBuilder;
import io.intino.konos.server.activity.displays.layouts.model.ElementOption;
import io.intino.konos.server.activity.displays.layouts.model.options.Group;
import io.intino.konos.server.activity.displays.layouts.model.options.Option;
import io.intino.konos.server.activity.displays.layouts.model.options.Options;
import io.intino.konos.server.activity.displays.panels.model.Panel;
import io.intino.konos.server.activity.displays.schemas.PlatformInfo;
import io.intino.konos.server.activity.displays.schemas.Reference;
import io.intino.konos.server.activity.displays.schemas.ReferenceProperty;
import io.intino.konos.server.activity.displays.schemas.UserInfo;
import io.intino.konos.server.activity.services.push.User;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public abstract class LayoutDisplay<DN extends LayoutDisplayNotifier> extends ElementStoreDisplay<DN> {
    private List<Item> items = new ArrayList<>();
    private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
    private List<Consumer<Boolean>> loadedListeners = new ArrayList<>();
    protected Map<Class<? extends ElementRender>, Function<ElementRender, List<Item>>> itemsProviders = new HashMap<>();

    public LayoutDisplay(Box box) {
        super(box);
        buildElementProviders();
    }

    public void onLoading(Consumer<Boolean> listener) {
        loadingListeners.add(listener);
    }

    public void onLoaded(Consumer<Boolean> listener) {
        loadedListeners.add(listener);
    }

    @Override
    protected void refreshLoading(boolean withMessage) {
        sendLoading();
        loadingListeners.forEach(l -> l.accept(withMessage));
    }

    @Override
    protected void refreshLoaded() {
        sendLoaded();
        loadedListeners.forEach(l -> l.accept(true));
    }

    public void logout() {
        session().logout();
        notifyLoggedOut();
    }

    @Override
    protected void init() {
        super.init();
        sendInfo(info());
        user().ifPresent(user -> sendUser(userOf(user)));
        sendItems();
        notifyLoaded();
    }

    @Override
    protected Element elementWithLabel(String label) {
        Item item = itemWithLabel(label);
        return item != null ? item.element() : null;
    }

    @Override
    protected io.intino.konos.server.activity.displays.elements.model.Item targetWithLabel(String label) {
        Item item = itemWithLabel(label);
        return item != null ? item.target() : null;
    }

    protected Item itemWithLabel(String label) {
        return items.stream().filter(i -> i.label().equals(label)).findFirst().orElse(null);
    }

    public void selectItem(String label) {
        openElement(label);
    }

    @Override
    protected void refreshSelected(String label) {
        notifier.refreshSelected(label);
    }

    protected void sendLoading() {
        notifier.loading();
    }

    protected void sendLoaded() {
        notifier.loaded();
    }

    protected void sendUser(UserInfo userInfo) {
        notifier.user(userInfo);
    }

    protected void sendInfo(PlatformInfo info) {
        notifier.info(info);
    }

    protected void sendItems(List<Item> itemList) {
        notifier.refreshItemList(itemList.stream().map(this::schemaItemOf).collect(toList()));
    }

    protected void notifyLoggedOut() {
        notifier.userLoggedOut(session().browser().homeUrl());
    }

    protected Reference schemaItemOf(Item item) {
        Reference result = ReferenceBuilder.build(item.name(), item.label());
        List<ReferenceProperty> itemProperties = result.referencePropertyList();
        itemProperties.add(new ReferenceProperty().name("type").value(typeOf(item.element())));
        itemProperties.add(new ReferenceProperty().name("group").value(item.group() != null ? item.group().label() : null));
        itemProperties.add(new ReferenceProperty().name("group_opened").value(item.group() != null ? String.valueOf(item.group().mode() == Group.Mode.Expanded) : null));
        return result;
    }

    private List<Item> itemOf(ElementOption option) {
        ElementRender render = renderOf(option);
        if (render == null) return emptyList();
        return itemsProviders.get(render.getClass()).apply(render);
    }

    private ElementRender renderOf(ElementOption option) {
        if (option instanceof Option) return ((Option)option).render();
        else if (option instanceof Options) return ((Options)option).render();
        return null;
    }

    private String labelOf(ElementRender render, Element element, io.intino.konos.server.activity.displays.elements.model.Item item) {
        ElementOption owner = render.option();

        if (owner instanceof Option) return ((Option)owner).label();
        else if (owner instanceof Group) return ((Group)owner).label();
        else if (owner instanceof Options) return ((Options)owner).label(element, item.object());

        return "no label";
    }

    private List<Item> items() {
        return optionList().map(this::itemOf).flatMap(Collection::stream).collect(toList());
    }

    private Stream<ElementOption> optionList() {
        return element().options().stream().map(v -> {
            if (v instanceof Group) return ((Group)v).options();
            return singletonList(v);
        }).flatMap(Collection::stream);
    }

    private static String typeOf(Element element) {
        if (element == null) return "";
        if (element instanceof Panel) return "panel";
        if (element instanceof TemporalCatalog && ((TemporalCatalog)element).type() == TemporalCatalog.Type.Range) return "temporal-range-catalog";
        if (element instanceof TemporalCatalog && ((TemporalCatalog)element).type() == TemporalCatalog.Type.Time) return "temporal-time-catalog";
        if (element instanceof Catalog) return "catalog";
        return "";
    }

    private void sendItems() {
        this.items = items();
        sendItems(items);
    }

    private List<Item> panelItem(ElementRender r) {
        Panel panel = ((RenderPanel)r).panel();
        return singletonList(itemOf(r, panel));
    }

    private List<Item> panelItems(ElementRender r) {
        List<Panel> panelList = ((RenderPanels)r).source();
        return panelList.stream().map(c -> itemOf(r, c)).collect(toList());
    }

    private List<Item> objectItem(ElementRender r) {
        RenderObject render = (RenderObject)r;
        return singletonList(itemOf(r, render.panel(), render.item()));
    }

    private List<Item> objectItems(ElementRender r) {
        RenderObjects render = (RenderObjects)r;
        ItemList itemList = render.source();
        return itemList.items().stream().map(record -> itemOf(r, render.panel(), record)).collect(toList());
    }

    private List<Item> catalogItem(ElementRender r) {
        Catalog catalog = ((RenderCatalog)r).catalog();
        return singletonList(itemOf(r, catalog));
    }

    private List<Item> catalogItems(ElementRender r) {
        RenderCatalogs render = (RenderCatalogs)r;
        return render.catalogs().stream().map(c -> itemOf(r, c)).collect(toList());
    }

    private List<Item> moldItems(ElementRender r) {
        RenderMold render = (RenderMold)r;
        return singletonList(itemOf(r, render.mold()));
    }

    private PlatformInfo info() {
        return PlatformInfoBuilder.build(element().settings());
    }

    private UserInfo userOf(User user) {
        return new UserInfo().fullName(user.fullName()).photo(user.photo().toString());
    }

    private void notifyLoaded() {
        loadedListeners.forEach(l -> l.accept(true));
    }

    private void buildElementProviders() {
        itemsProviders.put(RenderPanel.class, this::panelItem);
        itemsProviders.put(RenderPanels.class, this::panelItems);
        itemsProviders.put(RenderObject.class, this::objectItem);
        itemsProviders.put(RenderObjects.class, this::objectItems);
        itemsProviders.put(RenderCatalog.class, this::catalogItem);
        itemsProviders.put(RenderCatalogs.class, this::catalogItems);
        itemsProviders.put(RenderMold.class, this::moldItems);
    }

    private Item itemOf(ElementRender render, Element element) {
        return itemOf(render, element, null);
    }

    private Item itemOf(ElementRender render, Element element, io.intino.konos.server.activity.displays.elements.model.Item target) {
        return new Item() {
            @Override
            public String name() {
                return target != null ? target.name() : element.name();
            }

            @Override
            public String label() {
                return labelOf(render, element, target);
            }

            @Override
            public Group group() {
                return (Group)render.option();
            }

            @Override
            public Element element() {
                return element;
            }

            @Override
            public io.intino.konos.server.activity.displays.elements.model.Item target() {
                return target;
            }
        };
    }

    protected interface Item {
        String name();
        String label();
        Group group();
        Element element();
        io.intino.konos.server.activity.displays.elements.model.Item target();
    }

}