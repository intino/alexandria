package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Mark;
import io.intino.alexandria.schemas.Selected;
import io.intino.alexandria.schemas.ToolbarState;
import io.intino.alexandria.ui.displays.components.slider.Animation;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.BaseSliderNotifier;

import java.text.NumberFormat;
import java.util.List;
import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class BaseSlider<DN extends BaseSliderNotifier, B extends Box> extends AbstractBaseSlider<DN, B> {
	private Ordinal ordinal = null;
	private java.util.List<Ordinal> ordinalList = new ArrayList<>();
	private Animation animation;
	private ChangeListener changeListener = null;
	private Timer playerStepTimer = null;
	private boolean readonly;
	private java.util.List<BaseSlider<DN, B>> observers = new ArrayList<>();
	protected Object value;
	protected Range range = null;

	public BaseSlider(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		refresh();
	}

	public BaseSlider onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T value() {
    	return (T) value;
	}

	public Ordinal ordinal() {
		return ordinal;
	}

	public void selectOrdinal(String name) {
		selectOrdinal(name, value);
	}

	public BaseSlider add(Ordinal ordinal) {
		_add(ordinal);
		return this;
	}

	public BaseSlider ordinal(Ordinal ordinal) {
		this.ordinal = ordinal;
		updateRange();
		return this;
	}

	public Range range() {
		return range;
	}

	public boolean readonly() {
		return readonly;
	}

	public void value(Object value) {
		if (!checkRange(value)) return;
		_value(value);
		notifyChange();
	}

	public void addObserver(BaseSlider slider) {
		this.observers.add(slider);
	}

	public BaseSlider<DN, B> readonly(boolean value) {
		_readonly(value);
		notifier.refreshReadonly(value);
		return this;
	}

	public BaseSlider<DN, B> marks(Long... marks) {
		return marks(Arrays.stream(marks).map(this::markOf).collect(toList()));
	}

	public BaseSlider<DN, B> marks(Mark... marks) {
		return marks(Arrays.stream(marks).collect(toList()));
	}

	public BaseSlider<DN, B> marks(java.util.List<Mark> markList) {
		notifier.refreshMarks(markList);
		return this;
	}

	protected Mark markOf(long value) {
		return new Mark().value(value).label(format(value));
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
		notifier.refreshOrdinals(schemaOrdinals());
		refreshSelected(selectedValue());
		notifier.refreshRange(rangeSchema());
		if (ordinal() != null) notifier.refreshSelectedOrdinal(ordinal().name());
	}

	ChangeListener changeListener() {
		return changeListener;
	}

	void notifyChange() {
		notifyChange(value());
	}

	void notifyChange(Object value) {
		refreshSelected(schemaOf(value));
		notifier.refreshToolbar(toolbarState());
		notifyObservers();
		notifyListener();
	}

	io.intino.alexandria.schemas.Range rangeSchema() {
		return new io.intino.alexandria.schemas.Range().min(range().min()).max(range.max());
	}

	void notifyListener() {
		if (changeListener == null) return;
		changeListener.accept(new ChangeEvent(this, value));
	}

	private void notifyObservers() {
		observers.forEach(o -> {
			o._value(value());
			o.refreshSelected(selectedValue());
			o.notifier.refreshToolbar(toolbarState());
		});
	}

	public abstract void reset();
	abstract boolean checkRange(Object value);
	public abstract void previous();
	public abstract void next();
	abstract boolean canPrevious();
	abstract boolean canNext();
	abstract void nextValue();
	abstract <T> void _value(T value);
	abstract void _range(long min, long max);
	abstract String format(long value);
	abstract void updateRange();
	abstract <T extends Selected> void refreshSelected(T schema);
	abstract Selected schemaOf(Object value);

	public BaseSlider _add(Ordinal ordinal) {
		this.ordinalList.add(ordinal);
		if (this.ordinal == null) ordinal(ordinalList.get(ordinalList.size()-1));
		return this;
	}

	protected BaseSlider _animation(int interval, boolean loop) {
		this.animation = new Animation().interval(interval).loop(loop);
		return this;
	}

	protected BaseSlider _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	void selectOrdinal(String name, Object value) {
		ordinal(ordinals().stream().filter(o -> o.name().equals(name)).findFirst().orElse(null));
		notifier.refreshSelectedOrdinal(name);
		value(value);
	}

	ToolbarState toolbarState() {
		return new ToolbarState().canPrevious(canPrevious()).canNext(canNext()).playing(this.playerStepTimer != null);
	}

	List<Ordinal> defaultOrdinals() {
		return List.of(
				new Ordinal() {
					@Override
					public String name() {
						return "ordinal";
					}

					@Override
					public String label() {
						return "Ordinal";
					}

					@Override
					public int step() {
						return 1;
					}

					@Override
					public Formatter formatter(String language) {
						return value -> NumberFormat.getNumberInstance(Locale.forLanguageTag(language)).format(value);
					}
				}
		);
	}

	private List<io.intino.alexandria.schemas.Ordinal> schemaOrdinals() {
		return ordinals().stream().map(this::ordinalOf).collect(toList());
	}

	private List<Ordinal> ordinals() {
		return ordinalList.isEmpty() ? defaultOrdinals() : ordinalList;
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
		this.playerStepTimer = new Timer("playerStepTimer");
		this.playerStepTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				playerStep();
				notifier.refreshToolbar(toolbarState());
			}
		}, this.animation.interval);
	}

	protected boolean canLoop() {
		return animation != null && animation.loop;
	}

	private Selected selectedValue() {
		return schemaOf(value);
	}

}