package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.displays.factory.DisplayViewFactory;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaPanelNotifier;
import io.intino.konos.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.ui.helpers.ElementHelper;
import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.Panel;
import io.intino.konos.alexandria.ui.model.View;
import io.intino.konos.alexandria.ui.model.view.container.CatalogContainer;
import io.intino.konos.alexandria.ui.model.view.container.Container;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.ui.schemas.OpenElementParameters;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.HashMap;
import java.util.Map;

import static io.intino.konos.alexandria.ui.model.View.Layout.Tab;
import static java.util.stream.Collectors.toList;

public class AlexandriaPanel<DN extends AlexandriaPanelNotifier> extends AlexandriaElementDisplay<Panel, DN> implements ElementViewDisplayProvider {
	private Map<String, AlexandriaViewContainer> viewDisplayMap = new HashMap<>();
	private static final String ViewId = "%s%s";

	public AlexandriaPanel(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	public void refresh() {
		super.refresh();
		views().stream().filter(v -> !v.layout().equals(Tab) && viewDisplayMap.containsKey(v.name())).forEach(v -> viewDisplayMap.get(v.name()).refresh());
	}

	@Override
	public void refresh(Item... objects) {
		super.refresh();
		io.intino.konos.alexandria.ui.schemas.Item[] items = ElementHelper.items(objects, this, this.baseAssetUrl());
		views().stream().filter(v -> !v.layout().equals(Tab) && viewDisplayMap.containsKey(v.name())).forEach(v -> viewDisplayMap.get(v.name()).refresh(items));
	}

	@Override
	public void notifyUser(String message) {
		notifier.notifyUser(message);
	}

	@Override
	protected void refreshBreadcrumbs(String breadcrumbs) {
		notifier.refreshBreadcrumbs(breadcrumbs);
	}

	@Override
	protected void createPanel(CreatePanelParameters params) {
		notifier.createPanel(params);
	}

	@Override
	protected void showPanel() {
		notifier.showPanel();
	}

	@Override
	protected void hidePanel() {
		notifier.hidePanel();
	}

	@Override
	protected void showDialogBox() {
		notifier.showDialogBox();
	}

	@Override
	protected void currentItem(String id) {
	}

	@Override
	protected Item currentItem() {
		return this.target();
	}

	@Override
	protected void notifyFiltered(boolean value) {
	}

	@Override
	public boolean selectionEnabledByDefault() {
		return false;
	}

	private boolean viewContainsCatalogWithLabel(CatalogContainer container, String label) {
		Catalog catalog = container.catalog();
		return catalog.label() != null && catalog.label().equals(label);
	}

	@Override
	protected void init() {
		super.init();
		createDialogContainer();
		sendViewList();
		buildFixedViews();
		selectDefaultView();
	}

	private void selectDefaultView() {
		String path = routePath();
		View view = (path != null && viewOf(path) != null) ? viewOf(path) : findFirstTabView();
		if (view == null) return;
		openView(view.name());
	}

	private View findFirstTabView() {
		UISession session = session();
		return views().stream().filter(v -> v.layout() == Tab && !v.hidden(target(), session)).findFirst().orElse(null);
	}

	private void sendViewList() {
		notifier.refreshViewList(views().stream().map(this::referenceOf).collect(toList()));
	}

	private Reference referenceOf(View view) {
		Reference reference = ReferenceBuilder.build(view);
		reference.referencePropertyList().add(new ReferenceProperty().name("hidden").value(String.valueOf(view.hidden(target(), session()))));
		return reference;
	}

	public void buildFixedViews() {
		views().stream().filter(v -> v.layout() == View.Layout.LeftFixed || v.layout() == View.Layout.RightFixed).forEach(v -> buildView(v.name()).refresh());
	}

	private AlexandriaViewContainer buildView(String name) {
		if (viewDisplayMap.containsKey(name))
			return viewDisplayMap.get(name);
		notifyLoading(true);
		View view = viewOf(name);
		AlexandriaViewContainer display = buildView(view);
		viewDisplayMap.put(view.name(), display);
		notifyLoading(false);
		return display;
	}

	private View viewOf(String key) {
		return views().stream().filter(v -> (v.label() != null && v.label().equals(key)) || v.name().equals(key)).findFirst().orElse(null);
	}

	private AlexandriaViewContainer buildView(View view) {
		AlexandriaViewContainer display = DisplayViewFactory.build(box, view);
		if (display == null) return null;

		display.route(routeSubPath());
		display.provider(this);
		display.view(view);
		display.context(element());
		display.target(target());
		display.onLoading(v -> notifyLoading((Boolean) v));
		display.onOpenItem(event -> openItem((OpenItemEvent) event));
		display.onOpenItemDialog(event -> openItemDialog((OpenItemDialogEvent) event));
		display.onOpenItemCatalog(event -> openItemCatalog((OpenItemCatalogEvent) event));
		display.onExecuteItemTask(event -> executeItemTask((ExecuteItemTaskEvent) event));
		add(display);
		display.personify(viewId(view));

		return display;
	}

	private String viewId(View view) {
		return String.format(ViewId, id(), view.name());
	}

	@Override
	public void home() {
		super.home();
	}

	@Override
	public void openItem(String item) {
		super.openItem(item);
	}

	@Override
	public <E extends AlexandriaElementDisplay> E openElement(String label) {
		View view = views().stream().filter(v -> {
			Container container = v.container();
			return container instanceof CatalogContainer && viewContainsCatalogWithLabel((CatalogContainer)container, label);
		}).findFirst().orElse(null);

		if (view != null) {
			AlexandriaViewContainerCatalog viewDisplay = (AlexandriaViewContainerCatalog) openView(view.name());
			return viewDisplay.catalogDisplay();
		}
		else {
			AlexandriaPanel parent = parent(AlexandriaPanel.class);
			if (parent != null) return (E) parent.openElement(label);
			else return super.openElement(label);
		}
	}

	@Override
	public void openElement(OpenElementParameters params) {
		super.openElement(params);
	}

	public AlexandriaViewContainer openView(String key) {
		View view = viewOf(key);
		if (view == null) return null;
		AlexandriaViewContainer viewDisplay = buildView(view.name());
		viewDisplay.refresh();
		updateCurrentView(viewDisplay);
		notifier.refreshSelectedView(view.name());
		return viewDisplay;
	}

}