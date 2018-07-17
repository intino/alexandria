package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaCatalogNotifier;
import io.intino.konos.alexandria.ui.model.Catalog;
import io.intino.konos.alexandria.ui.model.ItemList;
import io.intino.konos.alexandria.ui.model.catalog.Scope;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.ui.schemas.GroupingGroup;
import io.intino.konos.alexandria.ui.schemas.GroupingSelection;
import io.intino.konos.alexandria.ui.schemas.OpenElementParameters;

public class AlexandriaCatalog<DN extends AlexandriaCatalogNotifier> extends AlexandriaAbstractCatalog<Catalog, DN> {

	public AlexandriaCatalog(Box box) {
		super(box);
	}

	@Override
	public void notifyItemsArrival(String message) {
		notifier.itemsArrival(message);
	}

	@Override
	public void notifyUser(String message) {
		notifier.notifyUser(message);
	}

	@Override
	protected ItemList filteredItemList(Scope scope, String condition) {
		ItemList itemList = element().items(scope, condition, session());
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
	protected void createPanel(CreatePanelParameters params) {
		notifier.createPanel(params);
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
	protected void showDialogBox() {
		notifier.showDialogBox();
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

	@Override
	public void home() {
		super.home();
	}

	@Override
	public void openItem(String item) {
		super.openItem(item);
	}

	@Override
	public void openElement(OpenElementParameters params) {
		super.openElement(params);
	}

	@Override
	public void openView(String name) {
		super.openView(name);
	}

	public void refreshItems() {
		forceRefresh();
	}
}