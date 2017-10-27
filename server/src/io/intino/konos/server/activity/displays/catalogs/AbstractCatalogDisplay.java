package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.builders.CatalogBuilder;
import io.intino.konos.server.activity.displays.catalogs.model.Catalog;
import io.intino.konos.server.activity.displays.catalogs.model.Events;
import io.intino.konos.server.activity.displays.catalogs.model.Scope;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.Group;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.GroupMap;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.Grouping;
import io.intino.konos.server.activity.displays.catalogs.model.arrangement.GroupingManager;
import io.intino.konos.server.activity.displays.catalogs.model.events.OnClickRecord;
import io.intino.konos.server.activity.displays.catalogs.model.views.GridView;
import io.intino.konos.server.activity.displays.catalogs.model.views.ListView;
import io.intino.konos.server.activity.displays.catalogs.model.views.MagazineView;
import io.intino.konos.server.activity.displays.catalogs.model.views.MoldView;
import io.intino.konos.server.activity.displays.catalogs.providers.CatalogViewDisplayProvider;
import io.intino.konos.server.activity.displays.catalogs.views.CatalogViewListDisplay;
import io.intino.konos.server.activity.displays.elements.AbstractView;
import io.intino.konos.server.activity.displays.elements.ElementDisplay;
import io.intino.konos.server.activity.displays.elements.ElementView;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay.OpenItemEvent;
import io.intino.konos.server.activity.displays.elements.items.ItemDisplay;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.ItemList;
import io.intino.konos.server.activity.displays.elements.model.Toolbar;
import io.intino.konos.server.activity.displays.molds.model.Mold;
import io.intino.konos.server.activity.displays.schemas.ClusterGroup;
import io.intino.konos.server.activity.displays.schemas.GroupingGroup;
import io.intino.konos.server.activity.displays.schemas.GroupingSelection;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractCatalogDisplay<E extends Catalog, DN extends CatalogDisplayNotifier> extends ElementDisplay<E, DN> implements CatalogViewDisplayProvider {
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
	protected Map<String, GroupingSelection> groupingSelectionMap = new HashMap<>();
	protected Scope scope = null;
	private String condition = null;
	private ItemList itemList = null;
	private String currentItem = null;
	protected GroupingManager groupingManager;

	public AbstractCatalogDisplay(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	public boolean isFor(E catalog) {
		return element().name().equals(catalog.name());
	}

	public void catalog(E catalog) {
		this.element(catalog);
	}

	public void selectView(String name) {
		child(CatalogViewListDisplay.class).selectView(name);
	}

	public void selectGrouping(GroupingSelection selection) {
		Grouping abstractGrouping = groupingOf(selection.name());

		if (selection.groups().size() <= 0 ||
				(abstractGrouping.histogram() == Grouping.Histogram.Absolute && selection.groups().size() <= 0))
			groupingSelectionMap.remove(selection.name());
		else
			groupingSelectionMap.put(selection.name(), selection);

		dirty(true);
		refreshGrouping();
	}

	public void deleteGroupingGroup(GroupingGroup groupingGroup) {
		Grouping grouping = groupingOf(groupingGroup.grouping());
		if (grouping == null || !grouping.cluster()) return;

		Group group = grouping.remove(groupingGroup.name());
		if (group == null) return;
		sendCatalog();

		if (groupingSelectionMap.containsKey(groupingGroup.grouping())) {
			GroupingSelection groupingSelection = groupingSelectionMap.get(groupingGroup.grouping());
			groupingSelection.groups().remove(group.label());
			selectGrouping(groupingSelection);
		}
	}

	public void refresh(Grouping grouping) {
		sendCatalog();
		currentView().ifPresent(ElementViewDisplay::refresh);
	}

	public void refreshViews() {
		child(CatalogViewListDisplay.class).refresh();
	}

	@Override
	public Object concept() {
		return element().concept();
	}

	@Override
	public int countItems(String condition) {
		this.updateCondition(condition);
		loadItemList();
		return itemList.count();
	}

	@Override
	public List<Item> items(int start, int limit, String condition) {
		return items(start, limit, condition, null);
	}

	public List<Item> items(int start, int limit, String condition, Sorting sorting) {
		this.updateCondition(condition);
		loadItemList();
		return itemList.items(start, limit, sorting);
	}

	@Override
	public Item rootItem(List<Item> itemList) {
		return element().rootItem(itemList, username());
	}

	@Override
	public Item defaultItem(String name) {
		return element().defaultItem(name, username());
	}

	@Override
	protected Item currentItem() {
		return item(currentItem);
	}

	@Override
	protected void refreshBreadcrumbs(String breadcrumbs) {
		notifier.refreshBreadcrumbs(breadcrumbs);
	}

	@Override
	protected void createPanel(String item) {
		notifier.createPanel(item);
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
	protected void notifyFiltered(boolean value) {
		notifier.refreshFiltered(value);
	}

	@Override
	protected void currentItem(String id) {
		this.currentItem = id;
	}

	@Override
	public Item item(String key) {
		Item item = null;

		if (!dirty() && itemList != null)
			item = itemList.items().stream().filter(i -> i.id().equals(key) || i.name().equals(key)).findFirst().orElse(null);

		return item != null ? item : loadItem(key);
	}

	@Override
	public List<io.intino.konos.server.activity.displays.catalogs.model.arrangement.Sorting> sortings() {
		return element().sortings();
	}

	@Override
	public io.intino.konos.server.activity.displays.catalogs.model.arrangement.Sorting sorting(String key) {
		return element().sortings().stream().filter(s -> s.name().equals(key) || s.label().equals(key)).findFirst().orElse(null);
	}

	public void onOpenItem(Consumer<OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	protected void resetGrouping() {
		groupingSelectionMap.clear();
		scope = null;
		element().scope(scope);
	}

	protected void refreshGrouping() {
		refreshScope();
		element().scope(scope);
		refreshView();
		sendCatalog();
	}

	@Override
	protected void init() {
		super.init();
		createGroupingManager();
		buildViewList();
		sendCatalog();
		createItemDisplay();
		createDialogContainer();
	}

	protected void createGroupingManager() {
		groupingManager = new GroupingManager(itemList(null).items(), groupings(), element().arrangementFilter());
	}

	protected ElementView<Catalog> catalogViewOf(AbstractView view) {
		return new ElementView<Catalog>() {
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
				if (view instanceof MagazineView) return ((MagazineView)view).width();
				else if (view instanceof ListView) return ((ListView)view).width();
				else if (view instanceof GridView) return ((GridView)view).width();
				return 100;
			}

			@Override
			public Mold mold() {
				if (view instanceof MoldView) return ((MoldView)view).mold();
				return null;
			}

			@Override
			public OnClickRecord onClickRecordEvent() {
				Events events = AbstractCatalogDisplay.this.element().events();
				return events != null ? events.onClickRecord() : null;
			}

			@Override
			public boolean canCreateClusters() {
				return AbstractCatalogDisplay.this.canCreateClusters();
			}

			@Override
			public boolean canSearch() {
				return AbstractCatalogDisplay.this.canSearch();
			}

			@Override
			public List<String> clusters() {
				return element().groupings().stream().filter(Grouping::cluster).map(Grouping::label).collect(toList());
			}

			@Override
			public Item target() {
				return AbstractCatalogDisplay.this.target();
			}

			@Override
			public Catalog element() {
				return AbstractCatalogDisplay.this.element();
			}

			@Override
			public String emptyMessage() {
				if (view instanceof MagazineView) return ((MagazineView)view).noRecordMessage();
				else if (view instanceof ListView) return ((ListView)view).noRecordsMessage();
				else if (view instanceof GridView) return ((GridView)view).noRecordsMessage();
				return null;
			}
		};
	}

	protected abstract ItemList itemList(String condition);

	protected boolean canCreateClusters() {
		return element().groupings().size() > 0;
	}

	protected boolean isGrouping(Map.Entry<String, GroupingSelection> entry) {
		return !groupingOf(entry.getKey()).cluster();
	}

	protected boolean isCluster(Map.Entry<String, GroupingSelection> entry) {
		return groupingOf(entry.getKey()).cluster();
	}

	protected void sendCatalog() {
		notifier.refreshCatalog(CatalogBuilder.build(element(), groupingManager, label(), embedded()));
	}

	private List<ElementView> viewList() {
		return views().stream().map(this::catalogViewOf).collect(toList());
	}

	private boolean canSearch() {
		Toolbar toolbar = element().toolbar();
		return toolbar == null || toolbar.canSearch();
	}

	private void updateCondition(String condition) {
		if (this.condition != null && !this.condition.equals(condition))
			dirty(true);
		this.condition = condition;
	}

	private void loadItemList() {
		if (!dirty() && itemList != null) return;
		itemList = AbstractCatalogDisplay.this.itemList(condition);
		dirty(false);
	}

	private void buildViewList() {
		CatalogViewListDisplay display = new CatalogViewListDisplay(box);
		display.itemProvider(this);
		display.viewList(viewList());
		display.onSelectView(this::updateCurrentView);
		display.onOpenItem(this::openItem);
		display.onOpenItemDialog(this::openItemDialog);
		display.onExecuteItemTask(this::executeItemTask);
		display.onLoading(this::notifyLoading);
		add(display);
		display.personifyOnce();
	}

	@Override
	protected void openItem(OpenItemEvent parameters) {
		if (parameters.panel() == null)
			return;

		if (openItemListeners.size() > 0) {
			openItemListeners.forEach(l -> l.accept(parameters));
			return;
		}

		super.openItem(parameters);
	}

	@Override
	public void createClusterGroup(ClusterGroup value) {
		List<Item> itemList = value.items().stream().map(itemId -> item(new String(Base64.getDecoder().decode(itemId)))).collect(toList());
		element().addGroupingGroup(value.cluster(), value.name(), itemList, username());
		sendCatalog();
	}

	private void refreshScope() {
		if (groupingSelectionMap.size() <= 0) {
			scope = null;
			return;
		}

		if (scope == null) scope = new Scope();
		groupingManager.clearFilter();
		scope.clear();
		scope.groups(groupingSelectionMap.entrySet().stream().filter(this::isGrouping).collect(toMap(Map.Entry::getKey, e -> groups(e.getValue()))));
		scope.objects(groupingSelectionMap.entrySet().stream().filter(this::isCluster).collect(toMap(Map.Entry::getKey, e -> objects(e.getValue()))));

		groupingSelectionMap.values().stream()
				.filter(g -> {
					Grouping grouping = groupingOf(g.name());
					return grouping != null && !grouping.cluster();
				})
				.forEach(selection -> {
					Grouping grouping = groupingOf(selection);
					if (grouping != null) groupingManager.filter(grouping.name(), selection.groups());
				});
	}

	private Grouping groupingOf(GroupingSelection selection) {
		Grouping grouping = groupingOf(selection.name());
		return grouping != null && !grouping.cluster() ? grouping : null;
	}

	private List<Group> groups(GroupingSelection selection) {
		GroupMap groupMap = groupingManager.groups(groupingOf(selection));
		return groupMap != null ? selection.groups().stream().map(groupMap::get).collect(toList()) : emptyList();
	}

	private List<Object> objects(GroupingSelection selection) {
		return selection.groups().stream().map(group -> objects(selection.name(), group)).flatMap(Collection::stream).collect(toList());
	}

	private List<Object> objects(String groupingName, String groupName) {
		Grouping grouping = groupingOf(groupingName);
		return objectsOf(grouping, groupName);
	}

	private List<Object> objectsOf(Grouping grouping, String groupLabel) {
		Group group = groupingManager.groups(grouping).values().stream().filter(g -> g.label().equals(groupLabel)).findFirst().orElse(null);
		return group != null ? group.objects() : emptyList();
	}

	private List<Grouping> groupings() {
		return element().groupings();
	}

	private Grouping groupingOf(String groupingName) {
		return element().groupings().stream().filter(g -> g.name().equals(groupingName)).findFirst().orElse(null);
	}

	private void createItemDisplay() {
		ItemDisplay display = new ItemDisplay(box);
		add(display);
		display.personifyOnce();
	}

}
