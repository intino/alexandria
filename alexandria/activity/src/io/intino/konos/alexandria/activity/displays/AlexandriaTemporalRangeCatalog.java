package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaTemporalRangeCatalogNotifier;
import io.intino.konos.alexandria.activity.helpers.TimeScaleHandler;
import io.intino.konos.alexandria.activity.model.ItemList;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.TimeScale;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.activity.schemas.GroupingSelection;

import java.time.Instant;
import java.util.List;

public class AlexandriaTemporalRangeCatalog<DN extends AlexandriaTemporalRangeCatalogNotifier> extends AlexandriaTemporalCatalog<DN, AlexandriaTimeRangeNavigator> {

	public AlexandriaTemporalRangeCatalog(Box box) {
		super(box, new AlexandriaTimeRangeNavigator(box));
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
		if (element().showAll()) {
			TimeRange timeRange = timeScaleHandler.boundsRange();
			timeScaleHandler.updateRange(timeRange.from(), timeRange.to(), false);
		}
	}

	@Override
	protected void configureNavigatorDisplay(AlexandriaTimeRangeNavigator navigatorDisplay, TimeScaleHandler timeScaleHandler) {
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
	protected void loadTimezoneOffset() {
		notifier.loadTimezoneOffset();
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
		return child(AlexandriaTimeRangeNavigator.class).timeScaleHandler();
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