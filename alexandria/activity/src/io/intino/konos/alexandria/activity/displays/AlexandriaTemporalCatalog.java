package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.providers.TemporalCatalogViewDisplayProvider;
import io.intino.konos.alexandria.activity.helpers.Bounds;
import io.intino.konos.alexandria.activity.helpers.TimeScaleHandler;
import io.intino.konos.alexandria.activity.model.*;
import io.intino.konos.alexandria.activity.model.catalog.Scope;
import io.intino.konos.alexandria.activity.model.catalog.views.DisplayView;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedDialog;
import io.intino.konos.alexandria.activity.model.mold.stamps.EmbeddedDisplay;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AlexandriaTemporalCatalog<DN extends AlexandriaDisplayNotifier, N extends AlexandriaNavigator> extends AlexandriaAbstractCatalog<TemporalCatalog, DN> implements TemporalCatalogViewDisplayProvider {
	private N navigatorDisplay = null;

	public AlexandriaTemporalCatalog(Box box, N navigatorDisplay) {
		super(box);
		this.navigatorDisplay = navigatorDisplay;
	}

	public void selectRange(TimeRange range) {
		dirty(true);
		TimeScale scale = range.scale();
		TimeScale referenceScale = (scale.ordinal() > TimeScale.Day.ordinal()) ? scale : TimeScale.Day;
		Instant from = range.from();
		Instant to = referenceScale.addTo(range.to(), 1);
		TimeRange bounds = element().range(session());

		if (from.isBefore(bounds.from())) from = bounds.to();
		if (to.isAfter(bounds.to())) to = bounds.to();

		timeScaleHandler().updateRange(from, to, false);
	}

	@Override
	public void forceRefresh() {
		reset();
	}

	@Override
	public void reset() {
		dirty(true);
		resetViews();
		this.refresh();
		reloadGroupings();
	}

	private void resetViews() {
		child(AlexandriaCatalogViewList.class).viewList().forEach(AlexandriaCatalogView::reset);
	}

	@Override
	public AlexandriaStamp display(String stampName) {
		EmbeddedDisplay stamp = (EmbeddedDisplay) stamp(stampName);

		AlexandriaTemporalStamp display = (AlexandriaTemporalStamp) stamp.createDisplay(session());
		display.range(timeScaleHandler().range());

		return display;
	}

	@Override
	public AlexandriaDialog dialog(String stampName) {
		EmbeddedDialog stamp = (EmbeddedDialog) stamp(stampName);
		return stamp.createDialog(session());
	}

	@Override
	public void refreshView() {
		super.refreshView();

		currentView().ifPresent(catalogView -> {
			AbstractView view = views().stream().filter(v -> v.name().equals(catalogView.view().name())).findFirst().orElse(null);
			if (view != null && view instanceof DisplayView && ((DisplayView)view).hideNavigator())
				hideNavigator();
			else if (isNavigatorVisible())
				showNavigator();
		});
	}

	@Override
	public TimeScale scale() {
		return timeScaleHandler().range().scale();
	}

	@Override
	public void refresh() {

		if (dirty()) {
			groupingManager.items(filteredItemList(defaultScope(), null).items());
			refreshGroupingsSelection();
			filterGroupingManager();
		}

		if (!isNavigatorVisible()) hideNavigator();

		refreshView();
	}

	@Override
	public Item rootItem(List<Item> itemList) {
		return element().rootItem(itemList, queryRange(), session());
	}

	@Override
	public Item defaultItem(String id) {
		return element().defaultItem(id, queryRange(), session());
	}

	@Override
	protected void loadItemList(String condition) {
		if (!dirty() && itemList != null) return;
		itemList = filteredItemList(scopeWithAttachedGrouping(), condition);
		range(timeScaleHandler().range());
		dirty(false);
	}

	@Override
	protected ItemList filteredItemList(Scope scope, String condition) {
		TimeRange range = showAll() ? timeScaleHandler().boundsRange() : queryRange();
		ItemList itemList = element().items(scope, condition, range, session());
		applyFilter(itemList);
		filterTimezone(itemList, range);
		return itemList;
	}

	@Override
	protected boolean canCreateClusters() {
		return false;
	}

	@Override
	protected void init() {
		TimeScaleHandler timeScaleHandler = buildTimeScaleHandler();
		buildNavigatorDisplay(timeScaleHandler);
		super.init();
		navigatorDisplay.personifyOnce(id());
		if (!isNavigatorVisible()) hideNavigator();
		loadTimezoneOffset();
	}

	protected void refresh(Instant instant) {
		refresh();
	}

	protected void refresh(TimeRange range) {
		refresh();
	}

	private void buildNavigatorDisplay(TimeScaleHandler timeScaleHandler) {
		navigatorDisplay.timeScaleHandler(timeScaleHandler);
		configureNavigatorDisplay(navigatorDisplay, timeScaleHandler);
		add(navigatorDisplay);
	}

	private TimeScaleHandler buildTimeScaleHandler() {
		TimeScaleHandler.Bounds bounds = new TimeScaleHandler.Bounds();
		List<TimeScale> scales = element().scales();
		Map<TimeScale, Bounds.Zoom> zoomMap = new HashMap<>();

		bounds.rangeLoader(() -> {
			TimeRange range = element().range(session());
			return new TimeRange(range.from(), range.to(), TimeScale.Minute);
		});
		bounds.mode(Bounds.Mode.ToTheLast);

		scales.forEach(scale -> zoomMap.put(scale, new Bounds.Zoom().min(1).max(maxZoom())));
		bounds.zooms(zoomMap);

		TimeScaleHandler timeScaleHandler = new TimeScaleHandler(bounds, scales, scales.get(0));
		timeScaleHandler.availableScales(scales);
		configureTimeScaleHandler(timeScaleHandler, element().range(session()), scales);

		return timeScaleHandler;
	}

	@Override
	public boolean dirty() {
		return super.dirty() || !equalsRange();
	}

	protected boolean equalsRange() {
		TimeRange range = timeScaleHandler().range();
		return range() != null && range().from() == range.from() && range().scale() == range.scale();
	}

	protected abstract int maxZoom();
	protected abstract void configureTimeScaleHandler(TimeScaleHandler timeScaleHandler, TimeRange range, List<TimeScale> scales);
	protected abstract void configureNavigatorDisplay(N navigatorDisplay, TimeScaleHandler timeScaleHandler);
	protected abstract void filterTimezone(ItemList itemList, TimeRange range);
	protected abstract TimeRange queryRange();
	protected abstract TimeScaleHandler timeScaleHandler();
	protected abstract void showNavigator();
	protected abstract void hideNavigator();
	protected abstract void loadTimezoneOffset();

	private boolean isNavigatorVisible() {
		return element().temporalFilterVisible(defaultScope(), session());
	}

	protected boolean showAll() {
		return !element().temporalFilterEnabled(defaultScope(), session());
	}
}
