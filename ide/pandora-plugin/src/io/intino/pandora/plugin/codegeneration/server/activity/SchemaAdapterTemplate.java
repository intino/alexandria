package io.intino.pandora.plugin.codegeneration.server.activity;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class SchemaAdapterTemplate extends Template {

	protected SchemaAdapterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SchemaAdapterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "adapters"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".schemas;\n\npublic class ActivitySchemaAdapters {\n\n\t")).add(mark("schema").multiple("\n\n")).add(literal("\n\n}\n\n")),
			rule().add((condition("type", "schema"))).add(literal("\npublic static ")).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("FromLayer(tara.magritte.Layer layer) {\n\treturn ")).add(mark("name", "firstLowerCase")).add(literal("FromNode(layer.node());\n}\n\nprivate static ")).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("FromNode(tara.magritte.Node node) {\n\t")).add(mark("name", "firstUpperCase")).add(literal(" schema = new ")).add(mark("name", "firstUpperCase")).add(literal("();\n\tfinal java.util.Map<String, java.util.List<?>> variables = node.variables();\n\t")).add(expression().add(literal("for (String variable : variables.keySet()) {")).add(literal("\n")).add(literal("\t\t")).add(mark("attribute", "fromLayer").multiple("\n")).add(literal("\n")).add(literal("\t}"))).add(literal("\n\t")).add(expression().add(literal("for (tara.magritte.Node component : node.componentList()) {")).add(literal("\n")).add(literal("\t\t")).add(mark("attribute", "componentfromLayer").multiple("\n")).add(literal("\n")).add(literal("\t}"))).add(literal("\n\treturn schema;\n}")),
			rule().add((condition("type", "single & string")), not(condition("type", "member")), (condition("trigger", "fromLayer"))).add(literal("if (variable.equals(\"")).add(mark("name", "firstLowerCase")).add(literal("\")) schema.")).add(mark("name", "firstLowerCase")).add(literal("((")).add(mark("type", "FirstUpperCase", "typeFormat")).add(literal(") variables.get(variable).get(0).toString());")),
			rule().add((condition("type", "single")), not(condition("type", "member")), (condition("trigger", "fromLayer"))).add(literal("if (variable.equals(\"")).add(mark("name", "firstLowerCase")).add(literal("\")) schema.")).add(mark("name", "firstLowerCase")).add(literal("((")).add(mark("type", "FirstUpperCase", "typeFormat")).add(literal(") variables.get(variable).get(0));")),
			rule().add((condition("type", "multiple & string")), not(condition("type", "member")), (condition("trigger", "fromLayer"))).add(literal("if (variable.equals(\"")).add(mark("name", "FirstLowerCase")).add(literal("\")) schema.")).add(mark("name", "firstLowerCase")).add(literal("().add(((")).add(mark("type", "FirstUpperCase", "typeFormat")).add(literal(") variables.get(variable).get(0).toString()));")),
			rule().add((condition("type", "multiple")), not(condition("type", "member")), (condition("trigger", "fromLayer"))).add(literal("if (variable.equals(\"")).add(mark("name", "FirstLowerCase")).add(literal("\")) schema.")).add(mark("name", "firstLowerCase")).add(literal("().add(((")).add(mark("type", "FirstUpperCase", "typeFormat")).add(literal(") variables.get(variable).get(0)));")),
			rule().add((condition("type", "single & member")), (condition("trigger", "componentfromLayer"))).add(literal("if (component.name().equalsIgnoreCase(")).add(mark("type", "FirstUpperCase")).add(literal(".class.getSimpleName())) schema.")).add(mark("name", "firstLowerCase")).add(literal("((")).add(mark("type", "FirstLowerCase")).add(literal("FromNode(component)));")),
			rule().add((condition("type", "multiple & member")), (condition("trigger", "componentfromLayer"))).add(literal("if (component.name().equalsIgnoreCase(")).add(mark("type", "FirstUpperCase")).add(literal(".class.getSimpleName())) schema.")).add(mark("name", "firstLowerCase")).add(literal("List().add(")).add(mark("type", "FirstLowerCase")).add(literal("FromNode(component));"))
		);
		return this;
	}
}