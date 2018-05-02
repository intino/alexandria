package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.ElementViewBuilder;
import io.intino.konos.alexandria.ui.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.ui.displays.builders.ItemValidationRefreshInfoBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaCatalogMapViewNotifier;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.schemas.*;
import io.intino.konos.alexandria.ui.spark.UIFile;

import java.util.Base64;
import java.util.List;

import static io.intino.konos.alexandria.ui.helpers.ElementHelper.itemBuilderProvider;
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
		io.intino.konos.alexandria.ui.model.Catalog catalog = (io.intino.konos.alexandria.ui.model.Catalog) provider().element();
		if ((catalog.events() == null || catalog.events().onClickItem() == null) && provider().expandedStamps(view().mold()).size() > 0)
			notifier.refreshSelection(selection().stream().allMatch(items::contains) ? emptyList() : items);
		selection(items);
	}

	@Override
	protected void refreshPicture(PictureData data) {
		notifier.refreshPicture(data);
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

	public UIFile downloadItemOperation(DownloadItemParameters params) {
		return super.downloadItemOperation(params);
	}

	public void executeOperation(ElementOperationParameters params) {
		super.executeOperation(params, emptyList());
	}

	public UIFile downloadOperation(ElementOperationParameters value) {
		return super.downloadOperation(value, emptyList());
	}

	public void openElement(OpenElementParameters params) {
		super.openElement(params);
	}

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildListOnlyLocation(provider().items(start, limit, null), itemBuilderProvider(provider(), view()), provider().baseAssetUrl()));
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
		notifier.refreshView(ElementViewBuilder.build(view(), provider()));
	}

	public void loadItem(String id) {
		String decodedId = new String(Base64.getDecoder().decode(id));
		io.intino.konos.alexandria.ui.model.Item modelItem = provider().item(decodedId);
		Item item = ItemBuilder.build(modelItem, modelItem.id(), itemBuilderProvider(provider(), view()), provider().baseAssetUrl());
		notifier.refreshItem(item);
		renderExpandedPictures(id);
	}

	public void openItemDialogOperation(OpenItemParameters params) {
		super.openItemDialogOperation(params);
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		super.executeItemTaskOperation(params);
	}

}