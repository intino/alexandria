package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.PlatformInfoBuilder;
import io.intino.konos.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.model.Element;
import io.intino.konos.alexandria.ui.model.Panel;
import io.intino.konos.alexandria.ui.model.Settings;
import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.panel.Desktop;
import io.intino.konos.alexandria.ui.model.view.ContainerView;
import io.intino.konos.alexandria.ui.model.view.container.*;
import io.intino.konos.alexandria.ui.schemas.PlatformInfo;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;
import io.intino.konos.alexandria.ui.schemas.UserInfo;
import io.intino.konos.alexandria.ui.services.push.UISession;
import io.intino.konos.alexandria.ui.services.push.User;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.intino.konos.alexandria.ui.utils.AvatarUtil.generateAvatar;
import static java.util.stream.Collectors.toList;

public abstract class AlexandriaLayout<DN extends AlexandriaDisplayNotifier> extends AlexandriaElementStore<Desktop, DN> {
	private List<LayoutItem> items = new ArrayList<>();
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<Boolean>> loadedListeners = new ArrayList<>();
	private List<Consumer<Boolean>> logoutListeners = new ArrayList<>();
	protected Map<Class<? extends Container>, Function<ContainerView, LayoutItem>> itemProviders = new HashMap<>();
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
	protected io.intino.konos.alexandria.ui.model.Item targetWithKey(String key) {
		return null;
	}

	@Override
	protected AlexandriaElementDisplay newDisplay(Element element, io.intino.konos.alexandria.ui.model.Item item) {
		return element().displayFor(element, item);
	}

	protected LayoutItem itemWithKey(String key) {
		return items.stream().filter(i -> key.equals(i.name()) || key.equals(i.label())).findFirst().orElse(null);
	}

	public void openItem(String key) {
		openElement(itemWithKey(key).label());
	}

	protected Reference schemaItemOf(LayoutItem item) {
		Reference result = ReferenceBuilder.build(item.name(), item.label());
		List<ReferenceProperty> referenceProperties = result.referencePropertyList();
		referenceProperties.add(new ReferenceProperty().name("type").value(item.type()));
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
		if (!(view instanceof ContainerView)) return null;
		ContainerView containerView = (ContainerView)view;
		Container container = containerView.container();
		return itemProviders.get(container.getClass()).apply(containerView);
	}

	private List<LayoutItem> items() {
		UISession session = session();
		return views().stream().filter(view ->!view.hidden(null, session)).map(this::itemOf).collect(toList());
	}

	private void sendItems() {
		this.items = items();
		sendItems(items);
	}

	private LayoutItem panelItem(ContainerView view) {
		PanelContainer container = view.container();
		return itemOf(view, container.panel());
	}

	private LayoutItem catalogItem(ContainerView view) {
		CatalogContainer container = view.container();
		return itemOf(view, container.catalog());
	}

	private LayoutItem setItem(ContainerView view) {
		return itemOf(view, null);
	}

	private LayoutItem moldItem(ContainerView view) {
		MoldContainer container = view.container();
		return itemOf(view, container.mold());
	}

	private LayoutItem displayItem(ContainerView view) {
		DisplayContainer container = view.container();
		Panel panel = new Panel();
		panel.views().add(new ContainerView().container(container).name(UUID.randomUUID().toString()).layout(View.Layout.Tab));
		return itemOf(view, panel);
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
			public String type() {
				return nameOf(AlexandriaLayout.this.element().displayTypeFor(element, null));
			}

			@Override
			public Element element() {
				return element;
			}
		};
	}

	protected interface LayoutItem {
		String name();
		String label();
		String type();
		Element element();
	}

}