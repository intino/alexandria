package io.intino.konos.builder.codegeneration.process;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ProcessTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("process","src"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.Task;\nimport io.intino.alexandria.bpm.Task.Type.*;\nimport io.intino.alexandria.message.MessageHub;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "firstUpperCase")).output(literal(" {\n\n\t")).output(mark("name", "firstUpperCase")).output(literal("(MessageHub messageHub, String id) {\n\t\tsuper(messageHub, id);\n\t}\n\n\t")).output(mark("state", "task").multiple("\n\n")).output(literal("\n}")),
			rule().condition((type("process"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.PersistenceManager.InMemoryPersistenceManager;\nimport static io.intino.alexandria.bpm.State.Type.*;\nimport static io.intino.alexandria.bpm.Link.Type.*;\nimport io.intino.alexandria.message.Message;\nimport io.intino.alexandria.message.MessageHub;\n\nimport java.util.List;\n\nimport static io.intino.alexandria.bpm.Link.Type.*;\n\npublic abstract class Abstract")).output(mark("name", "firstUpperCase")).output(literal(" extends io.intino.alexandria.bpm.Process {\n\n\tprivate MessageHub messageHub;\n\n\tAbstract")).output(mark("name", "firstUpperCase")).output(literal("(MessageHub messageHub, String id) {\n\t\tsuper(id);\n\t\tthis.messageHub = messageHub;\n\t\t")).output(mark("state").multiple("\n")).output(literal("\n\t\t")).output(mark("link").multiple("\n")).output(literal("\n\t}\n\n\t@Override\n\tpublic String name() {\n\t\treturn \"")).output(mark("name", "firstUpperCase")).output(literal("\";\n\t}\n\n\t")).output(mark("state", "method").multiple("\n\n")).output(literal("\n}")),
			rule().condition((type("callActivity")), (trigger("method"))).output(literal("private io.intino.alexandria.bpm.Task ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn new io.intino.alexandria.bpm.Task(")).output(mark("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic String execute() {\n\t\t\tmessageHub.sendMessage(\"ProcessStatus\", new io.intino.alexandria.bpm.ProcessStatus(java.util.UUID.randomUUID().toString(), \"")).output(mark("task")).output(literal("\", \"Enter\", id(), id(), \"")).output(mark("name")).output(literal("\").message());\n\t\t\treturn \"Subprocess called ")).output(mark("task")).output(literal("\";\n\t\t}\n\t};\n}")),
			rule().condition(not(type("callActivity")), (trigger("method"))).output(literal("protected abstract io.intino.alexandria.bpm.Task ")).output(mark("name", "firstLowerCase")).output(literal("();")),
			rule().condition((trigger("state"))).output(literal("addState(new io.intino.alexandria.bpm.State(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "firstLowerCase")).output(literal("()")).output(expression().output(literal(", ")).output(mark("type", "logercase", "FirstUpperCase"))).output(literal("));")),
			rule().condition((trigger("link"))).output(literal("addLink(new io.intino.alexandria.bpm.Link(\"")).output(mark("from")).output(literal("\", \"")).output(mark("to")).output(literal("\", ")).output(mark("type")).output(literal("));")),
			rule().condition(not(type("callActivity")), (trigger("task"))).output(literal("protected Task ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn new Task(")).output(mark("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic String execute() {\n\t\t\treturn null;\n\t\t}\n\t};\n}")),
			rule().condition((attribute("", "CallActivity")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.CallActivity")),
			rule().condition((attribute("", "User")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.Manual")),
			rule().condition((attribute("", "Manual")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.Manual")),
			rule().condition((trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.Automatic"))
		);
	}
}