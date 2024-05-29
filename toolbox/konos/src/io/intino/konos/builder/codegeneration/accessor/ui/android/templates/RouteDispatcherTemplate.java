package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class RouteDispatcherTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("dispatcher")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays\n\nimport android.content.Context\nimport android.content.Intent\nimport android.os.Bundle\n")).output(expression().output(placeholder("resource", "import").multiple("\n"))).output(literal("\nimport java.util.regex.Matcher\nimport java.util.regex.Pattern\n\nclass RouteDispatcher {\n    private var info : HashMap<String, RouteInfo> = hashMapOf()\n\n    init {\n        ")).output(expression().output(placeholder("resource", "patternRegister").multiple("\n"))).output(literal("\n    }\n\n    fun dispatch(context: Context, address: String) : Intent? {\n        val info = infoOf(address) ?: return null\n        val intent = Intent(context, info.activityClass)\n        addParameters(intent, address, info)\n        return intent\n    }\n\n    private fun infoOf(address: String): RouteInfo? {\n        ")).output(expression().output(placeholder("resource", "info").multiple("\n"))).output(literal("\n        return null\n    }\n\n    private fun matches(address: String, info: RouteInfo): Boolean {\n        if (address.matches(info.pattern)) return true\n        if (!address.endsWith(\"/\")) return false\n        return address.substring(0, address.length-1).matches(info.pattern)\n    }\n\n    private fun addParameters(intent: Intent, address: String, info: RouteInfo) {\n        val b = Bundle()\n        val parameters = paramsOf(address, info)\n        for (i in 0 until info.parameterNames.size) {\n            b.putString(info.parameterNames[i], parameters.get(i))\n        }\n        intent.putExtras(b)\n    }\n\n    private fun paramsOf(address: String, info: RouteInfo?): List<String?> {\n        if (info == null) return emptyList<String>()\n        val p: Pattern = Pattern.compile(info.pattern.pattern)\n        val m: Matcher = matcherOf(p, address) ?: return emptyList()\n        val result: MutableList<String?> = ArrayList()\n        for (i in 1..m.groupCount()) result.add(m.group(i)?.split(\"\\\\?\")!!.get(0))\n        return addQueryStringParams(address, result)\n    }\n\n    private fun matcherOf(pattern: Pattern, address: String): Matcher? {\n        var result: Matcher = pattern.matcher(address)\n        if (result.find()) return result\n        if (!address.endsWith(\"/\")) return null\n        result = pattern.matcher(address.substring(0, address.length-1))\n        return if (result.find()) result else null\n    }\n\n    private fun addQueryStringParams(address: String, result: MutableList<String?>): List<String?> {\n        if (address.indexOf(\"?\") == -1) return result\n        val parameters = address.split(\"\\\\?\".toRegex()).dropLastWhile { it.isEmpty() }\n            .toTypedArray()[1].split(\"&\".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()\n        for (i in parameters.indices) {\n            result.add(parameters[i].split(\"=\".toRegex()).dropLastWhile { it.isEmpty() }\n                .toTypedArray()[1])\n        }\n        return result\n    }\n\n    private class RouteInfo(pattern: String, var activityClass: Class<*>, var parameterNames: List<String>) {\n        var pattern : Regex = pattern.toRegex()\n    }\n\n}")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("import"))));
		rules.add(rule().condition(all(allTypes("resource"), trigger("import"))).output(literal("import ")).output(placeholder("package")).output(literal(".mobile.android.pages.")).output(placeholder("name", "firstUpperCase")).output(literal("Activity")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("patternregister"))));
		rules.add(rule().condition(all(allTypes("resource"), trigger("patternregister"))).output(literal("info.put(\"")).output(placeholder("name")).output(literal("\", RouteInfo(\"")).output(placeholder("pattern")).output(literal("\", ")).output(placeholder("name", "firstUpperCase")).output(literal("Activity::class.java, listOf(")).output(placeholder("param").multiple(",")).output(literal(")))")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("info"))).output(literal("if (matches(address, info.get(\"")).output(placeholder("name")).output(literal("\")!!)) return info.get(\"")).output(placeholder("name")).output(literal("\")")));
		rules.add(rule().condition(all(allTypes("resource"), trigger("info"))).output(literal("else if (matches(address, info.get(\"")).output(placeholder("name")).output(literal("\")!!)) return info.get(\"")).output(placeholder("name")).output(literal("\")")));
		rules.add(rule().condition(allTypes("param")).output(literal("\"")).output(placeholder("name")).output(literal("\"")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}