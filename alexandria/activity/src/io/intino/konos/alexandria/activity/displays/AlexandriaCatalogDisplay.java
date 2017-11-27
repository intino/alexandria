package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.activity.model.ItemList;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.schemas.GroupingGroup;
import io.intino.konos.alexandria.activity.schemas.GroupingSelection;

public class AlexandriaCatalogDisplay<DN extends io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogDisplayNotifier> extends AlexandriaAbstractCatalogDisplay<Catalog, DN> {

	public AlexandriaCatalogDisplay(Box box) {
		super(box);
	}

	@Override
	protected ItemList filteredItemList(String condition) {
		ItemList itemList = element().items(condition, username());
		applyFilter(itemList);
		return itemList;
	}

	@Override
	protected void sendCatalog() {
		notifier.refreshCatalog(CatalogBuilder.build(element(), groupingManager, label(), embedded()));
	}

	@Override
	protected void refreshBreadcrumbs(String breadcrumbs) {
		notifier.refreshBreadcrumbs(breadcrumbs);
	}

	@Override
	protected void createPanel(String item) {
		notifier.createPanel(item);
	}

	@Override
	protected void showPanel() {
		notifier.showPanel();
	}

	@Override
	protected void hidePanel() {
		notifier.hidePanel();
	}

	@Override
	protected void showDialog() {
		notifier.showDialog();
	}

	@Override
	protected void notifyFiltered(boolean value) {
		notifier.refreshFiltered(value);
	}

	public void selectGrouping(GroupingSelection value) {
		super.selectGrouping(value);
	}

	public void deleteGroupingGroup(GroupingGroup value) {
		super.deleteGroupingGroup(value);
	}

	public void clearFilter() {
		super.clearFilter();
	}

	public void navigate(String value) {
		super.navigate(value);
	}

}