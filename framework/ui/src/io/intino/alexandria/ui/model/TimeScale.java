package io.intino.alexandria.ui.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.of;
import static java.time.LocalDateTime.ofInstant;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoUnit.*;

public enum TimeScale {

	Year(YEARS) {

		@Override
		public String label(String language) {
			return labels.get(Year).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Year).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(of(yearOf(instant), 1, 1, 0, 0, 0));
		}

		@Override
		public long toMillis(long value) {
			return Day.toMillis((long) (value * 365.25));
		}

		@Override
		public String category(Instant instant, String format, String language) {
			return String.valueOf(yearOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return yearOf(instant);
		}

		@Override
		public String toString(Instant instant, String language) {
			return String.valueOf(yearOf(instant));
		}
	},

	QuarterOfYear(MONTHS) {

		@Override
		public String label(String language) {
			return labels.get(QuarterOfYear).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(QuarterOfYear).get(language);
		}

		@Override
		public long instantsBetween(Instant start, Instant end) {
			return Math.max(MONTHS.between(dateTimeOf(start), dateTimeOf(end)) / 3, 1);
		}

		@Override
		public Instant addTo(Instant time, long amount) {
			return instantOf(MONTHS.addTo(dateTimeOf(time), 3 * amount));
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withMonth(firstMonthOfQuarter(instant)).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Month.toMillis(value * 3);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			int quarter = quarterNumber(instant);
			String category = words.get("Category.Quarter" + quarter).get(language);

			if (format.equalsIgnoreCase("QuarterOfYear"))
				category += " " + yearOf(instant);

			return category;
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return quarterNumber(instant);
		}

		@Override
		public String toString(Instant instant, String language) {
			return "Q" + quarterNumber(instant) + " (" + range(instant, language) + ")";
		}

		private String range(Instant instant, String language) {
			DateTimeFormatter formatter = formatter(language);
			return formatter.format(dateTimeOf(instant)) + " - " + formatter.format(dateTimeOf(nextTime(instant)));
		}

		private int quarterNumber(Instant instant) {
			return ((monthNumberOf(instant) - 1) / 3) + 1;
		}

		private int firstMonthOfQuarter(Instant instant) {
			return ((monthNumberOf(instant) - 1) / 3) * 3 + 1;
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "MM/yyyy" : "yyyy/MM");
		}
	},

	Month(MONTHS) {

		@Override
		public String label(String language) {
			return labels.get(Month).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Month).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Day.toMillis(value * 30);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			int month = dateTimeOf(instant).getMonthValue();
			String category = words.get("Category.Month" + month).get(language);

			if (format.equalsIgnoreCase("MonthOfYear"))
				category += " " + yearOf(instant);

			return category;
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return dateTimeOf(instant).getMonthValue();
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(instant));
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "MM/yyyy" : "yyyy/MM");
		}
	},

	Week(WEEKS) {

		@Override
		public String label(String language) {
			return labels.get(Week).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Week).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).with(WeekFields.ISO.dayOfWeek(), 1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Day.toMillis(value * 7);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			LocalDateTime dateTime = dateTimeOf(instant);
			int week = dateTime.get(ALIGNED_WEEK_OF_YEAR);
			String category = words.get("Category.Week").get(language) + " " + format(week, 2);

			if (format.equalsIgnoreCase("WeekOfYear"))
				category += " " + yearOf(instant);

			return category;
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			LocalDateTime dateTime = dateTimeOf(instant);
			return dateTime.get(ALIGNED_WEEK_OF_YEAR);
		}

		@Override
		public String toString(Instant instant, String language) {
			LocalDateTime dateTime = dateTimeOf(instant);
			return "Week " + format(dateTime.get(ALIGNED_WEEK_OF_YEAR), 2) + " (" + range(instant, language) + ")";
		}

		private String range(Instant instant, String language) {
			LocalDateTime dateTime = dateTimeOf(instant);
			DateTimeFormatter formatter = formatter(language);
			return formatter.format(dateTime) + " - " + formatter.format(dateTimeOf(nextTime(instant)));
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy" : "yyyy/MM/dd");
		}
	},

	Day(DAYS) {
		@Override
		public String label(String language) {
			return labels.get(Day).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Day).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return SixHours.toMillis(value * 4);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			LocalDateTime dateTime = dateTimeOf(instant);

			if (format.equalsIgnoreCase("DayOfYear"))
				return words.get("Category.Day").get(language) + " " + dateTime.getDayOfYear();

			if (format.equalsIgnoreCase("DayOfMonth"))
				return words.get("Category.Day").get(language) + " " + dateTime.getDayOfMonth();

			return words.get("Category.Day" + dateTime.getDayOfWeek().ordinal()).get(language);
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			if (format.equalsIgnoreCase("DayOfYear")) return dateTimeOf(instant).getDayOfYear();
			if (format.equalsIgnoreCase("DayOfMonth")) return dateTimeOf(instant).getDayOfMonth();
			return dateTimeOf(instant).getDayOfWeek().ordinal();
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(instant));
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy" : "yyyy/MM/dd");
		}
	},

	SixHours(HOURS) {

		@Override
		public String label(String language) {
			return labels.get(SixHours).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(SixHours).get(language);
		}

		@Override
		public long instantsBetween(Instant start, Instant end) {
			return Math.max(HOURS.between(start, end) / 5, 1);
		}

		@Override
		public Instant addTo(Instant time, long amount) {
			return instantOf(HOURS.addTo(dateTimeOf(time), 6 * amount));
		}

		private int quarter(int hour) {
			return (hour / 6) * 6;
		}

		@Override
		public Instant normalise(Instant instant) {
			LocalDateTime dateTime = dateTimeOf(instant);
			return instantOf(dateTime.withHour(quarter(dateTime.getHour())).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Hour.toMillis(value * 6);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			int hourBefore = hourOf(addTo(instant, -1));
			return format(hourBefore, 2) + " - " + format(hourOf(instant), 2);
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return hourOf(addTo(instant, -1));
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(addTo(instant, -1))) + " (" + format(hourOf(addTo(instant, -1)), 2) + " - " + format(hourOf(instant), 2) + ")";
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy" : "yyyy/MM/dd");
		}
	},

	Hour(HOURS) {
		private final DateTimeFormatter CategoryFormatter = DateTimeFormatter.ofPattern("HH");

		@Override
		public String label(String language) {
			return labels.get(Hour).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Hour).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return FifteenMinutes.toMillis(value * 4);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			return CategoryFormatter.format(dateTimeOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return hourOf(instant);
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(instant));
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy HH" : "yyyy/MM/dd HH");
		}
	},

	FifteenMinutes(MINUTES) {

		@Override
		public String label(String language) {
			return labels.get(FifteenMinutes).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(FifteenMinutes).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			LocalDateTime dateTime = dateTimeOf(instant);
			return instantOf(dateTime.withMinute(quarter(dateTime.getMinute())).withSecond(0).withNano(0));
		}

		@Override
		public long instantsBetween(Instant start, Instant end) {
			return Math.max(MINUTES.between(start, end) / 14, 1);
		}

		@Override
		public Instant addTo(Instant time, long amount) {
			return instantOf(MINUTES.addTo(dateTimeOf(time), 15 * amount));
		}

		@Override
		public long toMillis(long value) {
			return Minute.toMillis(value * 15);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			int beforeMinute = minuteOf(addTo(instant, -1));
			return format(beforeMinute, 2) + " - " + format(minuteOf(instant), 2);
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return minuteOf(addTo(instant, -1));
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(addTo(instant, -1))) + " (" + format(minuteOf(addTo(instant, -1)), 2) + " - " + format(minuteOf(instant), 2) + ")";
		}

		private int quarter(int minutes) {
			return (minutes / 15) * 15;
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy HH" : "yyyy/MM/dd HH");
		}
	},

	Minute(MINUTES) {
		private final DateTimeFormatter CategoryFormatter = DateTimeFormatter.ofPattern("mm");

		@Override
		public String label(String language) {
			return labels.get(Minute).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Minute).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Second.toMillis(value * 60);
		}

		@Override
		public String category(Instant instant, String format, String language) {
			return CategoryFormatter.format(dateTimeOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return minuteOf(instant);
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(instant));
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy HH:mm" : "yyyy/MM/dd HH:mm");
		}
	},

	Second(SECONDS) {
		private final DateTimeFormatter CategoryFormatter = DateTimeFormatter.ofPattern("ss");

		@Override
		public String label(String language) {
			return labels.get(Second).get(language);
		}

		@Override
		public String symbol(String language) {
			return symbols.get(Second).get(language);
		}

		@Override
		public Instant normalise(Instant instant) {
			return instant;
		}

		@Override
		public long toMillis(long value) {
			return value * 1000;
		}

		@Override
		public String category(Instant instant, String format, String language) {
			return CategoryFormatter.format(dateTimeOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return secondOf(instant);
		}

		@Override
		public String toString(Instant instant, String language) {
			return formatter(language).format(dateTimeOf(instant));
		}

		private DateTimeFormatter formatter(String language) {
			return DateTimeFormatter.ofPattern(language.equals("es") ? "dd/MM/yyyy HH:mm:ss" : "yyyy/MM/dd HH:mm:ss");
		}
	};

	private final ChronoUnit unit;

	TimeScale(ChronoUnit unit) {
		this.unit = unit;
	}

	private static Map<TimeScale, Map<String, String>> labels = new HashMap<TimeScale, Map<String, String>>() {{
		put(Year, new HashMap<String, String>() {{ put("es", "año"); put("en", "year"); }});
		put(QuarterOfYear, new HashMap<String, String>() {{ put("es", "trimestre"); put("en", "quarter of year"); }});
		put(Month, new HashMap<String, String>() {{ put("es", "mes"); put("en", "month"); }});
		put(Week, new HashMap<String, String>() {{ put("es", "semana"); put("en", "week"); }});
		put(Day, new HashMap<String, String>() {{ put("es", "día"); put("en", "day"); }});
		put(SixHours, new HashMap<String, String>() {{ put("es", "seis horas"); put("en", "six hours"); }});
		put(Hour, new HashMap<String, String>() {{ put("es", "hora"); put("en", "hour"); }});
		put(FifteenMinutes, new HashMap<String, String>() {{ put("es", "quince minutos"); put("en", "fifteen minutes"); }});
		put(Minute, new HashMap<String, String>() {{ put("es", "minuto"); put("en", "minute"); }});
		put(Second, new HashMap<String, String>() {{ put("es", "segundo"); put("en", "second"); }});
	}};

	private static Map<TimeScale, Map<String, String>> symbols = new HashMap<TimeScale, Map<String, String>>() {{
		put(Year, new HashMap<String, String>() {{ put("es", "A"); put("en", "Y"); }});
		put(QuarterOfYear, new HashMap<String, String>() {{ put("es", "C"); put("en", "Q"); }});
		put(Month, new HashMap<String, String>() {{ put("es", "M"); put("en", "M"); }});
		put(Week, new HashMap<String, String>() {{ put("es", "S"); put("en", "W"); }});
		put(Day, new HashMap<String, String>() {{ put("es", "D"); put("en", "D"); }});
		put(SixHours, new HashMap<String, String>() {{ put("es", "6h"); put("en", "6h"); }});
		put(Hour, new HashMap<String, String>() {{ put("es", "h"); put("en", "h"); }});
		put(FifteenMinutes, new HashMap<String, String>() {{ put("es", "15'"); put("en", "15'"); }});
		put(Minute, new HashMap<String, String>() {{ put("es", "m"); put("en", "m"); }});
		put(Second, new HashMap<String, String>() {{ put("es", "s"); put("en", "s"); }});
	}};

	private static Map<String, Map<String, String>> words = new HashMap<String, Map<String, String>>() {{
		put("Category.Quarter1", new HashMap<String, String>() {{ put("es", "1er trimestre"); put("en", "1st quarter"); }});
		put("Category.Quarter2", new HashMap<String, String>() {{ put("es", "2do trimestre"); put("en", "2nd quarter"); }});
		put("Category.Quarter3", new HashMap<String, String>() {{ put("es", "3er trimestre"); put("en", "3th quarter"); }});
		put("Category.Quarter4", new HashMap<String, String>() {{ put("es", "4to trimestre"); put("en", "4th quarter"); }});
		put("Category.Month1", new HashMap<String, String>() {{ put("es", "Enero"); put("en", "January"); }});
		put("Category.Month2", new HashMap<String, String>() {{ put("es", "Febrero"); put("en", "February"); }});
		put("Category.Month3", new HashMap<String, String>() {{ put("es", "Marzo"); put("en", "March"); }});
		put("Category.Month4", new HashMap<String, String>() {{ put("es", "Abril"); put("en", "April"); }});
		put("Category.Month5", new HashMap<String, String>() {{ put("es", "Mayo"); put("en", "May"); }});
		put("Category.Month6", new HashMap<String, String>() {{ put("es", "Junio"); put("en", "June"); }});
		put("Category.Month7", new HashMap<String, String>() {{ put("es", "Julio"); put("en", "July"); }});
		put("Category.Month8", new HashMap<String, String>() {{ put("es", "Agosto"); put("en", "August"); }});
		put("Category.Month9", new HashMap<String, String>() {{ put("es", "Septiembre"); put("en", "September"); }});
		put("Category.Month10", new HashMap<String, String>() {{ put("es", "Octubre"); put("en", "October"); }});
		put("Category.Month11", new HashMap<String, String>() {{ put("es", "Noviembre"); put("en", "November"); }});
		put("Category.Month12", new HashMap<String, String>() {{ put("es", "Diciembre"); put("en", "December"); }});
		put("Category.Week", new HashMap<String, String>() {{ put("es", "Semana"); put("en", "Week"); }});
		put("Category.Day", new HashMap<String, String>() {{ put("es", "Día"); put("en", "Day"); }});
		put("Category.Day0", new HashMap<String, String>() {{ put("es", "Lunes"); put("en", "Monday"); }});
		put("Category.Day1", new HashMap<String, String>() {{ put("es", "Martes"); put("en", "Tuesday"); }});
		put("Category.Day2", new HashMap<String, String>() {{ put("es", "Miércoles"); put("en", "Wednesday"); }});
		put("Category.Day3", new HashMap<String, String>() {{ put("es", "Jueves"); put("en", "Thursday"); }});
		put("Category.Day4", new HashMap<String, String>() {{ put("es", "Viernes"); put("en", "Friday"); }});
		put("Category.Day5", new HashMap<String, String>() {{ put("es", "Sábado"); put("en", "Saturday"); }});
		put("Category.Day6", new HashMap<String, String>() {{ put("es", "Domingo"); put("en", "Sunday"); }});
	}};

	private static String format(int number, int withDecimals) {
		return String.format("%0" + withDecimals + "d", number);
	}

	public abstract Instant normalise(Instant instant);

	public String toString(Instant instant) {
		return toString(instant, "en");
	}

	public abstract String toString(Instant instant, String language);

	public Instant nextTime(Instant time) {
		return addTo(time, 1);
	}

	public long instantsBetween(Instant start, Instant end) {
		return Math.max(unit.between(dateTimeOf(start), unit.addTo(dateTimeOf(end), 1)), 1);
	}

	public abstract long toMillis(long value);

	public Instant addTo(Instant time, long amount) {
		return instantOf(unit.addTo(dateTimeOf(time), amount));
	}

	public Instant floor(Instant time) {
		return time.truncatedTo(unit);
	}

	public boolean accept(Enum value) {
		return value instanceof TimeScale;
	}

	public abstract String label(String language);

	public abstract String symbol(String language);

	public abstract String category(Instant instant, String format, String language);

	public abstract int sortingWeight(Instant instant, String format);

	public TimeScale next() {
		int ordinal = this.ordinal();
		ordinal++;
		if (ordinal > SECONDS.ordinal()) return TimeScale.Second;
		return TimeScale.values()[ordinal];
	}

	protected int yearOf(Instant instant) {
		return dateTimeOf(instant).getYear();
	}

	protected java.time.Month monthOf(Instant instant) {
		return dateTimeOf(instant).getMonth();
	}

	protected int monthNumberOf(Instant instant) {
		return dateTimeOf(instant).getMonthValue();
	}

	protected int dayOfYearOf(Instant instant) {
		return dateTimeOf(instant).getDayOfYear();
	}

	protected int dayOfMonthOf(Instant instant) {
		return dateTimeOf(instant).getDayOfMonth();
	}

	protected int hourOf(Instant instant) {
		return dateTimeOf(instant).getHour();
	}

	protected int minuteOf(Instant instant) {
		return dateTimeOf(instant).getMinute();
	}

	protected int secondOf(Instant instant) {
		return dateTimeOf(instant).getSecond();
	}

	protected ZoneId utc() {
		return ZoneId.of("UTC");
	}

	protected ZoneOffset utcOffset() {
		return ZoneOffset.UTC;
	}

	protected Instant instantOf(LocalDateTime dateTime) {
		return dateTime.toInstant(utcOffset());
	}

	LocalDateTime dateTimeOf(Instant instant) {
		return ofInstant(Instant.ofEpochMilli(instant.toEpochMilli()), utc());
	}

}
