package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.Box;
import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.ActivityDisplay;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.catalogs.AbstractCatalogDisplay;
import io.intino.konos.server.activity.displays.catalogs.TemporalCatalogDisplay;
import io.intino.konos.server.activity.displays.elements.ElementDisplay;
import io.intino.konos.server.activity.displays.elements.builders.ItemBuilder;
import io.intino.konos.server.activity.displays.elements.builders.ItemBuilder.ItemBuilderProvider;
import io.intino.konos.server.activity.displays.elements.builders.MoldBuilder;
import io.intino.konos.server.activity.displays.elements.model.Element;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.elements.model.TimeScale;
import io.intino.konos.server.activity.displays.elements.providers.ItemDisplayProvider;
import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.molds.TemporalStampDisplay;
import io.intino.konos.server.activity.displays.molds.model.Block;
import io.intino.konos.server.activity.displays.molds.model.Mold;
import io.intino.konos.server.activity.displays.molds.model.Stamp;
import io.intino.konos.server.activity.displays.molds.model.stamps.CatalogLink;
import io.intino.konos.server.activity.displays.molds.model.stamps.EmbeddedCatalog;
import io.intino.konos.server.activity.displays.molds.model.stamps.Page;
import io.intino.konos.server.activity.displays.molds.model.stamps.Tree;
import io.intino.konos.server.activity.displays.molds.model.stamps.operations.DownloadOperation;
import io.intino.konos.server.activity.displays.molds.model.stamps.operations.ExportOperation;
import io.intino.konos.server.activity.displays.molds.model.stamps.pages.ExternalPage;
import io.intino.konos.server.activity.displays.molds.model.stamps.pages.InternalPage;
import io.intino.konos.server.activity.displays.panels.model.Panel;
import io.intino.konos.server.activity.displays.schemas.*;
import io.intino.konos.server.activity.helpers.ElementHelper;
import io.intino.konos.server.activity.spark.ActivityFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.intino.konos.server.activity.displays.elements.ElementViewDisplay.*;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ItemDisplay extends ActivityDisplay<ItemDisplayNotifier> {
    private Mold mold;
    private Element context;
    private io.intino.konos.server.activity.displays.elements.model.Item item;
    private String mode;
    private ItemDisplayProvider provider;
    private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
    private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
    private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();
    private boolean recordDisplaysCreated = false;
    private String emptyMessage = null;

    public ItemDisplay(Box box) {
        super(box);
    }

    public void mold(Mold mold) {
        this.mold = mold;
    }

    public void context(Element context) {
        this.context = context;
    }

    public void item(io.intino.konos.server.activity.displays.elements.model.Item item) {
        this.item = item;
    }

    public void mode(String mode) {
        this.mode = mode;
    }

    public void provider(ItemDisplayProvider provider) {
        this.provider = provider;
    }

    public void onOpenItem(Consumer<OpenItemEvent> listener) {
        openItemListeners.add(listener);
    }

    public void onOpenItemDialog(Consumer<OpenItemDialogEvent> parameters) {
        openItemDialogListeners.add(parameters);
    }

    public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> parameters) {
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
        children(StampDisplay.class).forEach(display -> {
            display.item(item);
            if (display instanceof TemporalStampDisplay) {
                TemporalStampDisplay temporalDisplay = (TemporalStampDisplay) display;
                temporalDisplay.range(provider.range());
                display.refresh();
            }
            display.refresh();
        });
        remove(PageContainerDisplay.class);
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
            display.filter(item -> key.filter(context, ItemDisplay.this.item, (io.intino.konos.server.activity.displays.elements.model.Item) item));
            display.label(key.label());
            display.onOpenItem(params -> notifyOpenItem((OpenItemEvent) params));
            display.embedded(true);
            add(display);
            display.personifyOnce(id + key.name());
        });
        recordDisplaysCreated = true;
    }

    public void notifyOpenItem(OpenItemEvent params) {
        openItemListeners.forEach(l -> l.accept(params));
    }

    public void selectElement(Item stampItem) {
        Stamp stamp = provider.stamp(mold, stampItem.name());
        if (!(stamp instanceof CatalogLink)) return;

        CatalogLink catalogLinkStamp = (CatalogLink)stamp;
        ElementDisplay display = provider.openElement(catalogLinkStamp.catalog().label());

        display.filterAndNotify(item -> catalogLinkStamp.filter(this.item, (io.intino.konos.server.activity.displays.elements.model.Item)item));
        if (display instanceof TemporalCatalogDisplay)
            ((TemporalCatalogDisplay) display).selectRange(provider.range());

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
        sendInfo(ItemBuilder.build(item, new ItemBuilderProvider() {
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
                return ItemDisplay.this.username();
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

    private Map<io.intino.konos.server.activity.displays.molds.model.stamps.Display, StampDisplay> displays(io.intino.konos.server.activity.displays.elements.model.Item item) {
        List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof io.intino.konos.server.activity.displays.molds.model.stamps.Display).collect(toList());
        Map<io.intino.konos.server.activity.displays.molds.model.stamps.Display, StampDisplay> mapWithNulls = stamps.stream().collect(HashMap::new, (map, stamp)->map.put((io.intino.konos.server.activity.displays.molds.model.stamps.Display)stamp, provider.display(stamp.name())), HashMap::putAll);
        Map<io.intino.konos.server.activity.displays.molds.model.stamps.Display, StampDisplay> result = mapWithNulls.entrySet().stream().filter(e -> e.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        result.forEach((key, value) -> value.item(item));
        return result;
    }

    private List<Display> pages(io.intino.konos.server.activity.displays.elements.model.Item item) {
        return provider.stamps(mold).stream().filter(s -> s instanceof Page)
                .map(stamp -> new PageContainerDisplay(box).pageLocation(location(stamp, item)))
                .collect(Collectors.toList());
    }

    private Map<EmbeddedCatalog, AbstractCatalogDisplay> embeddedCatalogs() {
        List<Stamp> stamps = provider.stamps(mold).stream().filter(s -> s instanceof EmbeddedCatalog).collect(toList());
        return stamps.stream().collect(Collectors.toMap(s -> (EmbeddedCatalog)s, s -> displayFor((EmbeddedCatalog)s)));
    }

    private PageLocation location(Stamp stamp, io.intino.konos.server.activity.displays.elements.model.Item item) {
        PageLocation location = new PageLocation();
        if (stamp instanceof ExternalPage) return location.value(((ExternalPage)stamp).url(item).toString()).internal(false);
        return location.value(((InternalPage)stamp).path(item)).internal(true);
    }

    private AbstractCatalogDisplay displayFor(EmbeddedCatalog stamp) {
        AbstractCatalogDisplay result = stamp.display();
        if (result == null) return null;
        result.target(item);
        result.catalog(stamp.catalog());
        return result;
    }

    // TODO Mario. Es un Request -> refactorizar el cliente -> selectRecord por selectItem
    public void selectItem(Item item) {
        openItemListeners.forEach(l -> l.accept(new OpenItemEvent() {
            @Override
            public String itemId() {
                return item.name();
            }

            @Override
            public String label() {
                return item.label();
            }

            @Override
            public io.intino.konos.server.activity.displays.elements.model.Item item() {
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