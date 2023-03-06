package io.intino.alexandria.ui.model.timeline;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TimelineFormatter {

	public static String summaryLabel(Instant date, TimelineDatasource.TimelineScale scale, String language) {
		String format = Formatters.getOrDefault(scale, Formatters.get(TimelineDatasource.TimelineScale.Day)).getOrDefault(language, "en");
		return date(date, format, language);
	}

	private static String date(Instant date, String format, String language) {
		if (date == null) return null;
		return formatDate(format, date, locale(language));
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

	private static final Map<TimelineDatasource.TimelineScale, Map<String, String>> Formatters = new HashMap<>() {{
		put(TimelineDatasource.TimelineScale.Day, Map.of("es", "YYYY-MM-dd", "en", "dd-MM-YYYY"));
		put(TimelineDatasource.TimelineScale.Week, Map.of("es", "YYYY-Www", "en", "Www-YYYY"));
		put(TimelineDatasource.TimelineScale.Month, Map.of("es", "YYYY-MM", "en", "MM-YYYY"));
		put(TimelineDatasource.TimelineScale.Year, Map.of("es", "YYYY", "en", "YYYY"));
	}};

}
