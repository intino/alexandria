package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Selected;
import io.intino.alexandria.schemas.ToolbarState;
import io.intino.alexandria.ui.displays.components.slider.Animation;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.BaseSliderNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.stream.Collectors.toList;

public abstract class BaseSlider<DN extends BaseSliderNotifier, B extends Box> extends AbstractBaseSlider<DN, B> {
	private long value = 0;
	private Ordinal ordinal = null;
	private java.util.List<Ordinal> ordinalList = new ArrayList<>();
	private Animation animation;
	private ChangeListener changeListener = null;
	private Range range = null;
	private Timer playerStepTimer = null;

	public BaseSlider(B box) {
        super(box);
    }

	public BaseSlider onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public long value() {
    	return value;
	}

	public BaseSlider value(long value) {
		this.value = value;
		return this;
	}

	public BaseSlider add(Ordinal ordinal) {
		this.ordinalList.add(ordinal);
		if (this.ordinal == null && ordinalList.size() > 0) ordinal(ordinalList.get(0));
		return this;
	}

	public BaseSlider animation(int interval, boolean loop) {
		this.animation = new Animation().interval(interval).loop(loop);
		return this;
	}

	public Ordinal ordinal() {
		return ordinal;
	}

	public void selectOrdinal(String name) {
		ordinal(ordinalList.stream().filter(o -> o.name().equals(name)).findFirst().orElse(null));
		notifier.refreshSelectedOrdinal(name);
		update(value);
	}

	public BaseSlider ordinal(Ordinal ordinal) {
		this.ordinal = ordinal;
		updateRange();
		return this;
	}

	public Range range() {
		return range;
	}

	public BaseSlider range(long min, long max) {
		this.range = new Range().min(min).max(max);
		this.value = min;
		return this;
	}

	public void update(long value) {
		if (!checkRange(value)) return;
		this.value = value;
		notifyChange();
	}

	public void previous() {
		update(value-1);
		notifyChange();
	}

	public void next() {
		update(value+1);
		notifyChange();
	}

	public void play() {
		if (this.animation == null) return;
		playerStep();
		notifier.refreshToolbar(toolbarState());
	}

	public void pause() {
		if (this.animation == null) return;
		if (this.playerStepTimer == null) return;
		this.playerStepTimer.cancel();
		this.playerStepTimer = null;
		notifier.refreshToolbar(toolbarState());
	}

	@Override
	public void init() {
		super.init();
		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (notifier == null) return;
		notifier.refreshToolbar(toolbarState());
		notifier.refreshOrdinals(ordinals());
		notifier.refreshSelected(selectedValue());
		notifier.refreshRange(rangeSchema());
		if (ordinal() != null) notifier.refreshSelectedOrdinal(ordinal().name());
	}

	ChangeListener changeListener() {
		return changeListener;
	}

	void notifyChange() {
		notifier.refreshSelected(selectedValue());
		notifier.refreshToolbar(toolbarState());
		notifyListener();
	}

	io.intino.alexandria.schemas.Range rangeSchema() {
		return new io.intino.alexandria.schemas.Range().min(range().min()).max(range.max());
	}

	void notifyListener() {
		if (changeListener == null) return;
		changeListener.accept(new ChangeEvent(this, value));
	}

	abstract String formattedValue();
	abstract void updateRange();

	private List<io.intino.alexandria.schemas.Ordinal> ordinals() {
		return ordinalList.stream().map(this::ordinalOf).collect(toList());
	}

	private io.intino.alexandria.schemas.Ordinal ordinalOf(Ordinal ordinal) {
		return new io.intino.alexandria.schemas.Ordinal().name(ordinal.name()).label(ordinal.label()).step(ordinal.step());
	}

	private void playerStep() {
		if (!canNext() && !canLoop()) {
			this.playerStepTimer = null;
			return;
		}

		nextValue();
		this.playerStepTimer = new Timer();
		this.playerStepTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				playerStep();
				notifier.refreshToolbar(toolbarState());
			}
		}, this.animation.interval);
	}

	private boolean canLoop() {
		return animation != null && animation.loop;
	}

	private Boolean canPrevious() {
		return value > range.min;
	}

	private Boolean canNext() {
		return value < range.max;
	}

	private void nextValue() {
		if (value >= range.max) {
			if (canLoop()) value(range.min()-1);
			else return;
		}
		update(value+1);
		notifyChange();
	}

	private ToolbarState toolbarState() {
		return new ToolbarState().canPrevious(canPrevious()).canNext(canNext()).playing(this.playerStepTimer != null);
	}

	private boolean checkRange(long value) {
		if (value > range.max) return false;
		if (value < range.min) return false;
		return true;
	}

	private Selected selectedValue() {
		String formattedValue = formattedValue();
		return new Selected().value(value).formattedValue(formattedValue);
	}

}