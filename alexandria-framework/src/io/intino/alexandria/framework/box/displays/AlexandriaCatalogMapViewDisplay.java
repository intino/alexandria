package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.foundation.activity.Resource;
import io.intino.alexandria.foundation.activity.displays.PageDisplay;
import io.intino.alexandria.foundation.activity.spark.ActivityFile;
import io.intino.alexandria.framework.box.displays.builders.ElementViewBuilder;
import io.intino.alexandria.framework.box.displays.builders.ItemBuilder;
import io.intino.alexandria.framework.box.displays.notifiers.AlexandriaCatalogMapViewDisplayNotifier;
import io.intino.alexandria.framework.box.displays.providers.CatalogViewDisplayProvider;
import io.intino.alexandria.framework.box.schemas.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.intino.alexandria.framework.box.helpers.ElementHelper.*;
import static java.util.Collections.emptyList;

public class AlexandriaCatalogMapViewDisplay extends PageDisplay<AlexandriaCatalogMapViewDisplayNotifier> implements AlexandriaCatalogViewDisplay {
	private ElementView view;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();

	public AlexandriaCatalogMapViewDisplay(Box box) {
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
	public void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	@Override
	public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
	}


	@Override
	public void onOpenItem(Consumer<OpenItemEvent> listener) {
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

	@Override
	public int countItems() {
		return provider.countItems(null);
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
	public void refresh() {
		notifyLoading(true);
		super.refresh();
		notifyLoading(false);
	}

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildList(provider.items(start, limit, null), itemBuilderProvider(provider, view), provider.baseAssetUrl()));
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
		notifier.refreshView(ElementViewBuilder.build(view));
	}

	public void openItemDialogOperation(OpenItemDialogParameters params) {
		openItemDialogListeners.forEach(l -> l.accept(openItemDialogEvent(params.item(), provider.stamp(view.mold(), params.stamp()), username())));
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskListeners.forEach(l -> l.accept(executeItemTaskEvent(params.item(), provider.stamp(view.mold(), params.stamp()))));
	}

	@Override
	public void refresh(Item... items) {
		Stream.of(items).forEach(item -> notifier.refreshItem(item));
	}

	private void notifyLoading(boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	public ActivityFile downloadItemOperation(DownloadItemParameters value) {
		return null;
	}

	public void executeOperation(ElementOperationParameters value) {
		provider.executeOperation(value, emptyList());
	}

	public ActivityFile downloadOperation(ElementOperationParameters value) {
		Resource resource = provider.downloadOperation(value, emptyList());
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