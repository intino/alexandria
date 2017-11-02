package io.intino.konos.server.activity.displays.catalogs;

import io.intino.konos.Box;
import io.intino.konos.server.activity.displays.catalogs.model.TemporalCatalog;
import io.intino.konos.server.activity.displays.catalogs.model.views.DisplayView;
import io.intino.konos.server.activity.displays.catalogs.navigators.NavigatorDisplay;
import io.intino.konos.server.activity.displays.catalogs.views.CatalogViewDisplay;
import io.intino.konos.server.activity.displays.catalogs.views.CatalogViewListDisplay;
import io.intino.konos.server.activity.displays.elements.model.AbstractView;
import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.elements.model.ItemList;
import io.intino.konos.server.activity.displays.elements.model.TimeRange;
import io.intino.konos.server.activity.displays.elements.model.TimeScale;
import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.molds.TemporalStampDisplay;
import io.intino.konos.server.activity.displays.molds.model.stamps.Display;
import io.intino.konos.server.activity.helpers.Bounds;
import io.intino.konos.server.activity.helpers.TimeScaleHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TemporalCatalogDisplay<DN extends TemporalCatalogDisplayNotifier, N extends NavigatorDisplay> extends AbstractCatalogDisplay<TemporalCatalog, DN> {
	private N navigatorDisplay = null;

	public TemporalCatalogDisplay(Box box) {
		super(box);
	}

	public TemporalCatalogDisplay(Box box, N navigatorDisplay) {
		super(box);
		this.navigatorDisplay = navigatorDisplay;
	}

	public void selectRange(TimeRange range) {
		dirty(true);
		TimeScale scale = range.scale();
		TimeScale referenceScale = (scale.ordinal() > TimeScale.Day.ordinal()) ? scale : TimeScale.Day;
		timeScaleHandler().updateRange(range.from(), referenceScale.addTo(range.to(), 1), false);
	}

	@Override // TODO Mario al cambiar de namespace hay que invocar a esto
	public void reset() {
		dirty(true);
		resetViews();
		this.refresh();
		reloadGroupings();
	}

	private void resetViews() {
		child(CatalogViewListDisplay.class).viewList().forEach(CatalogViewDisplay::reset);
	}

	@Override
	public StampDisplay display(String stampName) {
		Display stamp = (Display) stamp(stampName);

		TemporalStampDisplay display = stamp.instance();
		display.range(timeScaleHandler().range());

		return display;
	}

	@Override
	public void refreshView() {
		super.refreshView();

		currentView().ifPresent(catalogView -> {
			AbstractView view = views().stream().filter(v -> v.name().equals(catalogView.view().name())).findFirst().orElse(null);
			if (view != null && view instanceof DisplayView && ((DisplayView)view).hideNavigator())
				hideNavigator();
			else
				showNavigator();
		});
	}

	@Override
	public TimeScale scale() {
		return timeScaleHandler().range().scale();
	}

	@Override
	public void refresh() {
		resetGrouping();
		createGroupingManager();
		refreshView();
	}

	@Override
	public Item rootItem(List<Item> itemList) {
		return element().rootItem(itemList, queryRange(), username());
	}

	@Override
	public Item defaultItem(String id) {
		return element().defaultItem(id, queryRange(), username());
	}

	@Override
	protected ItemList itemList(String condition) {
		if (!equalsRange()) resetGrouping();
		ItemList itemList = element().items(condition, queryRange(), username());
		applyFilter(itemList);
		if (!equalsRange()) reloadGroupings();
		range(timeScaleHandler().range());
		filterTimezone(itemList, range());
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
		navigatorDisplay.personify();
	}

	private TimeScaleHandler buildTimeScaleHandler() {
		TimeRange range = element().range(username());
		TimeScaleHandler.Bounds bounds = new TimeScaleHandler.Bounds();
		List<TimeScale> scales = element().scales();
		Map<TimeScale, Bounds.Zoom> zoomMap = new HashMap<>();

		bounds.range(new TimeRange(range.from(), range.to(), TimeScale.Minute));
		bounds.mode(Bounds.Mode.ToTheLast);

		scales.forEach(scale -> zoomMap.put(scale, new Bounds.Zoom().min(1).max(maxZoom())));
		bounds.zooms(zoomMap);

		TimeScaleHandler timeScaleHandler = new TimeScaleHandler(bounds, scales, scales.get(0));
		timeScaleHandler.availableScales(element().localizedScales());
		configureTimeScaleHandler(timeScaleHandler, range, scales);

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

	private void reloadGroupings() {
		sendCatalog();
	}
}
