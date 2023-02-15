package io.intino.alexandria.cli.util;

import io.intino.alexandria.cli.response.MessageData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHelper {

	private static final Pattern TagPattern = Pattern.compile("\\$([^\\$ \\)`~]*)");
	public static String replaceVariables(String message, MessageData data) {
		if (data == null || data.isEmpty()) return !hasVariables(message) ? message : null;
		Matcher matcher = TagPattern.matcher(message);
		while (matcher.find()) {
			String variable = message.substring(matcher.start(), matcher.end());
			String value = data.getOrDefault(variable.substring(1), variable);
			message = message.replace(variable, value != null ? value : "");
			matcher = TagPattern.matcher(message);
		}
		return message;
	}

	private static boolean hasVariables(String template) {
		return template.contains("$");
	}

}
