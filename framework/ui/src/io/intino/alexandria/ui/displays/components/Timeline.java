package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.notifiers.TimelineNotifier;
import io.intino.alexandria.ui.model.timeline.MagnitudeDefinition;
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
	private String stateLabel;
	private String historyLabel;
	private List<TimelineMagnitudeVisibility> magnitudesVisibility;
	private List<TimelineMagnitudeSorting> magnitudesSorting;
	private int summaryPointsCount = DefaultSummaryPointsCount;
	private final Map<TimelineDatasource.TimelineScale, Instant> selectedInstants = new HashMap<>();
	private TimelineDatasource.TimelineScale selectedScale = null;

	private static final int DefaultSummaryPointsCount = 24;

	public Timeline(B box) {
		super(box);
	}

	public enum Mode { Summary, Catalog }

	public <DS extends TimelineDatasource> Timeline<DN, B> stateLabel(String label) {
		this.stateLabel = label;
		return this;
	}

	public <DS extends TimelineDatasource> Timeline<DN, B> historyLabel(String label) {
		this.historyLabel = label;
		return this;
	}

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

	public List<TimelineMagnitudeVisibility> magnitudesVisibility() {
		return magnitudesVisibility;
	}

	public Timeline<DN, B> magnitudesVisibility(List<TimelineMagnitudeVisibility> magnitudesVisibility) {
		this.magnitudesVisibility = magnitudesVisibility;
		notifier.refreshMagnitudesVisibility(magnitudesVisibility);
		return this;
	}

	public List<TimelineMagnitudeSorting> magnitudesSorting() {
		return magnitudesSorting;
	}

	public Timeline<DN, B> magnitudesSorting(List<TimelineMagnitudeSorting> magnitudesSorting) {
		this.magnitudesSorting = magnitudesSorting;
		notifier.refreshMagnitudesSorting(magnitudesSorting);
		return this;
	}

	public Timeline<DN, B> summaryPointsCount(int count) {
		this.summaryPointsCount = count;
		return this;
	}

	public void first() {
		TimelineDatasource.TimelineScale scale = selectedScale();
		selectInstant(scale, source.from(scale));
	}

	public void previous() {
		TimelineDatasource.TimelineScale scale = selectedScale();
		Instant current = selectedInstant(scale);
		Instant from = source.from(scale);
		current = source.previous(current, selectedScale());
		if (current.isBefore(from)) current = from;
		selectInstant(scale, current);
	}

	public void next() {
		TimelineDatasource.TimelineScale scale = selectedScale();
		Instant current = selectedInstant(scale);
		Instant to = source.to(scale);
		current = source.next(current, selectedScale());
		if (current.isAfter(to)) current = to;
		selectInstant(scale, current);
	}

	public void last() {
		TimelineDatasource.TimelineScale scale = selectedScale();
		selectInstant(scale, source.to(scale));
	}

	public void changeScale(String scale) {
		selectedScale = TimelineDatasource.TimelineScale.valueOf(scale);
		refreshToolbar();
		refreshMagnitudes();
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Timeline source not defined");
		notifier.setup(new TimelineSetup()
							.mode(mode.name())
							.name(source.name())
							.stateLabel(stateLabel)
							.historyLabel(historyLabel)
							.scales(source.scales().stream().map(Enum::name).collect(Collectors.toList()))
							.toolbar(toolbar())
							.magnitudes(source.magnitudes().stream().map(this::schemaOf).collect(Collectors.toList()))
		);
	}

	private TimelineHistoryToolbar toolbar() {
		TimelineDatasource.TimelineScale scale = selectedScale();
		Instant date = selectedInstant(scale);
		TimelineHistoryToolbar result = new TimelineHistoryToolbar();
		result.label(TimelineFormatter.label(date, scale, language()));
		result.scale(selectedScale().name());
		result.canPrevious(!TimelineFormatter.label(date, scale, language()).equals(TimelineFormatter.label(source.from(scale), scale, language())));
		result.canNext(!TimelineFormatter.label(date, scale, language()).equals(TimelineFormatter.label(source.to(scale), scale, language())));
		return result;
	}

	public void openHistory(String magnitudeName) {
		TimelineDatasource.TimelineScale scale = selectedScale;
		notifier.showHistoryDialog(new TimelineHistory().from(source.from(scale)).to(source.to(scale)));
	}

	public void fetch(TimelineHistoryFetch fetch) {
		TimelineDatasource.Magnitude magnitude = source.magnitude(fetch.magnitude());
		TimelineDatasource.TimelineScale scale = selectedScale();
		Map<Instant, Double> values = magnitude.serie(scale, fetch.start(), fetch.end()).values();
		notifier.refreshHistory(fillWithZeros(values, fetch.end(), scale).entrySet().stream().map(this::historyEntryOf).collect(Collectors.toList()));
	}

	private TimelineHistoryEntry historyEntryOf(Map.Entry<Instant, Double> entry) {
		return new TimelineHistoryEntry().date(entry.getKey()).value(entry.getValue());
	}

	private TimelineSummary summaryOf(TimelineDatasource.Magnitude magnitude) {
		TimelineDatasource.TimelineScale scale = selectedScale();
		Instant date = selectedInstant(scale);
		TimelineDatasource.Summary summary = magnitude.summary(date, scale);
		return new TimelineSummary().average(summaryValueOf(summary.average(), date))
									.max(summaryValueOf(summary.max(), summary.maxDate()))
									.min(summaryValueOf(summary.min(), summary.minDate()));
	}

	private TimelineSummaryValue summaryValueOf(double value, Instant date) {
		return new TimelineSummaryValue().value(value).date(date);
	}

	private TimelineSerie serieOf(TimelineDatasource.Magnitude magnitude) {
		TimelineSerie result = new TimelineSerie();
		TimelineDatasource.TimelineScale scale = selectedScale();
		Instant current = selectedInstant(scale);
		TimelineDatasource.Serie serie = magnitude.serie(scale, current);
		result.name(serie.name());
		result.categories(categoriesOf(serie, current, scale));
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

	private TimelineDatasource.Magnitude magnitude(MagnitudeDefinition definition) {
		return source.magnitude(definition);
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

	private Instant selectedInstant(String scale) {
		return selectedInstant(TimelineDatasource.TimelineScale.valueOf(scale));
	}

	private Instant selectedInstant(TimelineDatasource.TimelineScale scale) {
		return selectedInstants.getOrDefault(scale, source.to(selectedScale()));
	}

	private TimelineDatasource.TimelineScale selectedScale() {
		return selectedScale != null ? selectedScale : source.scales().get(0);
	}

	private void selectInstant(TimelineDatasource.TimelineScale scale, Instant value) {
		selectedInstants.put(scale, value);
		refreshToolbar();
		refreshMagnitudes();
	}

	private void refreshToolbar() {
		notifier.refreshHistoryToolbar(toolbar());
	}

	private void refreshMagnitudes() {
		notifier.refreshMagnitudes(source.magnitudes().stream().map(this::schemaOf).collect(Collectors.toList()));
	}

	private TimelineMagnitude schemaOf(MagnitudeDefinition definition) {
		return schemaOf(magnitude(definition));
	}

	private TimelineMagnitude schemaOf(TimelineDatasource.Magnitude magnitude) {
		MagnitudeDefinition definition = magnitude.definition();
		return new TimelineMagnitude()
				.name(definition.name())
				.value(magnitude.value())
				.status(magnitude.status().name())
				.min(magnitude.min() != null ? String.valueOf(magnitude.min()) : null)
				.max(magnitude.max() != null ? String.valueOf(magnitude.max()) : null)
				.percentage(magnitude.percentage() != null ? String.valueOf(magnitude.percentage()) : null)
				.label(definition.label(language()))
				.unit(definition.unit())
				.decimalCount(definition.decimalCount())
				.summary(summaryOf(magnitude))
				.serie(serieOf(magnitude))
				.customView(magnitude.customHtmlView(selectedScale()));
	}

}