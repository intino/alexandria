package io.intino.pandora.plugin.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetsTemplate extends Template {

	protected WidgetsTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetsTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widgets"))).add(mark("widget").multiple("\n")),
			rule().add((condition("trigger", "widget"))).add(literal("<link rel=\"import\" href=\"")).add(mark("value")).add(literal("-widget.html\">\n"))
		);
		return this;
	}
}