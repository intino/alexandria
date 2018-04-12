package io.intino.konos.alexandria.activity.helpers;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.*;
import io.intino.konos.alexandria.activity.displays.AlexandriaElementView.OpenItemCatalogEvent;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.displays.providers.ItemDisplayProvider;
import io.intino.konos.alexandria.activity.displays.providers.TemporalCatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.OpenCatalogOperation;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.OpenDialogOperation;
import io.intino.konos.alexandria.activity.schemas.ChangeItemParameters;
import io.intino.konos.alexandria.activity.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.schemas.Position;
import io.intino.konos.alexandria.activity.schemas.ValidateItemParameters;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class ElementHelper {

	public static io.intino.konos.alexandria.activity.schemas.Item[] items(Item[] items, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return Stream.of(items).map(r -> item(r, provider, baseAssetUrl)).toArray(io.intino.konos.alexandria.activity.schemas.Item[]::new);
	}

	public static io.intino.konos.alexandria.activity.schemas.Item item(io.intino.konos.alexandria.activity.model.Item item, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return ItemBuilder.build(item, item.id(), provider, baseAssetUrl);
	}

	public static ItemDisplayProvider itemDisplayProvider(ElementViewDisplayProvider provider, ElementView view) {
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
				provider.changeItem(item, params);
			}

			@Override
			public void validateItem(Item item, ValidateItemParameters params) {
				provider.validateItem(item, params);
			}
		};
	}

	public static ItemBuilder.ItemBuilderProvider itemBuilderProvider(ElementViewDisplayProvider provider, ElementView view) {
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

	public static AlexandriaElementView.OpenItemDialogEvent openItemDialogEvent(Item item, Stamp stamp, ActivitySession session) {
		return new AlexandriaElementView.OpenItemDialogEvent() {
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

	public static AlexandriaElementView.ExecuteItemTaskEvent executeItemTaskEvent(Item item, Stamp stamp, AlexandriaDisplay self) {
		return new AlexandriaElementView.ExecuteItemTaskEvent() {
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
