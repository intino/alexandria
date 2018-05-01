package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaPanelNotifier;
import io.intino.konos.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.ui.helpers.ElementHelper;
import io.intino.konos.alexandria.ui.model.*;
import io.intino.konos.alexandria.ui.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.ui.model.views.ContainerView;
import io.intino.konos.alexandria.ui.model.views.container.*;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.ui.schemas.Reference;
import io.intino.konos.alexandria.ui.schemas.ReferenceProperty;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.intino.konos.alexandria.ui.model.View.Layout.Tab;
import static java.util.stream.Collectors.toList;

public class AlexandriaPanel<DN extends AlexandriaPanelNotifier> extends AlexandriaElementDisplay<Panel, DN> implements ElementViewDisplayProvider {
	protected Map<Class<? extends ElementRender>, Function<ElementRender, AlexandriaContainerView>> displayBuilders = new HashMap<>();
	private Map<String, AlexandriaContainerView> viewDisplayMap = new HashMap<>();
	private static final String ViewId = "%s%s";

	public AlexandriaPanel(Box box) {
		super(box);
		registerBuilders();
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
	public <E extends AlexandriaElementDisplay> E openElement(String label) {
		ContainerView view = views().stream().filter(v -> {
			Container container = ((ContainerView) v).container();
			return container instanceof CatalogContainer && viewContainsCatalogWithLabel((CatalogContainer)container, label);
		}).map(v -> (ContainerView)v).findFirst().orElse(null);

		if (view != null) {
			AlexandriaContainerViewCatalog viewDisplay = (AlexandriaContainerViewCatalog) openView(view.name());
			return viewDisplay.catalogDisplay();
		}
		else {
			AlexandriaPanel parent = parent(AlexandriaPanel.class);
			if (parent != null) return (E) parent.openElement(label);
			else return super.openElement(label);
		}
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

	private void registerBuilders() {
		displayBuilders.put(MoldContainer.class, this::buildMoldViewDisplay);
		displayBuilders.put(CatalogContainer.class, this::buildCatalogViewDisplay);
		displayBuilders.put(PanelContainer.class, this::buildPanelViewDisplay);
		displayBuilders.put(DisplayContainer.class, this::buildDisplayViewDisplay);
		displayBuilders.put(SetContainer.class, this::buildSetViewDisplay);
	}

	private AlexandriaContainerViewMold buildMoldViewDisplay(ElementRender render) {
		return new AlexandriaContainerViewMold(box);
	}

	private AlexandriaContainerViewCatalog buildCatalogViewDisplay(ElementRender render) {
		return new AlexandriaContainerViewCatalog(box);
	}

	private AlexandriaContainerViewPanel buildPanelViewDisplay(ElementRender render) {
		return new AlexandriaContainerViewPanel(box);
	}

	private AlexandriaContainerViewDisplay buildDisplayViewDisplay(ElementRender render) {
		return new AlexandriaContainerViewDisplay(box);
	}

	private AlexandriaContainerViewSet buildSetViewDisplay(ElementRender render) {
		return new AlexandriaContainerViewSet(box);
	}

	public void buildFixedViews() {
		views().stream().filter(v -> v.layout() == View.Layout.LeftFixed || v.layout() == View.Layout.RightFixed).forEach(v -> buildView(v.name()).refresh());
	}

	private AlexandriaContainerView buildView(String name) {
		if (viewDisplayMap.containsKey(name))
			return viewDisplayMap.get(name);
		notifyLoading(true);
		View view = viewOf(name);
		AlexandriaContainerView display = buildView(view);
		viewDisplayMap.put(view.name(), display);
		notifyLoading(false);
		return display;
	}

	private View viewOf(String key) {
		return views().stream().filter(v -> (v.label() != null && v.label().equals(key)) || v.name().equals(key)).findFirst().orElse(null);
	}

	private AlexandriaContainerView buildView(View view) {
		ContainerView containerView = (ContainerView) view;
		Container container = containerView.container();
		AlexandriaContainerView display = displayBuilders.get(render.getClass()).apply(render);
		display.route(routeSubPath());
		display.provider(this);
		display.definition(panelViewOf(containerView));
		display.context(element());
		display.target(target());
		display.onLoading(v -> notifyLoading((Boolean) v));
		display.onOpenItem(event -> openItem((OpenItemEvent) event));
		display.onOpenItemDialog(event -> openItemDialog((OpenItemDialogEvent) event));
		display.onOpenItemCatalog(event -> openItemCatalog((OpenItemCatalogEvent) event));
		display.onExecuteItemTask(event -> executeItemTask((ExecuteItemTaskEvent) event));
		add(display);
		display.personifyOnce(viewId(view));
		return display;
	}

	private String viewId(View view) {
		return String.format(ViewId, id(), view.name());
	}

	protected AlexandriaElementViewDefinition<Panel> panelViewOf(ContainerView view) {
		return new AlexandriaElementViewDefinition<Panel>() {
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
				return view.getClass().getSimpleName();
			}

			@Override
			public <V extends View> V raw() {
				return (V) view;
			}

			@Override
			public boolean embeddedElement() {
				return embedded();
			}

			@Override
			public Toolbar toolbar() {
				return element().toolbar();
			}

			@Override
			public int width() {
				return 100;
			}

			@Override
			public Mold mold() {
				Container container = view.container();
				if (container instanceof MoldContainer) return ((MoldContainer)container).mold();
				return null;
			}

			@Override
			public OnClickRecord onClickRecordEvent() {
				return null;
			}

			@Override
			public boolean canCreateClusters() {
				return false;
			}

			@Override
			public boolean canSearch() {
				return false;
			}

			@Override
			public boolean selectionEnabledByDefault() {
				return false;
			}

			@Override
			public List<String> clusters() {
				return Collections.emptyList();
			}

			@Override
			public Item target() {
				return AlexandriaPanel.this.target();
			}

			@Override
			public Panel element() {
				return AlexandriaPanel.this.element();
			}

			@Override
			public String emptyMessage() {
				return null;
			}
		};
	}

	public void home() {
		super.home();
	}

	public void openItem(String item) {
		super.openItem(item);
	}

	public AlexandriaContainerView openView(String key) {
		View view = viewOf(key);
		if (view == null) return null;
		AlexandriaContainerView viewDisplay = buildView(view.name());
		viewDisplay.refresh();
		updateCurrentView(viewDisplay);
		notifier.refreshSelectedView(view.name());
		return viewDisplay;
	}

}