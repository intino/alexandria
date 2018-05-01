package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.AlexandriaUiBox;
import io.intino.konos.alexandria.ui.displays.builders.PlatformInfoBuilder;
import io.intino.konos.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaContainerViewSetNotifier;
import io.intino.konos.alexandria.ui.model.*;
import io.intino.konos.alexandria.ui.model.views.ContainerView;
import io.intino.konos.alexandria.ui.model.views.container.RenderObjects;
import io.intino.konos.alexandria.ui.model.views.set.AbstractItem;
import io.intino.konos.alexandria.ui.model.views.set.item.Group;
import io.intino.konos.alexandria.ui.schemas.PlatformInfo;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;
import io.intino.konos.alexandria.ui.schemas.UserInfo;
import io.intino.konos.alexandria.ui.services.push.UISession;
import io.intino.konos.alexandria.ui.services.push.User;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.intino.konos.alexandria.ui.utils.AvatarUtil.generateAvatar;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


public class AlexandriaContainerViewSet extends AlexandriaDisplay<AlexandriaContainerViewSetNotifier> {
    private AlexandriaUiBox box;


    public AlexandriaContainerViewSet(AlexandriaUiBox box) {
        super();
        this.box = box;
    }

    public AlexandriaContainerViewSet box(AlexandriaUiBox box) {
        this.box = box;
        return this;
    }






    private List<AlexandriaLayout.Item> items = new ArrayList<>();
    private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
    private List<Consumer<Boolean>> loadedListeners = new ArrayList<>();
    protected Map<Class<? extends ElementRender>, Function<ElementRender, List<AlexandriaLayout.Item>>> itemsProviders = new HashMap<>();
    private Settings settings;

    public static final String AvatarColor = "#3F51B5";

    public AlexandriaLayout(Box box) {
        super(box);
        buildElementProviders();
    }

    public AlexandriaLayout settings(Settings settings) {
        this.settings = settings;
        return this;
    }

    public void onLoading(Consumer<Boolean> listener) {
        loadingListeners.add(listener);
    }

    public void onLoaded(Consumer<Boolean> listener) {
        loadedListeners.add(listener);
    }

