package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class NessJMXOperationsTemplate extends Template {

	protected NessJMXOperationsTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new NessJMXOperationsTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "interface"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".datalake;\n\nimport io.intino.alexandria.jmx.Description;\nimport io.intino.alexandria.jmx.Parameters;\n\npublic interface NessOperationsMBean {\n\t@Description(\"Shows information about the available operations\")\n\t@Parameters({})\n\tjava.util.List<String> help();\n\n\t@Description(\"Starts reflow mode to reproduce events coming from datalake\")\n\t@Parameters({})\n\tboolean reflow();\n}")),
				rule().add((condition("type", "operations")), not(condition("type", "interface"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".datalake;\n\nimport io.intino.alexandria.inl.Message;\nimport io.intino.alexandria.jmx.JMXServer;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.ness.core.Datalake.EventStore.MessageHandler;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\n\nimport java.util.Collections;\n\nimport static io.intino.ness.core.Datalake.EventStore.Reflow.Filter;\n\npublic class NessOperations implements NessOperationsMBean {\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\tprivate final ReflowAssistant assistant;\n\tprivate int processed = 0;\n\tprivate io.intino.ness.core.Datalake.EventStore.Reflow session;\n\n\tpublic NessOperations(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.assistant = new ")).add(mark("package", "validPackage")).add(literal(".datalake.ReflowAssistant(box);\n\t}\n\n\tpublic java.util.List<String> help() {\n\t\tjava.util.List<String> operations = new java.util.ArrayList<>();\n\t\toperations.add(\"boolean reflow():Starts reflow mode to reproduce events coming from datalake\");\n\t\toperations.add(\"boolean reflow(String from):Starts reflow mode to reproduce events coming from datalake since the instant parameter\");\n\t\toperations.add(\"boolean customReflow(String reflowConfiguration):Starts reflow mode to reproduce events coming from datalake since the instant parameter\");\n\t\treturn operations;\n\t}\n\n\tpublic boolean reflow() {\n\t\treturn reflow(assistant.filter());\n\t}\n\n\tprivate boolean reflow(Filter filter) {\n\t\tLogger.info(\"Starting Reflow...\");\n\t\tassistant.onStart();\n\t\t")).add(mark("package", "validPackage")).add(literal(".datalake.Datalake.unsubscribeAll(box.nessAccessor());\n\t\tthis.session = box.nessAccessor().eventStore().reflow(filter);\n\t\tthis.session.next(assistant.defaultBlockSize(), messageHandler());\n\t\treturn true;\n\t}\n\n\tprivate MessageHandler messageHandler() {\n\t\treturn new ReflowHandler();\n\t}\n\n\tpublic static JMXServer init(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tJMXServer server = new JMXServer(Collections.singletonMap(\"")).add(mark("package", "validPackage")).add(literal(".datalake.NessOperations\", new Object[]{box}));\n\t\tserver.init();\n\t\treturn server;\n\t}\n\n\tprivate class ReflowHandler implements MessageHandler, io.intino.ness.core.Datalake.EventStore.ReflowHandler {\n\n\t\t@Override\n\t\tpublic void handle(Message message) {\n\t\t\tDatalake.handlers().get(message.type().toLowerCase()).handle(message);\n\t\t}\n\n\t\t@Override\n\t\tpublic void onBlock(int processedMessages) {\n\t\t\tassistant.onBlock();\n\t\t\tLogger.info(\"Block processed - \" + processedMessages + \" messages processed\");\n\t\t\tsession.next(assistant.defaultBlockSize(), this);\n\t\t}\n\n\t\t@Override\n\t\tpublic void onFinish(int processedMessages) {\n\t\t\t//session.finish();\n\t\t\tassistant.onFinish();\n\t\t\tDatalake.registerTanks(box);\n\t\t\tLogger.info(\"Reflow finished - \" + processedMessages + \" messages processed\");\n\t\t}\n\t}\n}"))
		);
		return this;
	}
}