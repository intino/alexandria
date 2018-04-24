package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.ElementViewBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ItemValidationRefreshInfoBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogMapViewNotifier;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.spark.ActivityFile;

import java.util.Base64;
import java.util.List;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.itemBuilderProvider;
import static java.util.Collections.emptyList;

public class AlexandriaCatalogMapView extends AlexandriaCatalogPageDisplay<AlexandriaCatalogMapViewNotifier> {

	public AlexandriaCatalogMapView(Box box) {
		super(box);
		pageSize(100000);
	}

	@Override
	public void reset() {
	}

	@Override
	public void refreshSelection(List<String> items) {
		AlexandriaElementViewDefinition definition = definition();
		if (definition.onClickRecordEvent() == null && provider().expandedStamps(definition.mold()).size() > 0)
			notifier.refreshSelection(selection().stream().allMatch(items::contains) ? emptyList() : items);
		selection(items);
	}

	@Override
	public int countItems() {
		return provider().countItems(null);
	}

	public void page(Integer value) {
		super.page(value);
	}

	public void location(Bounds value) {
		// TODO Mario
	}

	@Override
	protected void init() {
		super.init();
		sendView();
	}

	@Override
	public void refresh(Item item) {
		notifier.refreshItem(item);
	}

	@Override
	public void refreshValidation(String validationMessage, Stamp stamp, Item item) {
		notifier.refreshItemValidation(ItemValidationRefreshInfoBuilder.build(validationMessage, stamp, item));
	}

	public ActivityFile downloadItemOperation(DownloadItemParameters params) {
		return super.downloadItemOperation(params);
	}

	public void executeOperation(ElementOperationParameters params) {
		super.executeOperation(params, emptyList());
	}

	public ActivityFile downloadOperation(ElementOperationParameters value) {
		return super.downloadOperation(value, emptyList());
	}

	public void openElement(OpenElementParameters params) {
		super.openElement(params);
	}

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildListOnlyLocation(provider().items(start, limit, null), itemBuilderProvider(provider(), definition()), provider().baseAssetUrl()));
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

	private void sendView() {
		notifier.refreshView(ElementViewBuilder.build(definition()));
	}

	public void loadItem(String id) {
		String decodedId = new String(Base64.getDecoder().decode(id));
		io.intino.konos.alexandria.activity.model.Item modelItem = provider().item(decodedId);
		Item item = ItemBuilder.build(modelItem, modelItem.id(), itemBuilderProvider(provider(), definition()), provider().baseAssetUrl());
		notifier.refreshItem(item);
	}

	public void openItemDialogOperation(OpenItemParameters value) {

	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters value) {

	}

}