package io.intino.konos.alexandria.framework.box.helpers;

import io.intino.konos.alexandria.framework.box.model.TimeRange;
import io.intino.konos.alexandria.framework.box.model.TimeScale;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TimeScaleHandler {
	private final Bounds bounds;
	private final List<TimeScale> availableScales = new ArrayList<>();
	private final List<TimeScale> scales;
	private final List<Consumer<TimeRange>> onRangeChangedListeners = new ArrayList<>();
	private final List<Consumer<TimeRange>> onScaleChangedListeners = new ArrayList<>();
	private final List<Consumer<TimeRange>> onNotValidRangeListeners = new ArrayList<>();
	private TimeScale currentScale;
	private TimeRange timeRange;
	private Instant lastLoadedFrom;
	private Instant lastLoadedTo;

	public TimeScaleHandler(Bounds bounds, List<TimeScale> scales, TimeScale initialScale) {
		this.bounds = bounds;
		this.scales = scales;
		this.currentScale = initialScale;
		this.timeRange = initialTimeRange(currentScale);
	}

	public TimeRange boundsRange() {
		return bounds.range();
	}

	public List<TimeScale> availableScales() {
		return availableScales;
	}

	public void availableScales(List<TimeScale> scales) {
		this.availableScales.clear();
		this.availableScales.addAll(scales);
	}

	public TimeRange leftRange() {
		Instant from = checkWithFrom(currentScale.addTo(timeRange.from(), -(currentScale.instantsBetween(timeRange.from(), timeRange.to()) - 1)));
		return timeRangeOf(from, currentScale.addTo(timeRange.from(), -1), currentScale);
	}

	public TimeRange rightRange() {
		Instant to = checkWithTo(currentScale.addTo(timeRange.to(), currentScale.instantsBetween(timeRange.from(), timeRange.to()) - 1));
		return timeRangeOf(currentScale.addTo(timeRange.to(), 1), to, currentScale);
	}

	void refresh() {
		this.timeRange = initialTimeRange(currentScale);
		resetLoadedPoints();
	}

	private TimeRange timeRangeOf(Instant from, Instant to, TimeScale scale) {
		return checkMinRange(new TimeRange(scale.normalise(from), scale.normalise(to), scale));
	}

	private TimeRange leftRange(long size) {
		Instant to = checkWithTo(lastLoadedFrom());
		lastLoadedFrom = checkWithFrom(currentScale.addTo(to, -size));
		return timeRangeOf(lastLoadedFrom, to, currentScale);
	}

	private TimeRange rightRange(long size) {
		Instant from = checkWithFrom(lastLoadedTo());
		lastLoadedTo = checkWithTo(currentScale.addTo(from, size));
		return timeRangeOf(from, lastLoadedTo, currentScale);
	}

	public TimeRange range() {
		return timeRange;
	}

	public List<TimeScale> scales() {
		return scales;
	}

	public void onRangeChange(Consumer<TimeRange> listener) {
		onRangeChangedListeners.add(listener);
	}

	public void onScaleChange(Consumer<TimeRange> listener) {
		onScaleChangedListeners.add(listener);
	}

	public void updateRange(Instant from, Instant to) {
		updateRange(from, to, true);
	}

	public void onNotValidRange(Consumer<TimeRange> consumer) {
		onNotValidRangeListeners.add(consumer);
	}

	public ZoomRange zoomRange() {
		Map<TimeScale, io.intino.konos.alexandria.framework.box.helpers.Bounds.Zoom> zooms = bounds.zooms();
		long max = scales.get(0).toMillis(zooms.get(scales.get(0)).max());
		long min = scales.get(scales.size() - 1).toMillis(zooms.get(scales.get(scales.size() - 1)).min());
		return new ZoomRange(max, min);
	}

	public TimeRange updateRangeFrom(Instant from) {
		if (from.isAfter(range().to())) return range();
		TimeRange range = validOlapRangeFor(from, range().to());
		updateRange(range.from(), range.to(), false);
		return range;
	}

	public TimeRange updateRangeTo(Instant to) {
		if (range().from().isAfter(to)) return range();
		TimeRange range = validOlapRangeFor(range().from(), to);
		updateRange(range.from(), range.to(), false);
		return range;
	}

	public void updateRange(Instant from, Instant to, boolean updateScaleIfNeeded) {
		from = currentScale.normalise(from);
		to = currentScale.normalise(to);

		if (isOutOfTimeline(from, to)) {
			tryToUpdateToValidRange(updateScaleIfNeeded);
			return;
		}

		long numberOfInstants = currentScale.instantsBetween(from, to);
		if (updateScaleIfNeeded && isBelowLowerBoundary(numberOfInstants)) {
			tryToChangeToLowerScale(from, to);
		} else if (updateScaleIfNeeded && isAboveUpperBoundary(numberOfInstants)) {
			tryToChangeToBiggerScale(from, to);
		} else {
			notifyRangeChange(timeRangeOf(from, to, currentScale));
		}
	}

	public void updateInstant(Instant instant, TimeScale scale) {
		TimeRange range = new TimeRange(instant, instant, scale);

		if (isOutOfTimeline(range.from(), range.to()))
			notifyNotValidRange(range);

		if (range.scale() != currentScale)
			notifyScaleChange(range);
		else
			notifyRangeChange(range);
	}

	public TimeRange moveLeft() {
		return moveLeft(5);
	}

	public TimeRange moveLeft(int countSteps) {
		Instant from = currentScale.addTo(this.timeRange.from(), -countSteps);
		Instant to = currentScale.addTo(this.timeRange.to(), -countSteps);
		return move(from, to);
	}

	public TimeRange moveLeft(long time) {
		Instant from = this.timeRange.from().minusMillis(time);
		Instant to = this.timeRange.to().minusMillis(time);
		return move(from, to);
	}

	public TimeRange moveRight() {
		return moveRight(5);
	}

	public TimeRange moveRight(int countSteps) {
		Instant from = currentScale.addTo(this.timeRange.from(), countSteps);
		Instant to = currentScale.addTo(this.timeRange.to(), countSteps);
		return move(from, to);
	}

	public TimeRange moveRight(long time) {
		Instant from = this.timeRange.from().plusMillis(time);
		Instant to = this.timeRange.to().plusMillis(time);
		return move(from, to);
	}

	public TimeRange move(Instant newFrom, Instant newTo) {
		newFrom = currentScale.normalise(newFrom);
		newTo = currentScale.normalise(newTo);

		if (timeRange.allInstants().count() != currentScale.instantsBetween(newFrom, newTo)) {
			if (movedLeft(newFrom)) {
				return move(currentScale.addTo(newTo, -timeRange.allInstants().count() + 1), newTo);
			}
			return move(newFrom, currentScale.addTo(newFrom, timeRange.allInstants().count() - 1));
		}

		long movement = movementSize(newFrom);
		if (movedLeft(newFrom)) {
			notifyRangeChange(timeRangeOf(newFrom, newTo, currentScale));
			return leftRange(movement);
		}

		notifyRangeChange(timeRangeOf(newFrom, newTo, currentScale));
		return rightRange(movement);
	}

	public void updateScale(TimeScale scale) {
		if (currentScale == scale) {
			return;
		}
		resetLoadedPoints();
		currentScale = scale;
		notifyScaleChange(calculateNewRange(timeRange.from(), timeRange.to(), currentScale));
	}

	public void resetLoadedPoints() {
		lastLoadedFrom = null;
		lastLoadedTo = null;
	}

	private void tryToUpdateToValidRange(boolean updateScaleIfNeeded) {
		if (currentScale != scales.get(0)) {
			if (updateScaleIfNeeded) biggerScale();
			notifyScaleChange(initialTimeRange(currentScale));
		} else {
			notifyNotValidRange(timeRange);
		}
	}

	private long movementSize(Instant newFrom) {
		if (movedLeft(newFrom)) {
			return currentScale.instantsBetween(newFrom, timeRange.from());
		}
		return currentScale.instantsBetween(timeRange.from(), newFrom);
	}

	private boolean movedLeft(Instant newFrom) {
		return !newFrom.equals(timeRange.from()) && newFrom.isBefore(timeRange.from());
	}

	private Instant lastLoadedFrom() {
		if (lastLoadedFrom == null) {
			lastLoadedFrom = timeRange.from();
		}
		return lastLoadedFrom;
	}

	private Instant lastLoadedTo() {
		if (lastLoadedTo == null) {
			lastLoadedTo = timeRange.to();
		}
		return lastLoadedTo;
	}

	private void tryToChangeToLowerScale(Instant from, Instant to) {
		if (currentScale != scales.get(scales.size() - 1)) {
			resetLoadedPoints();
			notifyScaleChange(calculateNewRange(from, to, lowerScale()));
		} else {
			notifyNotValidRange(timeRange);
		}
	}

	private void tryToChangeToBiggerScale(Instant from, Instant to) {
		if (currentScale != scales.get(0)) {
			resetLoadedPoints();
			notifyScaleChange(calculateNewRange(from, to, biggerScale()));
		} else {
			notifyNotValidRange(timeRange);
		}
	}

	private void notifyRangeChange(TimeRange timeRange) {
		this.timeRange = timeRange;
		onRangeChangedListeners.parallelStream().forEach(c -> c.accept(timeRange));
	}

	private void notifyNotValidRange(TimeRange timeRange) {
		this.timeRange = timeRange;
		onNotValidRangeListeners.parallelStream().forEach(c -> c.accept(timeRange));
	}

	private void notifyScaleChange(TimeRange timeRange) {
		this.timeRange = timeRange;
		this.currentScale = timeRange.scale();
		onScaleChangedListeners.parallelStream().forEach(c -> c.accept(timeRange));
		resetLoadedPoints();
	}

	private boolean isOutOfTimeline(Instant from, Instant to) {
		return boundsRange().from().isAfter(from) || boundsRange().to().isBefore(to);
	}

	private boolean isAboveUpperBoundary(long numberOfInstants) {
		return numberOfInstants > bounds.zooms().get(currentScale).max();
	}

	private boolean isBelowLowerBoundary(long numberOfInstants) {
		return numberOfInstants < bounds.zooms().get(currentScale).min();
	}

	private TimeScale lowerScale() {
		currentScale = scales.get(scales.indexOf(currentScale) + 1);
		return currentScale;
	}

	private TimeScale biggerScale() {
		currentScale = scales.get(scales.indexOf(currentScale) - 1);
		return currentScale;
	}

	private TimeRange initialTimeRange(TimeScale scale) {
		long halfRangeSize = 1;
		if (bounds.zooms().size() > 0) {
			halfRangeSize = (long) bounds.zooms().get(scale).max() / 2;
			if (bounds.mode() == io.intino.konos.alexandria.framework.box.helpers.Bounds.Mode.ToTheLast) {
				Instant to = scale.normalise(bounds.range().to());
				Instant from = scale.addTo(to, -halfRangeSize);
				return timeRangeOf(checkWithFrom(from), to, scale);
			}
		}
		Instant from = scale.normalise(bounds.range().from());
		Instant to = scale.addTo(from, halfRangeSize);
		return timeRangeOf(from, checkWithTo(to), scale);
	}

	private TimeRange checkMinRange(TimeRange timeRange) {
		TimeScale scale = timeRange.scale();
		if (scale.normalise(timeRange.from()).compareTo(timeRange.to()) != 0) return timeRange;
		TimeScale referenceScale = (scale.ordinal() > TimeScale.Day.ordinal()) ? scale : TimeScale.Day;
		return new TimeRange(timeRange.from(), referenceScale.addTo(timeRange.to(), 1), scale);
	}

	private TimeRange calculateNewRange(Instant from, Instant to, TimeScale scale) {
		from = scale.normalise(from);
		to = scale.normalise(to);
		Instant center = scale.addTo(from, scale.instantsBetween(from, to) / 2);
		long halfRangeSize = (long) bounds.zooms().get(scale).max() / 2;
		Instant start = checkWithFrom(addToCenter(center, -halfRangeSize));
		if (bounds.zooms().get(scale).max() == halfRangeSize * 2) {
			return timeRangeOf(start, checkWithTo(addToCenter(center, halfRangeSize)), scale);
		}
		return timeRangeOf(start, checkWithTo(addToCenter(center, halfRangeSize)), scale);
	}

	private Instant addToCenter(Instant center, long halfRangeSize) {
		return currentScale.addTo(center, halfRangeSize);
	}

	private Instant checkWithFrom(Instant from) {
		Instant timelineFromWithAir = currentScale.addTo(bounds.range().from(), 0);
		Instant fromWithAir = currentScale.addTo(from, 0);
		return from.isBefore(bounds.range().from()) ? timelineFromWithAir : fromWithAir;
	}

	private Instant checkWithTo(Instant to) {
		Instant timelineToWithAir = currentScale.addTo(bounds.range().to(), 0);
		Instant toWithAir = currentScale.addTo(to, 0);
		return to.isAfter(bounds.range().to()) ? timelineToWithAir : toWithAir;
	}

	private TimeRange validOlapRangeFor(Instant from, Instant to) {
		TimeRange range = boundsRange();

		if (range.from().isAfter(from))
			from = range.from();

		if (range.to().isBefore(to))
			to = range.to();

		return timeRangeOf(from, to, currentScale);
	}

	public static class Bounds {
		private TimeRange range;
		private io.intino.konos.alexandria.framework.box.helpers.Bounds.Mode mode;
		private Map<TimeScale, io.intino.konos.alexandria.framework.box.helpers.Bounds.Zoom> zooms;

		public TimeRange range() {
			return range;
		}

		public Bounds range(TimeRange range) {
			this.range = range;
			return this;
		}

		public io.intino.konos.alexandria.framework.box.helpers.Bounds.Mode mode() {
			return mode;
		}

		public Bounds mode(io.intino.konos.alexandria.framework.box.helpers.Bounds.Mode mode) {
			this.mode = mode;
			return this;
		}

		public Map<TimeScale, io.intino.konos.alexandria.framework.box.helpers.Bounds.Zoom> zooms() {
			return zooms;
		}

		public Bounds zooms(Map<TimeScale, io.intino.konos.alexandria.framework.box.helpers.Bounds.Zoom> zooms) {
			this.zooms = zooms;
			return this;
		}

		public interface Zoom {
			int min();
			int max();
		}
	}

}
