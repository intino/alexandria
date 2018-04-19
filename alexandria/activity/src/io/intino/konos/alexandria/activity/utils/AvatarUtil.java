package io.intino.amidas.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvatarHelper {
	private static final int Size = 256;

	public static String generateAvatar(String text, String color) {
		return generateAvatar(text, Size, color);
	}

	public static String generateAvatar(String text, int size, String color) {
		String initials = initials(text);
		double textSize = Math.ceil(size / 2.5);
		String font = "Proxima Nova, proxima-nova, HelveticaNeue-Light, Helvetica Neue Light, Helvetica Neue, Helvetica, Arial, Lucida Grande, sans-serif";

		String avatar = "<svg height='" + size + "' width='" + size + "' style='border-radius:100px;border:3px solid white;background: " + color + "'>";
		avatar += "<text text-anchor='middle' x='50%' y='50%' dy='0.35em' fill='white' font-size='" + textSize + "' font-family='" + font + "'>" + initials.toUpperCase() + "</text>";
		avatar += "</svg>";

		return avatar;
	}

	private static String initials(String fullName) {
		Pattern p = Pattern.compile("((^| )[A-Za-z])");
		Matcher m = p.matcher(fullName);
		StringBuilder initials = new StringBuilder();
		while (m.find()) initials.append(m.group().trim());
		return initials.toString();
	}

}
