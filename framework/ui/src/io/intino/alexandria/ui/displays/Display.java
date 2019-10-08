package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.International;
import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.SoulProvider;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.services.push.UISession;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

import static io.intino.alexandria.ui.utils.UUIDUtil.isUUID;
import static java.util.Collections.emptyList;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;

public class Display<N extends DisplayNotifier, B extends Box> {
	private final B box;
	private String id;
	private final Map<String, List<Display>> children = new HashMap<>();
	private Map<String, List<Display>> promisedChildren = new HashMap<>();
	protected DisplayRepository repository;
	protected N notifier;
	protected String container = null;
	private io.intino.alexandria.ui.SoulProvider soulProvider;
	private UISession session;
	private Display parent = null;
	private Display owner = null;
	private List<String> route = new ArrayList<>();
	private PropertyList propertyList = new PropertyList();
	private String label = "";
	private String name = "";

	private static final String DefaultInstanceContainer = "__elements";

	public Display(B box) {
		this.box = box;
		this.id = UUID.randomUUID().toString();
		propertyList.put("id", id);
	}

	public <D extends Display> D id(String id) {
		this.id = id;
		propertyList.put("id", id);
		return (D) this;
	}

	public Display owner() {
		return owner;
	}

	public <D extends Display> D owner(Display owner) {
		this.owner = owner;
		propertyList.put("o", owner.path());
		return (D) this;
	}

	public String label() {
		return label;
	}

	public <D extends Display> D label(String label) {
		this.label = label;
		return (D) this;
	}

	public String path() {
		Display owner = parent();
		List<String> result = new ArrayList<>();
		result.add(id());
		while (owner != null) {
			result.add(owner.id());
			if (isUUID(owner.id())) break;
			owner = owner.parent();
		}
		reverse(result);
		return String.join(".", result);
	}

	public B box() {
		return box;
	}

	public void inject(UISession session) {
		this.session = session;
	}

	public void inject(N notifier) {
		this.notifier = notifier;
	}

	public void inject(DisplayRepository repository) {
		this.repository = repository;
	}

	public void inject(SoulProvider soulProvider) {
		this.soulProvider = soulProvider;
	}

	public PropertyList properties() {
		return propertyList;
	}

	public void route(String... paths) {
		route(Arrays.asList(paths));
	}

	public void route(List<String> paths) {
		this.route = paths;
	}

	public List<String> route() { return this.route; }

	public String routePath() {
		return route.size() > 0 ? route.get(0) : null;
	}

	public List<String> routeSubPath() {
		return route.size() > 1 ? route.subList(1, route.size()) : emptyList();
	}

	public void setLanguage(String language) {
		propagateLanguageChanged(language);
	}

	public UISession session() {
		return session;
	}

	protected void init() {
	}

	protected <S extends Soul> S soul() {
		return (S) soulProvider.soul();
	}

	protected String assetUrl(URL asset) {
		return Asset.toResource(soul().baseAssetUrl(), asset).toUrl().toString();
	}

	protected String assetUrl(URL asset, String label) {
		return Asset.toResource(soul().baseAssetUrl(), asset).setLabel(label).toUrl().toString();
	}

	private void propagateLanguageChanged(String language) {
		if (this instanceof io.intino.alexandria.ui.International)
			((International) this).onChangeLanguage(language);
		allChildren().forEach(c -> c.propagateLanguageChanged(language));
	}

	public void update() {
	}

	public void refresh() {
	}

	public String id() {
		return id;
	}

	public void remove() {
		notifier.remove(id, DefaultInstanceContainer);
	}

	public List<Display> children() {
		return new ArrayList<>(allChildren());
	}

	public List<Display> promisedChildren() {
		return new ArrayList<>(allPromisedChildren());
	}

	public List<Display> promisedChildren(String container) {
		return promisedChildren.containsKey(container) ? promisedChildren.get(container) : new ArrayList<>();
	}

	public List<Display> promisedChildren(List<String> ids) {
		return allPromisedChildren().stream().filter(c -> ids.contains(c.id())).collect(toList());
	}

	public <T extends Display> List<T> children(Class<T> clazz) {
		return allChildren().stream()
				.filter(child -> clazz.isAssignableFrom(child.getClass()))
				.map(clazz::cast)
				.collect(toList());
	}

	public List<Display> children(List<String> ids) {
		return allChildren().stream().filter(child -> ids.contains(child.id())).collect(toList());
	}

	public <T extends Display> T child(Class<T> clazz) {
		return children(clazz).stream().findFirst().map(clazz::cast).orElse(null);
	}

