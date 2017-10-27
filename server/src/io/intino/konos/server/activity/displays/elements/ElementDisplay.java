package io.intino.konos.server.activity.displays.elements;

import com.google.gson.Gson;
import io.intino.konos.Box;
import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.catalogs.AbstractCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.CatalogInstantBlock;
import io.intino.konos.server.activity.displays.catalogs.model.View;
import io.intino.konos.server.activity.displays.catalogs.model.views.DisplayView;
import io.intino.konos.server.activity.displays.catalogs.model.views.MoldView;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay.OpenItemDialogEvent;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay.OpenItemEvent;
import io.intino.konos.server.activity.displays.elements.builders.ItemBuilder.ItemBuilderProvider;
import io.intino.konos.server.activity.displays.elements.items.DialogContainerDisplay;
import io.intino.konos.server.activity.displays.elements.model.*;
import io.intino.konos.server.activity.displays.elements.model.toolbar.*;
import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.molds.model.Block;
import io.intino.konos.server.activity.displays.molds.model.Mold;
import io.intino.konos.server.activity.displays.molds.model.Stamp;
import io.intino.konos.server.activity.displays.molds.model.stamps.Display;
import io.intino.konos.server.activity.displays.molds.model.stamps.Tree;
import io.intino.konos.server.activity.displays.molds.model.stamps.operations.TaskOperation;
import io.intino.konos.server.activity.displays.panels.PanelDisplay;
import io.intino.konos.server.activity.displays.schemas.ElementOperationParameters;
import io.intino.konos.server.activity.displays.schemas.SaveItemParameters;
import io.intino.konos.server.activity.helpers.ElementHelper;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public abstract class ElementDisplay<E extends Element, DN extends DisplayNotifier> extends ActivityDisplay<DN> implements ItemBuilderProvider {
	private String label;
	private E element;
	private Item target;
	private ElementDisplayManager elementDisplayManager = null;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private ElementViewDisplay currentView = null;
	private Function<Item, Boolean> itemListFilter = null;
	private Boolean dirty = null;
	private boolean embedded = false;
	private OpenItemEvent openedItem = null;
	private TimeRange range;

	public ElementDisplay(Box box) {
		super(box);
	}

	public String label() {
		return this.label;
	}

	public void label(String label) {
		this.label = label;
	}

	public E element() {
		return this.element;
	}

	public void element(E element) {
		this.element = element;
	}

	public Item target() {
		return this.target;
	}

	public void target(Item target) {
		this.target = target;
	}

	public ElementDisplayManager elementDisplayManager() {
		return this.elementDisplayManager;
	}

	public void elementDisplayManager(ElementDisplayManager manager) {
		this.elementDisplayManager = manager;
	}

	public TimeRange range() {
		return range;
	}

	public void range(TimeRange range) {
		this.range = range;
	}

	public boolean embedded() {
		return this.embedded;
	}

	public void embedded(boolean value) {
		this.embedded = value;
	}

	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	public void filter(Function<Item, Boolean> filter) {
		this.itemListFilter = filter;
		dirty(true);
	}

	public void filterAndNotify(Function<Item, Boolean> filter) {
		filter(filter);
		notifyFiltered(filter != null);
	}

	public void clearFilter() {
		boolean refresh = itemListFilter != null;
		filterAndNotify(null);
		if (refresh) refresh();
	}

	public List<Block> blocks() {
		return molds().stream().map(this::blocks).flatMap(Collection::stream).collect(toList());
	}

	public List<Block> blocks(Mold mold) {
		return mold.blocks().stream().map(this::blocks).flatMap(Collection::stream).collect(toList());
	}

	public List<Block> blocks(Block block) {
		List<Block> result = new ArrayList<>();
		result.add(block);
		block.blockList().forEach(child -> result.addAll(blocks(child)));
		return result;
	}

	public List<Stamp> stamps() {
		return molds().stream().map(Mold::blocks).flatMap(Collection::stream).map(this::stamps).flatMap(Collection::stream).collect(toList());
	}

	public List<Stamp> stamps(Mold mold) {
		return mold.blocks().stream().map(this::stamps).flatMap(Collection::stream).collect(toList());
	}

	public List<Stamp> stamps(Block block) {
		List<Stamp> stamps = new ArrayList<>();

		stamps.addAll(block.stampList());
		block.blockList().forEach(child -> stamps.addAll(stamps(child)));

		return stamps;
	}

	public List<Stamp> expandedStamps() {
		return molds().stream().map(Mold::blocks).flatMap(Collection::stream).filter(Block::expanded).map(this::stamps).flatMap(Collection::stream).collect(toList());
	}

	public List<Stamp> expandedStamps(Mold mold) {
		return mold.blocks().stream().filter(Block::expanded).map(this::stamps).flatMap(Collection::stream).collect(toList());
	}

	public Stamp stamp(Mold mold, String name) {
		return stamps(mold).stream().filter(s -> s.name().equals(name)).findFirst().orElse(null);
	}

	public Stamp stamp(String name) {
		return stamps().stream().filter(s -> s.name().equals(name)).findFirst().orElse(null);
	}

	public StampDisplay display(String stampKey) {
		return ((Display)stamp(stampKey)).instance();
	}

	public void executeOperation(ElementOperationParameters params, List<Item> selection) {
		Operation operation = operationOf(params);
		executeOperation(operation, params.option(), selection);
	}

	public Resource downloadOperation(ElementOperationParameters params, List<Item> selection) {
		Operation operation = operationOf(params);
		return downloadOperation(operation, params, selection);
	}

	public void saveItem(SaveItemParameters params, Item item) {
		Stamp stamp = stamp(params.stamp());
		if (!stamp.editable()) return;

		currentItem(new String(Base64.getDecoder().decode(params.item())));
		Stamp.Editable.Refresh refresh = stamp.save(item, params.value(), username());
		currentView().ifPresent(view -> {
			dirty(true);
			if (refresh == Stamp.Editable.Refresh.Object) view.refresh(currentItem_());
			else if (refresh == Stamp.Editable.Refresh.Catalog) view.refresh();
		});
	}

	public Optional<ElementViewDisplay> currentView() {
		return Optional.ofNullable(currentView);
	}

	public TimeScale scale() {
		return null;
	}

	public abstract void reset();

	@Override
	public void refresh() {
		refreshView();
	}

	public void refreshView() {
		currentView().ifPresent(ElementViewDisplay::refresh);
	}

	public void refresh(Item... objects) {
		currentView().ifPresent(v -> v.refresh(ElementHelper.items(objects, this, baseAssetUrl())));
	}

	public boolean dirty() {
		return dirty == null || dirty;
	}

	public void dirty(boolean value) {
		dirty = value;
	}

	public void navigate(String key) {
		if (!key.equals("main")) return;
		hidePanel();
		refreshBreadcrumbs("");
	}

	public void selectInstant(CatalogInstantBlock block) {
		AbstractCatalogDisplay display = catalogDisplayOf(block);
		List<String> entities = block.entities();
		display.filterAndNotify(item -> entities.contains(((Item)item).id()));
		display.refreshViews();
	}

	private AbstractCatalogDisplay catalogDisplayOf(CatalogInstantBlock block) {
		if (!this.element.name().equals(block.catalog()) && !this.element.label().equals(block.catalog()))
			return openElement(block.catalog());

		AbstractCatalogDisplay display = (AbstractCatalogDisplay) this;
		List<?> views = display.views();
		View view = views.stream().map(v -> (View)v).filter(v -> !(v instanceof DisplayView)).findFirst().orElse(null);
		if (view != null) display.selectView(view.name());

		return display;
	}

	protected void notifyLoading(Boolean loading) {
		loadingListeners.forEach(l -> l.accept(loading));
	}

	protected List<? extends AbstractView> views() {
		return element().views();
	}

	protected List<Mold> molds() {
		return views().stream().filter(v -> (v instanceof MoldView)).map(v -> ((MoldView)v).mold()).collect(toList());
	}

	protected void updateCurrentView(ElementViewDisplay display) {
		this.currentView = display;
		refreshView();
	}

	protected void createDialogContainer() {
		DialogContainerDisplay display = new DialogContainerDisplay(box);
		display.onDialogAssertion((modification) -> currentView().ifPresent(view -> {
			dirty(true);
			if (modification.toLowerCase().equals("itemmodified")) view.refresh(currentItem_());
			else if (modification.toLowerCase().equals("catalogmodified")) view.refresh();
		}));
		add(display);
		display.personifyOnce();
	}

	protected void openItem(OpenItemEvent event) {
		openedItem = event;
		createPanel(event.itemId());
		PanelDisplay display = createPanelDisplay(event);
		add(display);
		display.personifyOnce(event.itemId());
		showPanel();
		refreshBreadcrumbs(breadcrumbs(event));
	}

	protected void openItemDialog(OpenItemDialogEvent event) {
		currentItem(new String(Base64.getDecoder().decode(event.item())));

		DialogContainerDisplay display = child(DialogContainerDisplay.class);
		display.dialogWidth(event.width());
		display.dialogHeight(event.height());
		display.dialogLocation(event.path());
		display.refresh();
		showDialog();
	}

	protected void executeItemTask(ElementViewDisplay.ExecuteItemTaskEvent event) {
		currentItem(new String(Base64.getDecoder().decode(event.item())));
		Item item = this.currentItem();
		((TaskOperation)event.stamp()).execute(item, username());
		dirty(true);
		refresh(this.currentItem());
	}

	public <E extends ElementDisplay> E openElement(String label) {
		return elementDisplayManager.openElement(label);
	}

	public PanelDisplay createPanelDisplay(OpenItemEvent event) {
		PanelDisplay display = elementDisplayManager.createElement(event.panel(), event.item());
		display.range(event.range());
		return display;
	}

	public Item item(String key) {
		return loadItem(key);
	}

	protected abstract void showDialog();
	protected abstract void currentItem(String id);
	protected abstract Item currentItem();
	protected abstract void notifyFiltered(boolean value);
	protected abstract void refreshBreadcrumbs(String breadcrumbs);
	protected abstract void createPanel(String item);
	protected abstract void showPanel();
	protected abstract void hidePanel();

	protected Item loadItem(String id) {
		return element().item(id, username());
	}

	protected void applyFilter(ItemList itemList) {
		if (itemListFilter == null) return;
		itemList.filter(itemListFilter);
	}

	private io.intino.konos.server.activity.displays.schemas.Item currentItem_() {
		return ElementHelper.item(this.currentItem(), this, baseAssetUrl());
	}

	private Operation operationOf(ElementOperationParameters params) {
		Optional<Toolbar> toolbar = toolbar();

		if (!toolbar.isPresent())
			return null;

		return toolbar.get().operations().stream().filter(op -> op.name().equals(params.operation())).findFirst().orElse(null);
	}

	private Optional<Toolbar> toolbar() {
		return Optional.ofNullable(element().toolbar());
	}

	private void executeOperation(Operation operation, String option, List<Item> selection) {
		if (operation instanceof OpenDialog) {
			DialogContainerDisplay display = child(DialogContainerDisplay.class);
			OpenDialog openDialog = (OpenDialog)operation;
			display.dialogWidth(openDialog.width());
			display.dialogHeight(openDialog.height());
			display.dialogLocation(openDialog.path());
			display.refresh();
			showDialog();
		}

		if (operation instanceof Task) {
			Task taskOperation = (Task)operation;
			Task.Refresh refresh = taskOperation.execute(element(), option, username());
			if (refresh == Task.Refresh.Catalog)
				this.refresh();
			return;
		}

		if (operation instanceof TaskSelection) {
			TaskSelection taskSelectionOperation = (TaskSelection)operation;
			TaskSelection.Refresh refresh = taskSelectionOperation.execute(element(), option, selection, username());
			if (refresh == TaskSelection.Refresh.Catalog)
				this.refresh();
			else if (refresh == TaskSelection.Refresh.Selection)
				this.refresh(selection.toArray(new Item[selection.size()]));
		}
	}

	private Resource downloadOperation(Operation operation, ElementOperationParameters params, List<Item> selection) {
		E element = element();
		String username = username();

		if (operation instanceof Export)
			return ((Export)operation).execute(element, params.from(), params.to(), username);

		if (operation instanceof ExportSelection)
			return ((ExportSelection)operation).execute(element, params.from(), params.to(), selection, username);

		if (operation instanceof Download)
			return ((Download)operation).execute(element, params.option(), username);

		if (operation instanceof DownloadSelection)
			return ((DownloadSelection)operation).execute(element, params.option(), selection, username);

		return null;
	}

	private String breadcrumbs(OpenItemEvent event) {
		Tree tree = event.breadcrumbs();

		if (tree == null) {
			tree = new Tree();
			Tree.TreeItem main = new Tree.TreeItem().name("main").label(label());
			if (openedItem != null)
				main.add(new Tree.TreeItem().name(openedItem.item().name()).label(openedItem.label()));
			tree.add(main);
		}

		return new Gson().toJson(tree);
	}
}
