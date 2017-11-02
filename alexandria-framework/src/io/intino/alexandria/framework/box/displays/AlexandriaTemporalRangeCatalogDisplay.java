package io.intino.alexandria.framework.box.displays;

import io.intino.alexandria.Box;
import io.intino.alexandria.framework.box.displays.builders.CatalogBuilder;
import io.intino.alexandria.framework.box.displays.notifiers.AlexandriaTemporalRangeCatalogDisplayNotifier;
import io.intino.alexandria.framework.box.helpers.TimeScaleHandler;
import io.intino.alexandria.framework.box.model.ItemList;
import io.intino.alexandria.framework.box.model.TimeRange;
import io.intino.alexandria.framework.box.model.TimeScale;
import io.intino.alexandria.framework.box.schemas.GroupingSelection;

import java.time.Instant;
import java.util.List;

public class AlexandriaTemporalRangeCatalogDisplay<DN extends AlexandriaTemporalRangeCatalogDisplayNotifier> extends AlexandriaTemporalCatalogDisplay<DN, AlexandriaTimeRangeNavigatorDisplay> {

	public AlexandriaTemporalRangeCatalogDisplay(Box box) {
		super(box, new AlexandriaTimeRangeNavigatorDisplay(box));
	}

	@Override
	protected void sendCatalog() {
		notifier.refreshCatalog(CatalogBuilder.build(element(), groupingManager, label(), embedded()));
	}

	@Override
	protected int maxZoom() {
		return element().maxZoom();
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

	@Override
	protected void configureTimeScaleHandler(TimeScaleHandler timeScaleHandler, TimeRange range, List<TimeScale> scales) {
		timeScaleHandler.updateScale(scales.get(0));
	}

	@Override
	protected void configureNavigatorDisplay(AlexandriaTimeRangeNavigatorDisplay navigatorDisplay, TimeScaleHandler timeScaleHandler) {
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
		return child(AlexandriaTimeRangeNavigatorDisplay.class).timeScaleHandler();
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