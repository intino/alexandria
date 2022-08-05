package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.loaders.PageItemLoader;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

import java.util.Collections;
import java.util.List;

public class PageCollectionBehavior<DS extends PageDatasource<Item>, Item> extends CollectionBehavior<DS, Item, PageItemLoader<DS, Item>> {
	private int page = 0;
	public static final int DefaultPageSize = 20;

	public PageCollectionBehavior(Collection collection) {
		super(collection);
	}

	@Override
	public CollectionBehavior setup(DS source) {
		return setup(source, DefaultPageSize);
	}

	public CollectionBehavior setup(DS source, int pageSize) {
		if (source == null) return this;
		computeUpdate(e -> {
			itemLoader = new PageItemLoader<>(source, pageSize);
			page(0);
		}, false);
		return this;
    }

	public void page(int pos) {
		collection().clear();
		computeUpdate(e -> {
			page = pos;
			List<Item> items = itemLoader.page(this.page);
			collection().insert(items, itemLoader.start(this.page));
		}, false);
	}

	public void pageSize(int size) {
		computeUpdate(e -> {
			PageItemLoader<DS, Item> itemLoader = itemLoader();
			itemLoader.pageSize(size);
		});
	}

	public synchronized void nextPage() {
		page++;
		if (page >= itemLoader.pageCount()) {
			page = itemLoader.pageCount()-1;
			return;
		}
		List<Item> items = itemLoader.page(this.page);
		collection().insert(items, itemLoader.start(this.page));
	}

	public synchronized void moreItems(CollectionMoreItems info) {
		List<Item> items = itemLoader.moreItems(info.start(), info.stop());
		collection().insert(items, info.start());
	}

	public long itemCount() {
		if (itemLoader == null) return 0;
		return itemLoader.itemCount();
	}

	@Override
	void reset() {
		page = 0;
		super.reset();
	}

	@Override
	protected void update() {
		PageItemLoader<DS, Item> itemLoader = itemLoader();
		int count = itemLoader.pageCount();
		while (page > count && page > 0) page--;
		if (count == 0) return;
		collection().add(itemLoader.page(page));
	}

}
