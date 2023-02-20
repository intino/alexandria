package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.notifiers.TimelineNotifier;
import io.intino.alexandria.ui.model.timeline.Measurement;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Timeline<DN extends TimelineNotifier, B extends Box> extends AbstractTimeline<B> {
	private TimelineDatasource source;
	private Mode mode;
	private List<TimelineMeasurementVisibility> measurementsVisibility;
	private List<TimelineMeasurementSorting> measurementsSorting;
	private int summaryPointsCount = DefaultSummaryPointsCount;

	private static final int DefaultSummaryPointsCount = 24;

	public Timeline(B box) {
		super(box);
	}

	public enum Mode { Summary, Normal }

	public <DS extends TimelineDatasource> Timeline<DN, B> source(DS source) {
		this.source = source;
		return this;
	}

	public Mode mode() {
		return mode;
	}

	protected Timeline<DN, B> _mode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public List<TimelineMeasurementVisibility> measurementsVisibility() {
		return measurementsVisibility;
	}

	public Timeline<DN, B> measurementsVisibility(List<TimelineMeasurementVisibility> measurementsVisibility) {
		this.measurementsVisibility = measurementsVisibility;
		notifier.refreshMeasurementsVisibility(measurementsVisibility);
		return this;
	}

	public List<TimelineMeasurementSorting> measurementsSorting() {
		return measurementsSorting;
	}

	public Timeline<DN, B> measurementsSorting(List<TimelineMeasurementSorting> measurementsSorting) {
		this.measurementsSorting = measurementsSorting;
		notifier.refreshMeasurementsSorting(measurementsSorting);
		return this;
	}

	public Timeline<DN, B> summaryPointsCount(int count) {
		this.summaryPointsCount = count;
		return this;
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Timeline source not defined");
		notifier.setup(new TimelineSetup().mode(mode.name()).name(source.name()).measurements(source.measurements().stream().map(this::schemaOf).collect(Collectors.toList())));
	}

	public void openHistory(String measurementName) {
		TimelineDatasource.Timeline timeline = source.timeline(measurementName);
		notifier.showHistoryDialog(new TimelineHistory().from(timeline.from()).to(timeline.to()));
	}

	public void fetch(TimelineHistoryFetch fetch) {
		TimelineDatasource.Timeline timeline = source.timeline(fetch.measurement());
		Map<Instant, Double> values = timeline.stats(fetch.start(), fetch.end());
		notifier.refreshHistory(reverse(values.entrySet().stream().map(this::historyEntryOf).collect(Collectors.toList())));
	}

	private TimelineHistoryEntry historyEntryOf(Map.Entry<Instant, Double> entry) {
		return new TimelineHistoryEntry().date(entry.getKey()).value(entry.getValue());
	}

	private TimelineMeasurement schemaOf(Measurement measurement) {
		TimelineDatasource.Timeline timeline = timeline(measurement);
		return new TimelineMeasurement()
				.name(measurement.name())
				.value(timeline.value())
				.label(measurement.label(language()))
				.unit(measurement.unit())
				.decimalCount(measurement.decimalCount())
				.trend(TimelineMeasurement.Trend.valueOf(timeline.trend().name()))
				.evolution(mode == Mode.Summary ? emptyEvolution() : evolutionOf(timeline));
	}

	private TimelineEvolution emptyEvolution() {
		return new TimelineEvolution().categories(Collections.emptyList()).serie(new TimelineSerie().values(Collections.emptyList()));
	}

	private TimelineEvolution evolutionOf(TimelineDatasource.Timeline timeline) {
		TimelineEvolution result = new TimelineEvolution();
		result.categories(categoriesOf(timeline));
		result.serie(serieOf(timeline));
		return result;
	}

	private List<String> categoriesOf(TimelineDatasource.Timeline timeline) {
		return loadCategories(timeline).stream().map(this::date).collect(Collectors.toList());
	}

	private TimelineSerie serieOf(TimelineDatasource.Timeline timeline) {
		List<Double> values = loadValues(timeline);
		return new TimelineSerie().values(values);
	}

	private List<Instant> loadCategories(TimelineDatasource.Timeline timeline) {
		List<Instant> result = new ArrayList<>(timeline.stats().keySet());
		if (mode == Mode.Normal) return reverse(result);
		return reverse(fill(result.subList(0, Math.min(result.size(), summaryPointsCount)), timeline.to()));
	}

	private List<Double> loadValues(TimelineDatasource.Timeline timeline) {
		List<Double> values = new ArrayList<>(timeline.stats().values());
		if (mode == Mode.Normal) return reverse(values);
		return reverse(fillWithZeros(values.subList(0, Math.min(values.size(), summaryPointsCount))));
	}

	private TimelineDatasource.Timeline timeline(Measurement measurement) {
		return source.timeline(measurement);
	}

	private List<Instant> fill(List<Instant> result, Instant to) {
		if (result.size() >= summaryPointsCount) return result;
		Instant mockInstant = result.isEmpty() ? to : result.get(result.size() - 1);
		while (result.size() < summaryPointsCount) {
			result.add(mockInstant);
			mockInstant = mockInstant.minus(1, ChronoUnit.HOURS);
		}
		return result;
	}

	private List<Double> fillWithZeros(List<Double> result) {
		if (result.size() >= summaryPointsCount) return result;
		while (result.size() < summaryPointsCount) result.add(0.);
		return result;
	}

	private <T> List<T> reverse(List<T> result) {
		Collections.reverse(result);
		return result;
	}

	private String date(Instant t) {
		return mode == Mode.Normal ? dayAndHour(t) : hour(t);
	}

	private static final String HourFormat = "HH:mm";
	private String hour(Instant date) {
		return date(date, HourFormat, this::translate);
	}

	private static final String DayAndHourFormat = "dd/MM HH:mm";
	private String dayAndHour(Instant date) {
		return date(date, DayAndHourFormat, this::translate);
	}

	public String date(Instant date, String format, Function<String, String> translator) {
		if (date == null) return null;
		return formatDate(translator.apply(format), date, locale(language()));
	}

	private static String formatDate(String pattern, Instant instant, Locale locale) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		return format.format(Date.from(instant));
	}

	private static Locale locale(String language) {
		if (language.toLowerCase().contains("es")) return new Locale("es", "ES");
		if (language.toLowerCase().contains("pt")) return new Locale("pt", "PT");
		return new Locale("en", "EN");
	}

}