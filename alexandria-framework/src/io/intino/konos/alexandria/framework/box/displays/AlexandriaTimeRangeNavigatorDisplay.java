package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.framework.box.displays.builders.RangeBuilder;
import io.intino.konos.alexandria.framework.box.displays.builders.ScaleBuilder;
import io.intino.konos.alexandria.framework.box.displays.notifiers.AlexandriaTimeRangeNavigatorDisplayNotifier;
import io.intino.konos.alexandria.framework.box.helpers.TimeScaleHandler;
import io.intino.konos.alexandria.framework.box.model.TimeRange;
import io.intino.konos.alexandria.framework.box.model.TimeScale;
import io.intino.konos.alexandria.framework.box.schemas.RequestRange;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlexandriaTimeRangeNavigatorDisplay extends AlexandriaNavigatorDisplay<AlexandriaTimeRangeNavigatorDisplayNotifier> {
	private List<Consumer<TimeRange>> moveListeners = new ArrayList<>();
	private List<Consumer<TimeRange>> moveNextListeners = new ArrayList<>();
	private List<Consumer<TimeRange>> movePreviousListeners = new ArrayList<>();
	private List<Consumer<TimeRange>> fromListeners = new ArrayList<>();
	private List<Consumer<TimeRange>> scaleListener = new ArrayList<>();
	private List<Consumer<TimeRange>> toListeners = new ArrayList<>();

	public AlexandriaTimeRangeNavigatorDisplay(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();

		TimeRange range = timeScaleHandler().range();
		notifier.refreshScales(ScaleBuilder.buildList(scales(), currentLanguage()));
		notifier.refreshZoomRange(RangeBuilder.build(timeScaleHandler().zoomRange()));
		notifier.refreshOlapRange(RangeBuilder.build(timeScaleHandler().boundsRange()));
		notifier.refreshRange(RangeBuilder.build(range));
	}

	@Override
	protected void addListeners(TimeScaleHandler timeScaleHandler) {
		timeScaleHandler.onRangeChange(tr -> notifier.refreshRange(RangeBuilder.build(tr)));
		timeScaleHandler.onScaleChange(tr -> notifier.refreshRange(RangeBuilder.build(tr)));
	}

	public void onMove(Consumer<TimeRange> listener) {
		this.moveListeners.add(listener);
	}

	public void onMoveNext(Consumer<TimeRange> listener) {
		this.moveNextListeners.add(listener);
	}

	public void onMovePrevious(Consumer<TimeRange> listener) {
		this.movePreviousListeners.add(listener);
	}

	public void onFromChange(Consumer<TimeRange> listener) {
		this.fromListeners.add(listener);
	}

	public void onToChange(Consumer<TimeRange> listener) {
		this.toListeners.add(listener);
	}

	public void selectScale(String scale) {
		timeScaleHandler().updateScale(TimeScale.valueOf(scale));
		notifier.refreshRange(RangeBuilder.build(timeScaleHandler().range()));
	}

	public void moveNext() {
		TimeRange timeRange = timeScaleHandler().moveRight(1);
		moveNextListeners.forEach(l -> l.accept(timeRange));
	}

	public void movePrevious() {
		TimeRange timeRange = timeScaleHandler().moveLeft(1);
		movePreviousListeners.forEach(l -> l.accept(timeRange));
	}

	public void move(RequestRange range) {
		TimeRange timeRange = timeScaleHandler().move(range.from(), range.to());
		moveListeners.forEach(l -> l.accept(timeRange));
	}

	public void selectFrom(Instant value) {
		TimeRange timeRange = timeScaleHandler().updateRangeFrom(value);
		notifier.refreshRange(RangeBuilder.build(timeScaleHandler().range()));
		fromListeners.forEach(l -> l.accept(timeRange));
	}

	public void selectTo(Instant value) {
		TimeRange timeRange = timeScaleHandler().updateRangeTo(value);
		notifier.refreshRange(RangeBuilder.build(timeScaleHandler().range()));
		toListeners.forEach(l -> l.accept(timeRange));
	}

}