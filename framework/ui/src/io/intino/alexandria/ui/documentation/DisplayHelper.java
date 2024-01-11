package io.intino.alexandria.ui.documentation;

import io.intino.alexandria.schemas.Widget;

import java.util.function.Function;

public class DisplayHelper {

	public static String label(Widget widget, Function<String, String> translator) {
		return translator.apply(name(widget));
	}

	public static String name(Widget widget) {
		return widget.getClass().getSimpleName().replace("Widget", "");
	}

}
