package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RouteDispatcherTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("dispatcher"))).output(literal("package ")).output(mark("package")).output(literal(".mobile.android.displays\n\nimport android.content.Context\nimport android.content.Intent\nimport android.os.Bundle\nimport ")).output(mark("package")).output(literal(".mobile.android.pages.HomeActivity\nimport ")).output(mark("package")).output(literal(".mobile.android.pages.MicroSiteActivity\nimport ")).output(mark("package")).output(literal(".mobile.android.pages.WidgetTypeActivity\nimport ")).output(mark("package")).output(literal(".mobile.android.pages.WidgetsActivity\nimport java.util.regex.Matcher\nimport java.util.regex.Pattern\n\nclass RouteDispatcher {\n    private var info : HashMap<String, RouteInfo> = hashMapOf()\n\n    init {\n        ")).output(expression().output(mark("resource", "patternRegister").multiple("\n"))).output(literal("\n    }\n\n    fun dispatch(context: Context, address: String) : Intent? {\n        val info = infoOf(address) ?: return null\n        val intent = Intent(context, info.activityClass)\n        addParameters(intent, address, info)\n        return intent\n    }\n\n    private fun infoOf(address: String): RouteInfo? {\n        ")).output(expression().output(mark("resource", "info").multiple("\n"))).output(literal("\n        return null\n    }\n\n    private fun matches(address: String, info: RouteInfo): Boolean {\n        if (address.matches(info.pattern)) return true\n        if (!address.endsWith(\"/\")) return false\n        return address.substring(0, address.length-1).matches(info.pattern)\n    }\n\n    private fun addParameters(intent: Intent, address: String, info: RouteInfo) {\n        val b = Bundle()\n        val parameters = paramsOf(address, info)\n        for (i in 0 until info.parameterNames.size) {\n            b.putString(info.parameterNames[i], parameters.get(i))\n        }\n        intent.putExtras(b)\n    }\n\n    private fun paramsOf(address: String, info: RouteInfo?): List<String?> {\n        if (info == null) return emptyList<String>()\n        val p: Pattern = Pattern.compile(info.pattern.pattern)\n        val m: Matcher = matcherOf(p, address) ?: return emptyList()\n        val result: MutableList<String?> = ArrayList()\n        for (i in 1..m.groupCount()) result.add(m.group(i)?.split(\"\\\\?\")!!.get(0))\n        return addQueryStringParams(address, result)\n    }\n\n    private fun matcherOf(pattern: Pattern, address: String): Matcher? {\n        var result: Matcher = pattern.matcher(address)\n        if (result.find()) return result\n        if (!address.endsWith(\"/\")) return null\n        result = pattern.matcher(address.substring(0, address.length-1))\n        return if (result.find()) result else null\n    }\n\n    private fun addQueryStringParams(address: String, result: MutableList<String?>): List<String?> {\n        if (address.indexOf(\"?\") == -1) return result\n        val parameters = address.split(\"\\\\?\".toRegex()).dropLastWhile { it.isEmpty() }\n            .toTypedArray()[1].split(\"&\".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()\n        for (i in parameters.indices) {\n            result.add(parameters[i].split(\"=\".toRegex()).dropLastWhile { it.isEmpty() }\n                .toTypedArray()[1])\n        }\n        return result\n    }\n\n    private class RouteInfo(pattern: String, var activityClass: Class<*>, var parameterNames: List<String>) {\n        var pattern : Regex = pattern.toRegex()\n    }\n\n}")),
			rule().condition((allTypes("resource","main")), (trigger("patternregister"))),
			rule().condition((type("resource")), (trigger("patternregister"))).output(literal("info.put(\"")).output(mark("name")).output(literal("\", RouteInfo(\"")).output(mark("pattern")).output(literal("\", ")).output(mark("name", "firstUpperCase")).output(literal("Activity::class.java, listOf(")).output(mark("param").multiple(",")).output(literal(")))")),
			rule().condition((allTypes("resource","main")), (trigger("info"))).output(literal("if (matches(address, info.get(\"")).output(mark("name")).output(literal("\")!!)) return info.get(\"")).output(mark("name")).output(literal("\")")),
			rule().condition((type("resource")), (trigger("info"))).output(literal("else if (matches(address, info.get(\"")).output(mark("name")).output(literal("\")!!)) return info.get(\"")).output(mark("name")).output(literal("\")")),
			rule().condition((type("param"))).output(literal("\"")).output(mark("name")).output(literal("\""))
		);
	}
}