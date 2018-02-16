package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.builders.MoldBuilder;
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
	private boolean embeddedDisplaysCreated = false;
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
		if (!embeddedDisplaysCreated) {
			embeddedDisplays().forEach((key, display) -> {
				display.item(item);
				display.provider(AlexandriaItem.this);
				add(display);
				display.personifyOnce(id + key.displayType());
				updateRange(display);
				display.refresh();
			});
			embeddedDialogs().forEach((key, dialog) -> {
				dialog.target(item);
				add(dialog);
				dialog.personifyOnce(id + key.dialogType());
				dialog.refresh();
			});
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
		pages(item).forEach(display -> {
			add(display);
			display.personifyOnce(id);
			display.refresh();
		});
		embeddedDisplaysCreated = true;
	}

	public void notifyOpenItem(AlexandriaElementView.OpenItemEvent params) {
		openItemListeners.forEach(l -> l.accept(params));
	}

	public void openElement(OpenElementParameters params) {
		Stamp stamp = provider.stamp(mold, params.stamp().name());
		if (!(stamp instanceof CatalogLink)) return;

		CatalogLink catalogLinkStamp = (CatalogLink)stamp;
		AlexandriaElementDisplay display = provider.openElement(catalogLinkStamp.catalog().label());
		display.target(this.item);

		if (catalogLinkStamp.filtered())
			display.filterAndNotify(item -> catalogLinkStamp.filter(this.item, (Item)item, session()));

		if (display instanceof AlexandriaTemporalCatalog && provider.range() != null)
			((AlexandriaTemporalCatalog) display).selectRange(provider.range());

		display.refresh();
	}

	public void openItemDialogOperation(OpenItemDialogParameters params) {
		openItemDialogListeners.forEach(l -> l.accept(ElementHelper.openItemDialogEvent(itemOf(params.item()), provider.stamp(mold, params.stamp()), session())));
	}

	public void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskListeners.forEach(l -> l.accept(ElementHelper.executeItemTaskEvent(itemOf(params.item()), provider.stamp(mold, params.stamp()))));
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

	private Map<EmbeddedDisplay, AlexandriaStamp> embeddedDisplays() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedDisplay).collect(toList());
		Map<EmbeddedDisplay, AlexandriaStamp> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((EmbeddedDisplay)stamp, ((EmbeddedDisplay)stamp).createDisplay(session())), HashMap::putAll);
		return mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map<EmbeddedDialog, AlexandriaDialog> embeddedDialogs() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedDialog).collect(toList());
		Map<EmbeddedDialog, AlexandriaDialog> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((EmbeddedDialog)stamp, ((EmbeddedDialog)stamp).createDialog(session())), HashMap::putAll);
		return mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<AlexandriaDisplay> pages(Item item) {
		return provider.stamps(mold).stream().filter(s -> s instanceof Page)
				.map(stamp -> new AlexandriaPageContainer(box).pageLocation(location(stamp, item)))
				.collect(Collectors.toList());
	}

	private Map<EmbeddedCatalog, AlexandriaAbstractCatalog> embeddedCatalogs() {
		List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedCatalog).collect(toList());
		return stamps.stream().collect(Collectors.toMap(s -> (EmbeddedCatalog)s, s -> displayFor((EmbeddedCatalog)s)));
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
		return provider.item(item);
	}

}