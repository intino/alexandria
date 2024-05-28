package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class RouteDispatcherTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("dispatcher", "gen")).output(literal("package ")).output(placeholder("package")).output(literal(".ui.displays;\n\nimport io.intino.alexandria.ui.Soul;\n\nimport java.util.ArrayList;\nimport java.util.Collections;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.regex.Matcher;\nimport java.util.regex.Pattern;\n\npublic abstract class AbstractRouteDispatcher implements io.intino.alexandria.ui.displays.DisplayRouteDispatcher {\n\tprivate static java.util.Map<String, String> patterns = new HashMap<>();\n\n\tpublic AbstractRouteDispatcher() {\n\t\tregisterPatterns();\n\t}\n\n\t@Override\n\tpublic void dispatch(Soul soul, String address) {\n\t\taddress = address.replaceFirst(soul.session().browser().basePath(), \"\");\n\t\tList<String> params = paramsOf(address);\n\t\t")).output(expression().output(placeholder("resource", "call").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(expression().output(placeholder("resource", "declaration").multiple("\n"))).output(literal("\n\n\tprivate void registerPatterns() {\n\t\tif (patterns.size() > 0) return;\n\t\t")).output(expression().output(placeholder("resource", "patternRegister").multiple("\n"))).output(literal("\n\t}\n\n\tprivate String patternOf(String address) {\n\t\t")).output(expression().output(placeholder("resource", "pattern").multiple("\n"))).output(literal("\n\t\treturn null;\n\t}\n\n\tprivate List<String> paramsOf(String address) {\n\t\treturn paramsOf(address, patternOf(address));\n\t}\n\n\tprivate List<String> paramsOf(String address, String pattern) {\n\t\tif (pattern == null) return java.util.Collections.emptyList();\n\t\tPattern p = Pattern.compile(pattern);\n\t\tMatcher m = p.matcher(address);\n\t\tif (!m.find()) return Collections.emptyList();\n\t\tList<String> result = new ArrayList<>();\n\t\tfor (int i=1; i<=m.groupCount(); i++) result.add(m.group(i).split(\"\\\\?\")[0]);\n\t\treturn addQueryStringParams(address, result);\n\t}\n\n\tprivate List<String> addQueryStringParams(String address, List<String> result) {\n\t\tif (address.indexOf(\"?\") == -1) return result;\n\t\tString[] parameters = address.split(\"\\\\?\")[1].split(\"&\");\n\t\tfor (int i = 0; i < parameters.length; i++) {\n\t\t\tresult.add(parameters[i].split(\"=\")[1]);\n\t\t}\n\t\treturn result;\n\t}\n\n}")));
		rules.add(rule().condition(allTypes("dispatcher")).output(literal("package ")).output(placeholder("package")).output(literal(".ui.displays;\n\nimport io.intino.alexandria.ui.Soul;\n\npublic class RouteDispatcher extends AbstractRouteDispatcher {\n\t")).output(expression().output(placeholder("resource", "implementation").multiple("\n"))).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("call"))).output(literal("if (address.length() <= 1) { dispatch")).output(placeholder("name", "firstUpperCase")).output(literal("(soul")).output(expression().output(literal(", ")).output(placeholder("param", "call").multiple(", "))).output(literal("); return; }")));
		rules.add(rule().condition(all(allTypes("resource"), trigger("call"))).output(literal("if (address.matches(patterns.get(\"")).output(placeholder("name")).output(literal("\"))) { dispatch")).output(placeholder("name", "firstUpperCase")).output(literal("(soul")).output(expression().output(literal(", ")).output(placeholder("param", "call").multiple(", "))).output(literal("); return; }")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("declaration"))));
		rules.add(rule().condition(all(allTypes("resource"), trigger("declaration"))).output(literal("public abstract void dispatch")).output(placeholder("name", "firstUpperCase")).output(literal("(Soul soul")).output(expression().output(literal(", ")).output(placeholder("param").multiple(", "))).output(literal(");")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("implementation"))));
		rules.add(rule().condition(all(allTypes("resource"), trigger("implementation"))).output(literal("@Override\npublic void dispatch")).output(placeholder("name", "firstUpperCase")).output(literal("(Soul soul")).output(expression().output(literal(", ")).output(placeholder("param").multiple(", "))).output(literal(") {\n\t// TODO\n}")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("patternregister"))));
		rules.add(rule().condition(all(allTypes("resource"), trigger("patternregister"))).output(literal("patterns.put(\"")).output(placeholder("name")).output(literal("\", \"")).output(placeholder("pattern")).output(literal("\");")));
		rules.add(rule().condition(all(allTypes("resource", "main"), trigger("pattern"))).output(literal("if (address.matches(patterns.get(\"")).output(placeholder("name")).output(literal("\"))) return patterns.get(\"")).output(placeholder("name")).output(literal("\");")));
		rules.add(rule().condition(all(allTypes("resource"), trigger("pattern"))).output(literal("else if (address.matches(patterns.get(\"")).output(placeholder("name")).output(literal("\"))) return patterns.get(\"")).output(placeholder("name")).output(literal("\");")));
		rules.add(rule().condition(all(allTypes("param", "optional"), trigger("call"))).output(literal("params.size() > ")).output(placeholder("index")).output(literal(" ? params.get(")).output(placeholder("index")).output(literal(") : null")));
		rules.add(rule().condition(all(allTypes("param"), trigger("call"))).output(literal("params.get(")).output(placeholder("index")).output(literal(")")));
		rules.add(rule().condition(allTypes("param")).output(literal("String ")).output(placeholder("name")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}