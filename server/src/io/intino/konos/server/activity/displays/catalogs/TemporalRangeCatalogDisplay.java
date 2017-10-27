package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.builders.CatalogBuilder;
import io.intino.konos.server.activity.displays.catalogs.navigators.TimeRangeNavigatorDisplay;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.elements.model.TimeScale;
import io.intino.konos.server.activity.displays.elements.model.ItemList;
import io.intino.konos.server.activity.helpers.TimeScaleHandler;

import java.time.Instant;
import java.util.List;

public class TemporalRangeCatalogDisplay<DN extends TemporalRangeCatalogDisplayNotifier> extends TemporalCatalogDisplay<DN, TimeRangeNavigatorDisplay> {

	public TemporalRangeCatalogDisplay(Box box) {
		super(box, new TimeRangeNavigatorDisplay(box));
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
	protected int maxZoom() {
		return element().maxZoom();
	}

	@Override
	protected void configureTimeScaleHandler(TimeScaleHandler timeScaleHandler, TimeRange range, List<TimeScale> scales) {
		timeScaleHandler.updateScale(scales.get(0));
	}

	@Override
	protected void configureNavigatorDisplay(TimeRangeNavigatorDisplay navigatorDisplay, TimeScaleHandler timeScaleHandler) {
		navigatorDisplay.onMove(this::refresh);
		navigatorDisplay.onFromChange(this::refresh);
		navigatorDisplay.onToChange(this::refresh);
		navigatorDisplay.onMoveNext(this::refresh);
		navigatorDisplay.onMovePrevious(this::refresh);
	}

	@Override
	protected void showNavigator() {
		notifier.showTimeRangeNavigator();
	}

	@Override
	protected void hideNavigator() {
		notifier.hideTimeRangeNavigator();
	}

	@Override
	protected TimeRange queryRange() {
		int offset = timezoneOffset();
		TimeRange range = timeScaleHandler().range();
		Instant newFrom = range.scale().normalise(range.from().plusSeconds(offset * 3600));
		Instant newTo = range.scale().nextTime(newFrom);
		while(!newTo.isAfter(range.to())) newTo = range.scale().nextTime(newTo);
		newTo = newTo.plusSeconds(offset * 3600).minusMillis(1);
		return new TimeRange(newFrom, newTo, range.scale());
	}

	@Override
	protected void filterTimezone(ItemList itemList, TimeRange range) {
		Instant from = range.from().plusSeconds(timezoneOffset() * 3600);
		Instant to = range.to().plusSeconds(timezoneOffset() * 3600);
		itemList.filter(item -> {
			Instant created = element().created(item);
			return created.isAfter(from) && created.isBefore(to);
		});
	}

	@Override
	protected TimeScaleHandler timeScaleHandler() {
		return child(TimeRangeNavigatorDisplay.class).timeScaleHandler();
	}

}
