package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.events.OpenElementEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.ui.helpers.ElementHelper;
import io.intino.konos.alexandria.ui.model.*;
import io.intino.konos.alexandria.ui.model.catalog.Events;
import io.intino.konos.alexandria.ui.model.catalog.Scope;
import io.intino.konos.alexandria.ui.model.catalog.arrangement.Group;
import io.intino.konos.alexandria.ui.model.catalog.arrangement.GroupMap;
import io.intino.konos.alexandria.ui.model.catalog.arrangement.Grouping;
import io.intino.konos.alexandria.ui.model.catalog.arrangement.GroupingManager;
import io.intino.konos.alexandria.ui.model.catalog.events.OnClickRecord;
import io.intino.konos.alexandria.ui.model.catalog.views.GridView;
import io.intino.konos.alexandria.ui.model.catalog.views.ListView;
import io.intino.konos.alexandria.ui.model.catalog.views.MagazineView;
import io.intino.konos.alexandria.ui.model.catalog.views.MoldView;
import io.intino.konos.alexandria.ui.schemas.ClusterGroup;
import io.intino.konos.alexandria.ui.schemas.GroupingGroup;
import io.intino.konos.alexandria.ui.schemas.GroupingSelection;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public abstract class AlexandriaAbstractCatalog<E extends Catalog, DN extends AlexandriaDisplayNotifier> extends AlexandriaElementDisplay<E, DN> implements CatalogViewDisplayProvider {
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<OpenElementEvent>> openElementListeners = new ArrayList<>();
	private List<Consumer<List<Item>>> selectItemListeners = new ArrayList<>();
	private String condition = null;
	private String currentItem = null;
	protected Map<String, GroupingSelection> groupingSelectionMap = new HashMap<>();
	protected ItemList itemList = null;
	protected GroupingManager groupingManager;
	private String attachedGrouping = null;
	private int maxItems = -1;
	private boolean selectionEnabledByDefault = false;

	public AlexandriaAbstractCatalog(Box box) {
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
		child(AlexandriaCatalogViewList.class).selectView(name);
	}

	public void selectGrouping(GroupingSelection selection) {
		Grouping abstractGrouping = groupingOf(selection.name());

		if (selection.groups().size() <= 0 ||
				(abstractGrouping.histogram() == Grouping.Histogram.Absolute && selection.groups().size() <= 0)) {
			groupingSelectionMap.remove(selection.name());
			refreshGroupingsSelection();
		}
		else
			groupingSelectionMap.put(selection.name(), selection);

		dirty(true);
		filterGroupingManager();
		attachGrouping(selection.name());
	}

	public void attachGrouping(String grouping) {
		attachedGrouping = grouping;
		refreshGrouping();
	}

	public void detachGrouping(String grouping) {
		attachedGrouping = grouping;
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

	@Override
	public void forceRefresh() {
		super.forceRefresh();
		createGroupingManager();
		reloadGroupings();
	}

	public void refresh(Grouping grouping) {
		sendCatalog();
		currentView().ifPresent(AlexandriaElementView::refresh);
	}

	public void refreshViews() {
		child(AlexandriaCatalogViewList.class).refresh();
	}

	public int countItems(String condition) {
		this.updateCondition(condition);
		loadItemList(condition);
		return itemList.count();
	}

	@Override
	public List<Item> items(int start, int limit, String condition) {
		return items(start, limit, condition, null);
	}

	public List<Item> items(int start, int limit, String condition, Sorting sorting) {
		this.updateCondition(condition);
		loadItemList(condition);
		return itemList.items(start, limit, sorting);
	}

	@Override
	public Item rootItem(List<Item> itemList) {
		return element().rootItem(itemList, session());
	}

	@Override
	public Item defaultItem(String name) {
		return element().defaultItem(name, session());
	}

	@Override
	protected Item currentItem() {
		return item(currentItem);
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

		return item != null ? item : element().item(key, session());
	}

	@Override
	public List<io.intino.konos.alexandria.ui.model.catalog.arrangement.Sorting> sortings() {
		return element().sortings();
	}

	@Override
	public io.intino.konos.alexandria.ui.model.catalog.arrangement.Sorting sorting(String key) {
		return element().sortings().stream().filter(s -> s.name().equals(key) || s.label().equals(key)).findFirst().orElse(null);
	}

	public void onOpenItem(Consumer<OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	public void onOpenElement(Consumer<OpenElementEvent> listener) {
		openElementListeners.add(listener);
	}

	protected void refreshGrouping() {
		refreshView();
		refreshScope();
		sendCatalog();
	}

	protected void refreshScope() {
		currentView().ifPresent(v -> {
			if (v instanceof AlexandriaCatalogDisplayView)
				((AlexandriaCatalogDisplayView) v).refresh(scopeWithAttachedGrouping());
		});
	}

	@Override
	protected void init() {
		super.init();
		createDialogContainer();
		createGroupingManager();
		buildViewList();
		sendCatalog();
		createItemDisplay();
	}

	protected void createGroupingManager() {
		groupingManager = new GroupingManager(filteredItemList(defaultScope(),null).items(), groupings(), element().arrangementFilterer(session()));
	}

	protected AlexandriaElementViewDefinition<Catalog> catalogViewOf(View view) {
		return new AlexandriaElementViewDefinition<Catalog>() {
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
			public <V extends View> V raw() {
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
				return AlexandriaAbstractCatalog.this.onClickRecordEvent();
			}

			@Override
			public boolean canCreateClusters() {
				return AlexandriaAbstractCatalog.this.canCreateClusters();
			}

			@Override
			public boolean canSearch() {
				return AlexandriaAbstractCatalog.this.canSearch();
			}

			@Override
			public boolean selectionEnabledByDefault() {
				return AlexandriaAbstractCatalog.this.selectionEnabledByDefault();
			}

			@Override
			public List<String> clusters() {
				return element().groupings().stream().filter(Grouping::cluster).map(Grouping::label).collect(toList());
			}

			@Override
			public Item target() {
				return AlexandriaAbstractCatalog.this.target();
			}

			@Override
			public Catalog element() {
				return AlexandriaAbstractCatalog.this.element();
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

	public boolean selectionEnabledByDefault() {
		return selectionEnabledByDefault;
	}

	public AlexandriaAbstractCatalog selectionEnabledByDefault(boolean value) {
		selectionEnabledByDefault = value;
		return this;
	}

	protected abstract void sendCatalog();
	protected abstract ItemList filteredItemList(Scope scope, String condition);

	protected boolean canCreateClusters() {
		return element().groupings().size() > 0;
	}

	protected boolean isGrouping(Map.Entry<String, GroupingSelection> entry) {
		return !groupingOf(entry.getKey()).cluster();
	}

	protected boolean isCluster(Map.Entry<String, GroupingSelection> entry) {
		return groupingOf(entry.getKey()).cluster();
	}

	private List<AlexandriaElementViewDefinition> viewList() {
		return views().stream().map(this::catalogViewOf).collect(toList());
	}

	private boolean canSearch() {
		Toolbar toolbar = element().toolbar();
		return toolbar == null || toolbar.canSearch();
	}

	private void updateCondition(String condition) {
		if ((this.condition == null && condition != null && !condition.isEmpty()) || (this.condition != null && !this.condition.equals(condition)))
			dirty(true);
		this.condition = condition;
	}

	protected void loadItemList(String condition) {
		if (!dirty() && itemList != null) return;
		itemList = filteredItemList(scopeWithAttachedGrouping(), condition);
		dirty(false);
	}

	protected void refreshGroupingsSelection() {

		groupingSelectionMap.forEach((key, value) -> {
			List<String> newGroups = new ArrayList<>();
			GroupMap groupMap = groupingManager.groups(groupingOf(key));
			value.groups().forEach(groupLabel -> {
				String name = Group.name(groupLabel);
				if (groupMap.containsKey(name))
					newGroups.add(groupLabel);
			});
			value.groups(newGroups);
		});

		groupingSelectionMap = groupingSelectionMap.entrySet().stream().filter(e -> e.getValue().groups().size() > 0).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	protected Scope defaultScope() {
		return new Scope().target(target());
	}

	protected Scope scopeWithAttachedGrouping() {
		return calculateScope(true);
	}

	private void buildViewList() {
		AlexandriaCatalogViewList display = new AlexandriaCatalogViewList(box);
		display.itemProvider(this);
		display.viewList(viewList());
		display.onSelectView(this::updateCurrentView);
		display.onSelectItems(this::itemsSelected);
		display.onOpenItemDialog(this::openItemDialog);
		display.onOpenItemCatalog(this::openItemCatalog);
		display.onExecuteItemTask(this::executeItemTask);
		display.onLoading(this::notifyLoading);
		add(display);
		display.personifyOnce();
	}

	public void openView(String name) {
		child(AlexandriaCatalogViewList.class).selectView(name);
	}

	public void openItem(String value) {
		Optional<AlexandriaCatalogView> optionalView = currentView();
		if (!optionalView.isPresent()) return;

		AlexandriaCatalogView view = optionalView.get();
		AlexandriaElementViewDefinition definition = view.definition();
		OnClickRecord onClickRecord = onClickRecordEvent();
		Item item = item(new String(Base64.getDecoder().decode(value)));

		view.refreshSelection(singletonList(value));
		if (onClickRecord == null) return;

		if (onClickRecord.openPanel() != null)
			openItem(ElementHelper.openItemEvent(value, this, definition, session()));
		else if (onClickRecord.openCatalog() != null)
			openItemCatalog(ElementHelper.openItemCatalogEvent(item, this, definition, session()));
		else if (onClickRecord.openDialog() != null)
			openItemDialog(ElementHelper.openItemDialogEvent(item, this, definition, session()));
	}

	@Override
	protected void openItem(OpenItemEvent event) {
		if (event.panel() == null)
			return;

		if (openItemListeners.size() > 0) {
			openItemListeners.forEach(l -> l.accept(event));
			return;
		}

		super.openItem(event);
	}

	@Override
	public void createClusterGroup(ClusterGroup value) {
		// TODO MC
//		List<Item> itemList = value.items().stream().map(itemId -> item(new String(Base64.getDecoder().decode(itemId)))).collect(toList());
//		element().addGroupingGroup(value.cluster(), value.name(), itemList, user());
//		sendCatalog();
	}

	public void maxItems(int max) {
		element().mode(Catalog.Mode.Preview);
		this.maxItems = max;
	}

	public void onSelectItems(Consumer<List<Item>> listener) {
		selectItemListeners.add(listener);
	}

	private Scope calculateScope(boolean addAttachedGrouping) {
		Scope scope = defaultScope();

		if (groupingSelectionMap.size() <= 0)
			return scope;

		scope.groups(groupingSelectionMap.entrySet().stream().filter(this::isGrouping)
										 .filter(g -> attachedGroupingFilter(g.getValue(), addAttachedGrouping))
										 .collect(toMap(Map.Entry::getKey, e -> groups(e.getValue()))));

//		scope.objects(groupingSelectionMap.entrySet().stream().filter(this::isCluster)
//										  .filter(g -> attachedGroupingFilter(g.getValue(), addAttachedGrouping))
//										  .collect(toMap(Map.Entry::getKey, e -> objects(e.getValue()))));

		return scope;
	}

	protected void filterGroupingManager() {
		groupingManager.clearFilter();

		groupingSelectionMap.values().stream()
			.filter(g -> {
				Grouping grouping = groupingOf(g.name());
				return grouping != null && !grouping.cluster();
			})
			.forEach(selection -> {
				Grouping grouping = groupingOf(selection);
				if (grouping != null) groupingManager.filter(grouping.name(), groupsNames(selection.groups()));
			});

		sendCatalog();
	}

	@Override
	protected void applyFilter(ItemList itemList) {
		super.applyFilter(itemList);
		if (this.maxItems > 0) itemList.filterCount(maxItems);
	}

	private boolean attachedGroupingFilter(GroupingSelection groupingSelection, boolean addAttachedGrouping) {
		return addAttachedGrouping || !groupingSelection.name().equals(attachedGrouping);
	}

	private List<String> groupsNames(List<String> labels) {
		return labels.stream().map(Group::name).collect(toList());
	}

	private Grouping groupingOf(GroupingSelection selection) {
		Grouping grouping = groupingOf(selection.name());
		return grouping != null && !grouping.cluster() ? grouping : null;
	}

	private List<Group> groups(GroupingSelection selection) {
		GroupMap groupMap = groupingManager.groups(groupingOf(selection));
		return groupMap != null ? groupsNames(selection.groups()).stream().map(groupMap::get).collect(toList()) : emptyList();
	}

//	private List<Object> objects(GroupingSelection selection) {
//		return groupsNames(selection.groups()).stream().map(group -> objects(selection.name(), group)).flatMap(Collection::stream).collect(toList());
//	}

//	private List<Object> objects(String groupingName, String groupName) {
//		Grouping grouping = groupingOf(groupingName);
//		return objectsOf(grouping, groupName);
//	}

//	private List<Object> objectsOf(Grouping grouping, String groupLabel) {
//		Group group = groupingManager.groups(grouping).values().stream().filter(g -> g.label().equals(groupLabel)).findFirst().orElse(null);
//		return group != null ? group.objects() : emptyList();
//	}

	private List<Grouping> groupings() {
		return element().groupings();
	}

	private Grouping groupingOf(String groupingName) {
		return element().groupings().stream().filter(g -> g.name().equals(groupingName)).findFirst().orElse(null);
	}

	private void createItemDisplay() {
		AlexandriaItem display = new AlexandriaItem(box);
		add(display);
		display.personifyOnce();
	}

	protected void reloadGroupings() {
		sendCatalog();
	}

	private void itemsSelected(List<Item> selection) {
		selectItemListeners.forEach(l -> l.accept(selection));
	}

	private OnClickRecord onClickRecordEvent() {
		Events events = AlexandriaAbstractCatalog.this.element().events();
		return events != null ? events.onClickRecord() : null;
	}

}
