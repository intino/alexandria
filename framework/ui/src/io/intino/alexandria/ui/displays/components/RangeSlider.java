package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.RangeSelected;
import io.intino.alexandria.schemas.RangeValue;
import io.intino.alexandria.schemas.Selected;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.displays.components.slider.Range;
import io.intino.alexandria.ui.displays.notifiers.RangeSliderNotifier;

public class RangeSlider<DN extends RangeSliderNotifier, B extends Box> extends AbstractRangeSlider<DN, B> {

	public RangeSlider(B box) {
		super(box);
		_value(0, 0);
	}

	@Override
	public void reset() {
		Range range = range();
		_value(range.min(), range.max());
	}

	protected RangeSlider<DN, B> _value(long from, long to) {
		return _value(new RangeValue().from(from).to(to));
	}

	protected RangeSlider<DN, B> _value(RangeValue value) {
		this.value = value;
		return this;
	}

	public RangeSlider<DN, B> range(long min, long max) {
		_range(min, max);
		notifier.refreshRange(rangeSchema());
		return this;
	}

	public String formattedValue(long value) {
		return format(value);
	}

	public void moved(RangeValue value) {
		notifier.refreshSelected(schemaOf(value));
		notifier.refreshToolbar(toolbarState());
	}

	public void update(RangeValue value) {
		value(value);
	}

	@Override
	String format(long value) {
		Ordinal ordinal = ordinal();
		if (ordinal == null) ordinal = defaultOrdinals().get(0);
		return ordinal != null ? ordinal.formatter(language()).format(value) : String.valueOf(value);
	}

	@Override
	void updateRange() {
	}

	@Override
	<T extends Selected> void refreshSelected(T schema) {
		notifier.refreshSelected((RangeSelected) schema);
	}

	@Override
	protected void _value(Object value) {
		this.value = value;
	}

	@Override
	protected void _range(long min, long max) {
		this.range = new Range().min(min).max(max);
		RangeValue value = value();
		if (value.from() < min) value.from(min);
		if (value.to() < min) value.to(min);
		if (value.from() > max) value.from(max);
		if (value.to() > max) value.to(max);
		_value(value);
	}

	@Override
	public void previous() {
		RangeValue values = (RangeValue)value;
		values.from(values.from()-1);
		values.to(values.to()-1);
		value(values);
	}

	@Override
	public void next() {
		RangeValue values = (RangeValue)value;
		values.from(values.from()+1);
		values.to(values.to()+1);
		value(values);
	}

	@Override
	protected boolean canPrevious() {
		return ((RangeValue)value()).from() > range.min;
	}

	@Override
	protected boolean canNext() {
		return ((RangeValue)value()).to() < range.max;
	}

	@Override
	protected void nextValue() {
		RangeValue value = value();
		if (value.to() >= range.max) {
			if (canLoop()) {
				value.to(value.to()-value.from()-1);
				value.from(range.min()-1);
				value(value);
			}
			else return;
		}
		value.from(value.from()+1);
		value.to(value.to()+1);
		value(value);
	}

	@Override
	protected boolean checkRange(Object value) {
		RangeValue values = (RangeValue) value;
		if (values.to() > range.max) return false;
		if (values.from() < range.min) return false;
		return true;
	}

	@Override
	protected RangeSelected schemaOf(Object value) {
		RangeValue values = (RangeValue)value;
		String formattedFrom = formattedValue(values.from());
		String formattedTo = formattedValue(values.to());
		return (RangeSelected) new RangeSelected().to(values.to()).formattedTo(formattedTo).value(values.from()).formattedValue(formattedFrom);
	}

}