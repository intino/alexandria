package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.alexandria.ui.displays.factory.DisplayViewFactory;
import io.intino.alexandria.ui.model.View;
import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.displays.builders.ReferenceBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaCatalogViewListNotifier;
import io.intino.alexandria.ui.displays.providers.CatalogViewDisplayProvider;
import io.intino.alexandria.ui.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AlexandriaCatalogViewList extends ActivityDisplay<AlexandriaCatalogViewListNotifier, Box> {
	private List<Consumer<AlexandriaElementView>> selectListeners = new ArrayList<>();
	private List<View> viewList;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<OpenItemCatalogEvent>> openItemCatalogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private List<Consumer<List<Item>>> selectItemListeners = new ArrayList<>();
	private Map<String, AlexandriaElementView> displayViewMap = new HashMap<>();

	private static final String ViewId = "%s%s";

	public AlexandriaCatalogViewList(Box box) {
		super(box);
	}

	public void itemProvider(CatalogViewDisplayProvider provider) {
		this.provider = provider;
	}

	public void viewList(List<View> viewList) {
		this.viewList = viewList;
	}

	public void onSelectView(Consumer<AlexandriaElementView> listener) {
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

	public List<AlexandriaElementView> displayViewList() {
		return new ArrayList<>(displayViewMap.values());
	}

	public void selectView(String name) {
		AlexandriaElementView display = display(name);
		notifier.refreshSelectedView(name);
		selectListeners.forEach(l -> l.accept(display));
	}

	public void refresh() {
		displayViewMap.values().forEach(AlexandriaElementView::refresh);
	}

	@Override
	protected void init() {
		super.init();
		sendViewList();
		if (viewList.size() > 0)
			selectView(viewList.get(0).name());
	}

	private AlexandriaElementView display(String name) {
		if (!displayViewMap.containsKey(name))
			buildViewDisplay(name);
		return displayViewMap.get(name);
	}

	private void buildViewDisplay(String name) {
		View view = viewList.stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
		AlexandriaViewContainerCollection display = DisplayViewFactory.build(box, view);
		registerViewDisplay(display, view);
		add(display);
		display.personifyOnce(idOf(view));
	}

	private void sendViewList() {
		notifier.refreshViewList(ReferenceBuilder.buildCatalogViewList(viewList));
	}

	private void registerViewDisplay(AlexandriaElementView display, View view) {
		display.provider(provider);
		display.onOpenItemDialog(event -> openItemDialog((OpenItemDialogEvent) event));
		display.onOpenItemCatalog(event -> openItemCatalog((OpenItemCatalogEvent) event));
		display.onExecuteItemTask(event -> executeTask((ExecuteItemTaskEvent) event));
		if (display instanceof AlexandriaViewContainerCollectionPage) ((AlexandriaViewContainerCollectionPage) display).onSelectItems((selection) -> selectItems((List<Item>) selection));
		display.view(view);
		display.onLoading(value -> notifyLoading((Boolean) value));
		displayViewMap.put(view.name(), display);
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

	private String idOf(View view) {
		return String.format(ViewId, this.id(), view.name());
	}

}