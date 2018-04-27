package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaTemporalTimeCatalogNotifier;
import io.intino.konos.alexandria.activity.helpers.TimeScaleHandler;
import io.intino.konos.alexandria.activity.model.ItemList;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.TimeScale;
import io.intino.konos.alexandria.activity.model.catalog.TemporalFilter;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.activity.schemas.GroupingSelection;

import java.util.List;

public class AlexandriaTemporalTimeCatalog<DN extends AlexandriaTemporalTimeCatalogNotifier> extends AlexandriaTemporalCatalog<DN, AlexandriaTimeNavigator> {

	public AlexandriaTemporalTimeCatalog(Box box) {
		super(box, new AlexandriaTimeNavigator(box));
	}

	@Override
	public void notifyUser(String message) {
		notifier.notifyUser(message);
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
	protected void configureNavigatorDisplay(AlexandriaTimeNavigator navigatorDisplay, TimeScaleHandler timeScaleHandler) {
		navigatorDisplay.onMove(this::refresh);
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
	protected void sendCatalog() {
		notifier.refreshCatalog(CatalogBuilder.build(element(), groupingManager, label(), embedded()));
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
	protected void showNavigator() {
		notifier.showTimeNavigator();
	}

	@Override
	protected void hideNavigator() {
		notifier.hideTimeNavigator();
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
	protected void filterTimezone(ItemList itemList, TimeRange range) {
	}

	@Override
	protected TimeRange queryRange() {
		return range() != null ? range() : timeScaleHandler().range();
	}

	@Override
	protected TimeScaleHandler timeScaleHandler() {
		return child(AlexandriaTimeNavigator.class).timeScaleHandler();
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
		configureNavigatorDisplay((AlexandriaTimeNavigator) navigator, timeScaleHandler);
	}

	public void home() {
		super.home();
	}

	public void openItem(String item) {
		super.openItem(item);
	}

	public void openView(String name) {
		super.openView(name);
	}
}
