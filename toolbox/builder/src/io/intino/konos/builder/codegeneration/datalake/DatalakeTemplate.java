package io.intino.konos.builder.codegeneration.datalake;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DatalakeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("tanks"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".datalake;\n\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.ness.datalake.file.FileDatalake;\nimport io.intino.ness.datalake.file.eventsourcing.JmsConnection;\nimport io.intino.ness.datalake.file.eventsourcing.JmsEventSubscriber;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\nimport java.io.File;\nimport java.util.Arrays;\nimport java.util.List;\n\npublic class Datalake {\n\tpublic static List<String> splits = Arrays.asList(")).output(mark("split", "quoted").multiple(", ")).output(literal(");\n\tprivate final FileDatalake datalake;\n\tprivate final ")).output(mark("box", "firstUppercase")).output(literal("Box box;\n\tprivate final JmsConnection connection;\n\tprivate JmsEventSubscriber subscriber;\n\n\tpublic Datalake(Box box) {\n\t\tthis.box = (")).output(mark("box", "firstUppercase")).output(literal("Box) box;\n\t\tdatalake = new FileDatalake(new File(box.configuration().get(box.configuration().get(\"datalake\"))));\n\t\t")).output(mark("remote")).output(literal("\n\t\t//if mirror\n\t\t//new Mirror(datalake).clone(2, Months);\n\t}\n\n\tpublic io.intino.ness.datalake.Datalake.EventStore eventStore() {\n\t\treturn datalake.eventStore();\n\t}\n\n\tpublic io.intino.ness.datalake.Datalake.SetStore setStore() {\n\t\treturn datalake.setStore();\n\t}\n\n\tpublic void connect(String args) {\n\t\tconnection.connect(args);\n\t\t")).output(expression().output(literal("subscriber = new JmsEventSubscriber(connection);")).output(literal("\n")).output(literal("")).output(mark("tank", "subscribe").multiple("\n")).output(literal("\n")).output(literal(""))).output(literal("\n\t}\n\n\tpublic void disconnect() {\n\n\t}\n\n\t")).output(mark("tank", "getter")).output(literal("\n\n}")),
			rule().condition((trigger("remote"))).output(literal("connection = new JmsConnection(")).output(mark("url", "parameter")).output(literal(", ")).output(mark("user", "parameter")).output(literal(", ")).output(mark("password", "parameter")).output(literal(", ")).output(mark("clientId", "parameter")).output(literal(", null, null);")),
			rule().condition((type("custom")), (trigger("parameter"))).output(literal("box.configuration().get(\"")).output(mark("value", "customParameter", "lowercase")).output(literal("\")")),
			rule().condition((trigger("parameter"))).output(mark("value", "quoted")),
			rule().condition((allTypes("tank","event")), (trigger("subscribe"))).output(literal("subscriber.subscribe(datalake.eventStore().tank(\"")).output(mark("fullName")).output(literal("\")).using(new ")).output(mark("name", "firstUpperCase")).output(literal("Mounter(box));")),
			rule().condition((type("tank")), (trigger("getter"))).output(literal("public static TankAccessor ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn this.datalake.eventStore().tank(\"")).output(mark("fullName")).output(literal("\");\n}"))
		);
	}
}