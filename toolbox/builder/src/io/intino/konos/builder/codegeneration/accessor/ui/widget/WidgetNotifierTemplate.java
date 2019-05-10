package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WidgetNotifierTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("accessible", "widget"))).output(literal("var ")).output(mark("name", "FirstUpperCase")).output(literal("ProxyBehaviors = ")).output(mark("name", "FirstUpperCase")).output(literal("ProxyBehaviors || {};\n\n")).output(mark("name", "FirstUpperCase")).output(literal("ProxyBehaviors.NotifierListener = {\n\n\tproperties : {\n\t\t_listeningToDisplay : { type: Boolean, value: function() { return false; } }\n\t},\n\n\tlistenToDisplay : function() {\n\t\tif (this.display == null || this._listeningToDisplay) return;\n\t\tvar widget = this;\n\t\tthis.when(\"refreshBaseUrl\").toSelf().execute(function(parameters) {\n\t\t\twidget._refreshBaseUrl(parameters.value);\n\t\t});\n\t\tthis.when(\"refreshError\").toSelf().execute(function(parameters) {\n\t\t\twidget._refreshError(parameters.value);\n\t\t});\n\t\tthis._listeningToDisplay = true;\n\t}\n};")),
				rule().condition((type("widget"))).output(literal("var ")).output(mark("name", "FirstUpperCase")).output(literal("Behaviors = ")).output(mark("name", "FirstUpperCase")).output(literal("Behaviors || {};\n\n")).output(mark("name", "FirstUpperCase")).output(literal("Behaviors.NotifierListener = {\n\n\tproperties : {\n\t\t_listeningToDisplay : { type: Boolean, value: function() { return false; } }\n\t},\n\n    listenToDisplay : function() {\n\t\tif (this.display == null || this._listeningToDisplay) return;\n        var widget = this;\n        ")).output(mark("notification").multiple("\n")).output(literal("\n        this._listeningToDisplay = true;\n    }\n};")),
				rule().condition((trigger("notification"))).output(literal("this.when(\"")).output(mark("name")).output(literal("\")")).output(expression().output(mark("to"))).output(literal(".execute(function(parameters) {\n\twidget._")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameter"))).output(literal(");\n});")),
				rule().condition((trigger("parameter"))).output(literal("parameters.value")),
				rule().condition((attribute("display")), (trigger("to"))).output(literal(".toSelf()")),
				rule().condition((trigger("to")))
		);
	}
}