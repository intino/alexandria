package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public class PageCollectionBehavior<DS extends Datasource<Item>, Item> extends CollectionBehavior<DS, Item> {
	private int page = 0;

	public PageCollectionBehavior(Collection collection) {
		super(collection);
	}

	public void setup(DS source, int pageSize) {
		if (source == null) return;
		itemLoader(new PageItemLoader<>(source, pageSize));
        page(0);
    }

    public <IL extends ItemLoader<DS, Item>> IL itemLoader() {
		return (IL) itemLoader;
	}

	public void page(int pos) {
		PageItemLoader<DS, Item> itemLoader = itemLoader();
		page = pos;
		reset(itemLoader.page(pos));
	}

	public void pageSize(int size) {
		PageItemLoader<DS, Item> itemLoader = itemLoader();
		itemLoader.pageSize(size);
		reset();
	}

	public synchronized void moreItems(CollectionMoreItems info) {
		List<Item> items = itemLoader.moreItems(info.start(), info.stop());
		for (int i=0; i<items.size(); i++) collection.insert(items.get(i), info.start()+i);
	}

	public long itemCount() {
		return itemLoader.itemCount();
	}

	@Override
	protected void reset() {
		PageItemLoader<DS, Item> itemLoader = itemLoader();
		while (page > itemLoader.pageCount() && page > 0) page--;
		reset(itemLoader.page(page));
	}

}
