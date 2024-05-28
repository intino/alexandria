package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewRequesterTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.displays.requesters\n\nimport io.intino.alexandria.mobile.Application\nimport io.intino.alexandria.mobile.UiMessage\nimport io.intino.alexandria.mobile.util.Json\nimport io.intino.alexandria.mobile.util.Uri\n\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.")).output(expression().output(placeholder("packageType")).output(literal("s."))).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal("\n")).output(placeholder("schemaImport")).output(literal("\n\nopen class ")).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal("Requester(private var element: ")).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal(") : ")).output(placeholder("parentType")).output(literal("(element) {\n    ")).output(expression().output(placeholder("request").multiple("\n"))).output(literal("\n}")));
		rules.add(rule().condition(all(attribute("accessible"), trigger("proxy"))).output(literal("Proxy")));
		rules.add(rule().condition(trigger("proxy")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("parenttype"))).output(placeholder("parent", "firstUpperCase")).output(literal("Requester")));
		rules.add(rule().condition(trigger("parenttype")).output(literal("io.intino.alexandria.mobile.displays.requesters.")).output(expression().output(placeholder("type", "class", "FirstUpperCase"))).output(literal("Requester")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("request"))).output(literal("fun ")).output(placeholder("name")).output(literal("(value: String) {\n    ")).output(placeholder("method")).output(literal("(UiMessage(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("display", "lowercase")).output(literal("\", element.shortId(), ")).output(placeholder("parameter")).output(placeholder("nullParameter")).output(literal(", element.owner(), element.context()), element.ownerUnit());\n}")));
		rules.add(rule().condition(trigger("request")).output(literal("fun ")).output(placeholder("name")).output(literal("(")).output(expression().output(placeholder("parameterSignature")).output(literal(": ")).output(placeholder("customParameterType"))).output(literal(") {\n    ")).output(placeholder("method")).output(literal("(UiMessage(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("display", "lowercase")).output(literal("\", element.shortId(), ")).output(placeholder("parameter")).output(placeholder("nullParameter")).output(literal(", element.owner(), element.context()), element.ownerUnit());\n}")));
		rules.add(rule().condition(all(allTypes("object"), trigger("parameter"))).output(literal("Uri.encode(Json.stringify(value, ")).output(placeholder("type")).output(literal(".serializer()))")));
		rules.add(rule().condition(all(allTypes("list", "file"), trigger("parameter"))).output(literal("if (value != null) Uri.encode(Json.stringify(arrayListOf(\"BASE64:\" + io.intino.alexandria.mobile.util.Base64.encoder.encode(value).decodeToString()))) else null")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(literal("Uri.encode(Json.stringify(value))")));
		rules.add(rule().condition(all(allTypes("file"), trigger("parameter"))).output(literal("if (value != null) \"BASE64:\" + io.intino.alexandria.mobile.util.Base64.encoder.encode(value).decodeToString() else null")));
		rules.add(rule().condition(all(allTypes("boolean"), trigger("parameter"))).output(literal("value.toString()")));
		rules.add(rule().condition(all(allTypes("LongInteger"), trigger("parameter"))).output(literal("value.toString()")));
		rules.add(rule().condition(all(allTypes("Integer"), trigger("parameter"))).output(literal("value.toString()")));
		rules.add(rule().condition(all(allTypes("Real"), trigger("parameter"))).output(literal("value.toString()")));
		rules.add(rule().condition(all(allTypes("Date"), trigger("parameter"))).output(literal("value.toEpochMilliseconds().toString()")));
		rules.add(rule().condition(all(allTypes("DateTime"), trigger("parameter"))).output(literal("value.toEpochMilliseconds().toString()")));
		rules.add(rule().condition(trigger("parameter")).output(literal("Uri.encode(value)")));
		rules.add(rule().condition(all(attribute("upload"), trigger("method"))).output(literal("Application.fileService()!!.upload")));
		rules.add(rule().condition(all(attribute("download"), trigger("method"))).output(literal("Application.fileService()!!.download")));
		rules.add(rule().condition(trigger("method")).output(literal("Application.pushService(element.activity())!!.send")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".mobile.schemas.*;")));
		rules.add(rule().condition(allTypes("parameterType", "date")).output(literal("kotlinx.datetime.Instant")));
		rules.add(rule().condition(allTypes("parameterType", "datetime")).output(literal("kotlinx.datetime.Instant")));
		rules.add(rule().condition(allTypes("parameterType", "integer")).output(literal("Int")));
		rules.add(rule().condition(allTypes("parameterType", "file")).output(literal("ByteArray?")));
		rules.add(rule().condition(allTypes("parameterType")).output(placeholder("value")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}