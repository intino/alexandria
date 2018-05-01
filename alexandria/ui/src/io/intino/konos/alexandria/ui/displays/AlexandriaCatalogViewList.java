package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaCatalogViewListNotifier;
import io.intino.konos.alexandria.ui.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.catalog.views.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AlexandriaCatalogViewList extends ActivityDisplay<AlexandriaCatalogViewListNotifier, Box> {
	private List<Consumer<AlexandriaCatalogView>> selectListeners = new ArrayList<>();
	private Map<String, Function<AlexandriaElementViewDefinition, ? extends AlexandriaDisplay>> builders = new HashMap<>();
	private List<AlexandriaElementViewDefinition> viewList;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<OpenItemCatalogEvent>> openItemCatalogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private List<Consumer<List<Item>>> selectItemListeners = new ArrayList<>();
	private Map<String, AlexandriaCatalogView> viewDisplayMap = new HashMap<>();

	private static final String ViewId = "%s%s";

	public AlexandriaCatalogViewList(Box box) {
		super(box);
		registerBuilders();
	}

	public void itemProvider(CatalogViewDisplayProvider provider) {
		this.provider = provider;
	}

	public void viewList(List<AlexandriaElementViewDefinition> viewList) {
		this.viewList = viewList;
	}

	public void onSelectView(Consumer<AlexandriaCatalogView> listener) {
		selectListeners.add(listener);
	}

	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	public void onOpenItemCatalog(Consumer<OpenItemCatalogEvent> listener) {
		openItemCatalogListeners.add(listener);
	}

	public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
	}

	public void onSelectItems(Consumer<List<Item>> listener) {
		selectItemListeners.add(listener);
	}

	public List<AlexandriaCatalogView> viewList() {
		return new ArrayList<>(viewDisplayMap.values());
	}

	private void registerBuilders() {
		builders.put(MagazineView.class.getSimpleName(), this::buildMagazineViewDisplay);
		builders.put(ListView.class.getSimpleName(), this::buildListViewDisplay);
		builders.put(GridView.class.getSimpleName(), this::buildListViewDisplay);
		builders.put(MapView.class.getSimpleName(), this::buildMapViewDisplay);
		builders.put(DisplayView.class.getSimpleName(), this::buildDisplayViewDisplay);
	}

	public void selectView(String name) {
		AlexandriaCatalogView display = display(name);
		notifier.refreshSelectedView(name);
		selectListeners.forEach(l -> l.accept(display));
	}

	public void refresh() {
		viewDisplayMap.values().forEach(AlexandriaElementView::refresh);
	}

	@Override
	protected void init() {
		super.init();
		sendViewList();
		if (viewList.size() > 0)
			selectView(viewList.get(0).name());
	}

	private AlexandriaCatalogView display(String name) {
		if (!viewDisplayMap.containsKey(name))
			buildViewDisplay(name);
		return viewDisplayMap.get(name);
	}

	private void buildViewDisplay(String name) {
		AlexandriaElementViewDefinition view = viewList.stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
		builders.get(view.type()).apply(view);
	}

	private void sendViewList() {
		notifier.refreshViewList(ReferenceBuilder.buildCatalogViewList(viewList));
	}

	private AlexandriaCatalogMagazineView buildMagazineViewDisplay(AlexandriaElementViewDefinition view) {
		AlexandriaCatalogMagazineView display = new AlexandriaCatalogMagazineView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogListView buildListViewDisplay(AlexandriaElementViewDefinition view) {
		AlexandriaCatalogListView display = new AlexandriaCatalogListView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogMapView buildMapViewDisplay(AlexandriaElementViewDefinition view) {
		AlexandriaCatalogMapView display = new AlexandriaCatalogMapView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogDisplayView buildDisplayViewDisplay(AlexandriaElementViewDefinition view) {
		AlexandriaCatalogDisplayView display = new AlexandriaCatalogDisplayView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private void registerViewDisplay(AlexandriaCatalogView display, AlexandriaElementViewDefinition view) {
		display.provider(provider);
		display.onOpenItemDialog(event -> openItemDialog((OpenItemDialogEvent) event));
		display.onOpenItemCatalog(event -> openItemCatalog((OpenItemCatalogEvent) event));
		display.onExecuteItemTask(event -> executeTask((ExecuteItemTaskEvent) event));
		if (display instanceof AlexandriaCatalogPageDisplay) ((AlexandriaCatalogPageDisplay) display).onSelectItems((selection) -> selectItems((List<Item>) selection));
		display.definition(view);
		display.onLoading(value -> notifyLoading((Boolean) value));
		viewDisplayMap.put(view.name(), display);
	}

	private void selectItems(List<Item> selection) {
		selectItemListeners.forEach(l -> l.accept(selection));
	}

	private void openItemDialog(OpenItemDialogEvent event) {
		openItemDialogListeners.forEach(l -> l.accept(event));
	}

	private void openItemCatalog(OpenItemCatalogEvent event) {
		openItemCatalogListeners.forEach(l -> l.accept(event));
	}

	private void executeTask(ExecuteItemTaskEvent event) {
		executeItemTaskListeners.forEach(l -> l.accept(event));
	}

	private void notifyLoading(Boolean loading) {
		loadingListeners.forEach(l -> l.accept(loading));
	}

	private String idOf(AlexandriaElementViewDefinition view) {
		return String.format(ViewId, this.id(), view.name());
	}

}