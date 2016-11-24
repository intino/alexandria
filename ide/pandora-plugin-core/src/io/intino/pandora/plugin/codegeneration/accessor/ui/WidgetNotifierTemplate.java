package io.intino.pandora.plugin.codegeneration.accessor.ui;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class WidgetNotifierTemplate extends Template {

	protected WidgetNotifierTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetNotifierTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "widget"))).add(literal("var ApplicationWidgetBehaviors = ApplicationWidgetBehaviors || {};\n\nApplicationWidgetBehaviors.NotifierListener = {\n\n    listenToDisplay : function() {\n        var widget = this;\n        ")).add(mark("notification").multiple("\n")).add(literal("\n    }\n\n};")),
				rule().add((condition("trigger", "notification"))).add(literal("this.when(\"")).add(mark("name")).add(literal("\").")).add(expression().add(mark("type"))).add(literal(".execute(function(parameters) {\n\twidget._")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter"))).add(literal(");\n});")),
			rule().add((condition("trigger", "parameter"))).add(literal("parameters.value")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "type"))).add(literal(".toSelf()")),
			rule().add((condition("trigger", "type")))
		);
		return this;
	}
}