package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class WorkflowTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("workflow")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.PersistenceManager;\nimport io.intino.alexandria.bpm.ProcessFactory;\nimport io.intino.alexandria.bpm.ProcessStatus;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\nimport java.io.File;\n\npublic class Workflow extends io.intino.alexandria.bpm.Workflow {\n\n\tprivate ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic Workflow(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tsuper(factory(box), new PersistenceManager.FilePersistenceManager(")).output(placeholder("directory", "customizeDirectory")).output(literal("));\n\t\tthis.box = box;\n\t\tbox.terminal().subscribe((")).output(placeholder("terminal")).output(literal(".ProcessStatusConsumer) (status, topic) -> receive(status));\n\t}\n\n\t")).output(placeholder("process", "publicMethod").multiple("\n\n")).output(literal("\n\n\t@Override\n\tpublic void send(ProcessStatus processStatus) {\n\t\tbox.terminal().publish(processStatus);\n\t}\n\n\tprivate static ProcessFactory factory(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\treturn (id, name) -> {\n\t\t\tswitch (name) {\n\t\t\t\t")).output(placeholder("process").multiple("\n")).output(literal("\n\t\t\t}\n\t\t\treturn null;\n\t\t};\n\t}\n}")));
		rules.add(rule().condition(trigger("publicmethod")).output(literal("public void launch")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\tregisterProcess(new ")).output(placeholder("name", "firstUpperCase")).output(literal("(box")).output(expression().output(literal(", ")).output(placeholder("parameter", "firstLowerCase").multiple(", "))).output(literal("));\n}")));
		rules.add(rule().condition(trigger("signature")).output(literal("String ")).output(placeholder("", "firstLowerCase")));
		rules.add(rule().condition(trigger("process")).output(literal("case \"")).output(placeholder("name", "FirstUpperCase")).output(literal("\": return new ")).output(placeholder("name", "firstUpperCase")).output(literal("(id, box);")));
		rules.add(rule().condition(all(allTypes("archetype"), trigger("customizedirectory"))).output(literal("new ")).output(placeholder("package")).output(literal(".Archetype(box.configuration().home()).")).output(placeholder("path")));
		rules.add(rule().condition(trigger("customizedirectory")).output(literal("new java.io.File(\"")).output(placeholder("path")).output(literal("\")")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}