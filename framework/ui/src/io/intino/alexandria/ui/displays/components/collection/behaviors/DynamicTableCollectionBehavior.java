package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.ui.displays.components.DynamicTable;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.loaders.DynamicTableItemLoader;
import io.intino.alexandria.ui.model.datasource.DynamicTableDatasource;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.List;

public class DynamicTableCollectionBehavior<Item> extends CollectionBehavior<DynamicTableDatasource<Item>, Item, DynamicTableItemLoader<Item>> {
	private int page = 0;
	public static final int DefaultPageSize = 20;

	public DynamicTableCollectionBehavior(DynamicTable collection) {
		super(collection);
	}

	public DynamicTableCollectionBehavior(Collection collection) {
		super(collection);
	}

	@Override
	public DynamicTableCollectionBehavior setup(DynamicTableDatasource<Item> source) {
		return setup(source, DefaultPageSize);
	}

	public DynamicTableCollectionBehavior setup(DynamicTableDatasource<Item> source, int pageSize) {
		if (source == null) return this;
		itemLoader = new DynamicTableItemLoader<>(source, pageSize);
		return this;
	}

	public String dimension() {
		return this.itemLoader.dimension();
	}

	public DynamicTableCollectionBehavior dimension(String dimension) {
		this.itemLoader.dimension(dimension);
		return this;
	}

	public String drill() {
		return this.itemLoader.drill();
	}

	public DynamicTableCollectionBehavior drill(String drill) {
		this.itemLoader.drill(drill);
		return this;
	}

	public DynamicTableCollectionBehavior section(Section section) {
		this.itemLoader.section(section);
		return this;
	}

	public DynamicTableCollectionBehavior row(String row) {
		this.itemLoader.row(row);
		return this;
	}

	public void page(int pos) {
		computeUpdate(e -> page = pos);
	}

	public int pageSize() {
		return itemLoader.pageSize();
	}

	public void pageSize(int size) {
		computeUpdate(e -> {
			DynamicTableItemLoader<Item> itemLoader = itemLoader();
			itemLoader.pageSize(size);
		});
	}

	public synchronized void moreItems(CollectionMoreItems info) {
		List<Item> items = itemLoader.moreItems(info.start(), info.stop());
		collection().insert(items, info.start());
	}

	public long itemCount() {
		return itemLoader.itemCount();
	}

	@Override
	public void update() {
		collection().loading(true);
		updateSections();
		updateItems();
		collection().loading(false);
	}

	void updateSections() {
		DynamicTable collection = collection();
		collection.refreshSections(sections());
	}

	private void updateItems() {
		DynamicTableItemLoader<Item> itemLoader = itemLoader();
		int count = itemLoader.pageCount();
		while (page > count && page > 0) page--;
		if (count == 0) return;
		collection().add(itemLoader.page(page));
	}

	public List<Section> sections() {
		return itemLoader().sections();
	}
}
