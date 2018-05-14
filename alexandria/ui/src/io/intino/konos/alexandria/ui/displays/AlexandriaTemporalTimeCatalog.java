package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.builders.CatalogBuilder;
import io.intino.konos.alexandria.ui.displays.notifiers.AlexandriaTemporalTimeCatalogNotifier;
import io.intino.konos.alexandria.ui.helpers.TimeScaleHandler;
import io.intino.konos.alexandria.ui.model.ItemList;
import io.intino.konos.alexandria.ui.model.TimeRange;
import io.intino.konos.alexandria.ui.model.TimeScale;
import io.intino.konos.alexandria.ui.model.catalog.TemporalFilter;
import io.intino.konos.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.ui.schemas.GroupingSelection;
import io.intino.konos.alexandria.ui.schemas.OpenElementParameters;

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
}
