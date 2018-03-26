package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelNotifier;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.helpers.ElementHelper;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderCatalogs;
import io.intino.konos.alexandria.activity.model.renders.RenderDisplay;
import io.intino.konos.alexandria.activity.model.renders.RenderMold;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.activity.schemas.Reference;
import io.intino.konos.alexandria.activity.schemas.ReferenceProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.intino.konos.alexandria.activity.model.panel.View.Layout.Tab;
import static java.util.stream.Collectors.toList;

public class AlexandriaPanel<DN extends AlexandriaPanelNotifier> extends AlexandriaElementDisplay<Panel, DN> implements ElementViewDisplayProvider {
	protected Map<Class<? extends ElementRender>, Function<ElementRender, AlexandriaPanelView>> displayBuilders = new HashMap<>();
	private Map<String, AlexandriaPanelView> viewDisplayMap = new HashMap<>();
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
		views().stream().filter(v -> !((View)v).layout().equals(Tab) && viewDisplayMap.containsKey(v.name())).forEach(v -> viewDisplayMap.get(v.name()).refresh());
	}

	@Override
	public void refresh(Item... objects) {
		super.refresh();
		io.intino.konos.alexandria.activity.schemas.Item[] items = ElementHelper.items(objects, this, this.baseAssetUrl());
		views().stream().filter(v -> !((View)v).layout().equals(Tab) && viewDisplayMap.containsKey(v.name())).forEach(v -> viewDisplayMap.get(v.name()).refresh(items));
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
		View view = views().stream().filter(v -> {
			ElementRender render = ((View) v).render();
			return render instanceof RenderCatalogs && renderContainsCatalogWithLabel((RenderCatalogs) render, label);
		}).map(v -> (View)v).findFirst().orElse(null);

		if (view != null) {
			AlexandriaPanelCatalogView viewDisplay = (AlexandriaPanelCatalogView) selectView(view.name());
			return viewDisplay.catalogDisplay();
		}
		else {
			AlexandriaPanel parent = parent(AlexandriaPanel.class);
			if (parent != null) return (E) parent.openElement(label);
			else return super.openElement(label);
		}
	}

	private boolean renderContainsCatalogWithLabel(RenderCatalogs render, String label) {
		return render.catalogs().stream().anyMatch(c -> c.label() != null && c.label().equals(label));
	}

	@Override
	protected void init() {
		super.init();
		createDialogContainer();
		sendViewList();
		buildFixedViews();
		selectFirstTabView();
	}

	private void selectFirstTabView() {
		for (AbstractView view : views()) {
			if (((View)view).layout() == Tab) {
				selectView(view.name());
				break;
			}
		}
	}

	private void sendViewList() {
		notifier.refreshViewList(views().stream().map(this::referenceOf).collect(toList()));
	}

	private Reference referenceOf(AbstractView view) {
		Reference reference = ReferenceBuilder.build(view);
		reference.referencePropertyList().add(new ReferenceProperty().name("hidden").value(String.valueOf(view.hidden(target(), session()))));
		return reference;
	}

	private void registerBuilders() {
		displayBuilders.put(RenderMold.class, this::buildCustomViewDisplay);
		displayBuilders.put(RenderCatalogs.class, this::buildCatalogViewDisplay);
		displayBuilders.put(RenderDisplay.class, this::buildOlapViewDisplay);
	}

	private AlexandriaPanelCustomView buildCustomViewDisplay(ElementRender render) {
		return new AlexandriaPanelCustomView(box);
	}

	private AlexandriaPanelCatalogView buildCatalogViewDisplay(ElementRender render) {
		return new AlexandriaPanelCatalogView(box);
	}

	private AlexandriaPanelDisplayView buildOlapViewDisplay(ElementRender render) {
		return new AlexandriaPanelDisplayView(box);
	}

	public void buildFixedViews() {
		views().stream().filter(v -> {
			View view = (View) v;
			return view.layout() == View.Layout.LeftFixed || view.layout() == View.Layout.RightFixed;
		}).forEach(v -> buildView(v.name()).refresh());
	}

	public AlexandriaPanelView selectView(String key) {
		AbstractView view = viewOf(key);
		if (view == null) return null;
		AlexandriaPanelView viewDisplay = buildView(view.name());
		viewDisplay.refresh();
		updateCurrentView(viewDisplay);
		notifier.refreshSelectedView(view.name());
		return viewDisplay;
	}

	private AlexandriaPanelView buildView(String name) {
		if (viewDisplayMap.containsKey(name))
			return viewDisplayMap.get(name);
		notifyLoading(true);
		AbstractView view = viewOf(name);
		AlexandriaPanelView display = buildView(view);
		viewDisplayMap.put(view.name(), display);
		notifyLoading(false);
		return display;
	}

	private AbstractView viewOf(String key) {
		return views().stream().filter(v -> (v.label() != null && v.label().equals(key)) || v.name().equals(key)).findFirst().orElse(null);
	}

	private AlexandriaPanelView buildView(AbstractView view) {
		View panelView = (View) view;
		ElementRender render = panelView.render();
		AlexandriaPanelView display = displayBuilders.get(render.getClass()).apply(render);
		display.provider(this);
		display.view(panelViewOf(panelView));
		display.context(element());
		display.target(target());
		display.onLoading(v -> notifyLoading((Boolean) v));
		display.onOpenItem(params -> openItem((AlexandriaElementView.OpenItemEvent) params));
		display.onOpenItemDialog(params -> openItemDialog((AlexandriaElementView.OpenItemDialogEvent) params));
		display.onOpenItemCatalog(params -> openItemCatalog((AlexandriaElementView.OpenItemCatalogEvent) params));
		display.onExecuteItemTask(params -> executeItemTask((AlexandriaElementView.ExecuteItemTaskEvent) params));
		add(display);
		display.personifyOnce(viewId(view));
		return display;
	}

	private String viewId(AbstractView view) {
		return String.format(ViewId, id(), view.name());
	}

	public void navigate(String key) {
		super.navigate(key);
	}

	protected ElementView<Panel> panelViewOf(View view) {
		return new ElementView<Panel>() {
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
			public <V extends AbstractView> V raw() {
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
				ElementRender render = view.render();
				if (render instanceof RenderMold) return ((RenderMold)render).mold();
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

}