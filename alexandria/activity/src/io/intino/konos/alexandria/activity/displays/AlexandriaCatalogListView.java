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
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
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
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private String condition = null;
	private Map<String, List<AlexandriaStamp>> recordDisplaysMap = new HashMap<>();
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
		else if (view.onClickRecordEvent().openDialog() != null)
			notifyOpenDialog(value);
	}

	private void notifyOpenItem(String item) {
		openItemListeners.forEach(l -> l.accept(new OpenItemEvent() {
			@Override
			public String itemId() {
				return item;
			}

			@Override
			public String label() {
				Optional<Stamp> titleStamp = provider.stamps(view.mold()).stream().filter(s -> (s instanceof Title)).findAny();
				return titleStamp.isPresent() ? ((Title)titleStamp.get()).value(item(), username()) : item().name();
			}

			@Override
			public Item item() {
				return provider.item(new String(Base64.getDecoder().decode(item)));
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
				return openPanel != null ? openPanel.breadcrumbs(item(), username()) : null;
			}
		}));
	}

	private void notifyOpenDialog(String item) {
		openItemDialogListeners.forEach(l -> l.accept(new OpenItemDialogEvent() {
			@Override
			public String item() {
				return item;
			}

			@Override
			public String path() {
				return view.onClickRecordEvent().openDialog().path(item);
			}

			@Override
			public int width() {
				return view.onClickRecordEvent().openDialog().width();
			}

			@Override
			public int height() {
				return view.onClickRecordEvent().openDialog().height();
			}
		}));
	}

	public void renderExpandedPictures() {
		lastSelection.forEach(this::renderExpandedPictures);
	}

	private void renderDisplays(String item) {
		Map<EmbeddedDisplay, AlexandriaStamp> displays = displays(item);
		displays.forEach((stamp, display) -> {
			add(display);
			display.personify(item + stamp.displayType());
			display.refresh();
		});
		recordDisplaysMap.put(item, new ArrayList<>(displays.values()));
	}

	private void renderExpandedPictures(String item) {
		refreshPictures(item, expandedPictures(item));
	}

	private void refreshPictures(String item) {
		refreshPictures(item, allPictures(item));
	}

	private void refreshPictures(String itemId, List<Picture> pictures) {
		Item item = provider.item(new String(Base64.getDecoder().decode(itemId)));
		pictures.forEach(stamp -> {
			try {
				String name = stamp.name();
				Object data = stamp.value(item, username());
				if ((! (data instanceof List)) || ((List) data).size() != 1) return;
				List<URL> values = (List<URL>)data;
				byte[] pictureBytes = IOUtils.toByteArray(values.get(0).openStream());
				byte[] picture = Base64.getEncoder().encode(pictureBytes);
				notifier.refreshPicture(PictureDataBuilder.build(item, name, "data:image/png;base64," + new String(picture)));
			} catch (IOException e) {
				e.printStackTrace();
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
		return ((EmbeddedDisplay)stamp).createDisplay(username());
	}

	@Override
	public AlexandriaCatalog embeddedCatalog(String name) {
		Stamp stamp = provider.stamp(view.mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedCatalog)) return null;
		return ((EmbeddedCatalog)stamp).display();
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

	private Map<EmbeddedDisplay, AlexandriaStamp> displays(String record) {
		List<Stamp> stamps = provider.stamps(view.mold()).stream().filter(s -> (s instanceof EmbeddedDisplay)).collect(toList());
		Map<EmbeddedDisplay, AlexandriaStamp> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDisplay)s, s -> ((EmbeddedDisplay)s).createDisplay(username())));
		Map<EmbeddedDisplay, AlexandriaStamp> result = nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
		result.forEach((key, value) -> {
			value.item(provider.item(record));
			value.provider(AlexandriaCatalogListView.this);
		});
		return result;
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
	}

	public void itemRefreshed(String record) {
		refreshPictures(record);
	}

	private void notifyLoading(boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	public void openItemDialogOperation(OpenItemDialogParameters params) {
		openItemDialogListeners.forEach(l -> l.accept(openItemDialogEvent(params.item(), provider.stamp(view.mold(), params.stamp()), username())));
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskListeners.forEach(l -> l.accept(executeItemTaskEvent(params.item(), provider.stamp(view.mold(), params.stamp()))));
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
		return lastSelection.stream().map(name -> provider.item(new String(Base64.getDecoder().decode(name)))).collect(toList());
	}

	public void saveItem(SaveItemParameters value) {
		if (selectedItems().size() != 1) return;
		provider.saveItem(value, selectedItems().get(0));
	}

}