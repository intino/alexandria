package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RouteDispatcherTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("dispatcher","gen"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays;\n\nimport io.intino.alexandria.ui.Soul;\n\npublic abstract class AbstractRouteDispatcher implements io.intino.alexandria.ui.displays.DisplayRouteDispatcher {\n\n    @Override\n    public void dispatch(Soul soul, String address) {\n        String subPath = subPath(address);\n        java.util.List<String> params = paramsOf(address);\n        ")).output(expression().output(mark("resource", "call").multiple("\n"))).output(literal("\n    }\n\n    ")).output(expression().output(mark("resource", "declaration").multiple("\n"))).output(literal("\n\n    private String subPath(String address) {\n        java.util.stream.Stream<String> split = java.util.stream.Stream.of(address.split(\"/\"));\n        return split.filter(s -> !s.startsWith(\":\")).collect(java.util.stream.Collectors.joining(\"/\"));\n    }\n\n    private java.util.List<String> paramsOf(String address) {\n        java.util.stream.Stream<String> split = java.util.stream.Stream.of(address.split(\"/\"));\n        return split.filter(s -> s.startsWith(\":\")).map(s -> s.substring(1)).collect(java.util.stream.Collectors.toList());\n    }\n}")),
			rule().condition((type("dispatcher"))).output(literal("package ")).output(mark("package")).output(literal(".ui.displays;\n\nimport io.intino.alexandria.ui.Soul;\n\npublic class RouteDispatcher extends AbstractRouteDispatcher {\n    ")).output(expression().output(mark("resource", "implementation").multiple("\n"))).output(literal("\n}")),
			rule().condition((allTypes("resource","main")), (trigger("call"))).output(literal("if (subPath.length() <= 0) dispatch")).output(mark("name", "firstUpperCase")).output(literal("(soul);")),
			rule().condition((type("resource")), (trigger("call"))).output(literal("else if (params.size() == ")).output(mark("paramsCount")).output(literal(" && subPath.equals(\"")).output(mark("subPath")).output(literal("\")) dispatch")).output(mark("name", "firstUpperCase")).output(literal("(soul")).output(expression().output(literal(", ")).output(mark("param", "call").multiple(", "))).output(literal(");")),
			rule().condition((allTypes("resource","main")), (trigger("declaration"))),
			rule().condition((type("resource")), (trigger("declaration"))).output(literal("public abstract void dispatch")).output(mark("name", "firstUpperCase")).output(literal("(Soul soul")).output(expression().output(literal(", ")).output(mark("param").multiple(", "))).output(literal(");")),
			rule().condition((allTypes("resource","main")), (trigger("implementation"))),
			rule().condition((type("resource")), (trigger("implementation"))).output(literal("@Override\npublic void dispatch")).output(mark("name", "firstUpperCase")).output(literal("(Soul soul")).output(expression().output(literal(", ")).output(mark("param").multiple(", "))).output(literal(") {\n    // TODO\n}")),
			rule().condition((type("param")), (trigger("call"))).output(literal("params.get(")).output(mark("index")).output(literal(")")),
			rule().condition((type("param"))).output(literal("String ")).output(mark("name"))
		);
	}
}