	public <T extends Display> T child(int position) {
		List<Display> children = children();
		return children.size() > position ? (T) children().get(position) : null;
	}

	public <T extends Display> T child(String id) {
		return (T) children().stream().filter(d -> d.id().equals(id)).findFirst().orElse(null);
	}

	public <D extends Display> D register(D child) {
		((Display)child).parent(this);
		promisedChildren(child.container).remove(child);
		repository.register(child);
		addChild(child, container(child));
		child.init();
		return child;
	}

	public <D extends Display> D add(D child) {
		return add(child, container(child));
	}

	public <D extends Display> D add(D child, String container) {
		addPromise(child, container);
		register(child);
		return child;
	}

	public <D extends Display> D addPromise(D child) {
		return addPromise(child, container(child));
	}

	public <D extends Display> D addPromise(D child, String container) {
		return registerPromise(child, container, containerName -> notifier.add(child, containerName));
	}

	public <D extends Display> List<D> addPromise(List<D> children, String container) {
		return registerPromise(children, container, containerName -> notifier.add(children, containerName));
	}

	public <D extends Display> D insertPromise(D child, int index) {
		return insertPromise(child, index, container(child));
	}

	public <D extends Display> D insertPromise(D child, int index, String container) {
		return registerPromise(child, container, containerName -> notifier.insert(child, index, containerName));
	}

	public <D extends Display> List<D> insertPromise(List<D> children, int index, String container) {
		return registerPromise(children, container, containerName -> notifier.insert(children, index, containerName));
	}

	public <T extends Display> T parent() {
		return (T) parent;
	}

	public <T extends Display> T parent(Class<T> type) {
		return parent(parent, type);
	}

	public <T extends Display> T parent(Display display, Class<T> type) {
		if (display == null) return null;
		if (type.isAssignableFrom(display.getClass())) return (T) display;
		return parent(display.parent(), type);
	}

	private void parent(Display parent) {
		this.parent = parent;
	}

	public void remove(Class<? extends Display> clazz) {
		remove(clazz, DefaultInstanceContainer);
	}

	public void remove(Class<? extends Display> clazz, String container) {
		List<? extends Display> childrenToRemove = children(clazz);
		childrenToRemove.forEach(d -> removeChild(d, container));
	}

	public void clear(String container) {
		children(container).ifPresent(children -> children.forEach(d -> {
			d.remove();
			repository.remove(d.id);
		}));
		children.remove(container);
		promisedChildren.remove(container);
		notifier.clearContainer(container);
	}

	public void removeChild(Display display) {
		removeChild(display, null);
	}

	public void removeChild(Display display, String container) {
		display.remove();
		notifier.remove(display.id, container);
		children.get(container).remove(display);
		repository.remove(display.id);
	}

	public String name() {
		return name != null && !name.isEmpty() ? name : nameOf(this.getClass());
	}

	public <D extends Display> D name(String name) {
		this.name = name;
		return (D) this;
	}

	public static String nameOf(Class<? extends Display> clazz) {
		String name = clazz.getSimpleName();
		int index = name.lastIndexOf("Display");
		return index != -1 ? name.substring(0, index) : name;
	}

	public URL baseAssetUrl() {
		try {
			return new URL(session().browser().baseAssetUrl());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private Optional<List<Display>> children(String container) {
		return Optional.ofNullable(children.getOrDefault(container, null));
	}

	private List<Display> allChildren() {
		return children.values().stream().flatMap(Collection::stream).collect(toList());
	}

	private List<Display> allPromisedChildren() {
		return promisedChildren.values().stream().flatMap(Collection::stream).collect(toList());
	}

	private void addChild(Display child, String container) {
		if (!children.containsKey(container)) children.put(container, new ArrayList<>());
		children.get(container).add(child);
	}

	private void addPromisedChild(Display child, String container) {
		if (!promisedChildren.containsKey(container)) promisedChildren.put(container, new ArrayList<>());
		child.container = container;
		promisedChildren.get(container).add(child);
	}

	private String container(Display child) {
		return child.container != null ? child.container : DefaultInstanceContainer;
	}

	private <D extends Display> D registerPromise(D child, String container, Consumer<String> consumer) {
		String containerName = container != null ? container : DefaultInstanceContainer;
		child.owner(this);
		consumer.accept(containerName);
		addPromisedChild(child, containerName);
		return child;
	}

	private <D extends Display> List<D> registerPromise(List<D> children, String container, Consumer<String> consumer) {
		String containerName = container != null ? container : DefaultInstanceContainer;
		children.forEach(c -> c.owner(this));
		consumer.accept(container);
		children.forEach(c -> addPromisedChild(c, containerName));
		return children;
	}

}