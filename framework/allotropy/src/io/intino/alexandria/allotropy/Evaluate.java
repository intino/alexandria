package io.intino.alexandria.allotropy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class Evaluate {
    private String data;
    private String name;

    public Evaluate(String name) {
        this.name = name;
    }

    public static Evaluate that(String data) {
        return new Evaluate(data);
    }

    public From from(Map<String, String> map) {
        this.data = map.get(name);
        return new From();
    }

    public class From {
        public int isInteger() throws FormatError {
            try {
                return Integer.parseInt(data);
            } catch (NumberFormatException e) {
                throw new FormatError(data + invalid("number") + context());
            }
        }

        public long isLong() throws FormatError {
            try {
                return Long.parseLong(data);
            } catch (NumberFormatException e) {
                throw new FormatError(data + invalid("number") + context());
            }
        }

        public String isEnum(String... options) throws FormatError {
            if (!Arrays.asList(options).contains(data)) {
                throw new FormatError(data + invalid("option") + context());
            }
            return data;
        }

        public Date isAnyDate(String... patterns) throws FormatError {
            Date date = null;
            for (String pattern : patterns) {
                try { date = isDate(pattern); }
                catch (FormatError ignored) {}
                if (date != null) break;
            }
            if (date == null){
                throw new FormatError(data + invalid("date") + patterns(patterns) + context());
            }
            return date;
        }

        public Date isDate(String pattern) throws FormatError {
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                format.setLenient(false);
                return format.parse(data);
            } catch (ParseException e) {
                throw new FormatError(data + invalid("date") + pattern(pattern) + context());
            }
        }

        public int isMonth() throws FormatError {
            try {
                return Month.of(Integer.parseInt(data)).getValue();
            } catch (NumberFormatException | DateTimeException e) {
                throw new FormatError(data + invalid("month") + context());
            }
        }

        public void is(String value) throws FormatError {
            if (!data.equalsIgnoreCase(value)) {
                throw new FormatError(data + invalid("value") + context());
            }
        }

        public double isDouble() throws FormatError {
            try {
                return Double.parseDouble(data);
            } catch (NumberFormatException e) {
                throw new FormatError(data + invalid("double number") + context());
            }
        }

        public void hasMaxLength(int length) throws FormatError {
            if (data.length() > length) throw new FormatError(data + " exceedes max length" + context());
        }
    }

    private String invalid(String type) {
        return " is not a valid " + type;
    }

    private String context() {
        return " for " + name;
    }

    private String pattern(String pattern) {
        return " with pattern \"" + pattern + "\"";
    }

    private String patterns(String... pattern) {
        return " with patterns \"" + String.join(", ", pattern) + "\"";
    }
}
