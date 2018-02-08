package io.intino.konos.alexandria.activity.helpers;

import io.intino.konos.alexandria.activity.Resource;
import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.displays.AlexandriaElementDisplay;
import io.intino.konos.alexandria.activity.displays.AlexandriaElementView;
import io.intino.konos.alexandria.activity.displays.ElementView;
import io.intino.konos.alexandria.activity.displays.builders.ItemBuilder;
import io.intino.konos.alexandria.activity.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.activity.displays.providers.ItemDisplayProvider;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Mold;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.TimeScale;
import io.intino.konos.alexandria.activity.model.mold.Block;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.operations.OpenDialogOperation;
import io.intino.konos.alexandria.activity.schemas.ElementOperationParameters;
import io.intino.konos.alexandria.activity.schemas.SaveItemParameters;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class ElementHelper {

	public static io.intino.konos.alexandria.activity.schemas.Item[] items(Item[] items, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return Stream.of(items).map(r -> item(r, provider, baseAssetUrl)).toArray(io.intino.konos.alexandria.activity.schemas.Item[]::new);
	}

	public static io.intino.konos.alexandria.activity.schemas.Item item(io.intino.konos.alexandria.activity.model.Item item, ItemBuilder.ItemBuilderProvider provider, URL baseAssetUrl) {
		return ItemBuilder.build(item, provider, baseAssetUrl);
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
			public AlexandriaElementDisplay openElement(String label) {
				return provider.openElement(label);
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
			public void saveItem(SaveItemParameters params, Item item) {
				provider.saveItem(params, item);
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
			public User user() {
				return provider.user();
			}

			@Override
			public TimeScale scale() {
				return provider.range() != null ? provider.range().scale() : null;
			}
		};
	}

	public static AlexandriaElementView.OpenItemDialogEvent openItemDialogEvent(Item item, Stamp stamp, User user) {
		return new AlexandriaElementView.OpenItemDialogEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public AlexandriaDialog dialog() {
				return ((OpenDialogOperation)stamp).createDialog(item, user);
			}
		};
	}

	public static AlexandriaElementView.ExecuteItemTaskEvent executeItemTaskEvent(Item item, Stamp stamp) {
		return new AlexandriaElementView.ExecuteItemTaskEvent() {
			@Override
			public Item item() {
				return item;
			}

			@Override
			public Stamp stamp() {
				return stamp;
			}
		};
	}

}
