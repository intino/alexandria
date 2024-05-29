package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class ProcessTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("process", "src")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.Task;\nimport io.intino.alexandria.bpm.Task.Type.*;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\n\t")).output(placeholder("parameter", "fieldName").multiple("\n")).output(literal("\n\n\t")).output(placeholder("name", "firstUpperCase")).output(literal("(String id, ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(id, box);\n\t}\n\n\t")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("box", "firstUpperCase")).output(literal("Box box")).output(expression().output(literal(", ")).output(placeholder("parameter", "signature").multiple(", "))).output(literal(") {\n\t\tsuper(java.util.UUID.randomUUID().toString(), box);\n\t\t")).output(expression().output(placeholder("parameter", "put").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(placeholder("state", "execute").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(placeholder("state", "accept").multiple("\n\n"))).output(literal("\n}")));
		rules.add(rule().condition(allTypes("process")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".bpm;\n\nimport static io.intino.alexandria.bpm.State.Type.*;\nimport io.intino.alexandria.bpm.Task;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\n\nimport java.util.List;\n\nimport static io.intino.alexandria.bpm.Link.Type.*;\n\npublic abstract class Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(" extends io.intino.alexandria.bpm.Process {\n\n\tprotected ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box;\n\n\tAbstract")).output(placeholder("name", "firstUpperCase")).output(literal("(String id, ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(id);\n\t\tthis.box = box;\n\t\tinit();\n\t}\n\n\tprivate void init() {\n\t\t")).output(expression().output(placeholder("state").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("link").multiple("\n"))).output(literal("\n\t}\n\n\t@Override\n\tpublic String name() {\n\t\treturn \"")).output(placeholder("name", "firstUpperCase")).output(literal("\";\n\t}\n\n\t")).output(placeholder("state", "abstractExecute").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(placeholder("state", "abstractaccept").multiple("\n\n"))).output(literal("\n\n\t")).output(placeholder("state", "method").multiple("\n\n")).output(literal("\n}")));
		rules.add(rule().condition(trigger("method")).output(literal("private Task execute")).output(placeholder("method", "firstUpperCase")).output(literal("() {\n\treturn new Task(Task.Type.")).output(placeholder("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic void execute() {\n\t\t\t")).output(placeholder("method", "FirstLowerCase")).output(literal("();\n\t\t}\n\n\t\t@Override\n\t\tpublic boolean accept() {\n\t\t\treturn accept")).output(placeholder("method", "FirstUpperCase")).output(literal("();\n\t\t}\n\t};\n}")));
		rules.add(rule().condition(trigger("abstractexecute")).output(literal("protected abstract void ")).output(placeholder("method", "FirstLowerCase")).output(literal("();")));
		rules.add(rule().condition(trigger("execute")).output(literal("protected void ")).output(placeholder("method", "FirstLowerCase")).output(literal("() {\n\n}")));
		rules.add(rule().condition(trigger("abstractaccept")).output(literal("protected boolean accept")).output(placeholder("method", "FirstUpperCase")).output(literal("() {\n\treturn true;\n}")));
		rules.add(rule().condition(all(allTypes("conditional"), trigger("accept"))).output(literal("protected boolean accept")).output(placeholder("method", "FirstUpperCase")).output(literal("() {\n\treturn true;\n}")));
		rules.add(rule().condition(trigger("state")).output(literal("addState(new io.intino.alexandria.bpm.State(\"")).output(placeholder("label")).output(literal("\", execute")).output(placeholder("method", "firstUpperCase")).output(literal("()")).output(expression().output(literal(", ")).output(placeholder("type", "lowercase", "FirstUpperCase").multiple(", "))).output(literal("));")));
		rules.add(rule().condition(trigger("link")).output(literal("addLink(new io.intino.alexandria.bpm.Link(\"")).output(placeholder("from")).output(literal("\", \"")).output(placeholder("to")).output(literal("\", ")).output(placeholder("type", "linkType")).output(literal("));")));
		rules.add(rule().condition(all(attribute("", "Default"), trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Default")));
		rules.add(rule().condition(all(attribute("", "Line"), trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Inclusive")));
		rules.add(rule().condition(all(attribute("", "Exclusive"), trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Exclusive")));
		rules.add(rule().condition(all(attribute("", "Inclusive"), trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Inclusive")));
		rules.add(rule().condition(trigger("put")).output(literal("put(")).output(placeholder("", "firstUpperCase")).output(literal(", ")).output(placeholder("", "firstLowerCase")).output(literal(");")));
		rules.add(rule().condition(trigger("signature")).output(literal("String ")).output(placeholder("", "firstLowerCase")));
		rules.add(rule().condition(trigger("fieldname")).output(literal("private static final String ")).output(placeholder("", "FirstUpperCase")).output(literal(" = \"")).output(placeholder("")).output(literal("\";")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}