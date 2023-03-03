package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.notifiers.TimelineNotifier;
import io.intino.alexandria.ui.model.timeline.Measurement;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
	private Map<String, Map<TimelineDatasource.Timeline.Scale, LocalDateTime>> summaryDates = new HashMap<>();

	private static final int DefaultSummaryPointsCount = 24;

	public Timeline(B box) {
		super(box);
	}

	public enum Mode { Summary, Detail, Evolution }

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

	public void beforeSummary(TimelineSummaryInfo info) {
		TimelineDatasource.Timeline timeline = source.timeline(info.measurement());
		LocalDateTime current = summaryDate(timeline, info);
		current = current.minus(1, unitOf(info.scale()));
		saveSummaryDate(timeline, info, current);
		notifier.refreshSummaries(summariesOf(info.measurement(), timeline));
	}

	public void nextSummary(TimelineSummaryInfo info) {
		TimelineDatasource.Timeline timeline = source.timeline(info.measurement());
		LocalDateTime current = summaryDate(timeline, info);
		current = current.plus(1, unitOf(info.scale()));
		saveSummaryDate(timeline, info, current);
		notifier.refreshSummaries(summariesOf(info.measurement(), timeline));
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
		Map<Instant, Double> values = timeline.serie(fetch.start(), fetch.end()).values();
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
				.summaries(summariesOf(measurement.name(), timeline))
				.serie(serieOf(timeline));
	}

	private List<TimelineSummary> summariesOf(String measurement, TimelineDatasource.Timeline timeline) {
		List<TimelineSummary> result = new ArrayList<>();
		result.add(summaryOf(measurement, timeline, TimelineDatasource.Timeline.Scale.Day));
		result.add(summaryOf(measurement, timeline, TimelineDatasource.Timeline.Scale.Week));
		result.add(summaryOf(measurement, timeline, TimelineDatasource.Timeline.Scale.Month));
		return result;
	}

	private TimelineSummary summaryOf(String measurement, TimelineDatasource.Timeline timeline, TimelineDatasource.Timeline.Scale scale) {
		Instant date = summaryDate(measurement, timeline, scale).toInstant(ZoneOffset.UTC);
		TimelineDatasource.Summary summary = timeline.summary(date, scale);
		return new TimelineSummary().label(summary.label())
									.average(summaryValueOf(summary.average(), date))
									.max(summaryValueOf(summary.max(), date))
									.min(summaryValueOf(summary.min(), date))
									.canBefore(!date.isBefore(timeline.from()))
									.canNext(!date.isAfter(timeline.to()))
									.scale(scale.name());
	}

	private TimelineSummaryValue summaryValueOf(double value, Instant date) {
		return new TimelineSummaryValue().value(value).date(date);
	}

	private TimelineSerie serieOf(TimelineDatasource.Timeline timeline) {
		TimelineSerie result = new TimelineSerie();
		TimelineDatasource.Serie serie = timeline.serie();
		result.name(serie.name());
		result.categories(categoriesOf(serie, timeline.to()));
		result.values(loadValues(serie));
		return result;
	}

	private List<String> categoriesOf(TimelineDatasource.Serie serie, Instant to) {
		return loadCategories(serie, to).stream().map(this::date).collect(Collectors.toList());
	}

	private List<Instant> loadCategories(TimelineDatasource.Serie serie, Instant to) {
		List<Instant> result = new ArrayList<>(serie.values().keySet());
		if (mode == Mode.Detail) return reverse(result);
		return reverse(fill(result.subList(0, Math.min(result.size(), summaryPointsCount)), to));
	}

	private List<Double> loadValues(TimelineDatasource.Serie serie) {
		List<Double> values = new ArrayList<>(serie.values().values());
		if (mode == Mode.Detail) return reverse(values);
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
		return mode == Mode.Detail ? dayAndHour(t) : hour(t);
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

	private static final Map<TimelineDatasource.Timeline.Scale, TemporalUnit> TemporalUnits = Map.of(
			TimelineDatasource.Timeline.Scale.Day, ChronoUnit.DAYS,
			TimelineDatasource.Timeline.Scale.Week, ChronoUnit.WEEKS,
			TimelineDatasource.Timeline.Scale.Month, ChronoUnit.MONTHS,
			TimelineDatasource.Timeline.Scale.Year, ChronoUnit.YEARS
	);

	private TemporalUnit unitOf(String scaleName) {
		return TemporalUnits.getOrDefault(TimelineDatasource.Timeline.Scale.valueOf(scaleName), ChronoUnit.DAYS);
	}

	private LocalDateTime summaryDate(TimelineDatasource.Timeline timeline, TimelineSummaryInfo info) {
		return summaryDate(info.measurement(), timeline, TimelineDatasource.Timeline.Scale.valueOf(info.scale()));
	}

	private LocalDateTime summaryDate(String measurement, TimelineDatasource.Timeline timeline, TimelineDatasource.Timeline.Scale scale) {
		if (!summaryDates.containsKey(measurement)) return LocalDateTime.ofInstant(timeline.to(), ZoneOffset.UTC);
		return summaryDates.get(measurement).getOrDefault(scale, LocalDateTime.ofInstant(timeline.to(), ZoneOffset.UTC));
	}

	private void saveSummaryDate(TimelineDatasource.Timeline timeline, TimelineSummaryInfo info, LocalDateTime current) {
		if (!summaryDates.containsKey(info.measurement())) summaryDates.put(info.measurement(), new HashMap<>());
		summaryDates.get(info.measurement()).put(TimelineDatasource.Timeline.Scale.valueOf(info.scale()), current);
	}

}