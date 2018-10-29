package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.ItemList;
import io.intino.alexandria.ui.model.TimeScale;
import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaTemporalRangeCatalogNotifier;
import io.intino.alexandria.ui.helpers.TimeScaleHandler;
import io.intino.alexandria.ui.model.TimeRange;
import io.intino.alexandria.ui.model.catalog.TemporalFilter;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.ui.schemas.GroupingSelection;
import io.intino.konos.alexandria.ui.schemas.OpenElementParameters;

import java.time.Instant;
import java.util.List;

public class AlexandriaTemporalRangeCatalog<DN extends AlexandriaTemporalRangeCatalogNotifier> extends AlexandriaTemporalCatalog<DN, AlexandriaTimeRangeNavigator> {

	public AlexandriaTemporalRangeCatalog(Box box) {
		super(box, new AlexandriaTimeRangeNavigator(box));
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
	protected void showDialogBox() {
		notifier.showDialogBox();
	}

	@Override
	protected void notifyFiltered(boolean value) {
		notifier.refreshFiltered(value);
	}

	@Override
	protected void configureTimeScaleHandler(TimeScaleHandler timeScaleHandler, TimeRange range, List<TimeScale> scales) {
		timeScaleHandler.updateScale(scales.get(0));
		timeScaleHandler.onScaleChange(this::refresh);
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
	protected void refreshNavigatorLayout(TemporalFilter.Layout layout) {
		notifier.refreshNavigatorLayout(layout.toString());
	}

	@Override
	protected TimeRange queryRange(TimeRange range) {
		int offset = timezoneOffset();
		range = range != null ? range : timeScaleHandler().range();
		Instant newFrom = range.scale().normalise(range.from().plusSeconds(offset * 3600));
		Instant newTo = range.scale().nextTime(newFrom);
		while(!newTo.isAfter(range.to())) newTo = range.scale().nextTime(newTo);
		newTo = newTo.plusSeconds(offset * 3600).minusMillis(1);
		return new TimeRange(newFrom, newTo, range.scale());
	}

	@Override
	protected void filterTimezone(ItemList itemList, TimeRange range) {
		if (showAll()) return;
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

	@Override
	public <N extends AlexandriaNavigator> void configureTemporalNavigator(N navigator) {
		TimeScaleHandler timeScaleHandler = timeScaleHandler();
		navigator.timeScaleHandler(timeScaleHandler);
		configureNavigatorDisplay((AlexandriaTimeRangeNavigator) navigator, timeScaleHandler);
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
	public void openView(String name) {
		super.openView(name);
	}

	@Override
	public void openElement(OpenElementParameters params) {
		super.openElement(params);
	}

	public void refreshItems() {
		forceRefresh();
	}
}