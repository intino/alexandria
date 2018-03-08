package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaItemNotifier;
import io.intino.konos.alexandria.activity.displays.providers.AlexandriaStampProvider;
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
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.spark.ActivityFile;

import java.io.InputStream;
import java.util.*;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class AlexandriaItem extends ActivityDisplay<AlexandriaItemNotifier, Box> implements AlexandriaStampProvider {
	private Mold mold;
	private Element context;
	private Item item;
	private String mode;
	private ItemDisplayProvider provider;
	private List<Consumer<AlexandriaElementView.OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementView.OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<AlexandriaElementView.ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
	private boolean initialized = false;
	private String emptyMessage = null;

	public AlexandriaItem(Box box) {
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

	public void onOpenItem(Consumer<AlexandriaElementView.OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<AlexandriaElementView.OpenItemDialogEvent> parameters) {
		openItemDialogListeners.add(parameters);
	}

	public void onExecuteItemTask(Consumer<AlexandriaElementView.ExecuteItemTaskEvent> parameters) {
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
		children(AlexandriaStamp.class).forEach(display -> {
			display.item(item);
			display.provider(AlexandriaItem.this);
			updateRange(display);
			display.refresh();
		});
		remove(AlexandriaPageContainer.class);
		sendInfo();
	}

	public void refresh(io.intino.konos.alexandria.activity.schemas.Item item) {
		sendInfo(item);
	}

	public void itemStampsReady(String id) {
		if (!initialized) {
			createEmbeddedDisplays(id);
			initialized = true;
		}
		createPages(id);
	}

	public void notifyOpenItem(AlexandriaElementView.OpenItemEvent params) {
		openItemListeners.forEach(l -> l.accept(params));
	}

	public void openElement(OpenElementParameters params) {
		Stamp stamp = provider.stamp(mold, params.stamp().name());
		if (!(stamp instanceof CatalogLink)) return;

		CatalogLink catalogLinkStamp = (CatalogLink)stamp;
		AlexandriaAbstractCatalog display = provider.openElement(catalogLinkStamp.catalog().label());
		display.target(this.item);

		if (display instanceof AlexandriaTemporalCatalog && provider.range() != null)
			((AlexandriaTemporalCatalog) display).selectRange(provider.range());

		if (catalogLinkStamp.openItemOnLoad()) display.openItem(catalogLinkStamp.item(this.item, session()));
		else {
			if (catalogLinkStamp.filtered())
				display.filterAndNotify(item -> catalogLinkStamp.filter(this.item, (Item) item, session()));
			display.refresh();
		}
	}

	public void openItemDialogOperation(OpenItemDialogParameters params) {
		openItemDialogListeners.forEach(l -> l.accept(ElementHelper.openItemDialogEvent(itemOf(params.item()), provider.stamp(mold, params.stamp()), session())));
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskListeners.forEach(l -> l.accept(ElementHelper.executeItemTaskEvent(itemOf(params.item()), provider.stamp(mold, params.stamp()), this)));
	}

	public ActivityFile downloadItemOperation(DownloadItemParameters parameters) {
		Stamp stamp = provider.stamps(mold).stream().filter(s -> s.name().equals(parameters.stamp())).findFirst().orElse(null);
		if (stamp == null) return null;
		Resource resource = ((DownloadOperation)stamp).execute(item, parameters.option(), session());
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
		Resource resource = ((ExportOperation)stamp).execute(item, parameters.from(), parameters.to(), parameters.option(), session());
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

	@Override
	public AlexandriaStamp embeddedDisplay(String name) {
		Stamp stamp = provider.stamp(mold, name);
		if (stamp == null || !(stamp instanceof EmbeddedDisplay)) return null;
		return ((EmbeddedDisplay)stamp).createDisplay(session());
	}

	@Override
	public AlexandriaDialog embeddedDialog(String name) {
		Stamp stamp = provider.stamp(mold, name);
		if (stamp == null || !(stamp instanceof EmbeddedDialog)) return null;
		return ((EmbeddedDialog)stamp).createDialog(session());
	}

	@Override
	public AlexandriaAbstractCatalog embeddedCatalog(String name) {
		Stamp stamp = provider.stamp(mold, name);
		if (stamp == null || !(stamp instanceof EmbeddedCatalog)) return null;
		return ((EmbeddedCatalog)stamp).createCatalog(session());
	}

	public void saveItem(SaveItemParameters value) {
		provider.saveItem(value, item);
	}

	public void emptyMessage(String message) {
		this.emptyMessage = message;
	}

	public void openItem(Reference reference) {
		openItemListeners.forEach(l -> l.accept(new AlexandriaElementView.OpenItemEvent() {
			@Override
			public String itemId() {
				return new String(Base64.getDecoder().decode(reference.name()));
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

	private void sendInfo(io.intino.konos.alexandria.activity.schemas.Item item) {
		notifier.refresh(new ItemRefreshInfo().mold(mold.type()).item(item));
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
			public ActivitySession session() {
				return AlexandriaItem.this.session();
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

	private PageLocation location(Stamp stamp, Item item) {
		PageLocation location = new PageLocation();
		if (stamp instanceof ExternalPage) return location.value(((ExternalPage)stamp).url(item).toString()).internal(false);
		return location.value(((InternalPage)stamp).path(item)).internal(true);
	}

	private AlexandriaAbstractCatalog displayFor(EmbeddedCatalog stamp) {
		AlexandriaAbstractCatalog result = stamp.createCatalog(session());
		if (result == null) return null;
		result.target(item);
		return result;
	}

	private void updateRange(AlexandriaStamp display) {
		if (!(display instanceof AlexandriaTemporalStamp)) return;
		AlexandriaTemporalStamp temporalDisplay = (AlexandriaTemporalStamp) display;
		temporalDisplay.range(provider.range());
	}

	private Item itemOf(String item) {
		return provider.item(new String(Base64.getDecoder().decode(item)));
	}

	private void createEmbeddedDisplays(String id) {
		createEmbeddedCustomDisplays(id);
		createEmbeddedDialogs(id);
		createEmbeddedCatalogs(id);
		createTemporalCatalogNavigators(id);
	}

	private void createEmbeddedCustomDisplays(String id) {
		embeddedCustomDisplays().forEach((key, display) -> {
			display.item(item);
			display.provider(AlexandriaItem.this);
			add(display);
			display.personifyOnce(id + key.displayType());
			updateRange(display);
			display.refresh();
		});
	}

	private Map<EmbeddedDisplay, AlexandriaStamp> embeddedCustomDisplays() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedDisplay).collect(toList());
		Map<EmbeddedDisplay, AlexandriaStamp> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((EmbeddedDisplay)stamp, ((EmbeddedDisplay)stamp).createDisplay(session())), HashMap::putAll);
		return mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private void createEmbeddedDialogs(String id) {
		embeddedDialogs().forEach((key, dialog) -> {
			dialog.target(item);
			add(dialog);
			dialog.personifyOnce(id + key.dialogType());
			dialog.refresh();
		});
	}

	private Map<EmbeddedDialog, AlexandriaDialog> embeddedDialogs() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedDialog).collect(toList());
		Map<EmbeddedDialog, AlexandriaDialog> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((EmbeddedDialog)stamp, ((EmbeddedDialog)stamp).createDialog(session())), HashMap::putAll);
		return mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private void createEmbeddedCatalogs(String id) {
		embeddedCatalogs().forEach((key, display) -> {
			display.staticFilter(item -> key.filter(context, AlexandriaItem.this.item, (Item) item, session()));
			display.label(key.label());
			display.range(provider.range());
			display.onOpenItem(params -> notifyOpenItem((AlexandriaElementView.OpenItemEvent) params));
			display.embedded(true);
			add(display);
			display.personifyOnce(id + key.name());
		});
	}

	private Map<EmbeddedCatalog, AlexandriaAbstractCatalog> embeddedCatalogs() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedCatalog).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (EmbeddedCatalog)s, s -> displayFor((EmbeddedCatalog)s)));
	}

	private void createPages(String id) {
		pages(item).forEach(display -> {
			add(display);
			display.personifyOnce(id);
			display.refresh();
		});
	}

	private List<AlexandriaDisplay> pages(Item item) {
		return provider.stamps(mold).stream().filter(s -> s instanceof Page)
				.map(stamp -> new AlexandriaPageContainer(box).pageLocation(location(stamp, item)))
				.collect(Collectors.toList());
	}

	private void createTemporalCatalogNavigators(String id) {
		catalogTimeRanges().forEach((key, display) -> createTemporalCatalogNavigator(id, key.name(), display));
		catalogTimeRangeNavigators().forEach((key, display) -> createTemporalCatalogNavigator(id, key.name(), display));
		catalogTimes().forEach((key, display) -> createTemporalCatalogNavigator(id, key.name(), display));
		catalogTimeNavigators().forEach((key, display) -> createTemporalCatalogNavigator(id, key.name(), display));
	}

	private void createTemporalCatalogNavigator(String id, String name, AlexandriaNavigator navigator) {
		add(navigator);
		provider.configureTemporalNavigator(navigator);
		navigator.personifyOnce(id + name);
		navigator.refresh();
	}

	private Map<TemporalCatalogRange, AlexandriaTimeRangeNavigator> catalogTimeRanges() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof TemporalCatalogRange).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (TemporalCatalogRange)s, s -> new AlexandriaTimeRangeNavigator(box)));
	}

	private Map<TemporalCatalogRangeNavigator, AlexandriaTimeRangeNavigator> catalogTimeRangeNavigators() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof TemporalCatalogRangeNavigator).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (TemporalCatalogRangeNavigator)s, s -> new AlexandriaTimeRangeNavigator(box)));
	}

	private Map<TemporalCatalogTimeNavigator, AlexandriaTimeNavigator> catalogTimeNavigators() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof TemporalCatalogTimeNavigator).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (TemporalCatalogTimeNavigator)s, s -> new AlexandriaTimeNavigator(box)));
	}

	private Map<TemporalCatalogTime, AlexandriaTimeNavigator> catalogTimes() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof TemporalCatalogTime).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (TemporalCatalogTime)s, s -> new AlexandriaTimeNavigator(box)));
	}

}