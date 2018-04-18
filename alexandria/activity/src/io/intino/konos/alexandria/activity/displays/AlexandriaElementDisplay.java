package io.intino.konos.alexandria.activity.displays;

import com.google.gson.Gson;
import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.helpers.ElementHelper;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Toolbar;
import io.intino.konos.alexandria.activity.model.catalog.Events;
import io.intino.konos.alexandria.activity.model.catalog.View;
import io.intino.konos.alexandria.activity.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.activity.model.catalog.views.DisplayView;
import io.intino.konos.alexandria.activity.model.catalog.views.MoldView;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.StampResult;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedDialog;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedDisplay;
import io.intino.konos.alexandria.activity.model.mold.stamps.Title;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.OpenCatalogOperation;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.TaskOperation;
import io.intino.konos.alexandria.activity.model.toolbar.*;
import io.intino.konos.alexandria.activity.model.toolbar.Operation;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

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
		refresh();
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
			StampResult.Refresh refresh = result.refresh();
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

	public Optional<AlexandriaElementView> currentView() {
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
		currentView().ifPresent(AlexandriaElementView::refresh);
	}

	public void refresh(Item... objects) {
		currentView().ifPresent(v -> v.refresh(ElementHelper.items(objects, this, baseAssetUrl())));
	}

	public void navigate(String key) {
		String name = new String(Base64.getDecoder().decode(key.getBytes()));
		if (!name.equals("main")) return;
		navigateMain();
	}

	public void navigateMain() {
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

	public <E extends AlexandriaElementDisplay> E openElement(String label) {
		return elementDisplayManager.openElement(label);
	}

	public void openItem(String item) {
		openItem(new AlexandriaElementView.OpenItemEvent() {
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
				OnClickRecord onClickRecord = events.onClickRecord();
				return onClickRecord != null && onClickRecord.openPanel() != null ? onClickRecord.openPanel().panel() : null;
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
				OnClickRecord onClickRecord = events.onClickRecord();
				return onClickRecord != null && onClickRecord.openPanel() != null ? onClickRecord.openPanel().breadcrumbs(item(), session()) : null;
			}
		});
	}

	private AlexandriaAbstractCatalog catalogDisplayOf(CatalogInstantBlock block) {
		if (!this.element.name().equals(block.catalog()) && !this.element.label().equals(block.catalog()))
			return openElement(block.catalog());

		AlexandriaAbstractCatalog display = (AlexandriaAbstractCatalog) this;
		List<?> views = display.views();
		View view = views.stream().map(v -> (View)v).filter(v -> !(v instanceof DisplayView)).findFirst().orElse(null);
		if (view != null) display.selectView(view.name());

		return display;
	}

	protected void notifyLoading(Boolean loading) {
		loadingListeners.forEach(l -> l.accept(loading));
	}

	protected List<? extends AbstractView> views() {
		List<AbstractView> elementViews = element().views();
		if (enabledViews == null) return elementViews;
		return elementViews.stream().filter(v -> enabledViews.contains(v.name())).collect(toList());
	}

	protected List<Mold> molds() {
		return views().stream().filter(v -> (v instanceof MoldView)).map(v -> ((MoldView)v).mold()).collect(toList());
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

	protected void openItem(AlexandriaElementView.OpenItemEvent event) {
		removePanelDisplay();
		openedItem = Item.createFrom(event.item()).label(event.label());
		AlexandriaPanel display = createPanelDisplay(event);
		createPanel(new CreatePanelParameters().displayType(display.getClass().getSimpleName()).item(event.itemId()));
		add(display);
		display.personifyOnce(event.itemId());
		showPanel();
		refreshBreadcrumbs(breadcrumbs(event));
	}

	protected void closeCurrentItem() {
		navigateMain();
	}

	protected void removePanelDisplay() {
		if (openedItem == null) return;
		elementDisplayManager.removeElement(openedItem);
		remove(AlexandriaPanel.class);
	}

	protected void openItemDialog(AlexandriaElementView.OpenItemDialogEvent event) {
		currentItem(event.item().id());

		AlexandriaDialog dialog = event.dialog();

		dialog.onDone((modification) -> currentView().ifPresent(view -> {
			dirty(true);
			if (modification == DialogExecution.Modification.ItemModified) view.refresh(currentItem_());
			else if (modification == DialogExecution.Modification.CatalogModified) view.refresh();
			dialogBox.close();
		}));

		dialogBox.label(dialog.label());
		dialogBox.display(dialog);
		dialogBox.settings(dialog.width(), dialog.height());
		dialogBox.refresh();
		showDialogBox();
	}

	protected void openItemCatalog(AlexandriaElementView.OpenItemCatalogEvent event) {
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

	protected void openItemCatalogInDialogBox(AlexandriaElementView.OpenItemCatalogEvent event) {
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
				StampResult result = catalogOperation.execute(event.item(), ((AlexandriaCatalogView) v).selectedItems(), session());
				StampResult.Refresh refresh = result.refresh();
				if (refresh == StampResult.Refresh.Item) refresh(this.currentItem());
				else if (refresh == StampResult.Refresh.Container) forceRefresh();
				notifyUserIfNotEmpty(result.message());
			});
			dialogBox.close();
		});
		showDialogBox();
	}

	protected void executeItemTask(AlexandriaElementView.ExecuteItemTaskEvent event) {
		notifyLoading(true);
		currentItem(event.item().id());
		Item item = this.currentItem();
		TaskOperation stamp = (TaskOperation) event.stamp();
		StampResult result = stamp.execute(item, event.self(), session());
		StampResult.Refresh refresh = result.refresh();
		dirty(true);
		if (refresh == StampResult.Refresh.Item) refresh(this.currentItem());
		else if (refresh == StampResult.Refresh.Container) {
			closeCurrentItem();
			forceRefresh();
		}
		notifyUserIfNotEmpty(result.message());
		notifyLoading(false);
	}

	public AlexandriaPanel createPanelDisplay(AlexandriaElementView.OpenItemEvent event) {
		AlexandriaPanel display = elementDisplayManager.createElement(event.panel(), event.item());
		display.range(event.range());
		return display;
	}

	public Item item(String key) {
		return target;
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

	private io.intino.konos.alexandria.activity.schemas.Item currentItem_() {
		Item item = this.currentItem();
		return item != null ? ElementHelper.item(item, this, baseAssetUrl()) : null;
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
			ToolbarSelectionResult result = taskSelectionOperation.execute(element(), option, selection, session());
			ToolbarSelectionResult.Refresh refresh = result.refresh();
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
					ToolbarSelectionResult result = catalogOperation.execute(element(), selection, ((AlexandriaCatalogView) v).selectedItems(), session());
					ToolbarSelectionResult.Refresh refresh = result.refresh();
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
		ActivitySession session = session();

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

	private String breadcrumbs(AlexandriaElementView.OpenItemEvent event) {
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
