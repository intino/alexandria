package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.PlatformInfoBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.layout.ElementOption;
import io.intino.konos.alexandria.activity.model.layout.options.Group;
import io.intino.konos.alexandria.activity.model.layout.options.Option;
import io.intino.konos.alexandria.activity.model.layout.options.Options;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.*;
import io.intino.konos.alexandria.activity.schemas.PlatformInfo;
import io.intino.konos.alexandria.activity.schemas.Reference;
import io.intino.konos.alexandria.activity.schemas.ReferenceProperty;
import io.intino.konos.alexandria.activity.schemas.UserInfo;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.intino.konos.alexandria.activity.utils.AvatarUtil.generateAvatar;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public abstract class AlexandriaLayout<DN extends AlexandriaDisplayNotifier> extends AlexandriaElementStore<DN> {
	private List<Item> items = new ArrayList<>();
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<Boolean>> loadedListeners = new ArrayList<>();
	protected Map<Class<? extends ElementRender>, Function<ElementRender, List<Item>>> itemsProviders = new HashMap<>();
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

	public void showHome() {
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
		notifyLoaded();
	}

	@Override
	protected Element elementWithKey(String key) {
		Item item = itemWithKey(key);
		return item != null ? item.element() : null;
	}

	@Override
	protected io.intino.konos.alexandria.activity.model.Item targetWithKey(String key) {
		Item item = itemWithKey(key);
		return item != null ? item.target() : null;
	}

	@Override
	protected AlexandriaElementDisplay newDisplay(Element element, io.intino.konos.alexandria.activity.model.Item item) {
		return element().displayFor(element, item);
	}

	protected Item itemWithKey(String key) {
		return items.stream().filter(i -> key.equals(i.name()) || key.equals(i.label())).findFirst().orElse(null);
	}

	public void openItem(String label) {
		openElement(label);
	}

	protected Reference schemaItemOf(Item item) {
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

	protected abstract void sendItems(List<Item> value);

	protected abstract void sendUser(UserInfo value);

	protected abstract void notifyLoggedOut();

	private List<Item> itemOf(ElementOption option) {
		ElementRender render = renderOf(option);
		if (render == null) return emptyList();
		return itemsProviders.get(render.getClass()).apply(render);
	}

	private ElementRender renderOf(ElementOption option) {
		if (option instanceof Option) return ((Option) option).render();
		else if (option instanceof Options) return ((Options) option).render();
		return null;
	}

	private String labelOf(ElementRender render, Element element, io.intino.konos.alexandria.activity.model.Item item) {
		ElementOption owner = render.option();

		if (owner instanceof Option) return ((Option) owner).label();
		else if (owner instanceof Group) return ((Group) owner).label();
		else if (owner instanceof Options) return ((Options) owner).label(element, item != null ? item.object() : null);

		return "no label";
	}

	private List<Item> items() {
		ActivitySession session = session();
		return optionList().filter(option ->!option.hidden(session)).map(this::itemOf).flatMap(Collection::stream).collect(toList());
	}

	private Stream<ElementOption> optionList() {
		return element().options().stream().map(v -> {
			if (v instanceof Group) return ((Group) v).options();
			return singletonList(v);
		}).flatMap(Collection::stream);
	}

	private void sendItems() {
		this.items = items();
		sendItems(items);
	}

	private List<Item> panelItems(ElementRender r) {
		List<Panel> panelList = ((RenderPanels) r).panels();
		return panelList.stream().map(c -> itemOf(r, c)).collect(toList());
	}

	private List<Item> objectItems(ElementRender r) {
		RenderObjects render = (RenderObjects) r;
		ItemList itemList = render.source(session());
		return itemList.items().stream().map(record -> itemOf(r, render.panel(), record)).collect(toList());
	}

	private List<Item> catalogItems(ElementRender r) {
		RenderCatalogs render = (RenderCatalogs) r;
		return render.catalogs().stream().map(c -> itemOf(r, c)).collect(toList());
	}

	private List<Item> moldItems(ElementRender r) {
		RenderMold render = (RenderMold) r;
		return singletonList(itemOf(r, render.mold()));
	}

	private List<Item> displayItem(ElementRender r) {
		Panel panel = new Panel();
		panel.views().add(new View().render(r).name(UUID.randomUUID().toString()));
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

	private Item itemOf(ElementRender render, Element element) {
		return itemOf(render, element, null);
	}

	private Item itemOf(ElementRender render, Element element, io.intino.konos.alexandria.activity.model.Item target) {
		return new Item() {
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
			public io.intino.konos.alexandria.activity.model.Item target() {
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

		io.intino.konos.alexandria.activity.model.Item target();
	}

}