package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ProcessTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("process","src"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.Task;\nimport io.intino.alexandria.bpm.Task.Type.*;\nimport io.intino.alexandria.message.MessageHub;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "firstUpperCase")).output(literal(" {\n\n\t")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box, String id) {\n\t\tsuper(box, id);\n\t}\n\n\t")).output(mark("state", "task").multiple("\n\n")).output(literal("\n}")),
			rule().condition((type("process"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport static io.intino.alexandria.bpm.State.Type.*;\nimport io.intino.alexandria.bpm.Task;\nimport io.intino.alexandria.message.MessageHub;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\n\nimport java.util.List;\n\nimport static io.intino.alexandria.bpm.Link.Type.*;\n\npublic abstract class Abstract")).output(mark("name", "firstUpperCase")).output(literal(" extends io.intino.alexandria.bpm.Process {\n\n\tprotected ")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\t")).output(expression().output(mark("accessor", "field").multiple("\n"))).output(literal("\n\n\tAbstract")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box, String id) {\n\t\tsuper(id);\n\t\tthis.box = box;\n\t\t")).output(expression().output(mark("accessor", "assign").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("accessor", "subscribe").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("state").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("link").multiple("\n"))).output(literal("\n\t}\n\n\t@Override\n\tpublic String name() {\n\t\treturn \"")).output(mark("name", "firstUpperCase")).output(literal("\";\n\t}\n\n\t")).output(mark("state", "abstractTask").multiple("\n\n")).output(literal("\n\n\t")).output(mark("state", "method").multiple("\n\n")).output(literal("\n}")),
			rule().condition((type("callActivity")), (trigger("method"))).output(literal("private io.intino.alexandria.bpm.Task.Result ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn new io.intino.alexandria.bpm.Task(")).output(mark("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic String execute() {\n\t\t\tbox.messageHub().sendMessage(\"")).output(mark("businessUnit")).output(literal(".\" + io.intino.alexandria.bpm.Workflow.EventType, new io.intino.alexandria.bpm.ProcessStatus(java.util.UUID.randomUUID().toString(), \"")).output(mark("taskName")).output(literal("\", \"Enter\", id(), id(), \"")).output(mark("name")).output(literal("\").message());\n\t\t\treturn new io.intino.alexandria.bpm.Task.Result(\"Subprocess called ")).output(mark("taskName")).output(literal("\");\n\t\t}\n\t};\n}")),
			rule().condition((type("service")), (trigger("method"))).output(literal("private io.intino.alexandria.bpm.Task ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn new io.intino.alexandria.bpm.Task(")).output(mark("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic io.intino.alexandria.bpm.Task.Result execute() {\n\t\t\t")).output(mark("name", "firstLowerCase")).output(literal("Accessor().trigger(\"")).output(mark("name")).output(literal("\", new io.intino.alexandria.bpm.Callback(\"")).output(mark("businessUnit")).output(literal(".TaskFinished\", id(), \"")).output(mark("name")).output(literal("\").toString(), ")).output(mark("name", "firstLowerCase")).output(literal("Input());\n\t\t\treturn new io.intino.alexandria.bpm.Task.Result(\"")).output(mark("name")).output(literal(" done!\");\n\t\t}\n\t};\n}\n\nprivate void accept")).output(mark("name", "FirstUpperCase")).output(literal("(io.intino.alexandria.message.Message m) {\n\tif (m.get(\"callbackProcess\").data().equals(id()) && m.get(\"callbackState\").data().equals(\"")).output(mark("name", "FirstUpperCase")).output(literal("\")) {\n\t\tio.intino.alexandria.bpm.Task.Result result = on")).output(mark("name", "firstUpperCase")).output(literal("Finished(m.contains(\"data\")? io.intino.alexandria.Json.fromString(m.get(\"data\").data(), ")).output(mark("name", "firstLowerCase")).output(literal("Accessor().outputOf(\"")).output(mark("process")).output(literal("\")): null);\n\t\tbox.workflow().exitState(id(), name(), m.get(\"callbackState\").data(), result);\n\t}\n}")),
			rule().condition((type("accessor")), (trigger("field"))).output(literal("protected io.intino.alexandria.bpm.WorkflowAccessor ")).output(mark("name", "FirstLowerCase")).output(literal("Accessor;")),
			rule().condition((type("accessor")), (trigger("assign"))).output(literal("this.")).output(mark("name", "FirstLowerCase")).output(literal("Accessor = ")).output(mark("name", "FirstLowerCase")).output(literal("Accessor();")),
			rule().condition((type("accessor")), (trigger("subscribe"))).output(literal("this.")).output(mark("name", "FirstLowerCase")).output(literal("Accessor.subscribe(this::accept")).output(mark("name", "FirstUpperCase")).output(literal(");")),
			rule().condition((trigger("state"))).output(literal("addState(new io.intino.alexandria.bpm.State(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "firstLowerCase")).output(literal("()")).output(expression().output(literal(", ")).output(mark("type", "logercase", "FirstUpperCase").multiple(", "))).output(literal("));")),
			rule().condition((trigger("link"))).output(literal("addLink(new io.intino.alexandria.bpm.Link(\"")).output(mark("from")).output(literal("\", \"")).output(mark("to")).output(literal("\", ")).output(mark("type", "linkType")).output(literal("));")),
			rule().condition((type("service")), (trigger("abstracttask"))).output(literal("abstract io.intino.alexandria.bpm.WorkflowAccessor ")).output(mark("name", "firstLowerCase")).output(literal("Accessor();\n\nabstract Object ")).output(mark("name", "firstLowerCase")).output(literal("Input();\n\nabstract Task.Result on")).output(mark("name", "firstUpperCase")).output(literal("Finished(Object output);")),
			rule().condition((type("service")), (trigger("task"))).output(literal("io.intino.alexandria.bpm.WorkflowAccessor ")).output(mark("name", "firstLowerCase")).output(literal("Accessor() {\n\treturn null;//TODO\n}\n\nObject ")).output(mark("name", "firstLowerCase")).output(literal("Input() {\n\treturn null;//TODO\n}\n\nTask.Result on")).output(mark("name", "firstUpperCase")).output(literal("Finished(Object output) {\n\t//TODO\n\treturn new Task.Result(\"Done\");\n}")),
			rule().condition(not(type("callActivity")), (trigger("abstracttask"))).output(literal("protected abstract io.intino.alexandria.bpm.Task ")).output(mark("name", "firstLowerCase")).output(literal("();")),
			rule().condition(not(type("callActivity")), (trigger("task"))).output(literal("protected io.intino.alexandria.bpm.Task ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn new Task(")).output(mark("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic io.intino.alexandria.bpm.Task.Result execute() {\n\t\t\treturn new io.intino.alexandria.bpm.Task.Result(\"TODO\");\n\t\t}\n\t};\n}")),
			rule().condition((attribute("", "CallActivity")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.CallActivity")),
			rule().condition((attribute("", "DefaultLine")), (trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Default")),
			rule().condition((attribute("", "Exclusive")), (trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Exclusive")),
			rule().condition((trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Inclusive")),
			rule().condition((attribute("", "User")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.Manual")),
			rule().condition((attribute("", "Manual")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.Manual")),
			rule().condition((attribute("", "Service")), (trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.CallActivity")),
			rule().condition((trigger("tasktype"))).output(literal("io.intino.alexandria.bpm.Task.Type.Automatic"))
		);
	}
}