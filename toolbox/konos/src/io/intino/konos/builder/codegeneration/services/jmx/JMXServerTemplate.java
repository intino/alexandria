package io.intino.konos.builder.codegeneration.services.jmx;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class JMXServerTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("jmxserver")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.jmx.JMXServer;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.core.Box;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class JMX")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n\tpublic JMXServer init(Box box) {\n\t\tJMXServer server = new JMXServer(mbClasses(box));\n\t\tserver.init(\"intino")).output(expression().output(literal(".")).output(placeholder("path"))).output(literal("\");\n\t\treturn server;\n\t}\n\n\tprivate Map<String, Object[]> mbClasses(Box box) {\n\t\tMap<String, Object[]> map = new HashMap<>();\n\t\tmap.put(\"")).output(placeholder("package")).output(literal(".jmx.")).output(placeholder("name", "PascalCase")).output(literal("\", new Object[]{box});\n\t\treturn map;\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("jmx"), allTypes("interface"))).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(".jmx;\n\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.jmx.Description;\nimport io.intino.alexandria.jmx.Parameters;\n\nimport java.util.*;\nimport java.time.*;\n\npublic interface ")).output(placeholder("name", "PascalCase")).output(literal("MBean {\n\t")).output(placeholder("operation", "signature").multiple("\n\n")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("operation"), trigger("signature"))).output(literal("@Description(\"")).output(placeholder("description")).output(literal("\")\n@Parameters({")).output(placeholder("parameter", "displayName").multiple(", ")).output(literal("})\n")).output(placeholder("returnType")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(")).output(placeholder("parameter", "withType").multiple(", ")).output(literal(");")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("displayname"))).output(literal("\"")).output(placeholder("name")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("parameter", "list"), trigger("withtype"))).output(literal("java.util.List<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("withtype"))).output(placeholder("type")).output(literal(" ")).output(placeholder("name")).output(literal("\n")));
		rules.add(rule().condition(all(allTypes("jmx"), allTypes("implementation"))).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(".jmx;\n\nimport io.intino.alexandria.logger.Logger;\nimport javax.management.*;\nimport ")).output(placeholder("package")).output(literal(".")).output(placeholder("box", "validName", "FirstUpperCase")).output(literal("Box;\nimport java.util.*;\nimport java.time.*;\n\nimport static javax.management.MBeanOperationInfo.ACTION;\n\npublic class ")).output(placeholder("name", "CamelCase", "validName", "firstUpperCase")).output(literal(" extends StandardMBean implements ")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("MBean {\n\tprivate final ")).output(placeholder("box", "validName", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("(")).output(placeholder("box", "validName", "FirstUpperCase")).output(literal("Box box) throws NotCompliantMBeanException {\n\t\tsuper(")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("MBean.class);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic MBeanInfo getMBeanInfo() {\n\t\treturn new MBeanInfo(this.getClass().getName(), \"")).output(placeholder("name")).output(literal("\", null, null, new MBeanOperationInfo[]{")).output(placeholder("operation", "call").multiple(", ")).output(literal("}, null);\n\t}\n\n\t")).output(placeholder("operation", "info").multiple("\n\n")).output(literal("\n\t")).output(placeholder("operation", "implementation").multiple("\n\n")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("operation"), trigger("call"))).output(placeholder("name", "CamelCase", "validName", "firstLowerCase")).output(literal("Info()")));
		rules.add(rule().condition(all(allTypes("operation"), trigger("info"))).output(literal("public MBeanOperationInfo ")).output(placeholder("name", "CamelCase", "validName", "firstLowerCase")).output(literal("Info() {\n\tMBeanParameterInfo[] params = new MBeanParameterInfo[] {\n\t\t")).output(expression().output(placeholder("parameter", "info").multiple(",\n"))).output(literal("\n\t};\n\treturn new MBeanOperationInfo(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("description")).output(literal("\", params, \"")).output(expression().output(placeholder("returnType"))).output(literal("\", ACTION);\n}")));
		rules.add(rule().condition(all(allTypes("operation"), trigger("implementation"))).output(literal("public ")).output(placeholder("returnType")).output(literal(" ")).output(placeholder("name", "CamelCase", "validName", "firstLowerCase")).output(literal("(")).output(placeholder("parameter", "withType").multiple(", ")).output(literal(") {\n\ttry {\n\t")).output(placeholder("package", "ValidPackage")).output(literal(".actions.")).output(placeholder("action", "PascalCase")).output(literal("Action action = new ")).output(placeholder("package", "ValidPackage")).output(literal(".actions.")).output(placeholder("action", "PascalCase")).output(literal("Action();\n\taction.box = box;\n\t")).output(expression().output(placeholder("parameter", "assign").multiple("\n"))).output(literal("\n\t")).output(expression().output(placeholder("returnType", "return")).output(literal(" "))).output(literal("action.execute();\n\t} catch (Throwable e) {\n\t\tLogger.error(e);\n\t\tthrow e;\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("info"))).output(literal("new MBeanParameterInfo(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("type")).output(literal("\", \"")).output(placeholder("description")).output(literal("\")")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("assign"))).output(literal("action.")).output(placeholder("name")).output(literal(" = ")).output(placeholder("name")).output(literal(";")));
		rules.add(rule().condition(all(all(allTypes("returnType"), attribute("value","void")), trigger("return"))));
		rules.add(rule().condition(all(allTypes("returnType"), trigger("return"))).output(literal("return")));
		rules.add(rule().condition(all(allTypes("returnType", "list"), trigger("returntype"))).output(literal("java.util.List<")).output(placeholder("value")).output(literal(">")));
		rules.add(rule().condition(all(allTypes("returnType"), trigger("returntype"))).output(placeholder("value")));
		rules.add(rule().condition(trigger("quoted")).output(literal("\"")).output(placeholder("value")).output(literal("\"")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}