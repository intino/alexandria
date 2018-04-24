package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.*;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogListViewNotifier;
import io.intino.konos.alexandria.activity.displays.providers.AlexandriaStampProvider;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.catalog.arrangement.Sorting;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedCatalog;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedDialog;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedDisplay;
import io.intino.konos.alexandria.activity.model.mold.stamps.Picture;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.spark.ActivityFile;
import io.intino.konos.alexandria.activity.utils.StreamUtil;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.itemBuilderProvider;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AlexandriaCatalogListView extends AlexandriaCatalogPageDisplay<AlexandriaCatalogListViewNotifier> implements AlexandriaStampProvider {
	private ElementViewDisplayProvider.Sorting sorting;
	private String condition = null;
	private Map<String, List<AlexandriaStamp>> recordDisplaysMap = new HashMap<>();
	private Map<String, List<AlexandriaDialog>> recordDialogsMap = new HashMap<>();

	public AlexandriaCatalogListView(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	public void selectSorting(io.intino.konos.alexandria.activity.schemas.Sorting sorting) {
		this.sorting = sortingOf(sorting);
		sendSelectedSorting();
		sendClear();
		page(0);
	}

	@Override
	public int countItems() {
		return provider().countItems(condition);
	}

	public void page(Integer value) {
		super.page(value);
	}

	public void filter(String value) {
		this.condition = value;
		this.refresh();
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.activity.schemas.Item item) {
		notifier.refreshItemValidation(ItemValidationRefreshInfoBuilder.build(validationMessage, stamp, item));
	}

	public void selectItems(String[] records) {
		Arrays.stream(records).forEach(record -> {
			if (!recordDisplaysMap.containsKey(record)) renderDisplays(record);
			if (!recordDialogsMap.containsKey(record)) renderDialogs(record);
			renderExpandedPictures(record);
		});
		selection(new ArrayList<>(Arrays.asList(records)));
	}

	@Override
	public void refreshSelection(List<String> items) {
		AlexandriaElementViewDefinition definition = definition();
		if (definition.onClickRecordEvent() == null && provider().expandedStamps(definition.mold()).size() > 0)
			notifier.refreshSelection(selection().stream().allMatch(items::contains) ? emptyList() : items);
		selection(items);
	}

	public void openElement(OpenElementParameters params) {
		super.openElement(params);
	}

	public void renderExpandedPictures() {
		selection().forEach(this::renderExpandedPictures);
	}

	private void renderDisplays(String item) {
		Map<EmbeddedDisplay, AlexandriaStamp> displays = displays();
		Item itemObject = itemOf(item);
		displays.forEach((stamp, display) -> {
			display.item(itemObject);
			display.provider(AlexandriaCatalogListView.this);
			add(display);
			display.personifyOnce(item + stamp.displayType());
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
			dialog.personifyOnce(item + stamp.dialogType());
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
		provider().createClusterGroup(value);
	}

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildList(provider().items(start, limit, condition, sorting), itemBuilderProvider(provider(), definition()), baseAssetUrl()));
	}

	@Override
	protected void sendClear() {
		recordDisplaysMap.values().stream().flatMap(Collection::stream).forEach(this::removeChild);
		recordDialogsMap.values().stream().flatMap(Collection::stream).forEach(this::removeChild);
		recordDisplaysMap.clear();
		recordDialogsMap.clear();
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

		List<Sorting> sortings = provider().sortings();
		this.sorting = sortings.size() > 0 ? sortingOf(sortings.get(0)) : null;

		sendView();
		sendSortingList(sortings);
		sendSelectedSorting();
	}

	@Override
	public AlexandriaStamp embeddedDisplay(String name) {
		Stamp stamp = provider().stamp(definition().mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedDisplay)) return null;
		return ((EmbeddedDisplay)stamp).createDisplay(session());
	}

	@Override
	public AlexandriaDialog embeddedDialog(String name) {
		Stamp stamp = provider().stamp(definition().mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedDialog)) return null;
		return ((EmbeddedDialog)stamp).createDialog(session());
	}

	@Override
	public AlexandriaAbstractCatalog embeddedCatalog(String name) {
		Stamp stamp = provider().stamp(definition().mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedCatalog)) return null;
		return ((EmbeddedCatalog)stamp).createCatalog(session());
	}

	@Override
	public void refreshElement() {
		forceRefresh();
	}

	@Override
	public void refresh(io.intino.konos.alexandria.activity.schemas.Item item) {
		notifier.refreshItem(item);
		if (recordDisplaysMap.containsKey(item.name()))
			recordDisplaysMap.get(item.name()).forEach(AlexandriaDisplay::refresh);
		if (recordDialogsMap.containsKey(item.name()))
			recordDialogsMap.get(item.name()).forEach(AlexandriaDisplay::refresh);
	}

	@Override
	public void refreshItem() {
		ItemBuilder.ItemBuilderProvider provider = itemBuilderProvider(provider(), definition());

		selection().forEach(itemKey -> {
			Item item = itemOf(itemKey);
			refresh(ItemBuilder.build(item, item.id(), provider, baseAssetUrl()));
		});
	}

	private void sendView() {
		notifier.refreshView(ElementViewBuilder.build(definition()));
	}

	private void sendSortingList(List<Sorting> sortings) {
		notifier.refreshSortingList(CatalogSortingBuilder.buildList(sortings.stream().filter(Sorting::visible).collect(toList())));
	}

	private void sendSelectedSorting() {
		if (sorting == null) return;
		notifier.refreshSelectedSorting(CatalogSortingBuilder.build(sorting));
	}

	private Map<EmbeddedDisplay, AlexandriaStamp> displays() {
		List<Stamp> stamps = provider().stamps(definition().mold()).stream().filter(s -> (s instanceof EmbeddedDisplay)).collect(toList());
		Map<EmbeddedDisplay, AlexandriaStamp> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDisplay)s, s -> ((EmbeddedDisplay)s).createDisplay(session())));
		return nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map<EmbeddedDialog, AlexandriaDialog> dialogs() {
		List<Stamp> stamps = provider().stamps(definition().mold()).stream().filter(s -> (s instanceof EmbeddedDialog)).collect(toList());
		Map<EmbeddedDialog, AlexandriaDialog> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDialog)s, s -> ((EmbeddedDialog)s).createDialog(session())));
		return nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<Picture> expandedPictures(String record) {
		return provider().expandedStamps(definition().mold()).stream().filter(s -> (s instanceof Picture))
				.map(s -> (Picture)s)
				.collect(toList());
	}

	private List<Picture> allPictures(String record) {
		return provider().stamps(definition().mold()).stream().filter(s -> (s instanceof Picture))
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
				return provider().sorting(name).compare(item1, item2);
			}
		};
	}

	public void itemRefreshed(String record) {
		refreshPictures(record);
	}

	public void openItemDialogOperation(OpenItemParameters params) {
		super.openItemDialogOperation(params);
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		super.executeItemTaskOperation(params);
	}

	public ActivityFile downloadItemOperation(DownloadItemParameters params) {
		return super.downloadItemOperation(params);
	}

	public void executeOperation(ElementOperationParameters params) {
		super.executeOperation(params, selectedItems());
	}

	public ActivityFile downloadOperation(ElementOperationParameters params) {
		return super.downloadOperation(params, selectedItems());
	}

	public void changeItem(ChangeItemParameters params) {
		super.changeItem(params);
	}

	public void validateItem(io.intino.konos.alexandria.activity.schemas.ValidateItemParameters params) {
		super.validateItem(params);
	}

	public void openItemCatalogOperation(OpenItemParameters value) {

	}

}