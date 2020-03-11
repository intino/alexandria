package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WorkflowTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("workflow"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.PersistenceManager;\nimport io.intino.alexandria.bpm.ProcessFactory;\nimport io.intino.alexandria.bpm.ProcessStatus;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport java.io.File;\n\npublic class Workflow extends io.intino.alexandria.bpm.Workflow {\n\n\tprivate ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic Workflow(")).output(mark("box", "FirstUpperCase")).output(literal("Box box, File directory) {\n\t\tsuper(factory(box), new PersistenceManager.FilePersistenceManager(directory));\n\t\tthis.box = box;\n\t\tbox.terminal().subscribe((")).output(mark("terminal")).output(literal(".ProcessStatusConsumer) status -> receive(status));\n\t}\n\n\t")).output(mark("process", "publicMethod").multiple("\n\n")).output(literal("\n\n\t@Override\n\tpublic void send(ProcessStatus processStatus) {\n\t\tbox.terminal().publish(processStatus);\n\t}\n\n\tprivate static ProcessFactory factory(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\treturn (id, name) -> {\n\t\t\tswitch (name) {\n\t\t\t\t")).output(mark("process").multiple("\n")).output(literal("\n\t\t\t}\n\t\t\treturn null;\n\t\t};\n\t}\n}")),
				rule().condition((trigger("publicmethod"))).output(literal("public void launch")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\tregisterProcess(new ")).output(mark("name", "firstUpperCase")).output(literal("(box")).output(expression().output(literal(", ")).output(mark("parameter").multiple(", "))).output(literal("));\n}")),
				rule().condition((trigger("signature"))).output(literal("String ")).output(mark("")),
				rule().condition((trigger("process"))).output(literal("case \"")).output(mark("name")).output(literal("\": return new ")).output(mark("name", "firstUpperCase")).output(literal("(box, id);"))
		);
	}
}