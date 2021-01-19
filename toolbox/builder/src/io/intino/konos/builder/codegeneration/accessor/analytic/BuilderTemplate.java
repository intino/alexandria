package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class BuilderTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("builder"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".analytic;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".analytic.cubes.*;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.Timetag;\n\nimport java.util.function.Consumer;\nimport org.apache.commons.io.FileUtils;\nimport java.io.File;\nimport java.util.List;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(" {\n\tprivate final java.io.File destination;\n\tprivate final java.io.File stage;\n\tprivate final io.intino.alexandria.ingestion.TransactionSession session;\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(java.io.File destination) {\n\t\tthis(destination, 1_000_000);\n\t}\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(java.io.File destination, int factBufferSise) {\n\t\tthis.destination = destination;\n\t\tthis.stage = new java.io.File(destination, \"session\" + java.util.UUID.randomUUID().toString());\n\t\tthis.session = new io.intino.alexandria.ingestion.SessionHandler(stage).createTransactionSession(factBufferSise);\n\t}\n\n\t")).output(mark("cube", "put").multiple("\n\n")).output(literal("\n\n\tpublic void flush() {\n\t\tsession.close();\n\t}\n\n\tpublic void finish() {\n\t\tsession.close();\n\t\tseal();\n\t\ttry {\n\t\t\tFileUtils.deleteDirectory(stage);\n\t\t} catch (java.io.IOException e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n\n\tprivate void seal() {\n\t\tio.intino.alexandria.sealing.TransactionSessionManager.seal(stage, destination, tempFolder());\n\t}\n\n\tprivate File tempFolder() {\n\t\tFile temp = new File(this.stage, \"temp\");\n\t\ttemp.mkdir();\n\t\treturn temp;\n\t}\n}")),
			rule().condition((trigger("put"))).output(literal("public void put")).output(mark("name", "FirstUpperCase")).output(literal("(String tank, Timetag timetag, java.util.function.Consumer<")).output(mark("name", "FirstUpperCase")).output(literal("> fact) {\n\tsession.put(tank, timetag, ")).output(mark("name", "FirstUpperCase")).output(literal(".class, fact);\n}"))
		);
	}
}