package io.intino.alexandria.office.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Definition {

	static Map<String, String> propertiesToMap(String properties) {
		return Arrays.stream(properties.split(",")).collect(toMap(v -> v.trim().split("=")[0], v -> v.trim().split("=")[1]));
	}

	String formattedNumber(double value, int countDecimals, Locale locale) {
		NumberFormat numberInstance = NumberFormat.getNumberInstance(locale);
		numberInstance.setMinimumFractionDigits(countDecimals);
		return numberInstance.format(round(value, countDecimals));
	}

	double round(double value, int countDecimals) {
		long factor = (long) Math.pow(10, countDecimals);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	double amountValue(double value, long factor) {
		return value / factor;
	}

	String firstUpperCase(String content) {
		return content.substring(0, 1).toUpperCase() + content.substring(1);
	}

}
