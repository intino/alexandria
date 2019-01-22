package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.providers.TemporalCatalogViewDisplayProvider;
import io.intino.alexandria.ui.helpers.Bounds;
import io.intino.alexandria.ui.helpers.TimeScaleHandler;
import io.intino.alexandria.ui.model.*;
import io.intino.alexandria.ui.model.catalog.Scope;
import io.intino.alexandria.ui.model.catalog.TemporalFilter;
import io.intino.alexandria.ui.model.mold.stamps.EmbeddedDialog;
import io.intino.alexandria.ui.model.mold.stamps.EmbeddedDisplay;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AlexandriaTemporalCatalog<DN extends AlexandriaDisplayNotifier, N extends AlexandriaNavigator> extends AlexandriaAbstractCatalog<TemporalCatalog, DN> implements TemporalCatalogViewDisplayProvider {
	private N navigatorDisplay = null;
	private TimeRange moreItemsRange = null;

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
		TimeRange bounds = elementRange();

		if (from.isBefore(bounds.from())) from = bounds.to();
		if (to.isAfter(bounds.to())) to = bounds.to();

		timeScaleHandler().updateRange(from, to, false);
	}

	@Override
	public void range(TimeRange range) {
		super.range(range);
		moreItemsRange = null;
	}

	@Override
	public void clearFilter() {
		super.clearFilter();
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
		child(AlexandriaCatalogViewList.class).displayViewList().forEach(AlexandriaElementView::reset);
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

		currentView().ifPresent(viewDisplay -> {
			View view = views().stream().filter(v -> v.name().equals(viewDisplay.view().name())).findFirst().orElse(null);
			if (view != null && view.hideNavigator())
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
		return element().rootItem(itemList, queryRange(range()), session());
	}

	@Override
	public Item defaultItem(String id) {
		return element().defaultItem(id, queryRange(range()), session());
	}

	@Override
	protected void loadItemList(String condition) {
		if (!dirty() && itemList != null) return;
		range(timeScaleHandler().range());
		itemList = filteredItemList(scopeWithAttachedGrouping(), condition);
		dirty(false);
	}

	@Override
	public synchronized void loadMoreItems(String condition, Sorting sorting, int minCount) {
		ItemList newItemList = new ItemList();
		Instant boundingRangeTo = range().from();
		Instant boundingRangeFrom = range().to();

		while(newItemList.size() < minCount && this.timeScaleHandler().boundsRange().from().isBefore(this.timeScaleHandler().range().from())) {
			TimeRange currentRange = moreItemsRange != null ? moreItemsRange : new TimeRange(range().from(), range().to(), range().scale());
			long movement = currentRange.scale().instantsBetween(currentRange.from(), currentRange.to());
			Instant from = currentRange.scale().addTo(currentRange.from(), sorting != null && sorting.mode() == Sorting.Mode.Descendant ? movement : -movement);
			Instant to = currentRange.scale().addTo(currentRange.to(), sorting != null && sorting.mode() == Sorting.Mode.Descendant ? movement : -movement);

			if (sorting != null && sorting.mode() == Sorting.Mode.Descendant) boundingRangeTo = to;
			else boundingRangeFrom = from;

			moreItemsRange = new TimeRange(from, to, currentRange.scale());

			ItemList itemList = filteredItemList(moreItemsRange, scopeWithAttachedGrouping(), condition);
			if (itemList.size() <= 0) break;

			newItemList.addAll(itemList);
		}

		itemList.addAll(newItemList);
		createGroupingManager(filteredItemList(new TimeRange(boundingRangeFrom, boundingRangeTo, moreItemsRange != null ? moreItemsRange.scale() : range().scale()), defaultScope(),null));
		reloadGroupings();
	}

	@Override
	protected ItemList filteredItemList(Scope scope, String condition) {
		return filteredItemList(range(), scope, condition);
	}

	@Override
	protected void init() {
		TimeScaleHandler timeScaleHandler = buildTimeScaleHandler();
		buildNavigatorDisplay(timeScaleHandler);
		super.init();

		if (element().temporalFilterLayout() != TemporalFilter.Layout.Horizontal)
			refreshNavigatorLayout(element().temporalFilterLayout());

		navigatorDisplay.personifyOnce(id());
		if (isNavigatorVisible()) showNavigator();
		else hideNavigator();

		loadTimezoneOffset();
	}

	protected void refresh(Instant instant) {
		forceRefresh();
	}

	protected void refresh(TimeRange range) {
		forceRefresh();
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
			TimeRange range = elementRange();
			return new TimeRange(range.from(), range.to(), TimeScale.Second);
		});
		bounds.mode(Bounds.Mode.ToTheLast);

		scales.forEach(scale -> zoomMap.put(scale, new Bounds.Zoom().min(1).max(maxZoom())));
		bounds.zooms(zoomMap);

		TimeScaleHandler timeScaleHandler = new TimeScaleHandler(bounds, scales, scales.get(0));
		timeScaleHandler.availableScales(scales);
		configureTimeScaleHandler(timeScaleHandler, elementRange(), scales);

		return timeScaleHandler;
	}

	@Override
	public boolean dirty() {
		return super.dirty() || !equalsRange();
	}

	protected boolean equalsRange() {
		TimeRange range = timeScaleHandler().range();
		return range() != null && range().from().equals(range.from()) && range().scale() == range.scale();
	}

	protected abstract int maxZoom();
	protected abstract void configureTimeScaleHandler(TimeScaleHandler timeScaleHandler, TimeRange range, List<TimeScale> scales);
	protected abstract void configureNavigatorDisplay(N navigatorDisplay, TimeScaleHandler timeScaleHandler);
	protected abstract void filterTimezone(ItemList itemList, TimeRange range);
	protected abstract TimeRange queryRange(TimeRange range);
	protected abstract TimeScaleHandler timeScaleHandler();
	protected abstract void showNavigator();
	protected abstract void hideNavigator();
	protected abstract void loadTimezoneOffset();
	protected abstract void refreshNavigatorLayout(TemporalFilter.Layout layout);

	private boolean isNavigatorVisible() {
		return element().temporalFilterVisible(defaultScope(), session());
	}

	protected boolean showAll() {
		return !element().temporalFilterEnabled(defaultScope(), session());
	}

	private ItemList filteredItemList(TimeRange range, Scope scope, String condition) {
		range = showAll() ? timeScaleHandler().boundsRange() : queryRange(range);
		ItemList itemList = element().items(scope, condition, range, session());
		applyFilter(itemList);
		filterTimezone(itemList, range);
		return itemList;
	}

	private TimeRange elementRange() {
		TimeRange range = element().range(session());
		return range != null ? range : defaultElementRange();
	}

	private TimeRange defaultElementRange() {
		Instant now = Instant.now(Clock.systemUTC());
		return new TimeRange(now, now, TimeScale.Second);
	}

}
