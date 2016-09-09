package teseo.codegeneration.action;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ActionTemplate extends Template {

	protected ActionTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ActionTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "action"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".actions;\n\nimport tara.magritte.Graph;\nimport java.time.*;\nimport java.util.*;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Action {\n\n    public Graph graph;\n    ")).add(expression().add(mark("parameter", "type").multiple("\n"))).add(literal("\n\n\tpublic ")).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute() ")).add(expression().add(literal("throws ")).add(mark("throws").multiple(", "))).add(literal(" {\n        ")).add(mark("returnType", "return")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("return null;"))
		);
		return this;
	}
}