package io.intino.konos.builder.codegeneration.datalake.feeder;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractFeederTemplate extends Template {

	protected AbstractFeederTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractFeederTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "feeder"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.feeders;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ness.TanksConnectors;\nimport io.intino.konos.datalake.Feeder;\nimport io.intino.konos.datalake.Sensor;\nimport io.intino.ness.inl.Message;\n\nimport java.util.Arrays;\nimport java.util.List;\nimport java.util.Collections;\n\npublic class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder extends io.intino.konos.datalake.Feeder {\n\tprotected List<Sensor> sensors;\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder() {\n\t\tthis.sensors = Arrays.asList(")).add(mark("sensor", "new").multiple(", ")).add(literal(");\n\t}\n\n\tpublic List<String> eventTypes() {\n\t\treturn Arrays.asList(")).add(mark("eventType", "quoted").multiple(", ")).add(literal(");\n\t}\n\n\tpublic void feed(Message event) {\n\t\tTanksConnectors.byName(")).add(expression().add(literal("\"")).add(mark("domain")).add(literal("\" + "))).add(literal("event.type()).feed(event);\n\t}\n\n\tpublic List<Sensor> sensors() {\n\t\treturn sensors;\n\t}\n\n\t")).add(mark("sensor", "class").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "sensor")), (condition("trigger", "new"))).add(literal("new ")).add(mark("feeder", "FirstUpperCase")).add(literal("Feeder.")).add(mark("name", "FirstUpperCase")).add(literal("Sensor()")),
			rule().add((condition("type", "sensor")), (condition("trigger", "class"))).add(literal("static abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Sensor")).add(expression().add(literal(" extends io.intino.konos.datalake.sensors.")).add(mark("type"))).add(literal(" {\n\t")).add(mark("parent", "field")).add(literal("\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Sensor(")).add(mark("parent", "parameters")).add(literal(") {\n\t\t")).add(mark("parent", "super")).add(literal("\n\t}\n\n\t")).add(mark("parent", "methods")).add(literal("\n}")),
			rule().add((condition("type", "documentEdition")), (condition("trigger", "super"))).add(literal("super(\"")).add(mark("mode")).add(literal("\");")),
			rule().add((condition("type", "documentEdition")), (condition("trigger", "parameters"))).add(literal("String mode")),
			rule().add((condition("type", "documentSignature")), (condition("trigger", "super"))).add(literal("super(\"")).add(mark("signType")).add(literal("\", \"")).add(mark("signFormat")).add(literal("\");")),
			rule().add((condition("type", "documentSignature")), (condition("trigger", "parameters"))).add(literal("String signType, String signFormat")),
			rule().add((condition("type", "formEdition")), (condition("trigger", "super"))).add(literal("super(\"")).add(mark("path")).add(literal("\");")),
			rule().add((condition("type", "formEdition")), (condition("trigger", "parameters"))).add(literal("String path")),
			rule().add((condition("type", "poll")), (condition("trigger", "field"))).add(literal("private java.util.Map<String, java.util.function.Function<List<Object>, Object>> messageBuilders = new java.util.HashMap<>();")),
			rule().add((condition("type", "poll")), (condition("trigger", "super"))).add(literal("super(\"")).add(mark("defaultOption")).add(literal("\", Arrays.asList(")).add(mark("option").multiple(", ")).add(literal("));\nregisterMessageBuilders();")),
			rule().add((condition("type", "poll")), (condition("trigger", "methods"))).add(literal("private void registerMessageBuilders() {\n\t")).add(mark("option", "build").multiple("\n")).add(literal("\n}\n\npublic Message get(String option, Object... args) {\n\treturn io.intino.konos.alexandria.Inl.toMessage(messageBuilders.get(option).apply(Arrays.asList(args)));\n}\n\n")).add(mark("eventMethod").multiple("\n\n")),
			rule().add((condition("trigger", "build"))).add(literal("messageBuilders.put(\"")).add(mark("value")).add(literal("\", this::")).add(mark("event", "firstLowerCase")).add(literal(");")),
			rule().add((condition("trigger", "eventMethod"))).add(literal("protected abstract Object ")).add(mark("value", "firstLowerCase")).add(literal("(List<Object> objects);")),
			rule().add((condition("trigger", "option"))).add(literal("new Option(\"")).add(mark("value")).add(literal("\"")).add(expression().add(literal(", Arrays.asList(")).add(mark("option").multiple(",")).add(literal(")"))).add(literal(")")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}