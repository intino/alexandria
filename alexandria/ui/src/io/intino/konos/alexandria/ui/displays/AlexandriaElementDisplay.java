package io.intino.konos.alexandria.ui.displays;

import com.google.gson.Gson;
import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.Resource;
import io.intino.konos.alexandria.ui.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.helpers.ElementHelper;
import io.intino.konos.alexandria.ui.model.*;
import io.intino.konos.alexandria.ui.model.catalog.Events;
import io.intino.konos.alexandria.ui.model.catalog.events.OnClickItem;
import io.intino.konos.alexandria.ui.model.dialog.DialogResult;
import io.intino.konos.alexandria.ui.model.mold.Block;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.model.mold.StampResult;
import io.intino.konos.alexandria.ui.model.mold.stamps.EmbeddedDialog;
import io.intino.konos.alexandria.ui.model.mold.stamps.EmbeddedDisplay;
import io.intino.konos.alexandria.ui.model.mold.stamps.Title;
import io.intino.konos.alexandria.ui.model.mold.stamps.Tree;
import io.intino.konos.alexandria.ui.model.mold.stamps.operations.OpenCatalogOperation;
import io.intino.konos.alexandria.ui.model.mold.stamps.operations.TaskOperation;
import io.intino.konos.alexandria.ui.model.toolbar.*;
import io.intino.konos.alexandria.ui.model.view.container.DisplayContainer;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.ui.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.ui.schemas.OpenElementParameters;
import io.intino.konos.alexandria.ui.schemas.Position;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
public abstract class AlexandriaElementDisplay<E extends Element, DN extends AlexandriaDisplayNotifier> extends ActivityDisplay<DN, Box> implements ItemBuilder.ItemBuilderProvider {
	private String label;
	private E element;
	private Item target;
	private ElementDisplayManager elementDisplayManager = null;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private AlexandriaElementView currentView = null;
	private Function<Item, Boolean> staticFilter = null;
	private Function<Item, Boolean> dynamicFilter = null;
	private boolean embedded = false;
	private Item openedItem = null;
	private TimeRange range;
	private List<String> enabledViews = null;
	private AlexandriaDialogBox dialogBox = null;

