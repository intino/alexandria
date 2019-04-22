package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PassiveViewRequesterTemplate extends Template {

	protected PassiveViewRequesterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PassiveViewRequesterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display & accessible"))).add(literal("import Requester from \"./Requester\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Requester extends Requester {\n\nconstructor(element) {\n    super(element);\n};\n\nregisterPersonifiedDisplay = function(value) {\n\tthis.pushService.send({ op: \"registerPersonifiedTemplate\", s: \"")).add(mark("name", "lowercase")).add(literal("\", d: this.element.props.id, v: JSON.stringify(value)});\n}\n}")),
			rule().add((condition("type", "display"))).add(literal("import Requester from \"./Requester\";\n\nexport default class ")).add(mark("name", "firstUpperCase")).add(literal("Requester extends Requester {\n\nconstructor(element) {\n    super(element);\n};\n\n")).add(mark("request").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "request"))).add(mark("name")).add(literal(" = function(value) {\n\t")).add(mark("method")).add(literal("({ op: \"")).add(mark("name")).add(literal("\", s: \"")).add(mark("display", "lowercase")).add(literal("\", d: this.element.props.id, o: this.element.props.owner(), v: ")).add(mark("parameter")).add(literal("});\n}")),
			rule().add((condition("trigger", "request"))).add(mark("name")).add(literal(" = (")).add(expression().add(mark("parameterSignature"))).add(literal(") => {\n    ")).add(mark("method")).add(literal("({ op: \"")).add(mark("name")).add(literal("\", s: \"")).add(mark("display", "lowercase")).add(literal("\", d: this.element.props.id, o: this.element.props.owner()")).add(expression().add(literal(", v: ")).add(mark("parameter"))).add(literal("});\n};")),
			rule().add((condition("type", "object")), (condition("trigger", "parameter"))).add(literal("JSON.stringify(value)")),
			rule().add((condition("trigger", "parameter"))).add(literal("value")),
			rule().add((condition("attribute", "upload")), (condition("trigger", "method"))).add(literal("this.fileService.upload")),
			rule().add((condition("attribute", "download")), (condition("trigger", "method"))).add(literal("this.fileService.download")),
			rule().add((condition("trigger", "method"))).add(literal("this.pushService.send"))
		);
		return this;
	}
}