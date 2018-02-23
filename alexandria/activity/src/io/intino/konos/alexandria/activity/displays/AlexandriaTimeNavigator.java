package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.RangeBuilder;
import io.intino.konos.alexandria.activity.displays.builders.ScaleBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaTimeNavigatorNotifier;
import io.intino.konos.alexandria.activity.helpers.TimeScaleHandler;
import io.intino.konos.alexandria.activity.model.TimeRange;
import io.intino.konos.alexandria.activity.model.TimeScale;
import io.intino.konos.alexandria.activity.schemas.DateNavigatorState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AlexandriaTimeNavigator extends AlexandriaNavigator<AlexandriaTimeNavigatorNotifier> {
	private Instant date;
	private TimeScale scale;
	private List<Consumer<Instant>> moveListeners = new ArrayList<>();
	private Timer playerStepTimer = null;
	private Timer moveTimer = null;

	private static final int PlayDelay = 3000;

	public AlexandriaTimeNavigator(Box box) {
		super(box);
	}

	@Override
	protected void addListeners(TimeScaleHandler timeScaleHandler) {
		timeScaleHandler.onRangeChange(this::refreshDate);
		timeScaleHandler.onScaleChange(this::refreshScale);
	}

	@Override
	protected void init() {
		super.init();

		TimeRange range = timeScaleHandler().range();
		scale = range.scale();
		date = scale.normalise(range.from());
		notifier.refreshScales(ScaleBuilder.buildList(scales(), currentLanguage()));
		notifier.refreshScale(scale.name());
		notifier.refreshOlapRange(RangeBuilder.build(timeScaleHandler().boundsRange()));
		notifier.refreshDate(date);
		notifier.refreshState(state());
	}

	public TimeScale scale() {
		return scale;
	}

	public void selectScale(String value) {
		TimeScale scale = TimeScale.valueOf(value);
		updateDate(scale, date);
		timeScaleHandler().updateInstant(date, scale);
		notifyMove(date);
	}

	public Instant date() {
		return date;
	}

	public void selectDate(Instant date) {
		updateDate(scale, date);
		timeScaleHandler().updateInstant(this.date, scale);
		notifyMove(this.date);
	}

	public void lastDate() {
		updateDate(scale, timeScaleHandler().boundsRange().to());
		timeScaleHandler().updateInstant(this.date, scale);
		notifyMove(this.date);
	}

	public void previousDate() {
		addToDate(-1);
		notifyMoveDelayed();
	}

	public void play() {
		playerStep();
		notifier.refreshState(state());
	}

	public void pause() {
		if (this.playerStepTimer == null) return;
		this.playerStepTimer.cancel();
		this.playerStepTimer = null;
		notifier.refreshState(state());
	}

	private void playerStep() {
		if (!canNext()) {
			this.playerStepTimer = null;
			return;
		}

		nextDate();
		this.playerStepTimer = new Timer();
		this.playerStepTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				playerStep();
				notifier.refreshState(state());
			}
		}, PlayDelay);
	}

	public void nextDate() {
		if (date.isAfter(Instant.now())) return;
		addToDate(1);
		notifyMoveDelayed();
	}

	private void notifyMoveDelayed() {
		if (moveTimer != null)
			moveTimer.cancel();

		moveTimer = new Timer();
		moveTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				notifyMove(date);
			}
		}, 500);
	}

	public void onMove(Consumer<Instant> listener) {
		this.moveListeners.add(listener);
	}

	private void addToDate(int count) {
		this.date = scale.addTo(date, count);
		timeScaleHandler().updateInstant(scale.normalise(date), scale);
	}

	private void notifyMove(Instant value) {
		moveListeners.forEach(l -> l.accept(value));
	}

	private DateNavigatorState state() {
		return new DateNavigatorState().canPrevious(canPrevious()).canNext(canNext()).canPlay(canNext()).playing(this.playerStepTimer != null);
	}

	private Boolean canPrevious() {
		TimeRange range = timeScaleHandler().boundsRange();
		return date.isAfter(scale.normalise(range.from()));
	}

	private Boolean canNext() {
		TimeRange range = timeScaleHandler().boundsRange();
		Instant maxDate = scale.addTo(scale.normalise(range.to()), -1);
		return date.isBefore(maxDate) || date.equals(maxDate);
	}

	private void refreshDate(TimeRange timeRange) {
		this.date = scale.normalise(timeRange.from());
		notifier.refreshDate(this.date);
		notifier.refreshState(state());
	}

	private void refreshScale(TimeRange timeRange) {
		scale = timeRange.scale();
		notifier.refreshDate(date);
		notifier.refreshScale(scale.name());
		notifier.refreshState(state());
	}

	private void updateDate(TimeScale scale, Instant date) {
		TimeRange range = timeScaleHandler().boundsRange();
		this.date = scale.normalise(date);
		if (date.isBefore(range.from())) this.date = scale.normalise(range.from());
		if (date.isAfter(range.to())) this.date = scale.normalise(range.to());
	}

}