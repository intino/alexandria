package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.framework.box.displays.builders.CatalogBuilder;
import io.intino.alexandria.framework.box.displays.notifiers.AlexandriaTemporalTimeCatalogDisplayNotifier;
import io.intino.alexandria.framework.box.helpers.TimeScaleHandler;
import io.intino.alexandria.framework.box.model.ItemList;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.TimeScale;
import io.intino.alexandria.framework.box.schemas.GroupingSelection;

import java.util.List;

public class AlexandriaTemporalTimeCatalogDisplay<DN extends AlexandriaTemporalTimeCatalogDisplayNotifier> extends AlexandriaTemporalCatalogDisplay<DN, AlexandriaTimeNavigatorDisplay> {

	public AlexandriaTemporalTimeCatalogDisplay(Box box) {
		super(box, new AlexandriaTimeNavigatorDisplay(box));
	}

	@Override
	protected int maxZoom() {
		return 0;
	}

	@Override
	protected void configureTimeScaleHandler(TimeScaleHandler timeScaleHandler, TimeRange range, List<TimeScale> scales) {
		timeScaleHandler.updateInstant(range.to(), scales.get(0));
	}

	@Override
	protected void configureNavigatorDisplay(AlexandriaTimeNavigatorDisplay navigatorDisplay, TimeScaleHandler timeScaleHandler) {
		navigatorDisplay.onMove(this::refresh);
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
	protected void sendCatalog() {
		notifier.refreshCatalog(CatalogBuilder.build(element(), groupingManager, label(), embedded()));
	}

	@Override
	protected void showDialog() {
		notifier.showDialog();
	}

	@Override
	protected void notifyFiltered(boolean value) {
		notifier.refreshFiltered(value);
	}

	@Override
	protected void showNavigator() {
		notifier.showTimeNavigator();
	}

	@Override
	protected void hideNavigator() {
		notifier.hideTimeNavigator();
	}

	@Override
	protected void filterTimezone(ItemList itemList, TimeRange range) {
	}

	@Override
	protected TimeRange queryRange() {
		return range();
	}

	@Override
	protected TimeScaleHandler timeScaleHandler() {
		return child(AlexandriaTimeNavigatorDisplay.class).timeScaleHandler();
	}

	public void selectGrouping(GroupingSelection value) {
		super.selectGrouping(value);
	}

	public void clearFilter() {
		super.clearFilter();
	}

	public void timezoneOffset(Integer value) {
		super.timezoneOffset(value);
	}

	public void navigate(String value) {
		super.navigate(value);
	}

}
