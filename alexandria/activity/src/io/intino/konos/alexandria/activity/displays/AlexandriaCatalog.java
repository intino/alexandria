package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaCatalogNotifier;
import io.intino.konos.alexandria.activity.model.Catalog;
import io.intino.konos.alexandria.activity.model.ItemList;
import io.intino.konos.alexandria.activity.model.catalog.Scope;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.activity.schemas.GroupingGroup;
import io.intino.konos.alexandria.activity.schemas.GroupingSelection;

public class AlexandriaCatalog<DN extends AlexandriaCatalogNotifier> extends AlexandriaAbstractCatalog<Catalog, DN> {

	public AlexandriaCatalog(Box box) {
		super(box);
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

	public void navigate(String value) {
		super.navigate(value);
	}

	public void navigateMain() {
		super.navigateMain();
	}

}