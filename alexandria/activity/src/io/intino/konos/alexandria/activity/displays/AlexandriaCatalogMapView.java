package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.ElementViewBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogMapViewNotifier;
import io.intino.konos.alexandria.activity.displays.providers.CatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Panel;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.catalog.events.OpenPanel;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.Title;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.spark.ActivityFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.intino.konos.alexandria.activity.helpers.ElementHelper.*;
import static java.util.Collections.emptyList;

public class AlexandriaCatalogMapView extends PageDisplay<AlexandriaCatalogMapViewNotifier> implements AlexandriaCatalogView {
	private ElementView view;
	private CatalogViewDisplayProvider provider;
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();

	public AlexandriaCatalogMapView(Box box) {
		super(box);
		pageSize(100000);
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
		openItemListeners.add(listener);
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

	public ActivityFile downloadItemOperation(DownloadItemParameters value) {
		return null;
	}

	public void executeOperation(ElementOperationParameters value) {
		provider.executeOperation(value, emptyList());
	}

	public ActivityFile downloadOperation(ElementOperationParameters value) {
		io.intino.konos.alexandria.activity.Resource resource = provider.downloadOperation(value, emptyList());
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

	public void openElement(OpenElementParameters params) {
	}

	@Override
	protected void sendItems(int start, int limit) {
		notifier.refresh(ItemBuilder.buildListOnlyLocation(provider.items(start, limit, null), itemBuilderProvider(provider, view), provider.baseAssetUrl()));
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

	private void notifyLoading(boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	public void loadItem(String id) {
		String decodedId = new String(Base64.getDecoder().decode(id));
		Item item = ItemBuilder.build(provider.item(decodedId), itemBuilderProvider(provider, view), provider.baseAssetUrl());
		notifier.refreshItem(item);
	}

	public void openItem(String value) {
		notifyOpenItem(value);
	}

	private void notifyOpenItem(String item) {
		openItemListeners.forEach(l -> l.accept(new OpenItemEvent() {
			@Override
			public String itemId() {
				return new String(Base64.getDecoder().decode(item));
			}

			@Override
			public String label() {
				Optional<Stamp> titleStamp = provider.stamps(view.mold()).stream().filter(s -> (s instanceof Title)).findAny();
				return titleStamp.isPresent() ? ((Title)titleStamp.get()).value(item(), username()) : item().name();
			}

			@Override
			public io.intino.konos.alexandria.activity.model.Item item() {
				return provider.item(itemId());
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

}