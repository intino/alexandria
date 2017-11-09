package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.framework.box.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.framework.box.model.ItemList;
import io.intino.konos.alexandria.framework.box.model.Catalog;
import io.intino.konos.alexandria.framework.box.schemas.GroupingGroup;
import io.intino.konos.alexandria.framework.box.schemas.GroupingSelection;

public class AlexandriaCatalogDisplay<DN extends io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaCatalogDisplayNotifier> extends AlexandriaAbstractCatalogDisplay<Catalog, DN> {

	public AlexandriaCatalogDisplay(Box box) {
		super(box);
	}

	@Override
	protected ItemList itemList(String condition) {
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