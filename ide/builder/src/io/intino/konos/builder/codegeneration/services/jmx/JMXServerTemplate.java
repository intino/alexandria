package io.intino.konos.builder.codegeneration.services.jmx;

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
			rule().add((condition("type", "jmxserver"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport io.intino.konos.jmx.JMXServer;\n\nimport io.intino.konos.alexandria.Box;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class JMX")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tpublic JMXServer init(Box box) {\n\t\tJMXServer server = new JMXServer(mbClasses(box));\n\t\tserver.init();\n\t\treturn server;\n\t}\n\n\tprivate Map<String, Object[]> mbClasses(Box box) {\n\t\tMap<String, Object[]> map = new HashMap<>();\n\t\tmap.put(\"")).add(mark("package")).add(literal(".jmx.")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("\", new Object[]{box});\n\t\treturn map;\n\t}\n}")),
			rule().add((condition("type", "jmx")), (condition("type", "interface"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".jmx;\n\nimport io.intino.konos.jmx.Description;\nimport io.intino.konos.jmx.Parameters;\n\nimport java.util.*;\nimport java.time.*;\n\npublic interface ")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("MBean {\n\n\t@Description(\"Shows information about the available operations\")\n\t@Parameters({})\n\tjava.util.List<String> help();\n\n\t")).add(mark("operation", "signature").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "signature"))).add(literal("@Description(\"")).add(mark("description")).add(literal("\")\n@Parameters({")).add(mark("parameter", "displayName").multiple(", ")).add(literal("})\n")).add(mark("returnType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(");")),
			rule().add((condition("type", "parameter")), (condition("trigger", "displayName"))).add(literal("\"")).add(mark("name")).add(literal("\"")),
			rule().add((condition("type", "parameter & list")), (condition("trigger", "withType"))).add(literal("java.util.List<")).add(mark("type")).add(literal("> ")).add(mark("name")),
			rule().add((condition("type", "parameter")), (condition("trigger", "withType"))).add(mark("type")).add(literal(" ")).add(mark("name")),
			rule().add((condition("type", "jmx")), (condition("type", "implementation"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".jmx;\n\nimport ")).add(mark("package")).add(literal(".")).add(mark("box", "validName", "FirstUpperCase")).add(literal("Box;\nimport java.util.*;\nimport java.time.*;\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "validName", "firstUpperCase")).add(literal(" implements ")).add(mark("name", "validName", "firstUpperCase")).add(literal("MBean {\n\n\tprivate final ")).add(mark("box", "validName", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic java.util.List<String> help() {\n\t\tList<String> operations = new ArrayList<>();\n\t\toperations.addAll(java.util.Arrays.asList(new String[]{")).add(mark("operation", "help").multiple(", ")).add(literal("}));\n\t\treturn operations;\n\t}\n\n\tpublic ")).add(mark("name", "validName", "firstUpperCase")).add(literal("(")).add(mark("box", "validName", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t")).add(mark("operation", "implementation").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "implementation"))).add(literal("public ")).add(mark("returnType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "validName", "firstLowerCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(") {\n\t")).add(mark("package", "ValidPackage")).add(literal(".actions.")).add(mark("action", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package", "ValidPackage")).add(literal(".actions.")).add(mark("action", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Action();\n\taction.box = box;\n\t")).add(expression().add(mark("parameter", "assign").multiple("\n"))).add(literal("\n\t")).add(expression().add(mark("returnType", "return")).add(literal(" "))).add(literal("action.execute();\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "help"))).add(literal("\"")).add(mark("returnType")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "validName", "firstLowerCase")).add(literal("(")).add(mark("parameter", "withType").multiple(", ")).add(literal(")")).add(expression().add(literal(": ")).add(mark("description"))).add(literal("\"")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name")).add(literal(" = ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "returnType")), (condition("attribute", "value:void")), (condition("trigger", "return"))),
			rule().add((condition("type", "returnType")), (condition("trigger", "return"))).add(literal("return")),
			rule().add((condition("type", "returnType & list")), (condition("trigger", "returnType"))).add(literal("java.util.List<")).add(mark("value")).add(literal(">")),
			rule().add((condition("type", "returnType")), (condition("trigger", "returnType"))).add(mark("value")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}