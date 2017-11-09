package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.ActivityDisplay;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.box.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.activity.box.displays.notifiers.AlexandriaCatalogViewListDisplayNotifier;
import io.intino.konos.alexandria.activity.box.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.box.model.catalog.views.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AlexandriaCatalogViewListDisplay extends ActivityDisplay<AlexandriaCatalogViewListDisplayNotifier> {
	private List<Consumer<AlexandriaCatalogViewDisplay>> selectListeners = new ArrayList<>();
	private Map<String, Function<ElementView, ? extends AlexandriaDisplay>> builders = new HashMap<>();
	private List<ElementView> viewList;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<ElementViewDisplay.OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<ElementViewDisplay.OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<ElementViewDisplay.ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private Map<String, AlexandriaCatalogViewDisplay> viewDisplayMap = new HashMap<>();

	public AlexandriaCatalogViewListDisplay(Box box) {
		super(box);
		registerBuilders();
	}

	public void itemProvider(CatalogViewDisplayProvider provider) {
		this.provider = provider;
	}

	public void viewList(List<ElementView> viewList) {
		this.viewList = viewList;
	}

	public void onSelectView(Consumer<AlexandriaCatalogViewDisplay> listener) {
		selectListeners.add(listener);
	}

	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	public void onOpenItem(Consumer<ElementViewDisplay.OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<ElementViewDisplay.OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	public void onExecuteItemTask(Consumer<ElementViewDisplay.ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
	}

	public List<AlexandriaCatalogViewDisplay> viewList() {
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
		AlexandriaCatalogViewDisplay display = display(name);
		notifier.refreshSelectedView(name);
		selectListeners.forEach(l -> l.accept(display));
	}

	public void refresh() {
		viewDisplayMap.values().forEach(ElementViewDisplay::refresh);
	}

	@Override
	protected void init() {
		super.init();
		sendTarget();
		sendViewList();
		if (viewList.size() > 0)
			selectView(viewList.get(0).name());
	}

	private AlexandriaCatalogViewDisplay display(String name) {
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

	private AlexandriaCatalogMagazineViewDisplay buildMagazineViewDisplay(ElementView view) {
		AlexandriaCatalogMagazineViewDisplay display = new AlexandriaCatalogMagazineViewDisplay(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogListViewDisplay buildListViewDisplay(ElementView view) {
		AlexandriaCatalogListViewDisplay display = new AlexandriaCatalogListViewDisplay(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogMapViewDisplay buildMapViewDisplay(ElementView view) {
		AlexandriaCatalogMapViewDisplay display = new AlexandriaCatalogMapViewDisplay(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private AlexandriaCatalogDisplayViewDisplay buildDisplayViewDisplay(ElementView view) {
		AlexandriaCatalogDisplayViewDisplay display = new AlexandriaCatalogDisplayViewDisplay(box);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
		return display;
	}

	private void registerViewDisplay(AlexandriaCatalogViewDisplay display, ElementView view) {
		display.provider(provider);
		display.onOpenItem(this::openItem);
		display.onOpenItemDialog(this::openItemDialog);
		display.onExecuteItemTask(this::executeTask);
		display.view(view);
		display.onLoading(this::notifyLoading);
		viewDisplayMap.put(view.name(), display);
	}

	private void openItem(ElementViewDisplay.OpenItemEvent parameters) {
		openItemListeners.forEach(l -> l.accept(parameters));
	}

	private void openItemDialog(ElementViewDisplay.OpenItemDialogEvent event) {
		openItemDialogListeners.forEach(l -> l.accept(event));
	}

	private void executeTask(ElementViewDisplay.ExecuteItemTaskEvent event) {
		executeItemTaskListeners.forEach(l -> l.accept(event));
	}

	private void notifyLoading(Boolean loading) {
		loadingListeners.forEach(l -> l.accept(loading));
	}

	private String idOf(ElementView view) {
		return (provider.target() != null ? provider.target().name() : "") + view.name();
	}

}