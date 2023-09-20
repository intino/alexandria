package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.DateNavigatorInfo;
import io.intino.alexandria.schemas.DateNavigatorSetup;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.DateNavigatorNotifier;
import io.intino.alexandria.ui.model.ScaleFormatter;
import io.intino.alexandria.ui.model.datenavigator.DateNavigatorDatasource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

public class DateNavigator<DN extends DateNavigatorNotifier, B extends Box> extends AbstractDateNavigator<B> {
	private java.util.List<Scale> scales = new ArrayList<>();
	private Instant from;
	private Instant to;
	private Scale selectedScale = null;
	private final Map<Scale, Instant> selectedInstants = new HashMap<>();
	private SelectListener selectListener;
	private SelectListener selectScaleListener;
	private DateNavigatorDatasource source = null;
	private List<Timeline<?, ?>> timelineBindings = new ArrayList<>();
	private List<Reel<?, ?>> reelBindings = new ArrayList<>();
	private List<Eventline<?, ?>> eventlineBindings = new ArrayList<>();

	public DateNavigator(B box) {
		super(box);
	}

	public DateNavigator<DN, B> source(DateNavigatorDatasource source) {
		this.source = source;
		return this;
	}

	public DateNavigator<DN, B> onSelect(SelectListener listener) {
		this.selectListener = listener;
		return this;
	}

	public DateNavigator<DN, B> onSelectScale(SelectListener listener) {
		this.selectScaleListener = listener;
		return this;
	}

	public DateNavigator<DN, B> select(Instant instant) {
		if (selectedInstant(selectedScale()) != null && selectedInstant(selectedScale()).equals(instant)) return this;
		if (instant.isBefore(source.from(selectedScale()))) instant = source.from(selectedScale());
		if (instant.isAfter(source.to(selectedScale()))) instant = source.to(selectedScale());
		selectInstant(selectedScale(), instant);
		return this;
	}

	public DateNavigator<DN, B> bindTo(Timeline<?, ?>... timelines) {
		timelineBindings.addAll(List.of(timelines));
		return this;
	}

	public DateNavigator<DN, B> bindTo(Reel<?, ?>... reels) {
		reelBindings.addAll(List.of(reels));
		return this;
	}

	public DateNavigator<DN, B> bindTo(Eventline<?, ?>... eventlines) {
		eventlineBindings.addAll(List.of(eventlines));
		return this;
	}

	public void first() {
		Scale scale = selectedScale();
		selectInstant(scale, source.from(scale));
	}

	public void previous() {
		Scale scale = selectedScale();
		Instant current = selectedInstant(scale);
		Instant from = source.from(scale);
		current = source.previous(selectedScale(), current);
		if (current.isBefore(from)) current = from;
		selectInstant(scale, current);
	}

	public void next() {
		Scale scale = selectedScale();
		Instant current = selectedInstant(scale);
		Instant to = source.to(scale);
		current = source.next(selectedScale(), current);
		if (current.isAfter(to)) current = to;
		selectInstant(scale, current);
	}

	public void last() {
		Scale scale = selectedScale();
		selectInstant(scale, source.to(scale));
	}

	public void select(Scale scale) {
		changeScale(scale);
	}

	public void changeScale(String scale) {
		changeScale(Scale.valueOf(scale));
	}

	public void changeScale(Scale scale) {
		if (selectedScale == scale) return;
		selectedScale = scale;
		refreshInfo();
		notifyScaleSelected(selectedScale);
	}

	@Override
	public void init() {
		super.init();
		source = defaultDatasource();
		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Timeline source not defined");
		notifier.setup(new DateNavigatorSetup()
				.scales(source.scales().stream().map(Enum::name).collect(Collectors.toList()))
				.info(info())
		);
	}

	protected void _scales(Scale... scales) {
		this.scales = List.of(scales);
	}

	protected void _range(Instant from, Instant to) {
		this.from = from;
		this.to = to;
	}

	private Instant selectedInstant(Scale scale) {
		return selectedInstants.getOrDefault(scale, source.to(selectedScale()));
	}

	private void selectInstant(Scale scale, Instant value) {
		selectedInstants.put(scale, value);
		refresh();
		notifySelected(value);
	}

	private Scale selectedScale() {
		return selectedScale != null ? selectedScale : source.scales().get(0);
	}

	private void refreshInfo() {
		notifier.refresh(info());
	}

	private DateNavigatorInfo info() {
		Scale scale = selectedScale();
		Instant date = selectedInstant(scale);
		DateNavigatorInfo result = new DateNavigatorInfo();
		result.selected(date);
		result.selectedLabel(ScaleFormatter.label(date, timezoneOffset(), scale, language()));
		result.scale(selectedScale().name());
		result.canPrevious(!ScaleFormatter.label(date, timezoneOffset(), scale, language()).equals(ScaleFormatter.label(source.from(scale), timezoneOffset(), scale, language())));
		result.canNext(!ScaleFormatter.label(date, timezoneOffset(), scale, language()).equals(ScaleFormatter.label(source.to(scale), timezoneOffset(), scale, language())));
		return result;
	}

	private DateNavigatorDatasource defaultDatasource() {
		return new DateNavigatorDatasource() {
			@Override
			public List<Scale> scales() {
				return scales;
			}

			@Override
			public Instant from(Scale scale) {
				return from != null ? from : Instant.now();
			}

			@Override
			public Instant previous(Scale scale, Instant date) {
				return LocalDateTime.ofInstant(date, UTC).minus(1, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant next(Scale scale, Instant date) {
				return LocalDateTime.ofInstant(date, UTC).plus(1, scale.temporalUnit()).toInstant(UTC);
			}

			@Override
			public Instant to(Scale scale) {
				return to != null ? to : Instant.now();
			}
		};
	}

	private void notifySelected(Instant value) {
		if (selectListener != null) selectListener.accept(new SelectEvent(this, value));
		timelineBindings.forEach(t -> t.select(value));
		reelBindings.forEach(r -> r.select(value));
		eventlineBindings.forEach(r -> r.select(value));
	}

	private void notifyScaleSelected(Scale value) {
		if (selectScaleListener != null) selectScaleListener.accept(new SelectEvent(this, value));
		timelineBindings.forEach(t -> t.changeScale(value));
		reelBindings.forEach(r -> r.changeScale(value));
	}

}