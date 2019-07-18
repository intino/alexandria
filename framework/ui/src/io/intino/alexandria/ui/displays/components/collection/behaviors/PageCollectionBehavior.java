package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.loaders.PageItemLoader;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

import java.util.List;

public class PageCollectionBehavior<DS extends PageDatasource<Item>, Item> extends CollectionBehavior<DS, Item, PageItemLoader<DS, Item>> {
	private int page = 0;
	private static final int DefaultPageSize = 20;

	public PageCollectionBehavior(Collection collection) {
		super(collection);
	}

	@Override
	public CollectionBehavior setup(DS source) {
		return setup(source, DefaultPageSize);
	}

	public CollectionBehavior setup(DS source, int pageSize) {
		if (source == null) return this;
		itemLoader = new PageItemLoader<>(source, pageSize);
		page(0);
		return this;
    }

	public void page(int pos) {
		page = pos;
		reset();
	}

	public void pageSize(int size) {
		PageItemLoader<DS, Item> itemLoader = itemLoader();
		itemLoader.pageSize(size);
		reset();
	}

	public synchronized void moreItems(CollectionMoreItems info) {
		List<Item> items = itemLoader.moreItems(info.start(), info.stop());
		collection().insert(items, info.start());
	}

	public long itemCount() {
		return itemLoader.itemCount();
	}

	@Override
	protected void update() {
		PageItemLoader<DS, Item> itemLoader = itemLoader();
		while (page > itemLoader.pageCount() && page > 0) page--;
		collection().add(itemLoader.page(page));
	}

}
