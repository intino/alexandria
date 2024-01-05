package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewRequesterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("package ")).output(mark("package")).output(literal(".mobile.displays.requesters\n\nimport io.intino.alexandria.mobile.Application\nimport io.intino.alexandria.mobile.UiMessage\nimport io.intino.alexandria.mobile.util.Json\nimport io.intino.alexandria.mobile.util.Uri\n\nimport ")).output(mark("package")).output(literal(".mobile.displays.")).output(expression().output(mark("packageType")).output(literal("s."))).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal("\n")).output(mark("schemaImport")).output(literal("\n\nopen class ")).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal("Requester(private var element: ")).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal(") : ")).output(mark("parentType")).output(literal("(element) {\n    ")).output(expression().output(mark("request").multiple("\n"))).output(literal("\n}")),
			rule().condition((attribute("accessible")), (trigger("proxy"))).output(literal("Proxy")),
			rule().condition((trigger("proxy"))),
			rule().condition((attribute("extensionof")), (trigger("parenttype"))).output(mark("parent", "firstUpperCase")).output(literal("Requester")),
			rule().condition((trigger("parenttype"))).output(literal("io.intino.alexandria.mobile.displays.requesters.")).output(expression().output(mark("type", "class", "FirstUpperCase"))).output(literal("Requester")),
			rule().condition((type("parameter")), (trigger("request"))).output(literal("fun ")).output(mark("name")).output(literal("(value: String) {\n    ")).output(mark("method")).output(literal("(UiMessage(\"")).output(mark("name")).output(literal("\", \"")).output(mark("display", "lowercase")).output(literal("\", element.shortId(), ")).output(mark("parameter")).output(mark("nullParameter")).output(literal(", element.owner(), element.context()), element.ownerUnit());\n}")),
			rule().condition((trigger("request"))).output(literal("fun ")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameterSignature")).output(literal(": ")).output(mark("customParameterType"))).output(literal(") {\n    ")).output(mark("method")).output(literal("(UiMessage(\"")).output(mark("name")).output(literal("\", \"")).output(mark("display", "lowercase")).output(literal("\", element.shortId(), ")).output(mark("parameter")).output(mark("nullParameter")).output(literal(", element.owner(), element.context()), element.ownerUnit());\n}")),
			rule().condition((type("object")), (trigger("parameter"))).output(literal("Uri.encode(Json.stringify(value, ")).output(mark("type")).output(literal(".serializer()))")),
			rule().condition((allTypes("list","file")), (trigger("parameter"))).output(literal("if (value != null) Uri.encode(Json.stringify(arrayListOf(\"BASE64:\" + io.intino.alexandria.mobile.util.Base64.encoder.encode(value).decodeToString()))) else null")),
			rule().condition((type("list")), (trigger("parameter"))).output(literal("Uri.encode(Json.stringify(value))")),
			rule().condition((type("file")), (trigger("parameter"))).output(literal("if (value != null) \"BASE64:\" + io.intino.alexandria.mobile.util.Base64.encoder.encode(value).decodeToString() else null")),
			rule().condition((type("boolean")), (trigger("parameter"))).output(literal("value.toString()")),
			rule().condition((type("LongInteger")), (trigger("parameter"))).output(literal("value.toString()")),
			rule().condition((type("Integer")), (trigger("parameter"))).output(literal("value.toString()")),
			rule().condition((type("Real")), (trigger("parameter"))).output(literal("value.toString()")),
			rule().condition((type("Date")), (trigger("parameter"))).output(literal("value.toEpochMilliseconds().toString()")),
			rule().condition((type("DateTime")), (trigger("parameter"))).output(literal("value.toEpochMilliseconds().toString()")),
			rule().condition((trigger("parameter"))).output(literal("Uri.encode(value)")),
			rule().condition((attribute("upload")), (trigger("method"))).output(literal("Application.fileService()!!.upload")),
			rule().condition((attribute("download")), (trigger("method"))).output(literal("Application.fileService()!!.download")),
			rule().condition((trigger("method"))).output(literal("Application.pushService(element.activity())!!.send")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".mobile.schemas.*;")),
			rule().condition((allTypes("parameterType","date"))).output(literal("kotlinx.datetime.Instant")),
			rule().condition((allTypes("parameterType","datetime"))).output(literal("kotlinx.datetime.Instant")),
			rule().condition((allTypes("parameterType","integer"))).output(literal("Int")),
			rule().condition((allTypes("parameterType","file"))).output(literal("ByteArray?")),
			rule().condition((type("parameterType"))).output(mark("value"))
		);
	}
}