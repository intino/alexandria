package teseo.codegeneration.server.action;

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
			rule().add((condition("type", "action"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".action;\n\nimport tara.magritte.Graph;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Action {\n\n    private final Graph graph;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Action(Graph graph) {\n\t\tthis.graph = graph;\n\t}\n\n\tpublic ")).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute(")).add(mark("parameter").multiple(", ")).add(literal(") {\n        ")).add(mark("returnType", "return")).add(literal("\n\t}\n}")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("return null;"))
		);
		return this;
	}
}