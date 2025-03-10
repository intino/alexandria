package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.TimelineNotifier;
import io.intino.alexandria.ui.model.ScaleFormatter;
import io.intino.alexandria.ui.model.timeline.Formatter;
import io.intino.alexandria.ui.model.timeline.MagnitudeDefinition;
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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Timeline<DN extends TimelineNotifier, B extends Box> extends AbstractTimeline<B> {
	private TimelineDatasource source;
	private Mode mode;
	private String stateLabel;
	private String historyLabel;
	private List<TimelineMagnitudeVisibility> magnitudesVisibility;
	private List<TimelineMagnitudeSorting> magnitudesSorting;
	private int summaryPointsCount = DefaultSummaryPointsCount;
	private final Map<Scale, Instant> selectedInstants = new HashMap<>();
	private Scale selectedScale = null;
	private SelectListener selectListener;
	private SelectListener selectScaleListener;
	private boolean historyWithRelativeValues = true;

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

	public Timeline<DN, B> onSelect(SelectListener listener) {
		this.selectListener = listener;
		return this;
	}

	public Timeline<DN, B> onSelectScale(SelectListener listener) {
		this.selectScaleListener = listener;
		return this;
	}

	public Timeline<DN, B> select(Instant instant) {
		if (source == null) return this;
		if (selectedInstant(selectedScale()) != null && selectedInstant(selectedScale()).equals(instant)) return this;
		selectInstant(selectedScale(), instant);
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

	public void select(Scale scale) {
		changeScale(scale);
	}

	public void changeScale(String scale) {
		changeScale(Scale.valueOf(scale));
	}

	public void changeScale(Scale scale) {
		if (selectedScale == scale) return;
		selectedScale = scale;
		refreshMagnitudes();
		if (selectScaleListener != null) selectScaleListener.accept(new SelectEvent(this, selectedScale));
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
							.scales(source.scales().stream().map(Enum::name).collect(toList()))
							.magnitudes(source.magnitudes().stream().map(this::schemaOf).collect(toList()))
		);
	}

	public void historyWithRelativeValues(Boolean value) {
		Scale scale = selectedScale();
		this.historyWithRelativeValues = value;
		notifier.showHistoryDialog(new TimelineHistory().from(source.from(scale)).to(source.to(scale)).hasRelativeValues(true));
	}

	public void openHistory(String magnitudeName) {
		Scale scale = selectedScale();
		TimelineDatasource.Magnitude magnitude = source.magnitude(magnitudeName);
		boolean hasRelativeValues = magnitude.definition().unit() != null && !magnitude.definition().unit().equals("%") && magnitude.max() != null;
		this.historyWithRelativeValues = magnitude.definition().unit() != null && magnitude.definition().unit().equals("%");
		notifier.showHistoryDialog(new TimelineHistory().from(source.from(scale)).to(source.to(scale)).hasRelativeValues(hasRelativeValues));
	}

	public void fetch(TimelineHistoryFetch fetch) {
		if (fetch.start() == null || fetch.end() == null) return;
		TimelineDatasource.Magnitude magnitude = source.magnitude(fetch.magnitude());
		Scale scale = selectedScale();
		TimelineDatasource.Serie serie = magnitude.serie(scale, fetch.start(), fetch.end());
		Map<Instant, Double> values = serie.values();
		Map<Instant, List<TimelineDatasource.Annotation>> annotations = serie.annotations();
		Formatter formatter = magnitude.definition().formatter();
		notifier.refreshHistory(fillWithZeros(values, fetch.end(), scale).entrySet().stream().map(e -> historyEntryOf(magnitude, e, formatter, annotations)).collect(toList()));
	}

	private TimelineHistoryEntry historyEntryOf(TimelineDatasource.Magnitude magnitude, Map.Entry<Instant, Double> entry, Formatter formatter, Map<Instant, List<TimelineDatasource.Annotation>> annotations) {
		TimelineHistoryEntry result = new TimelineHistoryEntry();
		Double percentage = historyWithRelativeValues ? percentage(magnitude, entry.getValue()) : null;
		result.date(entry.getKey());
		result.formattedDate(ScaleFormatter.label(entry.getKey(), timezoneOffset(), selectedScale, language()));
		result.value(Double.isNaN(entry.getValue()) ? null : String.valueOf(historyWithRelativeValues ? percentage(magnitude, entry.getValue()) : entry.getValue()));
		result.formattedValue(adapt(formatter.format(percentage != null ? percentage : entry.getValue())));
		result.annotation(annotations.containsKey(entry.getKey()) ? annotationOf(date(normalize(entry.getKey()), selectedScale()), annotations.get(entry.getKey())) : null);
		return result;
	}

	private Double percentage(TimelineDatasource.Magnitude magnitude, Double value) {
		Double max = magnitude.max();
		if (max == null) return null;
		return value * 100 / max;
	}

	private TimelineSummary summaryOf(TimelineDatasource.Magnitude magnitude) {
		Scale scale = selectedScale();
		Formatter formatter = magnitude.definition().formatter();
		Instant date = selectedInstant(scale);
		TimelineDatasource.Summary summary = magnitude.summary(scale, date);
		return new TimelineSummary().average(summaryValueOf(summary.average(), date, formatter))
									.max(summaryValueOf(summary.max(), summary.maxDate(), formatter))
									.min(summaryValueOf(summary.min(), summary.minDate(), formatter))
									.attributes(summaryAttributes(summary.attributes(), formatter));
	}

	private List<TimelineSummaryAttribute> summaryAttributes(List<TimelineDatasource.Summary.Attribute> attributes, Formatter formatter) {
		return attributes.stream().map(a -> summaryAttribute(a, formatter)).collect(Collectors.toList());
	}

	private TimelineSummaryAttribute summaryAttribute(TimelineDatasource.Summary.Attribute attribute, Formatter formatter) {
		return new TimelineSummaryAttribute().name(attribute.name()).value(summaryValueOf(attribute.value(), attribute.date(), formatter));
	}

	private TimelineSummaryValue summaryValueOf(double value, Instant date, Formatter formatter) {
		return new TimelineSummaryValue().value(Double.isNaN(value) ? null : adapt(formatter.format(value))).date(date);
	}

	private TimelineSerie serieOf(TimelineDatasource.Magnitude magnitude) {
		TimelineSerie result = new TimelineSerie();
		Scale scale = selectedScale();
		Instant current = selectedInstant(scale);
		TimelineDatasource.Serie serie = magnitude.serie(scale, current, summaryPointsCount);
		List<Double> values = loadValues(magnitude, serie);
		Formatter formatter = magnitude.definition().formatter();
		result.name(serie.name());
		result.categories(categoriesOf(serie, current, scale));
		result.values(replaceNaNs(values));
		result.annotations(annotationsOf(serie, current, scale));
		result.formattedValues(values.stream().map(formatter::format).collect(toList()));
		return result;
	}

	private List<Double> replaceNaNs(List<Double> values) {
		return values.stream().map(v -> Double.isNaN(v) ? null : v).collect(toList());
	}

	private List<String> categoriesOf(TimelineDatasource.Serie serie, Instant to, Scale scale) {
		return loadCategories(serie, to, scale).stream().map(d -> date(d, scale)).collect(toList());
	}

	private List<TimelineAnnotation> annotationsOf(TimelineDatasource.Serie serie, Instant to, Scale scale) {
		List<Instant> instantList = loadCategories(serie, to, scale);
		Map<Instant, List<TimelineDatasource.Annotation>> annotationList = serie.annotations().entrySet().stream().collect(toMap(e -> normalize(e.getKey()), Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
		return instantList.stream().map(i -> annotationOf(date(i, scale), annotationList.getOrDefault(normalize(i), Collections.emptyList()))).collect(toList());
	}

	private Instant normalize(Instant instant) {
		return new Timetag(instant, selectedScale()).instant();
	}

	private TimelineAnnotation annotationOf(String category, List<TimelineDatasource.Annotation> annotationList) {
		if (annotationList.isEmpty()) return null;
		TimelineDatasource.Annotation annotation = annotationList.get(0);
		return new TimelineAnnotation().category(category).color(annotation.color()).symbol(annotation.symbol().name().toLowerCase()).entries(annotationEntriesOf(annotationList));
	}

	private List<String> annotationEntriesOf(List<TimelineDatasource.Annotation> annotationList) {
		return annotationList.stream().map(TimelineDatasource.Annotation::label).collect(toList());
	}

	private List<Instant> loadCategories(TimelineDatasource.Serie serie, Instant to, Scale scale) {
		List<Instant> result = new ArrayList<>(serie.values().keySet());
		return reverse(fill(reverse(result).subList(0, Math.min(result.size(), summaryPointsCount)), to, scale));
	}

	private List<Double> loadValues(TimelineDatasource.Magnitude magnitude, TimelineDatasource.Serie serie) {
		boolean renderPercentages = magnitude.percentage() != null;
		List<Double> values = serie.values().values().stream().map(v -> renderPercentages ? percentage(magnitude, v) : v).collect(toList());
		return reverse(fillWithZeros(reverse(values).subList(0, Math.min(values.size(), summaryPointsCount))));
	}

	private TimelineDatasource.Magnitude magnitude(MagnitudeDefinition definition) {
		return source.magnitude(definition);
	}

	private List<Instant> fill(List<Instant> result, Instant to, Scale scale) {
		if (result.size() >= summaryPointsCount) return result;
		LocalDateTime mockDate = LocalDateTime.ofInstant(result.isEmpty() ? to : result.get(result.size() - 1), ZoneOffset.UTC);
		while (result.size() < summaryPointsCount) {
			mockDate = mockDate.minus(1, chronoUnitOf(scale));
			result.add(mockDate.toInstant(ZoneOffset.UTC));
		}
		return result;
	}

	private TemporalUnit chronoUnitOf(Scale scale) {
		if (scale == Scale.Hour) return ChronoUnit.HOURS;
		if (scale == Scale.Minute) return ChronoUnit.MINUTES;
		if (scale == Scale.Day) return ChronoUnit.DAYS;
		if (scale == Scale.Week) return ChronoUnit.WEEKS;
		if (scale == Scale.Month) return ChronoUnit.MONTHS;
		return ChronoUnit.YEARS;
	}

	private Map<Instant, Double> fillWithZeros(Map<Instant, Double> result, Instant to, Scale scale) {
		if (result.size() >= summaryPointsCount) return result;
		LocalDateTime mockDate = LocalDateTime.ofInstant(result.isEmpty() ? to : new ArrayList<>(result.keySet()).get(0), ZoneOffset.UTC);
		while (result.size() < summaryPointsCount) {
			mockDate = mockDate.minus(1, chronoUnitOf(scale));
			result.put(mockDate.toInstant(ZoneOffset.UTC), 0.);
		}
		List<Instant> keys = new ArrayList<>(result.keySet());
		keys.sort(Instant::compareTo);
		return keys.stream().collect(toMap(k -> k, result::get, (k, v) -> k, LinkedHashMap::new));
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

	private String date(Instant date, Scale scale) {
		return ScaleFormatter.shortLabel(date, timezoneOffset(), scale, language());
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
		return selectedInstant(Scale.valueOf(scale));
	}

	private Instant selectedInstant(Scale scale) {
		return selectedInstants.getOrDefault(scale, source.to(selectedScale()));
	}

	private Scale selectedScale() {
		return selectedScale != null ? selectedScale : source.scales().get(0);
	}

	private void selectInstant(Scale scale, Instant value) {
		selectedInstants.put(scale, value);
		refreshMagnitudes();
		if (selectListener != null) selectListener.accept(new SelectEvent(this, value));
	}

	private void refreshMagnitudes() {
		notifier.refreshMagnitudes(source.magnitudes().stream().map(this::schemaOf).collect(toList()));
	}

	private TimelineMagnitude schemaOf(MagnitudeDefinition definition) {
		return schemaOf(magnitude(definition));
	}

	private TimelineMagnitude schemaOf(TimelineDatasource.Magnitude magnitude) {
		MagnitudeDefinition definition = magnitude.definition();
		Formatter formatter = definition.formatter();
		double value = magnitude.value();
		return new TimelineMagnitude()
				.name(definition.name())
				.value(String.valueOf(value))
				.formattedValue(!Double.isNaN(value) ? adapt(formatter.format(value)) : "-")
				.status(magnitude.status() != null ? magnitude.status().name() : null)
				.min(magnitude.min() != null ? String.valueOf(magnitude.min()) : null)
				.formattedMin(magnitude.min() != null ? adapt(formatter.format(magnitude.min())) : null)
				.max(magnitude.max() != null ? String.valueOf(magnitude.max()) : null)
				.formattedMax(magnitude.max() != null ? adapt(formatter.format(magnitude.max())) : null)
				.percentage(magnitude.percentage() != null ? adapt(formatter.format(magnitude.percentage())) : null)
				.label(definition.label(language()))
				.unit(translate(definition.unit()))
				.summary(summaryOf(magnitude))
				.serie(serieOf(magnitude))
				.customView(magnitude.customHtmlView(selectedScale()));
	}

	private static final Set<String> EnglishNotationLanguages = Set.of("mx", "en");
	private String adapt(String value) {
		if (!EnglishNotationLanguages.contains(language().toLowerCase())) return value;
		return value.replace(",", "COMMA").replace(".", ",").replace("COMMA", ".");
	}

}