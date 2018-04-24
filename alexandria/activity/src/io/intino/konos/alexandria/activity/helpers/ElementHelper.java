package io.intino.konos.alexandria.activity.helpers;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.*;
import io.intino.konos.alexandria.activity.displays.events.ExecuteItemTaskEvent;
import io.intino.konos.alexandria.activity.displays.events.OpenItemCatalogEvent;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.events.OpenItemDialogEvent;
import io.intino.konos.alexandria.activity.displays.events.OpenItemEvent;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.displays.providers.ItemDisplayProvider;
import io.intino.konos.alexandria.activity.displays.providers.TemporalCatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.catalog.events.OpenPanel;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.Title;
import io.intino.konos.alexandria.activity.model.mold.stamps.Tree;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.OpenCatalogOperation;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.OpenDialogOperation;
import io.intino.konos.alexandria.activity.schemas.*;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ElementHelper {

	public static io.intino.konos.alexandria.activity.schemas.Item[] items(Item[] items, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return Stream.of(items).map(r -> item(r, provider, baseAssetUrl)).toArray(io.intino.konos.alexandria.activity.schemas.Item[]::new);
	}

	public static io.intino.konos.alexandria.activity.schemas.Item item(io.intino.konos.alexandria.activity.model.Item item, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return ItemBuilder.build(item, item.id(), provider, baseAssetUrl);
	}

	public static ItemDisplayProvider itemDisplayProvider(ElementViewDisplayProvider provider, AlexandriaElementViewDefinition view) {
		return new ItemDisplayProvider() {
			@Override
			public Mold mold() {
				return view.mold();
			}

			@Override
			public Item item(String id) {
				return provider.item(id);
			}

			@Override
			public TimeRange range() {
				return provider.range();
			}

			@Override
			public List<Block> blocks(Mold mold) {
				return provider.blocks(mold);
			}

			@Override
			public List<Stamp> stamps(Mold mold) {
				return provider.stamps(mold);
			}

			@Override
			public Stamp stamp(Mold mold, String stampName) {
				return provider.stamp(mold, stampName);
			}

			@Override
			public <D extends AlexandriaElementDisplay> D openElement(String label) {
				return provider.openElement(label);
			}

			@Override
			public <N extends AlexandriaNavigator> void configureTemporalNavigator(N navigator) {
				if (! (provider.element() instanceof TemporalCatalog)) return;
				((TemporalCatalogViewDisplayProvider)provider).configureTemporalNavigator(navigator);
			}

			@Override
			public void executeOperation(ElementOperationParameters params, List<Item> items) {
				provider.executeOperation(params, items);
			}

			@Override
			public Resource downloadOperation(ElementOperationParameters params, List<Item> items) {
				return provider.downloadOperation(params, items);
			}

			@Override
			public void changeItem(Item item, ChangeItemParameters params) {
				provider.changeItem(item, stamp(view.mold(), params.stamp()), params.value());
			}

			@Override
			public void validateItem(Item item, ValidateItemParameters params) {
				provider.validateItem(item, stamp(view.mold(), params.stamp()), params.value());
			}
		};
	}

	public static ItemBuilder.ItemBuilderProvider itemBuilderProvider(ElementViewDisplayProvider provider, AlexandriaElementViewDefinition view) {
		return new ItemBuilder.ItemBuilderProvider() {
			@Override
			public List<Block> blocks() {
				return provider.blocks(view.mold());
			}

			@Override
			public List<Stamp> stamps() {
				return provider.stamps(view.mold());
			}

			@Override
			public ActivitySession session() {
				return provider.session();
			}

			@Override
			public TimeScale scale() {
				return provider.range() != null ? provider.range().scale() : null;
			}
		};
	}

	public static OpenItemDialogEvent openItemDialogEvent(Item item, Stamp stamp, ActivitySession session) {
		return new OpenItemDialogEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return stamp;
			}

			@Override
			public AlexandriaDialog dialog() {
				return ((OpenDialogOperation)stamp).createDialog(item, session);
			}
		};
	}

	public static OpenItemEvent openItemEvent(String item, ElementViewDisplayProvider provider, AlexandriaElementViewDefinition definition, ActivitySession session) {
		return new OpenItemEvent() {
			@Override
			public String itemId() {
				return new String(Base64.getDecoder().decode(item));
			}

			@Override
			public String label() {
				Optional<Stamp> titleStamp = provider.stamps(definition.mold()).stream().filter(s -> (s instanceof Title)).findAny();
				return titleStamp.isPresent() ? ((Title)titleStamp.get()).value(item(), session) : item().name();
			}

			@Override
			public Item item() {
				return provider.item(itemId());
			}

			@Override
			public Panel panel() {
				return definition.onClickRecordEvent().openPanel().panel();
			}

			@Override
			public TimeRange range() {
				return provider.range();
			}

			@Override
			public Tree breadcrumbs() {
				OpenPanel openPanel = definition.onClickRecordEvent().openPanel();
				return openPanel != null ? openPanel.breadcrumbs(item(), session) : null;
			}
		};
	}

	public static OpenItemCatalogEvent openItemCatalogEvent(String item, ElementViewDisplayProvider provider, AlexandriaElementViewDefinition definition, ActivitySession session) {
		return new OpenItemCatalogEvent() {
			@Override
			public Item item() {
				return provider.item(new String(Base64.getDecoder().decode(item)));
			}

			@Override
			public Stamp stamp() {
				return null;
			}

			@Override
			public Catalog catalog() {
				return definition.onClickRecordEvent().openCatalog().catalog();
			}

			@Override
			public Position position() {
				return null;
			}

			@Override
			public boolean filtered() {
				return definition.onClickRecordEvent().openCatalog().filtered();
			}

			@Override
			public boolean filter(Item target) {
				return definition.onClickRecordEvent().openCatalog().filter(item(), target, session);
			}

			@Override
			public String itemToShow() {
				return definition.onClickRecordEvent().openCatalog().item(item(), session);
			}
		};
	}

	public static OpenItemCatalogEvent openItemCatalogEvent(Item item, ElementViewDisplayProvider provider, AlexandriaElementViewDefinition definition, ActivitySession session) {
		return new OpenItemCatalogEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return null;
			}

			@Override
			public Catalog catalog() {
				return definition.onClickRecordEvent().openCatalog().catalog();
			}

			@Override
			public Position position() {
				return null;
			}

			@Override
			public boolean filtered() {
				return definition.onClickRecordEvent().openCatalog().filtered();
			}

			@Override
			public boolean filter(Item target) {
				return definition.onClickRecordEvent().openCatalog().filter(item, target, session);
			}

			@Override
			public String itemToShow() {
				return definition.onClickRecordEvent().openCatalog().item(item, session);
			}
		};
	}

	public static OpenItemDialogEvent openItemDialogEvent(Item item, ElementViewDisplayProvider provider, AlexandriaElementViewDefinition definition, ActivitySession session) {
		return new OpenItemDialogEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return null;
			}

			@Override
			public AlexandriaDialog dialog() {
				return definition.onClickRecordEvent().openDialog().createDialog(item, session);
			}
		};
	}

	public static OpenItemCatalogEvent openItemCatalogEvent(Item item, Stamp stamp, Position position, Element context, ActivitySession session) {
		return new OpenItemCatalogEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return stamp;
			}

			@Override
			public Catalog catalog() {
				if (! (stamp instanceof OpenCatalogOperation)) return null;
				OpenCatalogOperation operation = (OpenCatalogOperation) stamp;
				return operation.catalog();
			}

			@Override
			public Position position() {
				if (! (stamp instanceof OpenCatalogOperation)) return null;
				OpenCatalogOperation operation = (OpenCatalogOperation) stamp;
				if (operation.position() == OpenCatalogOperation.Position.Standalone) return null;
				return position;
			}

			@Override
			public String itemToShow() {
				return null;
			}

			@Override
			public boolean filtered() {
				if (! (stamp instanceof OpenCatalogOperation)) return false;
				OpenCatalogOperation operation = (OpenCatalogOperation) stamp;
				return operation.filtered();
			}

			@Override
			public boolean filter(Item source) {
				if (! (stamp instanceof OpenCatalogOperation)) return true;
				OpenCatalogOperation operation = (OpenCatalogOperation) stamp;
				return operation.filter(context, item, source, session);
			}
		};
	}

	public static ExecuteItemTaskEvent executeItemTaskEvent(Item item, Stamp stamp, AlexandriaDisplay self) {
		return new ExecuteItemTaskEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return stamp;
			}

			@Override
			public AlexandriaDisplay self() {
				return self;
			}
		};
	}

}
