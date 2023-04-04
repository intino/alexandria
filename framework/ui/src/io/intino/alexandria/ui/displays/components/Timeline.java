package io.intino.alexandria.ui.displays.components;

import com.google.gson.JsonObject;
import io.intino.alexandria.Scale;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.notifiers.TimelineNotifier;
import io.intino.alexandria.ui.model.timeline.MeasurementDefinition;
import io.intino.alexandria.ui.model.timeline.TimelineDatasource;
import io.intino.alexandria.ui.model.timeline.TimelineFormatter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
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
		refreshViews(measurement);
	}

	public void nextSummary(TimelineParameterInfo info) {
		TimelineDatasource.Measurement measurement = source.measurement(info.measurement());
		LocalDateTime current = summaryDate(measurement, info);
		LocalDateTime to = LocalDateTime.ofInstant(measurement.to(), ZoneOffset.UTC);
		current = current.plus(1, unitOf(info.scale()));
		if (current.isAfter(to)) current = to;
		saveSummaryDate(measurement, info, current);
		refreshViews(measurement);
	}

	public void changeScale(TimelineParameterInfo info) {
		TimelineDatasource.TimelineScale scale = TimelineDatasource.TimelineScale.valueOf(info.scale());
		measurementScales.put(info.measurement(), scale);
		refreshViews(source.measurement(info.measurement()));
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
		TimelineDatasource.TimelineScale scale = scale(measurement);
		Map<Instant, Double> values = measurement.serie(scale, fetch.start(), fetch.end()).values();
		notifier.refreshHistory(fillWithZeros(values, fetch.end(), scale).entrySet().stream().map(this::historyEntryOf).collect(Collectors.toList()));
	}

	private TimelineHistoryEntry historyEntryOf(Map.Entry<Instant, Double> entry) {
		return new TimelineHistoryEntry().date(entry.getKey()).value(entry.getValue());
	}

	private TimelineMeasurement schemaOf(MeasurementDefinition definition) {
		TimelineDatasource.Measurement measurement = measurement(definition);
		return new TimelineMeasurement()
				.name(definition.name())
				.value(measurement.value())
				.min(measurement.min() != null ? String.valueOf(measurement.min()) : null)
				.max(measurement.max() != null ? String.valueOf(measurement.max()) : null)
				.percentage(measurement.percentage() != null ? String.valueOf(measurement.percentage()) : null)
				.label(definition.label(language()))
				.unit(definition.unit())
				.decimalCount(definition.decimalCount())
				.summary(summaryOf(measurement))
				.serie(serieOf(measurement))
				.customView(measurement.customHtmlView(scale(measurement)));
	}

	private TimelineSummary summaryOf(TimelineDatasource.Measurement measurement) {
		TimelineDatasource.TimelineScale scale = scale(measurement);
		Instant date = summaryDate(measurement, scale).toInstant(ZoneOffset.UTC);
		TimelineDatasource.Summary summary = measurement.summary(date, scale);
		return new TimelineSummary().label(summary.label())
									.average(summaryValueOf(summary.average(), date))
									.max(summaryValueOf(summary.max(), summary.maxDate()))
									.min(summaryValueOf(summary.min(), summary.minDate()))
									.canBefore(!TimelineFormatter.label(date, scale, language()).equals(TimelineFormatter.label(measurement.from(), scale, language())))
									.canNext(!TimelineFormatter.label(date, scale, language()).equals(TimelineFormatter.label(measurement.to(), scale, language())))
									.scale(scale.name());
	}

	private TimelineSummaryValue summaryValueOf(double value, Instant date) {
		return new TimelineSummaryValue().value(value).date(date);
	}

	private TimelineSerie serieOf(TimelineDatasource.Measurement measurement) {
		TimelineSerie result = new TimelineSerie();
		TimelineDatasource.TimelineScale scale = scale(measurement);
		TimelineDatasource.Serie serie = measurement.serie(scale);
		result.name(serie.name());
		result.categories(categoriesOf(serie, measurement.to(), scale));
		result.values(loadValues(serie));
		return result;
	}

	private List<String> categoriesOf(TimelineDatasource.Serie serie, Instant to, TimelineDatasource.TimelineScale scale) {
		return loadCategories(serie, to, scale).stream().map(d -> date(d, scale)).collect(Collectors.toList());
	}

	private List<Instant> loadCategories(TimelineDatasource.Serie serie, Instant to, TimelineDatasource.TimelineScale scale) {
		List<Instant> result = new ArrayList<>(serie.values().keySet());
		return reverse(fill(reverse(result).subList(0, Math.min(result.size(), summaryPointsCount)), to, scale));
	}

	private List<Double> loadValues(TimelineDatasource.Serie serie) {
		List<Double> values = new ArrayList<>(serie.values().values());
		return reverse(fillWithZeros(reverse(values).subList(0, Math.min(values.size(), summaryPointsCount))));
	}

	private TimelineDatasource.Measurement measurement(MeasurementDefinition definition) {
		return source.measurement(definition);
	}

	private List<Instant> fill(List<Instant> result, Instant to, TimelineDatasource.TimelineScale scale) {
		if (result.size() >= summaryPointsCount) return result;
		LocalDateTime mockDate = LocalDateTime.ofInstant(result.isEmpty() ? to : result.get(result.size() - 1), ZoneOffset.UTC);
		while (result.size() < summaryPointsCount) {
			mockDate = mockDate.minus(1, chronoUnitOf(scale));
			result.add(mockDate.toInstant(ZoneOffset.UTC));
		}
		return result;
	}

	private TemporalUnit chronoUnitOf(TimelineDatasource.TimelineScale scale) {
		if (scale == TimelineDatasource.TimelineScale.Hour) return ChronoUnit.HOURS;
		if (scale == TimelineDatasource.TimelineScale.Minute) return ChronoUnit.MINUTES;
		if (scale == TimelineDatasource.TimelineScale.Day) return ChronoUnit.DAYS;
		if (scale == TimelineDatasource.TimelineScale.Week) return ChronoUnit.WEEKS;
		if (scale == TimelineDatasource.TimelineScale.Month) return ChronoUnit.MONTHS;
		return ChronoUnit.YEARS;
	}

	private Map<Instant, Double> fillWithZeros(Map<Instant, Double> result, Instant to, TimelineDatasource.TimelineScale scale) {
		if (result.size() >= summaryPointsCount) return result;
		LocalDateTime mockDate = LocalDateTime.ofInstant(result.isEmpty() ? to : new ArrayList<>(result.keySet()).get(0), ZoneOffset.UTC);
		while (result.size() < summaryPointsCount) {
			mockDate = mockDate.minus(1, chronoUnitOf(scale));
			result.put(mockDate.toInstant(ZoneOffset.UTC), 0.);
		}
		List<Instant> keys = new ArrayList<>(result.keySet());
		keys.sort(Instant::compareTo);
		return keys.stream().collect(Collectors.toMap(k -> k, result::get, (k,v) -> k, LinkedHashMap::new));
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

	private String date(Instant date, TimelineDatasource.TimelineScale scale) {
		return TimelineFormatter.shortLabel(date, scale, language());
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

	private void refreshViews(TimelineDatasource.Measurement measurement) {
		notifier.refreshSummary(summaryOf(measurement));
		notifier.refreshSerie(serieOf(measurement));
		notifier.refreshCustomView(measurement.customHtmlView(scale(measurement)));
	}

}