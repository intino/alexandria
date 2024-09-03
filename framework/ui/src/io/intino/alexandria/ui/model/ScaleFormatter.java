package io.intino.alexandria.ui.model;

import io.intino.alexandria.Scale;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScaleFormatter {

	public static String label(Instant date, int zoneOffset, Scale scale, String language) {
		Map<String, String> formats = Formatters.getOrDefault(scale, Formatters.get(Scale.Day));
		String format = formats.getOrDefault(language, formats.get("en"));
		return date(date, zoneOffset, format, language);
	}

	public static String shortLabel(Instant date, int zoneOffset, Scale scale, String language) {
		Map<String, String> formats = ShortFormatters.getOrDefault(scale, ShortFormatters.get(Scale.Day));
		String format = formats.getOrDefault(language, formats.get("en"));
		return date(date, zoneOffset, format, language);
	}

	private static String date(Instant date, int zoneOffset, String format, String language) {
		if (date == null) return null;
		date = date.plus(zoneOffset, ChronoUnit.SECONDS);
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

	private static final Map<Scale, Map<String, String>> Formatters = new HashMap<>() {{
		put(Scale.Minute, Map.of("es", "YYYY-MM-dd HH:mm", "en", "dd-MM-YYYY HH:mm"));
		put(Scale.Hour, Map.of("es", "YYYY-MM-dd HH:mm", "en", "dd-MM-YYYY HH:mm"));
		put(Scale.Day, Map.of("es", "YYYY-MM-dd", "en", "dd-MM-YYYY"));
		put(Scale.Week, Map.of("es", "YYYY-'S'ww", "en", "'W'ww-YYYY"));
		put(Scale.Month, Map.of("es", "YYYY-MM", "en", "MM-YYYY"));
		put(Scale.Year, Map.of("es", "YYYY", "en", "YYYY"));
	}};

	private static final Map<Scale, Map<String, String>> ShortFormatters = new HashMap<>() {{
		put(Scale.Minute, Map.of("es", "HH:mm", "en", "HH:mm"));
		put(Scale.Hour, Map.of("es", "HH:mm", "en", "HH:mm"));
		put(Scale.Day, Map.of("es", "MM-dd", "en", "dd-MM-YYYY"));
		put(Scale.Week, Map.of("es", "'S'ww", "en", "'W'ww"));
		put(Scale.Month, Map.of("es", "MM", "en", "MM"));
		put(Scale.Year, Map.of("es", "YYYY", "en", "YYYY"));
	}};

}