	public AlexandriaElementDisplay(Box box) {
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

	public AlexandriaElementDisplay element(E element) {
		this.element = element;
		return this;
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

	public void staticFilter(Function<Item, Boolean> filter) {
		this.staticFilter = filter;
		dirty(true);
	}

	public void dynamicFilter(Function<Item, Boolean> filter) {
		this.dynamicFilter = filter;
		dirty(true);
	}

	public void filterAndNotify(Function<Item, Boolean> filter) {
		dynamicFilter(filter);
		notifyFiltered(filter != null);
	}

	public void clearFilter() {
		boolean hasDynamicFilters = dynamicFilter != null;
		filterAndNotify(null);
		if (!hasDynamicFilters) return;
		target(null);
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

	public AlexandriaStamp display(String stampKey) {
		return ((EmbeddedDisplay)stamp(stampKey)).createDisplay(session());
	}

	public AlexandriaDialog dialog(String stampKey) {
		return ((EmbeddedDialog)stamp(stampKey)).createDialog(session());
	}

	public void executeOperation(ElementOperationParameters params, List<Item> selection) {
		Operation operation = operationOf(params);
		executeOperation(operation, params.option(), selection, params.position());
	}

	public Resource downloadOperation(ElementOperationParameters params, List<Item> selection) {
		Operation operation = operationOf(params);
		return downloadOperation(operation, params, selection);
	}

	public void changeItem(Item item, Stamp stamp, String value) {
		if (!stamp.editable()) return;

		if (item != null) currentItem(item.id());
		StampResult result = stamp.change(item, value, this, session());
		currentView().ifPresent(view -> {
			dirty(true);
			StampResult.Refresh refresh = result != null ? result.refresh() : StampResult.none().refresh();
			if (refresh == StampResult.Refresh.Item) view.refresh(currentItem_());
			else if (refresh == StampResult.Refresh.Container) view.refresh();
		});
		notifyUserIfNotEmpty(result.message());
	}

	public void validateItem(Item item, Stamp stamp, String value) {
		if (!stamp.editable()) return;

		if (item != null) currentItem(item.id());
		String message = stamp.validate(item, value, this, session());
		currentView().ifPresent(view -> {
			dirty(true);
			if (message != null) view.refreshValidation(message, stamp, currentItem_());
		});
	}

	public <V extends AlexandriaElementView> Optional<V> currentView() {
		return Optional.ofNullable((V) currentView);
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
		currentView().ifPresent(AlexandriaElementView::refresh);
	}

	public void refresh(Item... objects) {
		currentView().ifPresent(v -> v.refresh(ElementHelper.items(objects, this, baseAssetUrl())));
	}

	public void home() {
		hidePanel();
		refreshBreadcrumbs("");
		refresh();
		removePanelDisplay();
	}

	public void selectInstant(CatalogInstantBlock block) {
		AlexandriaAbstractCatalog display = catalogDisplayOf(block);
		List<String> items = block.items();
		display.filterAndNotify(item -> items.contains(((Item)item).id()));
		display.refreshViews();
	}

	public void enabledViews(List<String> views) {
		this.enabledViews = views;
	}

	public void openElement(OpenElementParameters params) {
		currentView().ifPresent(viewDisplay -> viewDisplay.openElement(params));
	}

	public <D extends AlexandriaElementDisplay> D openElement(String label) {
		return elementDisplayManager.openElement(label);
	}

	public <D extends AlexandriaElementDisplay> D openElement(String label, String ownerId) {
		return elementDisplayManager.openElement(label, ownerId);
	}

	public void openItem(String item) {
		openItem(new OpenItemEvent() {
			@Override
			public String label() {
				if (molds().size() <= 0) return item;
				Optional<Stamp> titleStamp = stamps(molds().get(0)).stream().filter(s -> (s instanceof Title)).findAny();
				return titleStamp.isPresent() ? ((Title)titleStamp.get()).value(item(), session()) : item().name();
			}

			@Override
			public String itemId() {
				return item;
			}

			@Override
			public Item item() {
				return AlexandriaElementDisplay.this.item(item);
			}

			@Override
			public Panel panel() {
				E element = AlexandriaElementDisplay.this.element();
				if (!(element instanceof Catalog)) return null;
				Events events = ((Catalog) element).events();
				if (events == null) return null;
				OnClickItem onClickItem = events.onClickItem();
				return onClickItem != null && onClickItem.openPanel() != null ? onClickItem.openPanel().panel() : null;
			}

			@Override
			public TimeRange range() {
				return AlexandriaElementDisplay.this.range();
			}

			@Override
			public Tree breadcrumbs() {
				E element = AlexandriaElementDisplay.this.element();
				if (!(element instanceof Catalog)) return null;
				Events events = ((Catalog) element).events();
				if (events == null) return null;
				OnClickItem onClickItem = events.onClickItem();
				return onClickItem != null && onClickItem.openPanel() != null ? onClickItem.openPanel().breadcrumbs(item(), session()) : null;
			}
		});
	}

	private AlexandriaAbstractCatalog catalogDisplayOf(CatalogInstantBlock block) {
		if (!this.element.name().equals(block.catalog()) && !this.element.label().equals(block.catalog()))
			return openElement(block.catalog());

		AlexandriaAbstractCatalog display = (AlexandriaAbstractCatalog) this;
		List<View> views = display.views();
		View view = views.stream().filter(v -> v.container() instanceof DisplayContainer).findFirst().orElse(null);
		if (view != null) display.selectView(view.name());

		return display;
	}

	protected void notifyLoading(Boolean loading) {
		loadingListeners.forEach(l -> l.accept(loading));
	}

	protected List<View> views() {
		List<View> elementViews = element().views();
		if (enabledViews == null) return elementViews;
		return elementViews.stream().filter(v -> enabledViews.contains(v.name())).collect(toList());
	}

	protected List<Mold> molds() {
		return views().stream().map(View::mold).filter(Objects::nonNull).collect(toList());
	}

	protected void updateCurrentView(AlexandriaElementView display) {
		this.currentView = display;
		refreshView();
	}

	protected void createDialogContainer() {
		dialogBox = new AlexandriaDialogBox(box);
		add(dialogBox);
		dialogBox.personifyOnce();
	}

	protected void openItem(OpenItemEvent event) {
		removePanelDisplay();
		openedItem = Item.createFrom(event.item()).label(event.label());
		AlexandriaPanel display = createPanelDisplay(event);
		createPanel(new CreatePanelParameters().displayType(display.getClass().getSimpleName()).item(event.itemId()));
		add(display);
		display.personifyOnce(event.itemId());
		showPanel();
		refreshBreadcrumbs(breadcrumbs(event));
	}

	protected void openItemCatalog(OpenItemCatalogEvent event) {
		Stamp stamp = event.stamp();

		if (stamp != null && stamp instanceof OpenCatalogOperation) {
			openItemCatalogInDialogBox(event);
			return;
		}

		AlexandriaElementDisplay display = openElement(event.catalog().label());
		String itemToShow = event.itemToShow();

		if (itemToShow != null) display.openItem(itemToShow);
		else {
			display.closeCurrentItem();
			if (event.filtered()) display.filterAndNotify(i -> event.filter((Item) i));
			display.refresh();
		}
	}

	protected void openItemCatalogInDialogBox(OpenItemCatalogEvent event) {
		currentItem(event.item().id());

		OpenCatalogOperation catalogOperation = (OpenCatalogOperation) event.stamp();
		AlexandriaAbstractCatalog display = catalogOperation.createCatalog(session());
		if (event.filtered()) display.staticFilter(i -> event.filter((Item) i));
		if (catalogOperation.selection() == OpenCatalogOperation.Selection.Single) display.onSelectItems((s) -> dialogBox.accept());
		if (catalogOperation.selection() == OpenCatalogOperation.Selection.Multiple) display.selectionEnabledByDefault(true);
		dialogBox.label(catalogOperation.label(event.item(), session()));
		dialogBox.display(display);
		dialogBox.settings(catalogOperation.width(), catalogOperation.height(), true, event.position());
		dialogBox.refresh();
		dialogBox.onAccept((value) -> {
			display.currentView().ifPresent(v -> {
				StampResult result = catalogOperation.execute(event.item(), ((AlexandriaViewContainerCollection) v).selectedItems(), id(), session());
				StampResult.Refresh refresh = result != null ? result.refresh() : StampResult.none().refresh();
				if (refresh == StampResult.Refresh.Item) refresh(this.currentItem());
				else if (refresh == StampResult.Refresh.Container) forceRefresh();
				notifyUserIfNotEmpty(result.message());
			});
			dialogBox.close();
		});
		showDialogBox();
	}

	protected void openItemDialog(OpenItemDialogEvent event) {
		currentItem(event.item().id());

		AlexandriaDialog dialog = event.dialog();

		dialog.onDone((result) -> currentView().ifPresent(view -> {
			dirty(true);
			if (result != null) {
				DialogResult.Refresh refresh = result.refresh();
				if (refresh == DialogResult.Refresh.Item) view.refresh(currentItem_());
				else if (refresh == DialogResult.Refresh.Container) view.refresh();
			}
			dialogBox.close();
		}));

		dialogBox.label(dialog.label());
		dialogBox.display(dialog);
		dialogBox.settings(dialog.width(), dialog.height());
		dialogBox.refresh();
		showDialogBox();
	}

	protected void closeCurrentItem() {
		home();
	}

	protected void removePanelDisplay() {
		if (openedItem == null) return;
		elementDisplayManager.removeElement(openedItem);
		remove(AlexandriaPanel.class);
	}

	protected void executeItemTask(ExecuteItemTaskEvent event) {
		notifyLoading(true);
		currentItem(event.item().id());
		Item item = this.currentItem();
		TaskOperation stamp = (TaskOperation) event.stamp();
		StampResult result = stamp.execute(item, event.self(), session());
		StampResult.Refresh refresh = result != null ? result.refresh() : StampResult.none().refresh();
		dirty(true);
		if (refresh == StampResult.Refresh.Item) refresh(this.currentItem());
		else if (refresh == StampResult.Refresh.Container) {
			closeCurrentItem();
			forceRefresh();
		}
		notifyUserIfNotEmpty(result.message());
		notifyLoading(false);
	}

	public AlexandriaPanel createPanelDisplay(OpenItemEvent event) {
		AlexandriaPanel display = elementDisplayManager.createElement(event.panel(), event.item());
		display.range(event.range());
		return display;
	}

	public Item item(String key) {
		return target;
	}

	public Optional<Toolbar> toolbar() {
		return Optional.ofNullable(element().toolbar());
	}

	public abstract void notifyUser(String message);
	protected abstract void showDialogBox();
	protected abstract void currentItem(String id);
	protected abstract Item currentItem();
	protected abstract void notifyFiltered(boolean value);
	protected abstract void refreshBreadcrumbs(String breadcrumbs);
	protected abstract void createPanel(CreatePanelParameters params);
	protected abstract void showPanel();
	protected abstract void hidePanel();

	protected void applyFilter(ItemList itemList) {
		if (staticFilter != null) itemList.filter(staticFilter);
		if (dynamicFilter != null) itemList.filter(dynamicFilter);
	}

	private io.intino.konos.alexandria.ui.schemas.Item currentItem_() {
		Item item = this.currentItem();
		return item != null ? ElementHelper.item(item, this, baseAssetUrl()) : null;
	}

	private Operation operationOf(ElementOperationParameters params) {
		Optional<Toolbar> toolbar = toolbar();

		if (!toolbar.isPresent())
			return null;

		return toolbar.get().operations().stream().filter(op -> op.name().equals(params.operation())).findFirst().orElse(null);
	}

	private void executeOperation(Operation operation, String option, List<Item> selection, Position position) {
		if (operation instanceof OpenDialog) {
			OpenDialog openDialog = (OpenDialog)operation;
			AlexandriaDialog dialog = openDialog.createDialog(session());
			dialogBox.label(dialog.label());
			dialogBox.display(dialog);
			dialogBox.settings(dialog.width(), dialog.height());
			dialogBox.refresh();
			showDialogBox();
		}

		if (operation instanceof Task) {
			Task taskOperation = (Task)operation;
			ToolbarResult result = taskOperation.execute(element(), id(), session());
			if (result.refresh() == ToolbarResult.Refresh.Container) this.refresh();
			notifyUserIfNotEmpty(result.message());
			return;
		}

		if (operation instanceof TaskSelection) {
			TaskSelection taskSelectionOperation = (TaskSelection)operation;
			ToolbarSelectionResult result = taskSelectionOperation.execute(element(), option, selection, id(), session());
			ToolbarSelectionResult.Refresh refresh = result != null ? result.refresh() : ToolbarSelectionResult.none().refresh();
			if (refresh == ToolbarSelectionResult.Refresh.Container) this.forceRefresh();
			else if (refresh == ToolbarSelectionResult.Refresh.Selection) this.refresh(selection.toArray(new Item[selection.size()]));
			notifyUserIfNotEmpty(result.message());
		}

		if (operation instanceof OpenCatalog) {
			OpenCatalog openCatalog = (OpenCatalog)operation;
			AlexandriaAbstractCatalog display = openCatalog.createCatalog(session());
			if (openCatalog.filtered()) display.staticFilter(i -> openCatalog.filter(element(), (Item) i, session()));
			dialogBox.label(openCatalog.title());
			dialogBox.display(display);
			dialogBox.settings(openCatalog.width(), openCatalog.height(), false);
			dialogBox.refresh();
			showDialogBox();
			return;
		}

		if (operation instanceof OpenCatalogSelection) {
			OpenCatalogSelection catalogOperation = (OpenCatalogSelection) operation;
			AlexandriaAbstractCatalog display = catalogOperation.createCatalog(session());
			if (catalogOperation.filtered()) display.staticFilter(i -> catalogOperation.filter(element(), selection, (Item) i, session()));
			if (catalogOperation.selection() == OpenCatalogSelection.Selection.Single) display.onSelectItems((s) -> dialogBox.accept());
			if (catalogOperation.selection() == OpenCatalogSelection.Selection.Multiple) display.selectionEnabledByDefault(true);
			dialogBox.label(catalogOperation.title());
			dialogBox.display(display);
			dialogBox.settings(catalogOperation.width(), catalogOperation.height(), true, catalogOperation.position() == OpenCatalogSelection.Position.RelativeToOperation ? position : null);
			dialogBox.refresh();
			dialogBox.onAccept((value) -> {
				display.currentView().ifPresent(v -> {
					ToolbarSelectionResult result = catalogOperation.execute(element(), selection, ((AlexandriaViewContainerCollection) v).selectedItems(), id(), session());
					ToolbarSelectionResult.Refresh refresh = result != null ? result.refresh() : ToolbarSelectionResult.none().refresh();
					if (refresh == ToolbarSelectionResult.Refresh.Item) refresh(this.currentItem());
					else if (refresh == ToolbarSelectionResult.Refresh.Container) forceRefresh();
				});
				dialogBox.close();
			});
			showDialogBox();
		}
	}

	private Resource downloadOperation(Operation operation, ElementOperationParameters params, List<Item> selection) {
		E element = element();
		UISession session = session();

		if (operation instanceof Export)
			return ((Export)operation).execute(element, params.from(), params.to(), id(), session);

		if (operation instanceof ExportSelection)
			return ((ExportSelection)operation).execute(element, params.from(), params.to(), selection, id(), session);

		if (operation instanceof Download)
			return ((Download)operation).execute(element, params.option(), id(), session);

		if (operation instanceof DownloadSelection)
			return ((DownloadSelection)operation).execute(element, params.option(), selection, id(), session);

		return null;
	}

	private String breadcrumbs(OpenItemEvent event) {
		Tree tree = event.breadcrumbs();

		if (tree == null) {
			tree = new Tree();
			Tree.TreeItem main = new Tree.TreeItem().name("main").label(label());
			if (openedItem != null) main.add(new Tree.TreeItem().name(openedItem.name()).label(openedItem.label()));
			tree.add(main);
		}

		return new Gson().toJson(tree);
	}

	private void notifyUserIfNotEmpty(String message) {
		if (message == null || message.isEmpty()) return;
		notifyUser(message);
	}

}
