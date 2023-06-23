package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.notifiers.ReelNotifier;
import io.intino.alexandria.ui.model.reel.ReelDatasource;
import io.intino.alexandria.ui.model.reel.ReelDatasource.Annotation;
import io.intino.alexandria.ui.model.reel.SignalDefinition;
import io.intino.alexandria.ui.model.ScaleFormatter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Reel<DN extends ReelNotifier, B extends Box> extends AbstractReel<B> {
	private ReelDatasource source;
	private List<ReelSignalSorting> signalsSorting;
	private final Map<Scale, Instant> selectedInstants = new HashMap<>();
	private Scale selectedScale = null;
	private int stepsCount = DefaultStepsCount;

	private static final int DefaultStepsCount = 24;

	public Reel(B box) {
		super(box);
	}

	public <DS extends ReelDatasource> Reel<DN, B> source(DS source) {
		this.source = source;
		return this;
	}

	public List<ReelSignalSorting> signalsSorting() {
		return signalsSorting;
	}

	public Reel<DN, B> signalsSorting(List<ReelSignalSorting> signalsSorting) {
		this.signalsSorting = signalsSorting;
		notifier.refreshSignalsSorting(signalsSorting);
		return this;
	}

	public Reel<DN, B> stepsCount(int count) {
		this.stepsCount = count;
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

	public void changeScale(String scale) {
		selectedScale = Scale.valueOf(scale);
		refreshToolbar();
		refreshSignals();
		refreshNavigation();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Reel source not defined");
		notifier.setup(new ReelSetup()
				.name(source.name())
				.scales(source.scales().stream().map(Enum::name).collect(Collectors.toList()))
				.toolbar(toolbar())
				.signals(source.signals().stream().map(this::schemaOf).collect(Collectors.toList()))
				.navigation(navigation()));
	}

	public void fetch(ReelFetch fetch) {
	}

	private Scale selectedScale() {
		return selectedScale != null ? selectedScale : source.scales().get(0);
	}

	private void selectInstant(Scale scale, Instant value) {
		selectedInstants.put(scale, value);
		refreshToolbar();
		refreshSignals();
	}

	private void refreshToolbar() {
		notifier.refreshToolbar(toolbar());
	}

	private void refreshSignals() {
		notifier.refreshSignals(source.signals().stream().map(this::schemaOf).collect(Collectors.toList()));
	}

	private void refreshNavigation() {
		notifier.refreshNavigation(navigation());
	}

	private ReelNavigationInfo navigation() {
		Scale scale = selectedScale();
		Instant current = source.from(scale);
		Instant to = source.to(scale);
		int steps = 0;
		while (current != to && current.isBefore(to)) {
			steps++;
			current = source.next(scale, current);
		}
		return new ReelNavigationInfo().steps(steps);
	}

	private ReelSignal schemaOf(SignalDefinition definition) {
		return schemaOf(signal(definition));
	}

	private ReelSignal schemaOf(ReelDatasource.Signal signal) {
		SignalDefinition definition = signal.definition();
		Scale scale = selectedScale();
		Instant to = selectedInstant(scale);
		Instant from = LocalDateTime.ofInstant(to, ZoneId.of("UTC")).minus(stepsCount, scale.temporalUnit()).toInstant(ZoneOffset.UTC);
		String reel = signal.reel(scale, from, to);
		Map<Instant, Annotation> annotations = signal.annotations(scale, from, to);
		return new ReelSignal()
				.name(definition.name())
				.type(definition.type().name())
				.label(definition.label(language()))
				.color(definition.color())
				.steps(stepsOf(fillWithZeros(reel), from))
				.annotations(annotationsOf(annotations));
	}

	private String fillWithZeros(String reel) {
		return String.format("%1$" + stepsCount + "s", reel);
	}

	private List<ReelSignalStep> stepsOf(String reel, Instant from) {
		Scale scale = selectedScale();
		Instant current = from;
		List<ReelSignalStep> result = new ArrayList<>();
		for (int i = 0; i < reel.length(); i++) {
			result.add(new ReelSignalStep().value(String.valueOf(reel.charAt(i))).date(ScaleFormatter.label(current, scale, language())));
			current = source.next(scale, current);
		}
		return result;
	}

	private List<ReelSignalAnnotation> annotationsOf(Map<Instant, Annotation> annotations) {
		return annotations.entrySet().stream().map(this::annotationOf).collect(Collectors.toList());
	}

	private ReelSignalAnnotation annotationOf(Map.Entry<Instant, Annotation> entry) {
		Scale scale = selectedScale();
		Annotation annotation = entry.getValue();
		return new ReelSignalAnnotation().date(ScaleFormatter.label(entry.getKey(), scale, language())).label(annotation.label()).color(annotation.color());
	}

	private ReelDatasource.Signal signal(SignalDefinition definition) {
		return source.signal(definition);
	}

	private ReelToolbarInfo toolbar() {
		Scale scale = selectedScale();
		Instant date = selectedInstant(scale);
		ReelToolbarInfo result = new ReelToolbarInfo();
		result.label(ScaleFormatter.label(date, scale, language()));
		result.scale(selectedScale().name());
		result.canPrevious(!ScaleFormatter.label(date, scale, language()).equals(ScaleFormatter.label(source.from(scale), scale, language())));
		result.canNext(!ScaleFormatter.label(date, scale, language()).equals(ScaleFormatter.label(source.to(scale), scale, language())));
		return result;
	}

	private Instant selectedInstant() {
		return selectedInstant(selectedScale());
	}

	private Instant selectedInstant(Scale scale) {
		return selectedInstants.getOrDefault(scale, source.to(selectedScale()));
	}

}