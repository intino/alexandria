package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.builders.CatalogBuilder;
import io.intino.konos.server.activity.displays.catalogs.navigators.TimeNavigatorDisplay;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.elements.model.TimeScale;
import io.intino.konos.server.activity.displays.elements.model.ItemList;
import io.intino.konos.server.activity.helpers.TimeScaleHandler;

import java.util.List;

public class TemporalTimeCatalogDisplay<DN extends TemporalTimeCatalogDisplayNotifier> extends TemporalCatalogDisplay<DN, TimeNavigatorDisplay> {

	public TemporalTimeCatalogDisplay(Box box) {
		super(box, new TimeNavigatorDisplay(box));
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
	protected void configureNavigatorDisplay(TimeNavigatorDisplay navigatorDisplay, TimeScaleHandler timeScaleHandler) {
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
		return child(TimeNavigatorDisplay.class).timeScaleHandler();
	}

}
