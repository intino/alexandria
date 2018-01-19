package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetNotifierTemplate extends Template {

	protected WidgetNotifierTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetNotifierTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widget"))).add(literal("var ")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors = ")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors || {};\n\n")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors.NotifierListener = {\n\n    listenToDisplay : function() {\n\t\tif (this.display == null) return;\n        var widget = this;\n        ")).add(mark("notification").multiple("\n")).add(literal("\n    }\n};")),
			rule().add((condition("trigger", "notification"))).add(literal("this.when(\"")).add(mark("name")).add(literal("\")")).add(expression().add(mark("to"))).add(literal(".execute(function(parameters) {\n\twidget._")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter"))).add(literal(");\n});")),
			rule().add((condition("trigger", "parameter"))).add(literal("parameters.value")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "to"))).add(literal(".toSelf()")),
			rule().add((condition("trigger", "to")))
		);
		return this;
	}
}