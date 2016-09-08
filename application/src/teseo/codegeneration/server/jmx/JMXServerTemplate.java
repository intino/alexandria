package teseo.codegeneration.server.jmx;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMXServerTemplate extends Template {

	protected JMXServerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMXServerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "jmxserver"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport org.siani.teseo.jmx.JMXServer;\nimport tara.magritte.Graph;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class JMX")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tpublic JMXServer init(int port, Graph graph) {\n\t\tJMXServer server = new JMXServer(mbClasses(graph));\n\t\tserver.init(port);\n\t\treturn server;\n\t}\n\n\tprivate Map<String, Object[]> mbClasses(Graph graph) {\n        Map<String, Object[]> map = new HashMap<>();\n        map.put(\"")).add(mark("package")).add(literal(".jmx.")).add(mark("name", "firstUpperCase")).add(literal("\", new Object[]{graph});\n        return map;\n\t}\n}")),
			rule().add((condition("type", "jmx")), (condition("type", "interface"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".jmx;\n\nimport java.util.*;\nimport java.time.*;\n\npublic interface ")).add(mark("name", "firstUpperCase")).add(literal("MBean {\n\n    ")).add(mark("operation", "signature").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "signature"))).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(");")),
			rule().add((condition("type", "parameter")), (condition("trigger", "withType"))).add(mark("type")).add(literal(" ")).add(mark("name")),
			rule().add((condition("type", "jmx")), (condition("type", "implementation"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".jmx;\n\nimport tara.magritte.Graph;\nimport java.util.*;\nimport java.time.*;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" implements ")).add(mark("name", "firstUpperCase")).add(literal("MBean {\n\n    private final Graph graph;\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(Graph graph) {\n        this.graph = graph;\n    }\n\n    ")).add(mark("operation", "implementation").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "implementation"))).add(literal("public ")).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(") {\n    ")).add(expression().add(mark("returnType", "return")).add(literal(" "))).add(literal("new ")).add(mark("package", "validname")).add(literal(".actions.")).add(mark("action", "firstUpperCase")).add(literal("Action(graph")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(").execute();\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("return"))
		);
		return this;
	}
}