package io.intino.pandora.plugin.codegeneration.server.jmx;

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
			rule().add((condition("type", "jmxserver"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport io.intino.pandora.jmx.JMXServer;\nimport io.intino.pandora.Box;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class JMX")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tpublic JMXServer init(int port, Box box) {\n\t\tJMXServer server = new JMXServer(mbClasses(box));\n\t\tserver.init(port);\n\t\treturn server;\n\t}\n\n\tprivate Map<String, Object[]> mbClasses(Box box) {\n        Map<String, Object[]> map = new HashMap<>();\n        map.put(\"")).add(mark("package")).add(literal(".jmx.")).add(mark("name", "firstUpperCase")).add(literal("\", new Object[]{box});\n        return map;\n\t}\n}")),
			rule().add((condition("type", "jmx")), (condition("type", "interface"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".jmx;\n\nimport java.util.*;\nimport java.time.*;\n\npublic interface ")).add(mark("name", "firstUpperCase")).add(literal("MBean {\n\n    ")).add(mark("operation", "signature").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "signature"))).add(mark("returnType")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(");")),
			rule().add((condition("type", "returnType")), (condition("trigger", "return"))).add(literal("return")),
			rule().add((condition("type", "returnType & list")), (condition("trigger", "returnType"))).add(literal("java.util.List<")).add(mark("value")).add(literal(">")),
			rule().add((condition("type", "returnType")), (condition("trigger", "returnType"))).add(mark("value")),
			rule().add((condition("type", "parameter & list")), (condition("trigger", "withType"))).add(literal("java.util.List<")).add(mark("type")).add(literal("> ")).add(mark("name")),
			rule().add((condition("type", "parameter")), (condition("trigger", "withType"))).add(mark("type")).add(literal(" ")).add(mark("name")),
			rule().add((condition("type", "jmx")), (condition("type", "implementation"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".jmx;\n\nimport ")).add(mark("package")).add(literal(".")).add(mark("box", "validName", "FirstUpperCase")).add(literal("Box;\nimport java.util.*;\nimport java.time.*;\n\npublic class ")).add(mark("name", "validName", "firstUpperCase")).add(literal(" implements ")).add(mark("name", "validName", "firstUpperCase")).add(literal("MBean {\n\n    private final ")).add(mark("box", "validName", "FirstUpperCase")).add(literal("Box box;\n\n    public ")).add(mark("name", "validName", "firstUpperCase")).add(literal("(")).add(mark("box", "validName", "FirstUpperCase")).add(literal("Box box) {\n        this.box = box;\n    }\n\n    ")).add(mark("operation", "implementation").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "implementation"))).add(literal("public ")).add(mark("returnType")).add(literal(" ")).add(mark("name", "validName", "firstLowerCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(") {\n    ")).add(mark("package", "ValidPackage")).add(literal(".actions.")).add(mark("action", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package", "ValidPackage")).add(literal(".actions.")).add(mark("action", "firstUpperCase")).add(literal("Action();\n    action.box = box;\n    ")).add(expression().add(mark("parameter", "assign").multiple("\n"))).add(literal("\n    ")).add(expression().add(mark("returnType", "return")).add(literal(" "))).add(literal("action.execute();\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name")).add(literal(" = ")).add(mark("name")).add(literal(";")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return")))
		);
		return this;
	}
}