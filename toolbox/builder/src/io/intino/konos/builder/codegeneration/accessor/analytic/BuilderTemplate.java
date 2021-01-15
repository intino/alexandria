package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class BuilderTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("terminal"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.Scale;\nimport io.intino.alexandria.event.Event;\nimport io.intino.alexandria.logger.Logger;\nimport ")).output(mark("package", "validPackage")).output(literal(".events.*;\n\nimport java.util.function.Consumer;\nimport java.util.List;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(" {\n\tprivate final java.io.File dataHubStage;\n\tprivate final java.io.File temporalStage;\n\tprivate final io.intino.alexandria.ingestion.TransactionSession session;\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(java.io.File destination, java.io.File temporalStage) {\n\t\tthis(destination, temporalStage, new Config());\n\t}\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(java.io.File destination, java.io.File temporalStage, Config config) {\n\t\tthis.destination = destination;\n\t\tthis.temporalStage = temporalStage;\n\t\tthis.sessionHandler = new io.intino.alexandria.ingestion.SessionHandler(temporalStage);\n\t\tthis.eventSession = sessionHandler.createEventSession();\n\t\tthis.setSession = sessionHandler.createSetSession(config.setsBufferSise);\n\t\tthis.transactionSession = sessionHandler.createTransactionSession(config.transactionsBufferSise);\n\t}\n\n\t")).output(mark("cube", "put").multiple("\n\n")).output(literal("\n\n\tpublic void flush() {\n\t\tsession.close();\n\t}\n\n\tpublic void finish() {\n\t\tsession.close();\n\t\tseal();\n\t}\n\n\tprivate void seal() {\n\n\t}\n\n\tpublic static class Config {\n\t\tprivate int transactionsBufferSise = 1_000_000;\n\t\tprivate int setsBufferSise = 1_000_000;\n\n\t\tpublic Config transactionsBufferSise(int transactionsBufferSise) {\n\t\t\tthis.transactionsBufferSise = transactionsBufferSise;\n\t\t\treturn this;\n\t\t}\n\n\t\tpublic Config setsBufferSise(int setsBufferSise) {\n\t\t\tthis.setsBufferSise = setsBufferSise;\n\t\t\treturn this;\n\t\t}\n\t}\n}\n\n\n\tpublic interface SessionEventConsumer extends java.util.function.Consumer<io.intino.alexandria.event.SessionEvent> {\n\t}\n\n\t")).output(mark("event", "interface").multiple("\n\n")).output(literal("\n}")),
			rule().condition((trigger("put"))).output(literal("public void feed")).output(mark("namespace", "FirstUpperCase")).output(mark("name", "FirstUpperCase")).output(literal("(String tank, Timetag timetag, java.util.function.Consumer<")).output(mark("qn")).output(literal("> fact) {\n\tsession.put(tank, timetag, ")).output(mark("qn")).output(literal(".class, fact);\n}"))
		);
	}
}