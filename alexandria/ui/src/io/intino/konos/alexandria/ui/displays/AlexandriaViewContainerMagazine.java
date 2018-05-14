package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaViewContainerMagazineNotifier;
import io.intino.konos.alexandria.ui.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.model.view.container.CollectionContainer;
import io.intino.konos.alexandria.ui.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.ui.schemas.PictureData;
import io.intino.konos.alexandria.ui.spark.UIFile;

import java.util.List;

import static io.intino.konos.alexandria.ui.helpers.ElementHelper.itemDisplayProvider;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


public class AlexandriaViewContainerMagazine extends AlexandriaViewContainerCollection<AlexandriaViewContainerMagazineNotifier> {
	private io.intino.konos.alexandria.ui.model.Item item = null;
	private String condition = null;
	private String currentItem = null;

	public AlexandriaViewContainerMagazine(Box box) {
		super(box);
	}

	@Override
	public void reset() {
		currentItem = null;
	}

	@Override
	public List<io.intino.konos.alexandria.ui.model.Item> selectedItems() {
		return emptyList();
	}

	@Override
	public void refreshSelection(List<String> items) {
	}

	@Override
	protected void refreshPicture(PictureData data) {
	}

	@Override
	public void refresh(io.intino.konos.alexandria.ui.schemas.Item... items) {
		if (items.length <= 0) return;
		refresh(items[0]);
	}

	@Override
	public void refresh(io.intino.konos.alexandria.ui.schemas.Item item) {
		child(AlexandriaItem.class).refresh(item);
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.ui.schemas.Item item) {
		child(AlexandriaItem.class).refreshValidation(validationMessage, stamp, item);
	}

	@Override
	protected void init() {
		super.init();
		createRecordDisplay();
	}

	private void createRecordDisplay() {
		CollectionContainer view = view().container();
		AlexandriaItem display = new AlexandriaItem(box);
		display.emptyMessage(view.noRecordsMessage());
		display.mold(view.mold());
		display.context(provider().element());
		display.item(null);
		display.mode("magazine");
		display.provider(itemDisplayProvider(provider(), view()));
		display.onOpenItem(this::selectRecord);
		display.onOpenItemDialog(this::openItemDialogOperation);
		display.onOpenItemCatalog(this::openItemCatalogOperation);
		display.onExecuteItemTask(this::executeItemTaskOperation);
		add(display);
		display.personifyOnce();
	}

	@Override
	public void refresh() {
		super.refresh();
		notifyLoading(true);
		loadRecord();
		if (item == null) return;
		AlexandriaItem itemDisplay = child(AlexandriaItem.class);
		itemDisplay.item(item);
		itemDisplay.refresh();
		notifyLoading(false);
	}

	public void filter(String value) {
		this.condition = value;
		this.loadRecord();
		this.refresh();
	}

	public void selectRecord(OpenItemEvent params) {
		currentItem = params.itemId();
		refresh();
	}

	private void loadRecord() {
		CatalogViewDisplayProvider provider = provider();
		int count = provider.countItems(condition);
		List<io.intino.konos.alexandria.ui.model.Item> items = provider.items(0, count, condition);
		item = currentItem != null ? provider.item(nameOf(currentItem)) : provider.rootItem(items);
		currentItem = item != null ? item.id() : currentItem;
		item = item != null ? item : provider.defaultItem(nameOf(currentItem));
	}

	private String nameOf(String currentRecord) {
		if (currentRecord == null) return null;
		String shortName = currentRecord.contains(".") ? currentRecord.substring(currentRecord.lastIndexOf(".") + 1) : currentRecord;
		shortName = shortName.contains("#") ? shortName.substring(shortName.lastIndexOf("#") + 1) : shortName;
		shortName = shortName.contains("$") ? shortName.substring(shortName.lastIndexOf("$") + 1) : shortName;
		return shortName;
	}

	public void openItemDialogOperation(OpenItemDialogEvent event) {
		super.openItemDialogOperation(event);
	}

	public void openItemCatalogOperation(OpenItemCatalogEvent event) {
		super.openItemCatalogOperation(event);
	}

	public void executeItemTaskOperation(ExecuteItemTaskEvent event) {
		super.executeItemTaskOperation(event);
	}

	public void executeOperation(ElementOperationParameters params) {
		super.executeOperation(params, singletonList(item));
	}

	public UIFile downloadOperation(ElementOperationParameters params) {
		return super.downloadOperation(params, singletonList(item));
	}
}