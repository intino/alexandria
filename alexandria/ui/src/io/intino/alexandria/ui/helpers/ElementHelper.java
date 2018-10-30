package io.intino.alexandria.ui.helpers;

import io.intino.alexandria.ui.Resource;
import io.intino.alexandria.ui.displays.AlexandriaDialog;
import io.intino.alexandria.ui.displays.AlexandriaDisplay;
import io.intino.alexandria.ui.displays.AlexandriaElementDisplay;
import io.intino.alexandria.ui.displays.AlexandriaNavigator;
import io.intino.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.alexandria.ui.displays.events.OpenItemEvent;
import io.intino.alexandria.ui.model.*;
import io.intino.alexandria.ui.model.catalog.events.OnClickItem;
import io.intino.alexandria.ui.model.catalog.events.OpenPanel;
import io.intino.alexandria.ui.model.mold.Block;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.displays.*;
import io.intino.alexandria.ui.displays.builders.ItemBuilder;
import io.intino.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.alexandria.ui.displays.providers.ItemDisplayProvider;
import io.intino.alexandria.ui.displays.providers.TemporalCatalogViewDisplayProvider;
import io.intino.alexandria.ui.model.Catalog;
import io.intino.alexandria.ui.model.mold.stamps.Title;
import io.intino.alexandria.ui.model.mold.stamps.Tree;
import io.intino.alexandria.ui.model.mold.stamps.operations.OpenCatalogOperation;
import io.intino.alexandria.ui.model.mold.stamps.operations.OpenDialogOperation;
import io.intino.alexandria.ui.schemas.*;
import io.intino.alexandria.ui.services.push.UISession;

import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ElementHelper {

	public static io.intino.alexandria.ui.schemas.Item[] items(Item[] items, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return Stream.of(items).map(r -> item(r, provider, baseAssetUrl)).toArray(io.intino.alexandria.ui.schemas.Item[]::new);
	}

	public static io.intino.alexandria.ui.schemas.Item item(Item item, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return ItemBuilder.build(item, item.id(), provider, baseAssetUrl);
	}

	public static ItemDisplayProvider itemDisplayProvider(ElementViewDisplayProvider provider, View view) {
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
				return (D) provider.openElement(label);
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

	public static ItemBuilder.ItemBuilderProvider itemBuilderProvider(ElementViewDisplayProvider provider, View view) {
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
			public UISession session() {
				return provider.session();
			}

			@Override
			public TimeScale scale() {
				return provider.range() != null ? provider.range().scale() : null;
			}
		};
	}

	public static OpenItemDialogEvent openItemDialogEvent(Item item, Stamp stamp, UISession session) {
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

	public static OpenItemEvent openItemEvent(String item, ElementViewDisplayProvider provider, View view, OnClickItem onClickItem, UISession session) {
		return new OpenItemEvent() {
			@Override
			public String itemId() {
				return new String(Base64.getDecoder().decode(item));
			}

			@Override
			public String label() {
				Optional<Stamp> titleStamp = provider.stamps(view.mold()).stream().filter(s -> (s instanceof Title)).findAny();
				return titleStamp.isPresent() ? ((Title)titleStamp.get()).value(item(), session) : item().name();
			}

			@Override
			public Item item() {
				return provider.item(itemId());
			}

			@Override
			public Panel panel() {
				return onClickItem.openPanel().panel();
			}

			@Override
			public TimeRange range() {
				return provider.range();
			}

			@Override
			public Tree breadcrumbs() {
				OpenPanel openPanel = onClickItem.openPanel();
				return openPanel != null ? openPanel.breadcrumbs(item(), session) : null;
			}
		};
	}

	public static OpenItemCatalogEvent openItemCatalogEvent(String item, ElementViewDisplayProvider provider, View view, OnClickItem onClickItem, UISession session) {
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
				return onClickItem.openCatalog().catalog();
			}

			@Override
			public Position position() {
				return null;
			}

			@Override
			public boolean filtered() {
				return onClickItem.openCatalog().filtered();
			}

			@Override
			public boolean filter(Item target) {
				return onClickItem.openCatalog().filter(item(), target, session);
			}

			@Override
			public String itemToShow() {
				return onClickItem.openCatalog().item(item(), session);
			}
		};
	}

	public static OpenItemCatalogEvent openItemCatalogEvent(Item item, ElementViewDisplayProvider provider, View view, OnClickItem onClickItem, UISession session) {
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
				return onClickItem.openCatalog().catalog();
			}

			@Override
			public Position position() {
				return null;
			}

			@Override
			public boolean filtered() {
				return onClickItem.openCatalog().filtered();
			}

			@Override
			public boolean filter(Item target) {
				return onClickItem.openCatalog().filter(item, target, session);
			}

			@Override
			public String itemToShow() {
				return onClickItem.openCatalog().item(item, session);
			}
		};
	}

	public static OpenItemDialogEvent openItemDialogEvent(Item item, ElementViewDisplayProvider provider, View definition, OnClickItem onClickItem, UISession session) {
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
				return onClickItem.openDialog().createDialog(item, session);
			}
		};
	}

	public static OpenItemCatalogEvent openItemCatalogEvent(Item item, Stamp stamp, Position position, Element context, UISession session) {
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
