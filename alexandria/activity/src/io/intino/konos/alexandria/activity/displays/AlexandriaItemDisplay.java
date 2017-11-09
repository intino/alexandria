package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.builders.MoldBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaItemDisplayNotifier;
import io.intino.konos.alexandria.activity.displays.providers.ItemDisplayProvider;
import io.intino.konos.alexandria.activity.helpers.ElementHelper;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Mold;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.*;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.DownloadOperation;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.ExportOperation;
import io.intino.konos.alexandria.activity.model.mold.stamps.pages.ExternalPage;
import io.intino.konos.alexandria.activity.model.mold.stamps.pages.InternalPage;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.spark.ActivityFile;

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
	private Item item;
	private String mode;
	private ItemDisplayProvider provider;
	private List<Consumer<AlexandriaElementViewDisplay.OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementViewDisplay.OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementViewDisplay.ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
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

	public void item(Item item) {
		this.item = item;
	}

	public void mode(String mode) {
		this.mode = mode;
	}

	public void provider(ItemDisplayProvider provider) {
		this.provider = provider;
	}

	public void onOpenItem(Consumer<AlexandriaElementViewDisplay.OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<AlexandriaElementViewDisplay.OpenItemDialogEvent> parameters) {
		openItemDialogListeners.add(parameters);
	}

	public void onExecuteItemTask(Consumer<AlexandriaElementViewDisplay.ExecuteItemTaskEvent> parameters) {
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

	public void refresh(io.intino.konos.alexandria.activity.schemas.Item item) {
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
			display.filter(item -> key.filter(context, AlexandriaItemDisplay.this.item, (Item) item));
			display.label(key.label());
			display.onOpenItem(params -> notifyOpenItem((AlexandriaElementViewDisplay.OpenItemEvent) params));
			display.embedded(true);
			add(display);
			display.personifyOnce(id + key.name());
		});
		recordDisplaysCreated = true;
	}

	public void notifyOpenItem(AlexandriaElementViewDisplay.OpenItemEvent params) {
		openItemListeners.forEach(l -> l.accept(params));
	}

	public void selectElement(Reference reference) {
		Stamp stamp = provider.stamp(mold, reference.name());
		if (!(stamp instanceof CatalogLink)) return;

		CatalogLink catalogLinkStamp = (CatalogLink)stamp;
		AlexandriaElementDisplay display = provider.openElement(catalogLinkStamp.catalog().label());

		display.filterAndNotify(item -> catalogLinkStamp.filter(this.item, (Item)item));
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

	private void sendInfo(io.intino.konos.alexandria.activity.schemas.Item item) {
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

	private Map<Display, AlexandriaStampDisplay> displays(Item item) {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof Display).collect(toList());
		Map<Display, AlexandriaStampDisplay> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((Display)stamp, provider.display(stamp.name())), HashMap::putAll);
		Map<Display, AlexandriaStampDisplay> result = mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
		result.forEach((key, value) -> value.item(item));
		return result;
	}

	private List<AlexandriaDisplay> pages(Item item) {
		return provider.stamps(mold).stream().filter(s -> s instanceof Page)
				.map(stamp -> new AlexandriaPageContainerDisplay(box).pageLocation(location(stamp, item)))
				.collect(Collectors.toList());
	}

	private Map<EmbeddedCatalog, AlexandriaAbstractCatalogDisplay> embeddedCatalogs() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedCatalog).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (EmbeddedCatalog)s, s -> displayFor((EmbeddedCatalog)s)));
	}

	private PageLocation location(Stamp stamp, Item item) {
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
		openItemListeners.forEach(l -> l.accept(new AlexandriaElementViewDisplay.OpenItemEvent() {
			@Override
			public String itemId() {
				return reference.name();
			}

			@Override
			public String label() {
				return reference.label();
			}

			@Override
			public Item item() {
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