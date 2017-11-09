package io.intino.konos.alexandria.activity.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;

import static java.time.LocalDateTime.of;
import static java.time.LocalDateTime.ofInstant;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoUnit.*;

public enum TimeScale {

	Year(YEARS) {
		@Override
		public Instant normalise(Instant instant) {
			return instantOf(of(yearOf(instant), 1, 1, 0, 0, 0));
		}

		@Override
		public long toMillis(long value) {
			return Day.toMillis((long) (value * 365.25));
		}

		@Override
		public String symbol(String language) {
			return language.equals("es") ? "A" : "Y";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			return String.valueOf(yearOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return yearOf(instant);
		}

		@Override
		public String toString(Instant instant) {
			return String.valueOf(yearOf(instant));
		}
	},

	QuarterOfYear(MONTHS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("MM/yyyy");

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
		public String symbol(String language) {
			return language.equals("es") ? "C" : "Q";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			int quarter = quarterNumber(instant);
			String category = translator.translate("TemporalCategorization.Category.Quarter" + quarter);

			if (format.equalsIgnoreCase("QuarterOfYear"))
				category += " " + yearOf(instant);

			return category;
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return quarterNumber(instant);
		}

		@Override
		public String toString(Instant instant) {
			return "Q" + quarterNumber(instant) + " (" + range(instant) + ")";
		}

		private String range(Instant instant) {
			return Formatter.format(dateTimeOf(instant)) + " - " + Formatter.format(dateTimeOf(nextTime(instant)));
		}

		private int quarterNumber(Instant instant) {
			return ((monthNumberOf(instant) - 1) / 3) + 1;
		}

		private int firstMonthOfQuarter(Instant instant) {
			return ((monthNumberOf(instant) - 1) / 3) * 3 + 1;
		}
	},

	Month(MONTHS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("MM/yyyy");

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Day.toMillis(value * 30);
		}

		@Override
		public String symbol(String language) {
			return "M";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			int month = dateTimeOf(instant).getMonthValue();
			String category = translator.translate("TemporalCategorization.Category.Month" + month);

			if (format.equalsIgnoreCase("MonthOfYear"))
				category += " " + yearOf(instant);

			return category;
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return dateTimeOf(instant).getMonthValue();
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(instant));
		}
	},

	Week(WEEKS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).with(WeekFields.ISO.dayOfWeek(), 1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Day.toMillis(value * 7);
		}

		@Override
		public String symbol(String language) {
			return language.equals("es") ? "S" : "W";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			LocalDateTime dateTime = dateTimeOf(instant);
			int week = dateTime.get(ALIGNED_WEEK_OF_YEAR);
			String category = translator.translate("TemporalCategorization.Category.Week") + " " + format(week, 2);

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
		public String toString(Instant instant) {
			LocalDateTime dateTime = dateTimeOf(instant);
			return "Week " + format(dateTime.get(ALIGNED_WEEK_OF_YEAR), 2) + " (" + range(instant) + ")";
		}

		private String range(Instant instant) {
			LocalDateTime dateTime = dateTimeOf(instant);
			return Formatter.format(dateTime) + " - " + Formatter.format(dateTimeOf(nextTime(instant)));
		}
	},

	Day(DAYS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withHour(0).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return SixHours.toMillis(value * 4);
		}

		@Override
		public String symbol(String language) {
			return "D";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			LocalDateTime dateTime = dateTimeOf(instant);

			if (format.equalsIgnoreCase("DayOfYear"))
				return translator.translate("TemporalCategorization.Category.Day") + " " + dateTime.getDayOfYear();

			if (format.equalsIgnoreCase("DayOfMonth"))
				return translator.translate("TemporalCategorization.Category.Day") + " " + dateTime.getDayOfMonth();

			return translator.translate("TemporalCategorization.Category.Day" + dateTime.getDayOfWeek().ordinal());
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			if (format.equalsIgnoreCase("DayOfYear")) return dateTimeOf(instant).getDayOfYear();
			if (format.equalsIgnoreCase("DayOfMonth")) return dateTimeOf(instant).getDayOfMonth();
			return dateTimeOf(instant).getDayOfWeek().ordinal();
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(instant));
		}

	},

	SixHours(HOURS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
		public String symbol(String language) {
			return "6h";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			int hourBefore = hourOf(addTo(instant, -1));
			return format(hourBefore, 2) + " - " + format(hourOf(instant), 2);
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return hourOf(addTo(instant, -1));
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(addTo(instant, -1))) + " (" + format(hourOf(addTo(instant, -1)), 2) + " - " + format(hourOf(instant), 2) + ")";
		}
	},

	Hour(HOURS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH");
		private final DateTimeFormatter CategoryFormatter = DateTimeFormatter.ofPattern("HH");

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withMinute(0).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return FifteenMinutes.toMillis(value * 4);
		}

		@Override
		public String symbol(String language) {
			return "h";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			return CategoryFormatter.format(dateTimeOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return hourOf(instant);
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(instant));
		}
	},

	FifteenMinutes(MINUTES) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH");

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
		public String symbol(String language) {
			return "15'";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			int beforeMinute = minuteOf(addTo(instant, -1));
			return format(beforeMinute, 2) + " - " + format(minuteOf(instant), 2);
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return minuteOf(addTo(instant, -1));
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(addTo(instant, -1))) + " (" + format(minuteOf(addTo(instant, -1)), 2) + " - " + format(minuteOf(instant), 2) + ")";
		}

		private int quarter(int minutes) {
			return (minutes / 15) * 15;
		}
	},

	Minute(MINUTES) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		private final DateTimeFormatter CategoryFormatter = DateTimeFormatter.ofPattern("mm");

		@Override
		public Instant normalise(Instant instant) {
			return instantOf(dateTimeOf(instant).withSecond(0).withNano(0));
		}

		@Override
		public long toMillis(long value) {
			return Second.toMillis(value * 60);
		}

		@Override
		public String symbol(String language) {
			return "m";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			return CategoryFormatter.format(dateTimeOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return minuteOf(instant);
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(instant));
		}
	},

	Second(SECONDS) {
		private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		private final DateTimeFormatter CategoryFormatter = DateTimeFormatter.ofPattern("ss");

		@Override
		public Instant normalise(Instant instant) {
			return instant;
		}

		@Override
		public long toMillis(long value) {
			return value * 1000;
		}

		@Override
		public String symbol(String language) {
			return "s";
		}

		@Override
		public String category(Instant instant, String format, Translator translator) {
			return CategoryFormatter.format(dateTimeOf(instant));
		}

		@Override
		public int sortingWeight(Instant instant, String format) {
			return secondOf(instant);
		}

		@Override
		public String toString(Instant instant) {
			return Formatter.format(dateTimeOf(instant));
		}
	};

	private final ChronoUnit unit;
	private String label;

	TimeScale(ChronoUnit unit) {
		this(unit, "");
	}

	TimeScale(ChronoUnit unit, String label) {
		this.unit = unit;
		this.label = label;
	}

	private static String format(int number, int withDecimals) {
		return String.format("%0" + withDecimals + "d", number);
	}

	public String label() {
		return this.label;
	}

	public void label(String label) {
		this.label = label;
	}

	public abstract Instant normalise(Instant instant);

	public abstract String toString(Instant instant);

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

	public abstract String symbol(String language);

	public abstract String category(Instant instant, String format, Translator translator);

	public abstract int sortingWeight(Instant instant, String format);

	public TimeScale next() {
		int ordinal = this.ordinal();
		ordinal++;
		if (ordinal > SECONDS.ordinal()) return TimeScale.Second;
		return TimeScale.values()[ordinal];
	}

	public interface Translator {
		String translate(String word);
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
