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
			rule().add((condition("type", "widget & accessible"))).add(literal("var ")).add(mark("name", "FirstUpperCase")).add(literal("ProxyBehaviors = ")).add(mark("name", "FirstUpperCase")).add(literal("ProxyBehaviors || {};\n\n")).add(mark("name", "FirstUpperCase")).add(literal("ProxyBehaviors.NotifierListener = {\n\n\tproperties : {\n\t\t_listeningToDisplay : { type: Boolean, value: function() { return false; } }\n\t},\n\n\tlistenToDisplay : function() {\n\t\tif (this.display == null || this._listeningToDisplay) return;\n\t\tvar widget = this;\n\t\tthis.when(\"refreshBaseUrl\").toSelf().execute(function(parameters) {\n\t\t\twidget._refreshBaseUrl(parameters.value);\n\t\t});\n\t\tthis.when(\"refreshError\").toSelf().execute(function(parameters) {\n\t\t\twidget._refreshError(parameters.value);\n\t\t});\n\t\tthis._listeningToDisplay = true;\n\t}\n};")),
			rule().add((condition("type", "widget"))).add(literal("var ")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors = ")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors || {};\n\n")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors.NotifierListener = {\n\n\tproperties : {\n\t\t_listeningToDisplay : { type: Boolean, value: function() { return false; } }\n\t},\n\n    listenToDisplay : function() {\n\t\tif (this.display == null || this._listeningToDisplay) return;\n        var widget = this;\n        ")).add(mark("notification").multiple("\n")).add(literal("\n        this._listeningToDisplay = true;\n    }\n};")),
			rule().add((condition("trigger", "notification"))).add(literal("this.when(\"")).add(mark("name")).add(literal("\")")).add(expression().add(mark("to"))).add(literal(".execute(function(parameters) {\n\twidget._")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter"))).add(literal(");\n});")),
			rule().add((condition("trigger", "parameter"))).add(literal("parameters.value")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "to"))).add(literal(".toSelf()")),
			rule().add((condition("trigger", "to")))
		);
		return this;
	}
}