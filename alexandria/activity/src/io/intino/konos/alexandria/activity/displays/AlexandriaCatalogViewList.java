package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogViewListNotifier;
import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.catalog.views.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AlexandriaCatalogViewList extends ActivityDisplay<AlexandriaCatalogViewListNotifier> {
	private List<Consumer<AlexandriaCatalogView>> selectListeners = new ArrayList<>();
	private Map<String, Function<ElementView, ? extends AlexandriaDisplay>> builders = new HashMap<>();
	private List<ElementView> viewList;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementView.OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementView.OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementView.ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private Map<String, AlexandriaCatalogView> viewDisplayMap = new HashMap<>();

	public AlexandriaCatalogViewList(Box box) {
		super(box);
		registerBuilders();
	}

	public void itemProvider(CatalogViewDisplayProvider provider) {
		this.provider = provider;
	}

	public void viewList(List<ElementView> viewList) {
		this.viewList = viewList;
	}

	public void onSelectView(Consumer<AlexandriaCatalogView> listener) {
		selectListeners.add(listener);
	}

	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	public void onOpenItem(Consumer<AlexandriaElementView.OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<AlexandriaElementView.OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	public void onExecuteItemTask(Consumer<AlexandriaElementView.ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
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
		sendTarget();
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
		ElementView view = viewList.stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
		builders.get(view.type()).apply(view);
	}

	private void sendTarget() {
		if (provider.target() == null) return;
		notifier.refreshTarget(provider.target().name());
	}

	private void sendViewList() {
		notifier.refreshViewList(ReferenceBuilder.buildCatalogViewList(viewList));
	}

	private AlexandriaCatalogMagazineView buildMagazineViewDisplay(ElementView view) {
		AlexandriaCatalogMagazineView display = new AlexandriaCatalogMagazineView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogListView buildListViewDisplay(ElementView view) {
		AlexandriaCatalogListView display = new AlexandriaCatalogListView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogMapView buildMapViewDisplay(ElementView view) {
		AlexandriaCatalogMapView display = new AlexandriaCatalogMapView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogDisplayView buildDisplayViewDisplay(ElementView view) {
		AlexandriaCatalogDisplayView display = new AlexandriaCatalogDisplayView(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private void registerViewDisplay(AlexandriaCatalogView display, ElementView view) {
		display.provider(provider);
		display.onOpenItem(this::openItem);
		display.onOpenItemDialog(this::openItemDialog);
		display.onExecuteItemTask(this::executeTask);
		display.view(view);
		display.onLoading(this::notifyLoading);
		viewDisplayMap.put(view.name(), display);
	}

	private void openItem(AlexandriaElementView.OpenItemEvent parameters) {
		openItemListeners.forEach(l -> l.accept(parameters));
	}

	private void openItemDialog(AlexandriaElementView.OpenItemDialogEvent event) {
		openItemDialogListeners.forEach(l -> l.accept(event));
	}

	private void executeTask(AlexandriaElementView.ExecuteItemTaskEvent event) {
		executeItemTaskListeners.forEach(l -> l.accept(event));
	}

	private void notifyLoading(Boolean loading) {
		loadingListeners.forEach(l -> l.accept(loading));
	}

	private String idOf(ElementView view) {
		return (provider.target() != null ? provider.target().name() : "") + view.name();
	}

}