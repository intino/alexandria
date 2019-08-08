package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WorkflowTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("workflow"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.PersistenceManager;\nimport io.intino.alexandria.bpm.ProcessFactory;\nimport io.intino.alexandria.bpm.ProcessStatus;\nimport io.intino.alexandria.message.MessageHub;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport java.io.File;\n\npublic class Workflow extends io.intino.alexandria.bpm.Workflow {\n\tpublic Workflow(")).output(mark("box", "FirstUpperCase")).output(literal("Box box, File workspace) {\n\t\tsuper(messageHub, factory(box), new PersistenceManager.FilePersistenceManager(new File(workspace, \"bpm\")));\n\t}\n\n\tprivate static ProcessFactory factory(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\treturn (id, name) -> {\n\t\t\tswitch (name) {\n\t\t\t\t")).output(mark("process").multiple("\n")).output(literal("\n\t\t\t}\n\t\t\treturn null;\n\t\t};\n\t}\n\n\t")).output(mark("process", "method").multiple("\n")).output(literal("\n}")),
				rule().condition((trigger("method"))).output(literal("public void launch")).output(mark("", "firstUpperCase")).output(literal("() {\n\tmessageHub.sendMessage(\"")).output(mark("unit")).output(literal(".ProcessStatus\", new ProcessStatus(java.util.UUID.randomUUID().toString(), \"")).output(mark("")).output(literal("\", \"Enter\").message());\n}")),
				rule().condition((trigger("process"))).output(literal("case \"")).output(mark("")).output(literal("\": return new ")).output(mark("", "firstUpperCase")).output(literal("(box, id);"))
		);
	}
}