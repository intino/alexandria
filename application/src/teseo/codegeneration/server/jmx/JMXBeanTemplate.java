package teseo.codegeneration.server.jmx;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMXBeanTemplate extends Template {

	protected JMXBeanTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMXBeanTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "jmx"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".jmx;\n\nimport tara.magritte.Graph;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" implements ")).add(mark("name", "firstUpperCase")).add(literal("MBean {\n\n    private final Graph graph;\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(Graph graph) {\n        this.graph = graph;\n    }\n\n    ")).add(mark("action")).add(literal("\n}")),
			rule().add((condition("type", "action")), (condition("trigger", "action"))).add(literal("public ")).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("parameter").multiple(", ")).add(literal(") {\n    ")).add(expression().add(mark("returnType", "return")).add(literal(" "))).add(literal("new ")).add(mark("package", "validname")).add(literal(".action.")).add(mark("name", "firstUpperCase")).add(literal("Action(graph).execute(")).add(mark("parameter", "parameterName").multiple(", ")).add(literal(");\n}")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("return"))
		);
		return this;
	}
}