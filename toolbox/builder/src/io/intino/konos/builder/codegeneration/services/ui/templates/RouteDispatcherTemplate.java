package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RouteDispatcherTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("dispatcher","gen"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays;\n\nimport io.intino.alexandria.ui.Soul;\n\nimport java.util.ArrayList;\nimport java.util.Collections;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.regex.Matcher;\nimport java.util.regex.Pattern;\n\npublic abstract class AbstractRouteDispatcher implements io.intino.alexandria.ui.displays.DisplayRouteDispatcher {\n    private static java.util.Map<String, String> patterns = new HashMap<>();\n\n    public AbstractRouteDispatcher() {\n        registerPatterns();\n    }\n\n    @Override\n    public void dispatch(Soul soul, String address) {\n        List<String> params = paramsOf(address);\n        ")).output(expression().output(mark("resource", "call").multiple("\n"))).output(literal("\n    }\n\n    ")).output(expression().output(mark("resource", "declaration").multiple("\n"))).output(literal("\n\n    private void registerPatterns() {\n        if (patterns.size() > 0) return;\n        ")).output(expression().output(mark("resource", "patternRegister").multiple("\n"))).output(literal("\n    }\n\n    private String patternOf(String address) {\n        ")).output(expression().output(mark("resource", "pattern").multiple("\n"))).output(literal("\n        return null;\n    }\n\n    private List<String> paramsOf(String address) {\n        return paramsOf(address, patternOf(address));\n    }\n\n    private List<String> paramsOf(String address, String pattern) {\n        Pattern p = Pattern.compile(pattern);\n        Matcher m = p.matcher(address);\n        if (!m.find()) return Collections.emptyList();\n        List<String> result = new ArrayList<>();\n        for (int i=1; i<=m.groupCount(); i++) result.add(m.group(i));\n        return result;\n    }\n\n}")),
			rule().condition((type("dispatcher"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays;\n\nimport io.intino.alexandria.ui.Soul;\n\npublic class RouteDispatcher extends AbstractRouteDispatcher {\n    ")).output(expression().output(mark("resource", "implementation").multiple("\n"))).output(literal("\n}")),
			rule().condition((allTypes("resource","main")), (trigger("call"))).output(literal("if (address.length() <= 1) dispatch")).output(mark("name", "firstUpperCase")).output(literal("(soul);")),
			rule().condition((type("resource")), (trigger("call"))).output(literal("else if (address.matches(patterns.get(\"")).output(mark("name")).output(literal("\"))) dispatch")).output(mark("name", "firstUpperCase")).output(literal("(soul")).output(expression().output(literal(", ")).output(mark("param", "call").multiple(", "))).output(literal(");")),
			rule().condition((allTypes("resource","main")), (trigger("declaration"))),
			rule().condition((type("resource")), (trigger("declaration"))).output(literal("public abstract void dispatch")).output(mark("name", "firstUpperCase")).output(literal("(Soul soul")).output(expression().output(literal(", ")).output(mark("param").multiple(", "))).output(literal(");")),
			rule().condition((allTypes("resource","main")), (trigger("implementation"))),
			rule().condition((type("resource")), (trigger("implementation"))).output(literal("@Override\npublic void dispatch")).output(mark("name", "firstUpperCase")).output(literal("(Soul soul")).output(expression().output(literal(", ")).output(mark("param").multiple(", "))).output(literal(") {\n    // TODO\n}")),
			rule().condition((allTypes("resource","main")), (trigger("patternregister"))),
			rule().condition((type("resource")), (trigger("patternregister"))).output(literal("patterns.put(\"")).output(mark("name")).output(literal("\", \"")).output(mark("pattern")).output(literal("\");")),
			rule().condition((allTypes("resource","main")), (trigger("pattern"))).output(literal("if (address.matches(patterns.get(\"")).output(mark("name")).output(literal("\"))) return patterns.get(\"")).output(mark("name")).output(literal("\");")),
			rule().condition((type("resource")), (trigger("pattern"))).output(literal("else if (address.matches(patterns.get(\"")).output(mark("name")).output(literal("\"))) return patterns.get(\"")).output(mark("name")).output(literal("\");")),
			rule().condition((type("param")), (trigger("call"))).output(literal("params.get(")).output(mark("index")).output(literal(")")),
			rule().condition((type("param"))).output(literal("String ")).output(mark("name"))
		);
	}
}