package io.intino.konos.builder.codegeneration.services.taskinbox;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class TaskInboxServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("service"))).output(literal("package ")).output(mark("package")).output(literal(".taskinbox;\n\nimport io.intino.alexandria.message.MessageHub;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.bpm.ProcessStatus;\nimport java.io.IOException;\nimport ")).output(mark("package")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Service {\n\n\tprivate ")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\tprivate final MessageHub messageHub;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Service(MessageHub messageHub, ")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.messageHub = messageHub;\n\t\tregisterInbox();\n\t}\n\n\tprivate void registerInbox() {\n\tmessageHub.attachListener(\"Task\", \"")).output(mark("box", "lowercase-task")).output(literal("\", message -> {\n    \t\tString name = message.get(\"name\").data();\n    \t\t")).output(mark("inbox", "listener").multiple("\n")).output(literal("\n    \t});\n    \tmessageHub.attachListener(\"ProcessStatus\", message -> {\n    \t\tProcessStatus status = new ProcessStatus(message);\n    \t\tif (!status.hasCallback()) return;\n    \t\tString callback = message.get(\"callback\").data();\n    \t\tObject data = dispatcher(status.processName()).onResponse();\n    \t\tio.intino.alexandria.message.Message result = new io.intino.alexandria.message.Message(callback.substring(callback.lastIndexOf(\".\") + 1))\n    \t\t\t.set(\"callbackProcess\", status.callbackProcess())\n    \t\t\t.set(\"callbackState\", status.callbackState())\n    \t\t\t.set(\"data\", io.intino.alexandria.Json.toString(data));\n    \t\t((MessageHub) messageHub).sendMessage(callback, result); //ROOT MESSAGE\n    \t});\n\t}\n\n\t")).output(mark("inbox", "method").multiple("\n")).output(literal("\n\n\tprivate InboxDispatcher dispatcher(String name) {\n\t\t")).output(mark("inbox", "select")).output(literal("\n\t\treturn null;\n\t}\n\n\tpublic interface InboxDispatcher<Request, Response> {\n\t\tvoid onRequest(Request request);\n\t\tResponse onResponse();\n\t}\n}")),
				rule().condition((allTypes("inbox", "process")), (trigger("method"))).output(literal("private void launch")).output(mark("process", "firstUpperCase")).output(literal("(String owner, String callback, String callbackProcess, String callbackState) {\n\tmessageHub.sendMessage(\"ProcessStatus\", new ProcessStatus(java.util.UUID.randomUUID().toString(), \"")).output(mark("process")).output(literal("\", io.intino.alexandria.bpm.Process.Status.Enter, owner, callbackProcess, callbackState).message().set(\"callback\", callback));\n}")),
				rule().condition((trigger("select"))).output(literal("if (name.equalsIgnoreCase(\"")).output(mark("name")).output(literal("\")) return new ")).output(mark("name", "FirstUpperCase")).output(literal("(box);")),
				rule().condition((allTypes("inbox", "process")), (trigger("listener"))).output(literal("if (name.equalsIgnoreCase(\"")).output(mark("process")).output(literal("\")) {\n\tString callbackData = message.get(\"callback\").data();\n\tio.intino.alexandria.bpm.Callback callback = io.intino.alexandria.bpm.Callback.from(callbackData);\n\tInboxDispatcher dispatcher = dispatcher(name);\n\tif (dispatcher == null) return;\n\tdispatcher(name).onRequest(io.intino.alexandria.Json.fromString(message.get(\"data\").data(), ")).output(mark("input", "resolve")).output(literal(".class));\n\tlaunch")).output(mark("process", "firstUpperCase")).output(literal("(message.get(\"owner\").data(), callback.value(), callback.requesterId(), callback.requesterState());\n}")),
				rule().condition((type("list")), (trigger("resolve"))).output(literal("java.util.List<")).output(mark("value")).output(literal(">")),
				rule().condition((trigger("resolve"))).output(mark("value"))
		);
	}
}