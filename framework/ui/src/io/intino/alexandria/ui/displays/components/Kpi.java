package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.KpiColors;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.kpi.ValueFormatter;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.KpiNotifier;

import java.text.NumberFormat;
import java.util.Locale;

public class Kpi<DN extends KpiNotifier, B extends Box> extends AbstractKpi<B> {
	private ValueFormatter formatter = defaultFormatter();
	private double value;
	private SelectListener selectListener;
	private boolean highlighted = false;

	public Kpi(B box) {
		super(box);
	}

	public Kpi<DN, B> onSelect(SelectListener listener) {
		this.selectListener = listener;
		return this;
	}

	public Kpi<DN, B> formatter(ValueFormatter formatter) {
		this.formatter = formatter;
		return this;
	}

	public Kpi<DN, B> value(double value) {
		_value(value);
		refresh();
		return this;
	}

	public void select() {
		highlighted = !highlighted;
		notifier.refreshHighlighted(highlighted);
		if (selectListener == null) return;
		selectListener.accept(new SelectEvent(this, null));
	}

	@Override
	public <D extends Display> D label(String label) {
		super.label(label);
		if (notifier != null) notifier.refreshLabel(label);
		return (D) this;
	}

	public Kpi<DN, B> colors(String textColor, String backgroundColor) {
		notifier.refreshColors(new KpiColors().text(textColor).background(backgroundColor));
		return this;
	}

	public Kpi<DN, B> highlighted(boolean value) {
		highlighted = value;
		notifier.refreshHighlighted(value);
		return this;
	}

	protected Kpi<DN, B> _value(double value) {
		this.value = value;
		return this;
	}

	@Override
	public void didMount() {
		super.didMount();
		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		notifier.refresh(formatter.format(value, language()));
	}

	private ValueFormatter defaultFormatter() {
		return new ValueFormatter() {
			@Override
			public String format(double value, String language) {
				return formattedNumber(value, Locale.forLanguageTag(language));
			}

			private String formattedNumber(Double value, Locale locale) {
				if (value == null) return "-";
				return NumberFormat.getNumberInstance(locale).format(round(value));
			}

			private double round(double value) {
				long factor = (long) Math.pow(10, 2);
				value = value * factor;
				long tmp = Math.round(value);
				return (double) tmp / factor;
			}
		};
	}
}