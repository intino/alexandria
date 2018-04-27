package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.activity.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.activity.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.activity.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogMagazineViewNotifier;
import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.schemas.PictureData;
import io.intino.konos.alexandria.activity.spark.ActivityFile;

import java.util.List;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.itemDisplayProvider;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class AlexandriaCatalogMagazineView extends AlexandriaCatalogView<AlexandriaCatalogMagazineViewNotifier> {
	private Item item = null;
	private String condition = null;
	private String currentItem = null;

	public AlexandriaCatalogMagazineView(Box box) {
		super(box);
	}

	@Override
	public void reset() {
		currentItem = null;
	}

	@Override
	public List<io.intino.konos.alexandria.activity.model.Item> selectedItems() {
		return emptyList();
	}

	@Override
	public void refreshSelection(List<String> items) {
	}

	@Override
	protected void refreshPicture(PictureData data) {
	}

	@Override
	public void refresh(io.intino.konos.alexandria.activity.schemas.Item... items) {
		if (items.length <= 0) return;
		refresh(items[0]);
	}

	@Override
	public void refresh(io.intino.konos.alexandria.activity.schemas.Item item) {
		child(AlexandriaItem.class).refresh(item);
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.activity.schemas.Item item) {
		child(AlexandriaItem.class).refreshValidation(validationMessage, stamp, item);
	}

	@Override
	protected void init() {
		super.init();
		createRecordDisplay();
	}

	private void createRecordDisplay() {
		AlexandriaElementViewDefinition definition = definition();
		AlexandriaItem display = new AlexandriaItem(box);
		display.emptyMessage(definition.emptyMessage());
		display.mold(definition.mold());
		display.context(provider().element());
		display.item(null);
		display.mode("magazine");
		display.provider(itemDisplayProvider(provider(), definition));
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
		List<Item> items = provider.items(0, count, condition);
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

	public ActivityFile downloadOperation(ElementOperationParameters params) {
		return super.downloadOperation(params, singletonList(item));
	}

}