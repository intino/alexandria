package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.notifiers.TimelineNotifier;
import io.intino.alexandria.ui.model.timeline.MeasurementDefinition;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;
import io.intino.alexandria.ui.model.timeline.TimelineFormatter;

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
	private final Map<String, TimelineDatasource.TimelineScale> measurementScales = new HashMap<>();
	private final Map<String, Map<TimelineDatasource.TimelineScale, LocalDateTime>> summaryDates = new HashMap<>();

	private static final int DefaultSummaryPointsCount = 24;

	public Timeline(B box) {
		super(box);
	}

	public enum Mode { Summary, Catalog }

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

	public void beforeSummary(TimelineParameterInfo info) {
		TimelineDatasource.Measurement measurement = source.measurement(info.measurement());
		LocalDateTime current = summaryDate(measurement, info);
		LocalDateTime from = LocalDateTime.ofInstant(measurement.from(), ZoneOffset.UTC);
		current = current.minus(1, unitOf(info.scale()));
		if (current.isBefore(from)) current = from;
		saveSummaryDate(measurement, info, current);
		notifier.refreshSummary(summaryOf(measurement));
	}

	public void nextSummary(TimelineParameterInfo info) {
		TimelineDatasource.Measurement measurement = source.measurement(info.measurement());
		LocalDateTime current = summaryDate(measurement, info);
		LocalDateTime to = LocalDateTime.ofInstant(measurement.to(), ZoneOffset.UTC);
		current = current.plus(1, unitOf(info.scale()));
		if (current.isAfter(to)) current = to;
		saveSummaryDate(measurement, info, current);
		notifier.refreshSummary(summaryOf(measurement));
	}

	public void changeScale(TimelineParameterInfo info) {
		measurementScales.put(info.measurement(), TimelineDatasource.TimelineScale.valueOf(info.scale()));
		notifier.refreshSummary(summaryOf(source.measurement(info.measurement())));
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Timeline source not defined");
		notifier.setup(new TimelineSetup()
							.mode(mode.name())
							.name(source.name())
							.scales(source.scales().stream().map(Enum::name).collect(Collectors.toList()))
							.measurements(source.measurements().stream().map(this::schemaOf).collect(Collectors.toList()))
		);
	}

	public void openHistory(String measurementName) {
		TimelineDatasource.Measurement measurement = source.measurement(measurementName);
		notifier.showHistoryDialog(new TimelineHistory().from(measurement.from()).to(measurement.to()));
	}

	public void fetch(TimelineHistoryFetch fetch) {
		TimelineDatasource.Measurement measurement = source.measurement(fetch.measurement());
		Map<Instant, Double> values = measurement.serie(fetch.start(), fetch.end()).values();
		notifier.refreshHistory(reverse(values.entrySet().stream().map(this::historyEntryOf).collect(Collectors.toList())));
	}

	private TimelineHistoryEntry historyEntryOf(Map.Entry<Instant, Double> entry) {
		return new TimelineHistoryEntry().date(entry.getKey()).value(entry.getValue());
	}

	private TimelineMeasurement schemaOf(MeasurementDefinition definition) {
		TimelineDatasource.Measurement measurement = measurement(definition);
		return new TimelineMeasurement()
				.name(definition.name())
				.value(measurement.value())
				.label(definition.label(language()))
				.unit(definition.unit())
				.decimalCount(definition.decimalCount())
				.trend(TimelineMeasurement.Trend.valueOf(measurement.trend().name()))
				.distribution(distributionOf(measurement))
				.summary(summaryOf(measurement))
				.serie(serieOf(measurement));
	}

	private TimelineDistribution distributionOf(TimelineDatasource.Measurement measurement) {
		return new TimelineDistribution().value(measurement.distribution()).trend(TimelineDistribution.Trend.valueOf(measurement.distributionTrend().name()));
	}

	private TimelineSummary summaryOf(TimelineDatasource.Measurement measurement) {
		TimelineDatasource.TimelineScale scale = scale(measurement);
		Instant date = summaryDate(measurement, scale).toInstant(ZoneOffset.UTC);
		TimelineDatasource.Summary summary = measurement.summary(date, scale);
		return new TimelineSummary().label(summary.label())
									.average(summaryValueOf(summary.average(), date))
									.max(summaryValueOf(summary.max(), date))
									.min(summaryValueOf(summary.min(), date))
									.canBefore(!TimelineFormatter.summaryLabel(date, scale, language()).equals(TimelineFormatter.summaryLabel(measurement.from(), scale, language())))
									.canNext(!TimelineFormatter.summaryLabel(date, scale, language()).equals(TimelineFormatter.summaryLabel(measurement.to(), scale, language())))
									.scale(scale.name());
	}

	private TimelineSummaryValue summaryValueOf(double value, Instant date) {
		return new TimelineSummaryValue().value(value).date(date);
	}

	private TimelineSerie serieOf(TimelineDatasource.Measurement measurement) {
		TimelineSerie result = new TimelineSerie();
		TimelineDatasource.Serie serie = measurement.serie();
		result.name(serie.name());
		result.categories(categoriesOf(serie, measurement.to()));
		result.values(loadValues(serie));
		return result;
	}

	private List<String> categoriesOf(TimelineDatasource.Serie serie, Instant to) {
		return loadCategories(serie, to).stream().map(this::date).collect(Collectors.toList());
	}

	private List<Instant> loadCategories(TimelineDatasource.Serie serie, Instant to) {
		List<Instant> result = new ArrayList<>(serie.values().keySet());
//		if (mode == Mode.Evolution) return reverse(result);
		return reverse(fill(result.subList(0, Math.min(result.size(), summaryPointsCount)), to));
	}

	private List<Double> loadValues(TimelineDatasource.Serie serie) {
		List<Double> values = new ArrayList<>(serie.values().values());
//		if (mode == Mode.Evolution) return reverse(values);
		return reverse(fillWithZeros(values.subList(0, Math.min(values.size(), summaryPointsCount))));
	}

	private TimelineDatasource.Measurement measurement(MeasurementDefinition definition) {
		return source.measurement(definition);
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
//		return mode == Mode.Detail ? dayAndHour(t) : hour(t);
		return hour(t);
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

	private static final Map<TimelineDatasource.TimelineScale, TemporalUnit> TemporalUnits = Map.of(
			TimelineDatasource.TimelineScale.Day, ChronoUnit.DAYS,
			TimelineDatasource.TimelineScale.Week, ChronoUnit.WEEKS,
			TimelineDatasource.TimelineScale.Month, ChronoUnit.MONTHS,
			TimelineDatasource.TimelineScale.Year, ChronoUnit.YEARS
	);

	private TemporalUnit unitOf(String scaleName) {
		return TemporalUnits.getOrDefault(TimelineDatasource.TimelineScale.valueOf(scaleName), ChronoUnit.DAYS);
	}

	private LocalDateTime summaryDate(TimelineDatasource.Measurement measurement, TimelineParameterInfo info) {
		return summaryDate(measurement, TimelineDatasource.TimelineScale.valueOf(info.scale()));
	}

	private LocalDateTime summaryDate(TimelineDatasource.Measurement measurement, TimelineDatasource.TimelineScale scale) {
		if (!summaryDates.containsKey(measurement.definition().name())) return LocalDateTime.ofInstant(measurement.to(), ZoneOffset.UTC);
		return summaryDates.get(measurement.definition().name()).getOrDefault(scale, LocalDateTime.ofInstant(measurement.to(), ZoneOffset.UTC));
	}

	private TimelineDatasource.TimelineScale scale(TimelineDatasource.Measurement measurement) {
		return measurementScales.getOrDefault(measurement.definition().name(), source.scales().get(0));
	}

	private void saveSummaryDate(TimelineDatasource.Measurement measurement, TimelineParameterInfo info, LocalDateTime current) {
		if (!summaryDates.containsKey(info.measurement())) summaryDates.put(info.measurement(), new HashMap<>());
		summaryDates.get(info.measurement()).put(TimelineDatasource.TimelineScale.valueOf(info.scale()), current);
	}

}