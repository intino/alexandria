package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaPanelNotifier;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.activity.model.panel.View;
import io.intino.konos.alexandria.activity.model.renders.RenderCatalogs;
import io.intino.konos.alexandria.activity.model.renders.RenderDisplay;
import io.intino.konos.alexandria.activity.model.renders.RenderMold;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.activity.schemas.Reference;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
	protected void showDialog() {
		notifier.showDialog();
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
	protected void init() {
		super.init();
		sendTarget();
		sendViewList();
		createDialogContainer();
		if (views().size() > 0)
			selectView(views().get(0).name());
	}

	private void sendTarget() {
		if (target() == null) return;
		notifier.refreshTarget(target().name());
	}

	private void sendViewList() {
		notifier.refreshViewList(views().stream().map(this::referenceOf).collect(toList()));
	}

	private Reference referenceOf(AbstractView view) {
		return ReferenceBuilder.build(view);
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

	public void selectView(String name) {
		AlexandriaPanelView viewDisplay = buildView(name);
		viewDisplay.refresh();
		updateCurrentView(viewDisplay);
	}

	private AlexandriaPanelView buildView(String name) {
		if (viewDisplayMap.containsKey(name)) viewDisplayMap.get(name);
		notifyLoading(true);
		AlexandriaPanelView display = buildView(views().stream().filter(v -> v.name().equals(name)).findFirst().orElse(null));
		viewDisplayMap.put(name, display);
		notifyLoading(false);
		return display;
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
		display.onExecuteItemTask(params -> executeItemTask((AlexandriaElementView.ExecuteItemTaskEvent) params));
		add(display);
		display.personifyOnce(viewId(view));
		return display;
	}

	private String viewId(AbstractView view) {
		if (target() == null) return view.name();
		return String.format(ViewId, target().name(), view.name());
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