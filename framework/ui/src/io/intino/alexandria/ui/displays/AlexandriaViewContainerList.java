package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.builders.CatalogSortingBuilder;
import io.intino.alexandria.ui.displays.builders.ElementViewBuilder;
import io.intino.alexandria.ui.displays.builders.ItemBuilder;
import io.intino.alexandria.ui.displays.builders.ItemValidationRefreshInfoBuilder;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaViewContainerListNotifier;
import io.intino.alexandria.ui.displays.providers.AlexandriaStampProvider;
import io.intino.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.catalog.arrangement.Sorting;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.model.mold.stamps.EmbeddedCatalog;
import io.intino.alexandria.ui.model.mold.stamps.EmbeddedDialog;
import io.intino.alexandria.ui.model.mold.stamps.EmbeddedDisplay;
import io.intino.alexandria.ui.schemas.*;
import io.intino.alexandria.ui.spark.UIFile;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.alexandria.ui.helpers.ElementHelper.itemBuilderProvider;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AlexandriaViewContainerList extends AlexandriaViewContainerCollectionPage<AlexandriaViewContainerListNotifier> implements AlexandriaStampProvider {
	private ElementViewDisplayProvider.Sorting sorting;
	private String condition = null;
	private Map<String, List<AlexandriaStamp>> recordDisplaysMap = new HashMap<>();
	private Map<String, List<AlexandriaDialog>> recordDialogsMap = new HashMap<>();

	public AlexandriaViewContainerList(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	public void selectSorting(io.intino.alexandria.ui.schemas.Sorting sorting) {
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
	public void refreshValidation(String validationMessage, Stamp stamp, io.intino.alexandria.ui.schemas.Item item) {
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
		View view = view();
		Catalog catalog = (Catalog) provider().element();
		if ((catalog.events() == null || catalog.events().onClickItem() == null) && provider().expandedStamps(view.mold()).size() > 0) {
			List<String> newSelection = new ArrayList<>(items);
			newSelection.removeAll(selection());
			notifier.refreshSelection(newSelection);
		}
		selection(items);
	}

	@Override
	protected void refreshPicture(PictureData data) {
		notifier.refreshPicture(data);
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
			display.provider(AlexandriaViewContainerList.this);
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

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildList(provider().items(start, limit, condition, sorting), itemBuilderProvider(provider(), view()), baseAssetUrl()));
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
	protected void notifyNearToEnd() {
		provider().loadMoreItems(condition, sorting, PageSize+Offset);
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
		Stamp stamp = provider().stamp(view().mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedDisplay)) return null;
		return ((EmbeddedDisplay)stamp).createDisplay(session());
	}

	@Override
	public AlexandriaDialog embeddedDialog(String name) {
		Stamp stamp = provider().stamp(view().mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedDialog)) return null;
		return ((EmbeddedDialog)stamp).createDialog(session());
	}

	@Override
	public AlexandriaAbstractCatalog embeddedCatalog(String name) {
		Stamp stamp = provider().stamp(view().mold(), name);
		if (stamp == null || !(stamp instanceof EmbeddedCatalog)) return null;
		return ((EmbeddedCatalog)stamp).createCatalog(session());
	}

	@Override
	public void refreshElement() {
		forceRefresh();
	}

	@Override
	public void refresh(io.intino.alexandria.ui.schemas.Item item) {
		refresh(item, true);
	}

	@Override
	public void refresh(io.intino.alexandria.ui.schemas.Item item, boolean highlight) {
		notifier.refreshItem(new ItemRefreshInfo().item(item).highlight(highlight));
		if (recordDisplaysMap.containsKey(item.name()))
			recordDisplaysMap.get(item.name()).forEach(AlexandriaDisplay::refresh);
		if (recordDialogsMap.containsKey(item.name()))
			recordDialogsMap.get(item.name()).forEach(AlexandriaDisplay::refresh);
	}

	@Override
	public void refreshItem() {
		refreshItem(true);
	}

	@Override
	public void refreshItem(boolean highlight) {
		ItemBuilder.ItemBuilderProvider provider = itemBuilderProvider(provider(), view());

		selection().forEach(itemKey -> {
			Item item = itemOf(itemKey);
			refresh(ItemBuilder.build(item, item.id(), provider, baseAssetUrl()), highlight);
		});
	}

	@Override
	public void resizeItem() {
		ItemBuilder.ItemBuilderProvider provider = itemBuilderProvider(provider(), view());

		selection().forEach(itemKey -> {
			Item item = itemOf(itemKey);
			notifier.resizeItem(ItemBuilder.build(item, item.id(), provider, baseAssetUrl()));
		});
	}

	private void sendView() {
		notifier.refreshView(ElementViewBuilder.build(view(), provider()));
	}

	private void sendSortingList(List<Sorting> sortings) {
		notifier.refreshSortingList(CatalogSortingBuilder.buildList(sortings.stream().filter(Sorting::visible).collect(toList())));
	}

	private void sendSelectedSorting() {
		if (sorting == null) return;
		notifier.refreshSelectedSorting(CatalogSortingBuilder.build(sorting));
	}

	private Map<EmbeddedDisplay, AlexandriaStamp> displays() {
		List<Stamp> stamps = provider().stamps(view().mold()).stream().filter(s -> (s instanceof EmbeddedDisplay)).collect(toList());
		Map<EmbeddedDisplay, AlexandriaStamp> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDisplay)s, s -> ((EmbeddedDisplay)s).createDisplay(session())));
		return nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map<EmbeddedDialog, AlexandriaDialog> dialogs() {
		List<Stamp> stamps = provider().stamps(view().mold()).stream().filter(s -> (s instanceof EmbeddedDialog)).collect(toList());
		Map<EmbeddedDialog, AlexandriaDialog> nullableMap = stamps.stream().collect(Collectors.toMap(s -> (EmbeddedDialog)s, s -> ((EmbeddedDialog)s).createDialog(session())));
		return nullableMap.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private ElementViewDisplayProvider.Sorting sortingOf(io.intino.alexandria.ui.schemas.Sorting sorting) {
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
			public ElementViewDisplayProvider.Sorting.Mode mode() {
				return Mode.valueOf(mode);
			}

			@Override
			public int comparator(Item item1, Item item2) {
				return provider().sorting(name).compare(item1, item2, session());
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

	public UIFile downloadItemOperation(DownloadItemParameters params) {
		return super.downloadItemOperation(params);
	}

	public void executeOperation(ElementOperationParameters params) {
		super.executeOperation(params, selectedItems());
	}

	public UIFile downloadOperation(ElementOperationParameters params) {
		return super.downloadOperation(params, selectedItems());
	}

	public void changeItem(ChangeItemParameters params) {
		super.changeItem(params);
	}

	public void validateItem(io.intino.alexandria.ui.schemas.ValidateItemParameters params) {
		super.validateItem(params);
	}

	public void openItemCatalogOperation(OpenItemParameters params) {
		super.openItemCatalogOperation(params);
	}

}