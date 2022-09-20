package io.intino.alexandria.office.components;

import static java.util.Objects.requireNonNull;

public interface Style {

	String xml();

	class Bold implements Style {

		@Override
		public String xml() {
			return "<w:b/><w:bCs/>";
		}
	}

	class Italic implements Style {

		@Override
		public String xml() {
			return "<w:i/><w:iCs/>";
		}
	}

	class FontSize implements Style {

		private final int size;

		public FontSize(int size) {
			this.size = size;
		}

		@Override
		public String xml() {
			return String.format("<w:sz w:val=\"%d\"/><w:szCs w:val=\"%d\"/>", size * 2, size * 2);
		}
	}

	class Color implements Style {

		public static final Color Black = new Color("000000");
		public static final Color White = new Color("FFFFFF");
		public static final Color Red = new Color("FF0000");
		public static final Color Green = new Color("00FF00");
		public static final Color Blue = new Color("0000FF");
		public static final Color Orange = new Color("FFC000");
		public static final Color Yellow = new Color("FFFF00");
		public static final Color Violet = new Color("7030A0");
		public static final Color DarkGreen = new Color("00B050");
		public static final Color DarkBlue = new Color("0070C0");

		private final String value;
		private String themeColor;
		private String themeShade;

		public Color(int color) {
			this(Integer.toHexString(color));
		}

		public Color(String hexColor) {
			this(hexColor, null, null);
		}

		public Color(String hexColor, String themeColor, String themeShade) {
			this.value = requireNonNull(hexColor).toUpperCase();
			this.themeColor = themeColor;
			this.themeShade = themeShade;
		}

		@Override
		public String xml() {
			return "<w:color "
					+ "w:val=\"" + value + "\""
					+ (themeColor != null ? " w:themeColor=\"" + themeColor + "\"" : "")
					+ (themeShade != null ? " w:themeShade=\"" + themeShade + "\"" : "")
					+ "/>";
		}
	}

	class Underlined implements Style {

		@Override
		public String xml() {
			return "<w:u w:val=\"single\"/>";
		}
	}
}
