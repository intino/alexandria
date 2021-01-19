package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class BuilderTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("builder"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".analytic;\n\nimport io.intino.alexandria.logger.Logger;\nimport ")).output(mark("package", "validPackage")).output(literal(".cubes.*;\n\nimport java.util.function.Consumer;\nimport org.apache.commons.io.FileUtils;\nimport java.util.List;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(" {\n\tprivate final java.io.File destination;\n\tprivate final java.io.File temporalStage;\n\tprivate final io.intino.alexandria.ingestion.TransactionSession session;\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(java.io.File destination) {\n\t\tthis(destination, temporalStage, new Config());\n\t}\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(java.io.File destination, int factBufferSise) {\n\t\tthis.destination = destination;\n\t\tthis.temporalStage = new File(destination, \"session\" + java.util.UUID.randomUUID().toString());\n\t\tthis.session = new io.intino.alexandria.ingestion.SessionHandler(temporalStage).createTransactionSession(factBufferSise);\n\t}\n\n\t")).output(mark("cube", "put").multiple("\n\n")).output(literal("\n\n\tpublic void flush() {\n\t\tsession.close();\n\t}\n\n\tpublic void finish() {\n\t\tsession.close();\n\t\tseal();\n\t\tFileUtils.deleteDirectory(temporalStage);\n\t}\n\n\tprivate void seal() {\n\t\tio.intino.alexandria.sealing.TransactionSessionManager.seal(temporalStage, destination, tempFolder());\n\t}\n\n\tprivate File tempFolder() {\n\t\tFile temp = new File(this.stageFolder, \"temp\");\n\t\ttemp.mkdir();\n\t\treturn temp;\n\t}\n}")),
			rule().condition((trigger("put"))).output(literal("public void put")).output(mark("name", "FirstUpperCase")).output(literal("(String tank, Timetag timetag, java.util.function.Consumer<")).output(mark("name", "FirstUpperCase")).output(literal("> fact) {\n\tsession.put(tank, timetag, ")).output(mark("name", "FirstUpperCase")).output(literal(".class, fact);\n}"))
		);
	}
}