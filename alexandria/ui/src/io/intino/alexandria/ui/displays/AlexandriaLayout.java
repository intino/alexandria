package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.*;
import io.intino.alexandria.ui.model.view.container.*;
import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.displays.builders.PlatformInfoBuilder;
import io.intino.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.alexandria.ui.model.panel.Desktop;
import io.intino.konos.alexandria.ui.schemas.PlatformInfo;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;
import io.intino.konos.alexandria.ui.schemas.UserInfo;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.intino.alexandria.ui.utils.AvatarUtil.generateAvatar;
import static java.util.stream.Collectors.toList;

public abstract class AlexandriaLayout<DN extends AlexandriaDisplayNotifier> extends AlexandriaElementStore<Desktop, DN> {
	private List<LayoutItem> items = new ArrayList<>();
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<Boolean>> loadedListeners = new ArrayList<>();
	private List<Consumer<Boolean>> logoutListeners = new ArrayList<>();
	protected Map<Class<? extends Container>, Function<View, LayoutItem>> itemProviders = new HashMap<>();
	private Settings settings;
	private Consumer<String> onOpenItem = null;

	public static final String AvatarColor = "#3F51B5";

	public AlexandriaLayout(Box box) {
		super(box);
		elementDisplayManager(this);
		buildElementProviders();
	}

	public AlexandriaLayout settings(Settings settings) {
		this.settings = settings;
		return this;
	}

	public void onOpenItem(Consumer<String> listener) {
		this.onOpenItem = listener;
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

	public void whenLogout(Consumer<Boolean> listener) {
		logoutListeners.add(listener);
	}

	public void logout() {
		logoutListeners.forEach(l -> l.accept(true));
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
		LayoutItem item = itemWithKey(key);
		return item != null ? item.element() : null;
	}

	@Override
	protected Item targetWithKey(String key) {
		return null;
	}

	@Override
	protected String normalize(String key) {
		LayoutItem layoutItem = itemWithKey(key);
		return layoutItem != null ? layoutItem.label() : key;
	}

	@Override
	protected AlexandriaElementDisplay newDisplay(Element element, Item item) {
		return element().displayFor(element, item);
	}

	protected LayoutItem itemWithKey(String key) {
		return items.stream().filter(i -> key.equals(i.name()) || key.equals(i.label()) ||
										  (i.element() != null && key.equals(i.element().name())) ||
										  (i.element() != null && key.equals(i.element().label()))
		).findFirst().orElse(null);
	}

	public void openItem(String key) {
		LayoutItem layoutItem = itemWithKey(key);
		openElement(layoutItem.label());
		if (this.onOpenItem != null) this.onOpenItem.accept(layoutItem.name());
	}

	protected Reference schemaItemOf(LayoutItem item) {
		Reference result = ReferenceBuilder.build(item.name(), item.label());
		List<ReferenceProperty> referenceProperties = result.referencePropertyList();
		referenceProperties.add(new ReferenceProperty().name("type").value(item.type()));
		referenceProperties.add(new ReferenceProperty().name("elementType").value(item.elementType()));
		return result;
	}

	protected abstract void sendLoading();
	protected abstract void sendLoaded();
	protected abstract void refreshOpened(String value);
	protected abstract void sendInfo(PlatformInfo value);
	protected abstract void sendItems(List<LayoutItem> value);
	protected abstract void sendUser(UserInfo value);
	protected abstract void openDefaultItem(String item);
	protected abstract void notifyLoggedOut();

	private LayoutItem itemOf(View view) {
		Container container = view.container();
		return itemProviders.get(container.getClass()).apply(view);
	}

	private List<LayoutItem> items() {
		UISession session = session();
		return views().stream().filter(view ->!view.hidden(null, session)).map(this::itemOf).collect(toList());
	}

	private void sendItems() {
		this.items = items();
		sendItems(items);
	}

	private LayoutItem panelItem(View view) {
		PanelContainer container = view.container();
		return itemOf(view, container.panel());
	}

	private LayoutItem catalogItem(View view) {
		CatalogContainer container = view.container();
		return itemOf(view, container.catalog());
	}

	private LayoutItem setItem(View view) {
		return itemOf(view, createPanel(view));
	}

	private LayoutItem moldItem(View view) {
		return itemOf(view, createPanel(view));
	}

	private LayoutItem displayItem(View view) {
		return itemOf(view, createPanel(view));
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
		itemProviders.put(PanelContainer.class, this::panelItem);
		itemProviders.put(CatalogContainer.class, this::catalogItem);
		itemProviders.put(SetContainer.class, this::setItem);
		itemProviders.put(MoldContainer.class, this::moldItem);
		itemProviders.put(DisplayContainer.class, this::displayItem);
	}

	private LayoutItem itemOf(View view, Element element) {
		return new LayoutItem() {
			@Override
			public String name() {
				return view.name();
			}

			@Override
			public String label() {
				return view.label();
			}

			@Override
			public String elementType() {
				return AlexandriaLayout.this.typeOf(element);
			}

			@Override
			public String type() {
				return nameOf(AlexandriaLayout.this.element().displayTypeFor(element, null));
			}

			@Override
			public Element element() {
				return element;
			}
		};
	}

	private Panel createPanel(View view) {
		Panel panel = new Panel();
		panel.views().add(new View().container(view.container()).layout(view.layout()).width(view.width()).name(UUID.randomUUID().toString()).layout(View.Layout.Tab));
		return panel;
	}

	protected interface LayoutItem {
		String name();
		String label();
		String elementType();
		String type();
		Element element();
	}

}