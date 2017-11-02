package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.foundation.activity.Resource;
import io.intino.alexandria.foundation.activity.displays.ActivityDisplay;
import io.intino.alexandria.foundation.activity.displays.Display;
import io.intino.alexandria.foundation.activity.spark.ActivityFile;
import io.intino.alexandria.framework.box.displays.builders.ItemBuilder;
import io.intino.alexandria.framework.box.displays.builders.MoldBuilder;
import io.intino.alexandria.framework.box.displays.notifiers.AlexandriaItemDisplayNotifier;
import io.intino.alexandria.framework.box.displays.providers.ItemDisplayProvider;
import io.intino.alexandria.framework.box.helpers.ElementHelper;
import io.intino.alexandria.framework.box.model.Element;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.TimeScale;
import io.intino.alexandria.framework.box.model.mold.Block;
import io.intino.alexandria.framework.box.model.Mold;
import io.intino.alexandria.framework.box.model.mold.Stamp;
import io.intino.alexandria.framework.box.model.mold.stamps.CatalogLink;
import io.intino.alexandria.framework.box.model.mold.stamps.EmbeddedCatalog;
import io.intino.alexandria.framework.box.model.mold.stamps.Page;
import io.intino.alexandria.framework.box.model.mold.stamps.Tree;
import io.intino.alexandria.framework.box.model.mold.stamps.operations.DownloadOperation;
import io.intino.alexandria.framework.box.model.mold.stamps.operations.ExportOperation;
import io.intino.alexandria.framework.box.model.mold.stamps.pages.ExternalPage;
import io.intino.alexandria.framework.box.model.mold.stamps.pages.InternalPage;
import io.intino.alexandria.framework.box.model.Panel;
import io.intino.alexandria.framework.box.schemas.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AlexandriaItemDisplay extends ActivityDisplay<AlexandriaItemDisplayNotifier> {
	private Mold mold;
	private Element context;
	private io.intino.alexandria.framework.box.model.Item item;
	private String mode;
	private ItemDisplayProvider provider;
	private List<Consumer<ElementViewDisplay.OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<ElementViewDisplay.OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<ElementViewDisplay.ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private boolean recordDisplaysCreated = false;
	private String emptyMessage = null;

	public AlexandriaItemDisplay(Box box) {
		super(box);
	}

	public void mold(Mold mold) {
		this.mold = mold;
	}

	public void context(Element context) {
		this.context = context;
	}

	public void item(io.intino.alexandria.framework.box.model.Item item) {
		this.item = item;
	}

	public void mode(String mode) {
		this.mode = mode;
	}

	public void provider(ItemDisplayProvider provider) {
		this.provider = provider;
	}

	public void onOpenItem(Consumer<ElementViewDisplay.OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<ElementViewDisplay.OpenItemDialogEvent> parameters) {
		openItemDialogListeners.add(parameters);
	}

	public void onExecuteItemTask(Consumer<ElementViewDisplay.ExecuteItemTaskEvent> parameters) {
		executeItemTaskListeners.add(parameters);
	}

	@Override
	protected void init() {
		super.init();
		sendEmptyMessage();
		sendMode();
	}

	@Override
	public void refresh() {
		super.refresh();
		children(AlexandriaStampDisplay.class).forEach(display -> {
			display.item(item);
			if (display instanceof AlexandriaTemporalStampDisplay) {
				AlexandriaTemporalStampDisplay temporalDisplay = (AlexandriaTemporalStampDisplay) display;
				temporalDisplay.range(provider.range());
				display.refresh();
			}
			display.refresh();
		});
		remove(AlexandriaPageContainerDisplay.class);
		sendInfo();
	}

	public void refresh(Item item) {
		sendInfo(item);
	}

	public void itemStampsReady(String id) {
		if (!recordDisplaysCreated)
			displays(item).forEach((key, display) -> {
				add(display);
				display.item(item);
				display.personifyOnce(id + key.name());
				display.refresh();
			});
		pages(item).forEach(display -> {
			add(display);
			display.personifyOnce(id);
			display.refresh();
		});
		embeddedCatalogs().forEach((key, display) -> {
			display.filter(item -> key.filter(context, AlexandriaItemDisplay.this.item, (io.intino.alexandria.framework.box.model.Item) item));
			display.label(key.label());
			display.onOpenItem(params -> notifyOpenItem((ElementViewDisplay.OpenItemEvent) params));
			display.embedded(true);
			add(display);
			display.personifyOnce(id + key.name());
		});
		recordDisplaysCreated = true;
	}

	public void notifyOpenItem(ElementViewDisplay.OpenItemEvent params) {
		openItemListeners.forEach(l -> l.accept(params));
	}

	public void selectElement(Reference reference) {
		Stamp stamp = provider.stamp(mold, reference.name());
		if (!(stamp instanceof CatalogLink)) return;

		CatalogLink catalogLinkStamp = (CatalogLink)stamp;
		AlexandriaElementDisplay display = provider.openElement(catalogLinkStamp.catalog().label());

		display.filterAndNotify(item -> catalogLinkStamp.filter(this.item, (io.intino.alexandria.framework.box.model.Item)item));
		if (display instanceof AlexandriaTemporalCatalogDisplay)
			((AlexandriaTemporalCatalogDisplay) display).selectRange(provider.range());

		display.refresh();
	}

	public void openItemDialogOperation(OpenItemDialogParameters params) {
		openItemDialogListeners.forEach(l -> l.accept(ElementHelper.openItemDialogEvent(params.item(), provider.stamp(mold, params.stamp()), username())));
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskListeners.forEach(l -> l.accept(ElementHelper.executeItemTaskEvent(params.item(), provider.stamp(mold, params.stamp()))));
	}

	public ActivityFile downloadItemOperation(DownloadItemParameters parameters) {
		Stamp stamp = provider.stamps(mold).stream().filter(s -> s.name().equals(parameters.stamp())).findFirst().orElse(null);
		if (stamp == null) return null;
		Resource resource = ((DownloadOperation)stamp).execute(item, parameters.option(), username());
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

	public ActivityFile exportItemOperation(ExportItemParameters parameters) {
		Stamp stamp = provider.stamps(mold).stream().filter(s -> s.name().equals(parameters.stamp())).findFirst().orElse(null);
		if (stamp == null) return null;
		Resource resource = ((ExportOperation)stamp).execute(item, parameters.from(), parameters.to(), parameters.option(), username());
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

	private void sendInfo(Item item) {
		notifier.refresh(new ItemRefreshInfo().mold(MoldBuilder.build(mold)).item(item));
	}

	private void sendInfo() {
		sendInfo(ItemBuilder.build(item, new ItemBuilder.ItemBuilderProvider() {
			@Override
			public List<Block> blocks() {
				return provider.blocks(mold);
			}

			@Override
			public List<Stamp> stamps() {
				return provider.stamps(mold);
			}

			@Override
			public String username() {
				return AlexandriaItemDisplay.this.username();
			}

			@Override
			public TimeScale scale() {
				return provider.range() != null ? provider.range().scale() : null;
			}
		}, baseAssetUrl()));
	}

	private void sendEmptyMessage() {
		if (emptyMessage == null) return;
		notifier.refreshEmptyMessage(emptyMessage);
	}

	private void sendMode() {
		notifier.refreshMode(mode);
	}

	private Map<io.intino.alexandria.framework.box.model.mold.stamps.Display, AlexandriaStampDisplay> displays(io.intino.alexandria.framework.box.model.Item item) {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof io.intino.alexandria.framework.box.model.mold.stamps.Display).collect(toList());
		Map<io.intino.alexandria.framework.box.model.mold.stamps.Display, AlexandriaStampDisplay> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((io.intino.alexandria.framework.box.model.mold.stamps.Display)stamp, provider.display(stamp.name())), HashMap::putAll);
		Map<io.intino.alexandria.framework.box.model.mold.stamps.Display, AlexandriaStampDisplay> result = mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
		result.forEach((key, value) -> value.item(item));
		return result;
	}

	private List<Display> pages(io.intino.alexandria.framework.box.model.Item item) {
		return provider.stamps(mold).stream().filter(s -> s instanceof Page)
				.map(stamp -> new AlexandriaPageContainerDisplay(box).pageLocation(location(stamp, item)))
				.collect(Collectors.toList());
	}

	private Map<EmbeddedCatalog, AlexandriaAbstractCatalogDisplay> embeddedCatalogs() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedCatalog).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (EmbeddedCatalog)s, s -> displayFor((EmbeddedCatalog)s)));
	}

	private PageLocation location(Stamp stamp, io.intino.alexandria.framework.box.model.Item item) {
		PageLocation location = new PageLocation();
		if (stamp instanceof ExternalPage) return location.value(((ExternalPage)stamp).url(item).toString()).internal(false);
		return location.value(((InternalPage)stamp).path(item)).internal(true);
	}

	private AlexandriaAbstractCatalogDisplay displayFor(EmbeddedCatalog stamp) {
		AlexandriaAbstractCatalogDisplay result = stamp.display();
		if (result == null) return null;
		result.target(item);
		result.catalog(stamp.catalog());
		return result;
	}

	public void selectItem(Reference reference) {
		openItemListeners.forEach(l -> l.accept(new ElementViewDisplay.OpenItemEvent() {
			@Override
			public String itemId() {
				return reference.name();
			}

			@Override
			public String label() {
				return reference.label();
			}

			@Override
			public io.intino.alexandria.framework.box.model.Item item() {
				return null;
			}

			@Override
			public Panel panel() {
				return (Panel) context;
			}

			@Override
			public TimeRange range() {
				return provider.range();
			}

			@Override
			public Tree breadcrumbs() {
				return null;
			}
		}));
	}

	public void saveItem(SaveItemParameters value) {
		provider.saveItem(value, item);
	}

	public void emptyMessage(String message) {
		this.emptyMessage = message;
	}
}