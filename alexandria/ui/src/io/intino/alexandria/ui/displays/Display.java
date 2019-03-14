package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.International;
import io.intino.alexandria.ui.Soul;
import io.intino.alexandria.ui.SoulProvider;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.services.push.UISession;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Display<N extends DisplayNotifier, B extends Box> {
	private final B box;
	private String id;
	private final List<Display> children = new ArrayList<>();
	protected DisplayRepository repository;
	protected N notifier;
	private io.intino.alexandria.ui.SoulProvider soulProvider;
	private UISession session;
	private Display parent = null;
	private Display owner = null;
	private Boolean dirty = null;
	private List<String> route = new ArrayList<>();
	private PropertyList propertyList = new PropertyList();

	public Display(B box) {
		this.box = box;
		this.id = UUID.randomUUID().toString();
		propertyList.put("id", id);
	}

	public <D extends Display> D id(String id) {
		this.id = id;
		return (D) this;
	}

	public Display owner() {
		return owner;
	}

	public <D extends Display> D owner(Display owner) {
		this.owner = owner;
		return (D) this;
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
		children.stream().forEach(c -> c.propagateLanguageChanged(language));
	}

	public void refresh() {
	}

	public void forceRefresh() {
		dirty(true);
		refresh();
	}

	public boolean dirty() {
		return dirty == null || dirty;
	}

	public void dirty(boolean value) {
		dirty = value;
	}

	public String id() {
		return id;
	}

	public void remove() {
		notifier.remove(id);
	}

	public List<Display> children() {
		return children;
	}

	public <T extends Display> List<T> children(Class<T> clazz) {
		return children.stream()
				.filter(child -> clazz.isAssignableFrom(child.getClass()))
				.map(clazz::cast)
				.collect(toList());
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
		repository.register(child);
		children.add(child);
		child.init();
		return child;
	}

	public <D extends Display> D add(D child) {
		notifier.add(child);
		register(child);
		return child;
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
		List<? extends Display> childrenToRemove = children(clazz);
		childrenToRemove.forEach(this::removeChild);
	}

	public void removeChild(Display display) {
		display.remove();
		children.remove(display);
		repository.remove(display.id);
	}

	public String name() {
		return nameOf(this.getClass());
	}

	public static String nameOf(Class<? extends Display> clazz) {
		String name = clazz.getSimpleName();
		int index = name.lastIndexOf("Display");
		return index != -1 ? name.substring(0, index) : name;
	}

}