package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.builders.CatalogSortingBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ElementViewBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.builders.PictureDataBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogListViewNotifier;
import io.intino.konos.alexandria.activity.displays.providers.AlexandriaStampProvider;
import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.catalog.arrangement.Sorting;
import io.intino.konos.alexandria.activity.model.catalog.events.OpenPanel;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.*;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.spark.ActivityFile;
import io.intino.konos.alexandria.activity.utils.StreamUtil;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AlexandriaCatalogListView extends PageDisplay<AlexandriaCatalogListViewNotifier> implements AlexandriaCatalogView, AlexandriaStampProvider {
	private ElementViewDisplayProvider.Sorting sorting;
	private ElementView<Catalog> view;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<OpenItemCatalogEvent>> openItemCatalogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private String condition = null;
	private Map<String, List<AlexandriaStamp>> recordDisplaysMap = new HashMap<>();
	private Map<String, List<AlexandriaDialog>> recordDialogsMap = new HashMap<>();
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<String> lastSelection = new ArrayList<>();

	public AlexandriaCatalogListView(Box box) {
		super(box);
	}

	@Override
	public void view(ElementView view) {
		this.view = view;
	}

	@Override
	public void provider(CatalogViewDisplayProvider provider) {
		this.provider = provider;
	}

	public void selectSorting(io.intino.konos.alexandria.activity.schemas.Sorting sorting) {
		this.sorting = sortingOf(sorting);
		sendSelectedSorting();
		sendClear();
		page(0);
	}

	@Override
	public int countItems() {
		return provider.countItems(condition);
	}

	public void page(Integer value) {
		super.page(value);
	}

	@Override
	public void onOpenItem(Consumer<OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	@Override
	public void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	@Override
	public void onOpenItemCatalog(Consumer<OpenItemCatalogEvent> listener) {
		openItemCatalogListeners.add(listener);
	}

	@Override
	public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
	}

	@Override
	public void reset() {
	}

	@Override
	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	@Override
	public ElementView view() {
		return view;
	}

	public void filter(String value) {
		this.condition = value;
		this.refresh();
	}

	@Override
	public void refresh(io.intino.konos.alexandria.activity.schemas.Item... items) {
		Stream.of(items).forEach(this::refresh);
	}

	public void selectItems(String[] records) {
		List<String> newRecords = new ArrayList<>(Arrays.asList(records));
		newRecords.removeAll(lastSelection);
		newRecords.forEach(record -> {
			if (!recordDisplaysMap.containsKey(record)) renderDisplays(record);
			if (!recordDialogsMap.containsKey(record)) renderDialogs(record);
			renderExpandedPictures(record);
		});
		this.lastSelection = new ArrayList<>(Arrays.asList(records));
	}

	public void openItem(String value) {
		if (view.onClickRecordEvent() == null) {
			if (provider.expandedStamps(view.mold()).size() > 0)
				notifier.refreshSelection(lastSelection.contains(value) ? emptyList() : singletonList(value));
			return;
		}

		if (view.onClickRecordEvent().openPanel() != null)
			notifyOpenItem(value);
		else if (view.onClickRecordEvent().openCatalog() != null)
			notifyOpenCatalog(itemOf(value));
		else if (view.onClickRecordEvent().openDialog() != null)
			notifyOpenDialog(itemOf(value));
	}

	public void openElement(OpenElementParameters params) {
		Stamp stamp = provider.stamp(view.mold(), params.stamp().name());
		if (!(stamp instanceof CatalogLink)) return;

		CatalogLink catalogLinkStamp = (CatalogLink)stamp;
		AlexandriaAbstractCatalog display = provider.openElement(catalogLinkStamp.catalog().label());

		Item source = itemOf(params.item());
		if (display instanceof AlexandriaTemporalCatalog && provider.range() != null)
			((AlexandriaTemporalCatalog) display).selectRange(provider.range());

		if (catalogLinkStamp.openItemOnLoad()) display.openItem(catalogLinkStamp.item(source, session()));
		else {
			if (catalogLinkStamp.filtered())
				display.filterAndNotify(item -> catalogLinkStamp.filter(source, (Item) item, session()));
			display.refresh();
		}
	}

	private void notifyOpenItem(String item) {
		openItemListeners.forEach(l -> l.accept(openItemEventOf(item)));
	}

	private OpenItemEvent openItemEventOf(String item) {
		return new OpenItemEvent() {
			@Override
			public String itemId() {
				return new String(Base64.getDecoder().decode(item));
			}

			@Override
			public String label() {
				Optional<Stamp> titleStamp = provider.stamps(view.mold()).stream().filter(s -> (s instanceof Title)).findAny();
				return titleStamp.isPresent() ? ((Title)titleStamp.get()).value(item(), session()) : item().name();
			}

			@Override
			public Item item() {
				return provider.item(itemId());
			}

			@Override
			public Panel panel() {
				return view.onClickRecordEvent().openPanel().panel();
			}

			@Override
			public TimeRange range() {
				return provider.range();
			}

			@Override
			public Tree breadcrumbs() {
				OpenPanel openPanel = view.onClickRecordEvent().openPanel();
				return openPanel != null ? openPanel.breadcrumbs(item(), session()) : null;
			}
		};
	}

	private void notifyOpenCatalog(Item item) {
		openItemCatalogListeners.forEach(l -> l.accept(new OpenItemCatalogEvent() {
			@Override
			public Item sender() {
				return item;
			}

			@Override
			public Catalog catalog() {
				return view.onClickRecordEvent().openCatalog().catalog();
			}

			@Override
			public boolean filtered() {
				return view.onClickRecordEvent().openCatalog().filtered();
			}

			@Override
			public boolean filter(Item target) {
				return view.onClickRecordEvent().openCatalog().filter(item, target, session());
			}

			@Override
			public String itemToShow() {
				return view.onClickRecordEvent().openCatalog().item(item, session());
			}
		}));
	}

	private void notifyOpenDialog(Item item) {
		openItemDialogListeners.forEach(l -> l.accept(new OpenItemDialogEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public AlexandriaDialog dialog() {
				return view.onClickRecordEvent().openDialog().createDialog(item, session());
			}
		}));
	}

	public void renderExpandedPictures() {
		lastSelection.forEach(this::renderExpandedPictures);
	}

	private void renderDisplays(String item) {
		Map<EmbeddedDisplay, AlexandriaStamp> displays = displays();
		Item itemObject = provider.item(item);
		displays.forEach((stamp, display) -> {
			display.item(itemObject);
			display.provider(AlexandriaCatalogListView.this);
			add(display);
			display.personify(item + stamp.displayType());
			display.refresh();
		});
		recordDisplaysMap.put(item, new ArrayList<>(displays.values()));
	}

	private void renderDialogs(String item) {
		Map<EmbeddedDialog, AlexandriaDialog> dialogs = dialogs();
		Item itemObject = itemOf(item);
		dialogs.forEach((stamp, dialog) -> {
			dialog.target(itemObject);
			add(dialog);
			dialog.personify(item + stamp.dialogType());
			dialog.refresh();
		});
		recordDialogsMap.put(item, new ArrayList<>(dialogs.values()));
	}

	private void renderExpandedPictures(String item) {
		refreshPictures(item, expandedPictures(item));
	}

	private void refreshPictures(String item) {
		refreshPictures(item, allPictures(item));
	}

	private void refreshPictures(String itemId, List<Picture> pictures) {
		Item item = itemOf(itemId);
		pictures.forEach(stamp -> {
			InputStream stream = null;
			try {
				String name = stamp.name();
				Object data = stamp.value(item, session());
				if ((! (data instanceof List)) || ((List) data).size() != 1) return;
				List<URL> values = (List<URL>)data;
				stream = values.get(0).openStream();
				byte[] pictureBytes = IOUtils.toByteArray(stream);
				byte[] picture = Base64.getEncoder().encode(pictureBytes);
				notifier.refreshPicture(PictureDataBuilder.build(item, name, "data:image/png;base64," + new String(picture)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				StreamUtil.close(stream);
			}
		});
	}

	public void createClusterGroup(ClusterGroup value) {
		provider.createClusterGroup(value);
	}

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildList(provider.items(start, limit, condition, sorting), itemBuilderProvider(provider, view), baseAssetUrl()));
	}

	@Override
	protected void sendClear() {
		notifier.clear();
	}

	@Override
	protected void sendPageSize(int pageSize) {
		notifier.refreshPageSize(pageSize);
	}

	@Override
	protected void sendCount(int count) {
		notifier.refreshCount(count);
	}

	@Override
	protected void init() {
		super.init();

		List<Sorting> sortings = provider.sortings();
		this.sorting = sortings.size() > 0 ? sortingOf(sortings.get(0)) : null;

		sendView();
		sendSortingList(sortings);
		sendSelectedSorting();
	}

	@Override
	public void refresh() {
		notifyLoading(true);
		super.refresh();
		notifyLoading(false);
	}

	@Override
	public AlexandriaStamp embeddedDisplay(String name) {
		Stamp stamp = provider.stamp(view.mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedDisplay)) return null;
		return ((EmbeddedDisplay)stamp).createDisplay(session());
	}

	@Override
	public AlexandriaDialog embeddedDialog(String name) {
		Stamp stamp = provider.stamp(view.mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedDialog)) return null;
		return ((EmbeddedDialog)stamp).createDialog(session());
	}

	@Override
	public AlexandriaAbstractCatalog embeddedCatalog(String name) {
		Stamp stamp = provider.stamp(view.mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedCatalog)) return null;
		return ((EmbeddedCatalog)stamp).createCatalog(session());
	}

	private void sendView() {
		notifier.refreshView(ElementViewBuilder.build(view));
	}

	private void sendSortingList(List<Sorting> sortings) {
		notifier.refreshSortingList(CatalogSortingBuilder.buildList(sortings));
	}

	private void sendSelectedSorting() {
		if (sorting == null) return;
		notifier.refreshSelectedSorting(CatalogSortingBuilder.build(sorting));
	}

	private Map<EmbeddedDisplay, AlexandriaStamp> displays() {
		List<Stamp> stamps = provider.stamps(view.mold()).stream().filter(s -> (s instanceof EmbeddedDisplay)).collect(toList());
		Map<EmbeddedDisplay, AlexandriaStamp> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDisplay)s, s -> ((EmbeddedDisplay)s).createDisplay(session())));
		return nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map<EmbeddedDialog, AlexandriaDialog> dialogs() {
		List<Stamp> stamps = provider.stamps(view.mold()).stream().filter(s -> (s instanceof EmbeddedDialog)).collect(toList());
		Map<EmbeddedDialog, AlexandriaDialog> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDialog)s, s -> ((EmbeddedDialog)s).createDialog(session())));
		return nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<Picture> expandedPictures(String record) {
		return provider.expandedStamps(view.mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

	private List<Picture> allPictures(String record) {
		return provider.stamps(view.mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

	private ElementViewDisplayProvider.Sorting sortingOf(io.intino.konos.alexandria.activity.schemas.Sorting sorting) {
		return sortingOf(sorting.name(), sorting.mode());
	}

	private ElementViewDisplayProvider.Sorting sortingOf(Sorting sorting) {
		return sortingOf(sorting.name(), "Ascendant");
	}

	private ElementViewDisplayProvider.Sorting sortingOf(String name, String mode) {
		return new ElementViewDisplayProvider.Sorting() {
			@Override
			public String name() {
				return name;
			}

			@Override
			public Mode mode() {
				return Mode.valueOf(mode);
			}

			@Override
			public int comparator(Item item1, Item item2) {
				return provider.sorting(name).compare(item1, item2);
			}
		};
	}

	private void refresh(io.intino.konos.alexandria.activity.schemas.Item item) {
		notifier.refreshItem(item);
		if (recordDisplaysMap.containsKey(item.name()))
			recordDisplaysMap.get(item.name()).forEach(AlexandriaDisplay::refresh);
		if (recordDialogsMap.containsKey(item.name()))
			recordDialogsMap.get(item.name()).forEach(AlexandriaDisplay::refresh);
	}

	public void itemRefreshed(String record) {
		refreshPictures(record);
	}

	private void notifyLoading(boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	public void openItemDialogOperation(OpenItemDialogParameters params) {
		openItemDialogListeners.forEach(l -> l.accept(openItemDialogEvent(itemOf(params.item()), provider.stamp(view.mold(), params.stamp()), session())));
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskListeners.forEach(l -> l.accept(executeItemTaskEvent(itemOf(params.item()), provider.stamp(view.mold(), params.stamp()))));
	}

	public ActivityFile downloadItemOperation(DownloadItemParameters value) {
		return null;
	}

	public void executeOperation(ElementOperationParameters value) {
		provider.executeOperation(value, selectedItems());
	}

	public ActivityFile downloadOperation(ElementOperationParameters value) {
		Resource resource = provider.downloadOperation(value, selectedItems());
		return new ActivityFile() {
			@Override
			public String label() {
				return resource.label();
			}

			@Override
			public InputStream content() {
				return resource.content();
			}
		};
	}

	private List<Item> selectedItems() {
		return lastSelection.stream().map(this::itemOf).collect(toList());
	}

	public void saveItem(SaveItemParameters value) {
		if (selectedItems().size() != 1) return;
		provider.saveItem(value, selectedItems().get(0));
	}

	private Item itemOf(String id) {
		return provider.item(new String(Base64.getDecoder().decode(id)));
	}

}