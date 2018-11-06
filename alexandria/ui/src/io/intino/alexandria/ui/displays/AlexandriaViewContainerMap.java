package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.builders.ElementViewBuilder;
import io.intino.alexandria.ui.displays.builders.ItemBuilder;
import io.intino.alexandria.ui.displays.builders.ItemValidationRefreshInfoBuilder;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaViewContainerMapNotifier;
import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.schemas.*;
import io.intino.alexandria.ui.spark.UIFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static io.intino.alexandria.ui.helpers.ElementHelper.itemBuilderProvider;
import static java.util.Collections.emptyList;


public class AlexandriaViewContainerMap extends AlexandriaViewContainerCollectionPage<AlexandriaViewContainerMapNotifier> {

	public AlexandriaViewContainerMap(Box box) {
		super(box);
		pageSize(100000);
	}

	@Override
	public void reset() {
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
		refresh(item, true);
	}

	@Override
	public void refresh(Item item, boolean highlight) {
		notifier.refreshItem(new ItemRefreshInfo().item(item).highlight(highlight));
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

	@Override
	protected void notifyNearToEnd() {
		provider().loadMoreItems(null, null, PageSize);
	}

	private void sendView() {
		notifier.refreshView(ElementViewBuilder.build(view(), provider()));
	}

	public void loadItem(String id) {
		String decodedId = new String(Base64.getDecoder().decode(id));
		io.intino.alexandria.ui.model.Item modelItem = provider().item(decodedId);
		Item item = ItemBuilder.build(modelItem, modelItem.id(), itemBuilderProvider(provider(), view()), provider().baseAssetUrl());
		notifier.refreshItem(new ItemRefreshInfo().item(item).highlight(true));
		renderExpandedPictures(id);
	}

	public void openItemDialogOperation(OpenItemParameters params) {
		super.openItemDialogOperation(params);
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		super.executeItemTaskOperation(params);
	}

}