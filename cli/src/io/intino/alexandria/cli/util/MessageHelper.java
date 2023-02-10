package io.intino.alexandria.cli.util;

import io.intino.alexandria.cli.response.MessageData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHelper {

	private static final Pattern TagPattern = Pattern.compile("\\$([^\\$ ~]*)");
	public static String replaceVariables(String message, MessageData data) {
		if (data == null) return !hasVariables(message) ? message : null;
		Matcher matcher = TagPattern.matcher(message);
		while (matcher.find()) {
			String variable = message.substring(matcher.start(), matcher.end());
			message = message.replace(variable, data.getOrDefault(variable, variable));
		}
		return message;
	}

	private static boolean hasVariables(String template) {
		return template.contains("$");
	}

}