    public void home() {
        if (this.items.size() <= 1) return;
        openElement(this.items.get(0).label());
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

    public void refreshUser(User user) {
        sendUser(userInfoOf(user));
    }

    public void logout() {
        session().logout();
        notifyLoggedOut();
    }

    @Override
    protected void init() {
        super.init();
        sendInfo(info());
        User user = user();
        if (user != null) sendUser(userInfoOf(user));
        sendItems();
        openDefaultItem(routePath());
        notifyLoaded();
    }

    @Override
    protected Element elementWithKey(String key) {
        AlexandriaLayout.Item item = itemWithKey(key);
        return item != null ? item.element() : null;
    }

    @Override
    protected io.intino.konos.alexandria.ui.model.Item targetWithKey(String key) {
        AlexandriaLayout.Item item = itemWithKey(key);
        return item != null ? item.target() : null;
    }

    @Override
    protected AlexandriaElementDisplay newDisplay(Element element, io.intino.konos.alexandria.ui.model.Item item) {
        return element().displayFor(element, item);
    }

    protected AlexandriaLayout.Item itemWithKey(String key) {
        return items.stream().filter(i -> key.equals(i.name()) || key.equals(i.label())).findFirst().orElse(null);
    }

    public void openItem(String key) {
        openElement(itemWithKey(key).label());
    }

    protected Reference schemaItemOf(AlexandriaLayout.Item item) {
        Reference result = ReferenceBuilder.build(item.name(), item.label());
        List<ReferenceProperty> referenceProperties = result.referencePropertyList();
        referenceProperties.add(new ReferenceProperty().name("type").value(item.type()));
        referenceProperties.add(new ReferenceProperty().name("group").value(item.group() != null ? item.group().label() : null));
        referenceProperties.add(new ReferenceProperty().name("group_opened").value(item.group() != null ? String.valueOf(item.group().mode() == Group.Mode.Expanded) : null));
        return result;
    }

    protected abstract void sendLoading();
    protected abstract void sendLoaded();
    protected abstract void refreshOpened(String value);
    protected abstract void sendInfo(PlatformInfo value);
    protected abstract void sendItems(List<AlexandriaLayout.Item> value);
    protected abstract void sendUser(UserInfo value);
    protected abstract void openDefaultItem(String item);
    protected abstract void notifyLoggedOut();

    private List<AlexandriaLayout.Item> itemOf(AbstractItem option) {
        ElementRender render = renderOf(option);
        if (render == null) return emptyList();
        return itemsProviders.get(render.getClass()).apply(render);
    }

    private ElementRender renderOf(AbstractItem option) {
        if (option instanceof io.intino.konos.alexandria.ui.model.views.set.item.Item) return ((io.intino.konos.alexandria.ui.model.views.set.item.Item) option).render();
        else if (option instanceof Options) return ((Options) option).render();
        return null;
    }

    private String labelOf(ElementRender render, Element element, io.intino.konos.alexandria.ui.model.Item item) {
        AbstractItem owner = render.option();

        if (owner instanceof io.intino.konos.alexandria.ui.model.views.set.item.Item) return ((io.intino.konos.alexandria.ui.model.views.set.item.Item) owner).label();
        else if (owner instanceof Group) return ((Group) owner).label();
        else if (owner instanceof Options) return ((Options) owner).label(element, item != null ? item.object() : null);

        return "no label";
    }

    private List<AlexandriaLayout.Item> items() {
        UISession session = session();
        return views().stream().filter(view ->!view.hidden(session)).map(this::itemOf).flatMap(Collection::stream).collect(toList());
    }

    private Stream<AbstractItem> abstractItemList() {
        return element().items().stream().map(v -> {
            if (v instanceof Group) return ((Group) v).options();
            return singletonList(v);
        }).flatMap(Collection::stream);
    }

    private void sendItems() {
        this.items = items();
        sendItems(items);
    }

    private List<AlexandriaLayout.Item> panelItems(ElementRender r) {
        List<Panel> panelList = ((RenderPanels) r).panels();
        return panelList.stream().map(c -> itemOf(r, c)).collect(toList());
    }

    private List<AlexandriaLayout.Item> objectItems(ElementRender r) {
        RenderObjects render = (RenderObjects) r;
        ItemList itemList = render.source(session());
        return itemList.items().stream().map(record -> itemOf(record, r, render.panel())).collect(toList());
    }

    private List<AlexandriaLayout.Item> catalogItems(ElementRender r) {
        RenderCatalogs render = (RenderCatalogs) r;
        return render.catalogs().stream().map(c -> itemOf(r, c)).collect(toList());
    }

    private List<AlexandriaLayout.Item> moldItems(ElementRender r) {
        RenderMold render = (RenderMold) r;
        return singletonList(itemOf(r, render.mold()));
    }

    private List<AlexandriaLayout.Item> displayItem(ElementRender r) {
        Panel panel = new Panel();
        panel.views().add(new PanelView().render(r).name(UUID.randomUUID().toString()));
        return singletonList(itemOf(r, panel));
    }

    private PlatformInfo info() {
        return PlatformInfoBuilder.build(settings);
    }

    private UserInfo userInfoOf(User user) {
        return new UserInfo().fullName(user.fullName()).photo(photo(user));
    }

    private String photo(User user) {
        URL photo = user.photo();
        return photo != null ? photo.toString() : generateAvatar(user.fullName(), AvatarColor);
    }

    private void notifyLoaded() {
        loadedListeners.forEach(l -> l.accept(true));
    }

    private void buildElementProviders() {
        itemsProviders.put(RenderPanels.class, this::panelItems);
        itemsProviders.put(RenderObjects.class, this::objectItems);
        itemsProviders.put(RenderCatalogs.class, this::catalogItems);
        itemsProviders.put(RenderMold.class, this::moldItems);
        itemsProviders.put(RenderDisplay.class, this::displayItem);
    }

    private AlexandriaLayout.Item itemOf(ElementRender render, Element element) {
        return itemOf(null, render, element);
    }

    private AlexandriaLayout.Item itemOf(io.intino.konos.alexandria.ui.model.Item target, ContainerView view, Element element) {
        return new AlexandriaLayout.Item() {
            @Override
            public String name() {
                if (render.option() != null) return render.option().name();
                return target != null ? target.name() : element.name();
            }

            @Override
            public String label() {
                return labelOf(render, element, target);
            }

            @Override
            public String type() {
                return nameOf(AlexandriaLayout.this.element().displayTypeFor(element, target));
            }

            @Override
            public Group group() {
                return render.option().owner();
            }

            @Override
            public Element element() {
                return element;
            }

            @Override
            public io.intino.konos.alexandria.ui.model.Item target() {
                return target;
            }
        };
    }

    protected interface Item {
        String name();

        String label();

        String type();

        Group group();

        Element element();

        io.intino.konos.alexandria.ui.model.Item target();
    }

}