package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetRequesterTemplate extends Template {

	protected WidgetRequesterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetRequesterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widget"))).add(literal("var ")).add(mark("name", "FirstUpperCase")).add(literal("WidgetBehaviors = ")).add(mark("name", "FirstUpperCase")).add(literal("WidgetBehaviors || {};\n\n")).add(mark("name", "FirstUpperCase")).add(literal("WidgetBehaviors.Requester = {\n\n    ")).add(mark("requester").multiple(",\n")).add(literal("\n\n};")),
			rule().add((condition("trigger", "requester"))).add(mark("name")).add(literal(" : function(")).add(expression().add(mark("parameterSignature"))).add(literal(") {\n\tthis.")).add(mark("method")).add(literal("(\"")).add(mark("name")).add(literal("\"")).add(expression().add(literal(", ")).add(mark("parameter"))).add(literal(");\n}")),
			rule().add((condition("trigger", "parameter"))).add(literal("{ \"value\" : value }")),
			rule().add((condition("trigger", "parameterSignature"))).add(literal("value")),
			rule().add((condition("attribute", "Asset")), (condition("trigger", "method"))).add(literal("download")),
			rule().add((condition("trigger", "method"))).add(literal("carry"))
		);
		return this;
	}
}