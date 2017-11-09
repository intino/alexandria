package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogMagazineViewDisplayNotifier;
import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.schemas.Item;
import io.intino.konos.alexandria.activity.spark.ActivityFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.itemDisplayProvider;
import static java.util.Collections.singletonList;

public class AlexandriaCatalogMagazineViewDisplay extends ActivityDisplay<AlexandriaCatalogMagazineViewDisplayNotifier> implements AlexandriaCatalogViewDisplay {
	private ElementView view;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private Item item = null;
	private String condition = null;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private String currentItem = null;

	public AlexandriaCatalogMagazineViewDisplay(Box box) {
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

	@Override
	public void onOpenItemDialog(Consumer<OpenItemDialogEvent> parameters) {
		openItemDialogListeners.add(parameters);
	}

	@Override
	public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> parameters) {
		executeItemTaskListeners.add(parameters);
	}

	@Override
	public void onOpenItem(Consumer<OpenItemEvent> listener) {
	}

	@Override
	public void reset() {
		currentItem = null;
	}

	@Override
	public void onLoading(Consumer<Boolean> listener) {
		this.loadingListeners.add(listener);
	}

	@Override
	public ElementView view() {
		return view;
	}

	@Override
	public void refresh(Item... items) {
		if (items.length <= 0) return;
		child(AlexandriaItemDisplay.class).refresh(items[0]);
	}

	@Override
	protected void init() {
		super.init();
		createRecordDisplay();
	}

	private void createRecordDisplay() {
		AlexandriaItemDisplay display = new AlexandriaItemDisplay(box);
		display.emptyMessage(view.emptyMessage());
		display.mold(view.mold());
		display.context(provider.element());
		display.item(null);
		display.mode("magazine");
		display.provider(itemDisplayProvider(provider, view));
		display.onOpenItem(this::selectRecord);
		display.onOpenItemDialog(this::openItemDialogOperation);
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
		AlexandriaItemDisplay itemDisplay = child(AlexandriaItemDisplay.class);
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

	private void notifyLoading(boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	public void openItemDialogOperation(OpenItemDialogEvent event) {
		openItemDialogListeners.forEach(l -> l.accept(event));
	}

	public void executeItemTaskOperation(ExecuteItemTaskEvent event) {
		executeItemTaskListeners.forEach(l -> l.accept(event));
	}

	public void executeOperation(ElementOperationParameters value) {
		provider.executeOperation(value, singletonList(item));
	}

	public ActivityFile downloadOperation(ElementOperationParameters value) {
		Resource resource = provider.downloadOperation(value, singletonList(item));
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

}