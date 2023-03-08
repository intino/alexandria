package io.intino.alexandria.cli.util;

import io.intino.alexandria.cli.response.MessageData;
import io.intino.alexandria.logger.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHelper {

	private static final Pattern TagPattern = Pattern.compile("\\$([^\\$ \\)`~]*)");
	public static String replaceVariables(String message, MessageData data) {
		try {
			if (data == null || data.isEmpty()) return !hasVariables(message) ? message : null;
			Matcher matcher = TagPattern.matcher(message);
			String result = message;
			while (matcher.find()) {
				String variable = result.substring(matcher.start(), matcher.end());
				String value = data.getOrDefault(variable.substring(1), variable.substring(1));
				result = result.replace(variable, value != null ? value : "");
				matcher = TagPattern.matcher(result);
			}
			return result;
		}
		catch (Throwable e) {
			Logger.error(e);
			return null;
		}
	}

	private static boolean hasVariables(String template) {
		return template.contains("$");
	}

}
