package io.intino.konos.server.activity.helpers;

import io.intino.konos.server.activity.Resource;
import io.intino.konos.server.activity.displays.elements.*;
import io.intino.konos.server.activity.displays.elements.ElementViewDisplay.ExecuteItemTaskEvent;
import io.intino.konos.server.activity.displays.elements.builders.ItemBuilder;
import io.intino.konos.server.activity.displays.elements.builders.ItemBuilder.ItemBuilderProvider;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.elements.model.TimeScale;
import io.intino.konos.server.activity.displays.elements.providers.ElementViewDisplayProvider;
import io.intino.konos.server.activity.displays.elements.providers.ItemDisplayProvider;
import io.intino.konos.server.activity.displays.molds.model.Block;
import io.intino.konos.server.activity.displays.molds.model.Mold;
import io.intino.konos.server.activity.displays.molds.model.Stamp;
import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.molds.model.stamps.operations.OpenDialogOperation;
import io.intino.konos.server.activity.displays.schemas.ElementOperationParameters;
import io.intino.konos.server.activity.displays.schemas.SaveItemParameters;
import io.intino.konos.server.activity.displays.schemas.Item;

import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class ElementHelper {

	public static Item[] items(io.intino.konos.server.activity.displays.elements.model.Item[] items, ItemBuilderProvider provider, URL baseAssetUrl) {
		return Stream.of(items).map(r -> item(r, provider, baseAssetUrl)).toArray(Item[]::new);
	}

	public static Item item(io.intino.konos.server.activity.displays.elements.model.Item item, ItemBuilderProvider provider, URL baseAssetUrl) {
		return ItemBuilder.build(item, provider, baseAssetUrl);
	}

	public static ItemDisplayProvider itemDisplayProvider(ElementViewDisplayProvider provider, ElementView view) {
		return new ItemDisplayProvider() {
			@Override
			public Mold mold() {
				return view.mold();
			}

			@Override
			public io.intino.konos.server.activity.displays.elements.model.Item item(String id) {
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
			public StampDisplay display(String name) {
				return provider.display(name);
			}

			@Override
			public ElementDisplay openElement(String label) {
				return provider.openElement(label);
			}

			@Override
			public void executeOperation(ElementOperationParameters params, List<io.intino.konos.server.activity.displays.elements.model.Item> items) {
				provider.executeOperation(params, items);
			}

			@Override
			public Resource downloadOperation(ElementOperationParameters params, List<io.intino.konos.server.activity.displays.elements.model.Item> items) {
				return provider.downloadOperation(params, items);
			}

			@Override
			public void saveItem(SaveItemParameters params, io.intino.konos.server.activity.displays.elements.model.Item item) {
				provider.saveItem(params, item);
			}
		};
	}

	public static ItemBuilderProvider itemBuilderProvider(ElementViewDisplayProvider provider, ElementView view) {
		return new ItemBuilderProvider() {
			@Override
			public List<Block> blocks() {
				return provider.blocks(view.mold());
			}

			@Override
			public List<Stamp> stamps() {
				return provider.stamps(view.mold());
			}

			@Override
			public String username() {
				return provider.user().isPresent() ? provider.user().get().username() : null;
			}

			@Override
			public TimeScale scale() {
				return provider.range() != null ? provider.range().scale() : null;
			}
		};
	}

	public static ElementViewDisplay.OpenItemDialogEvent openItemDialogEvent(String item, Stamp stamp, String username) {
		return new ElementViewDisplay.OpenItemDialogEvent() {
			@Override
			public String item() {
				return item;
			}

			@Override
			public String path() {
				return ((OpenDialogOperation)stamp).path(item, username);
			}

			@Override
			public int width() {
				return ((OpenDialogOperation)stamp).width();
			}

			@Override
			public int height() {
				return ((OpenDialogOperation)stamp).height();
			}
		};
	}

	public static ExecuteItemTaskEvent executeItemTaskEvent(String item, Stamp stamp) {
		return new ExecuteItemTaskEvent() {
			@Override
			public String item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return stamp;
			}
		};
	}

}
