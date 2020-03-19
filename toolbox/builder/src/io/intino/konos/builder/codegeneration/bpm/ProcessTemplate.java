package io.intino.konos.builder.codegeneration.bpm;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ProcessTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("process", "src"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport io.intino.alexandria.bpm.Task;\nimport io.intino.alexandria.bpm.Task.Type.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "firstUpperCase")).output(literal(" {\n\n\t")).output(mark("parameter", "fieldName").multiple("\n")).output(literal("\n\n\t")).output(mark("name", "firstUpperCase")).output(literal("(String id, ")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(id, box);\n\t}\n\n\t")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box")).output(expression().output(literal(", ")).output(mark("parameter", "signature").multiple(", "))).output(literal(") {\n\t\tsuper(java.util.UUID.randomUUID().toString(), box);\n\t\t")).output(expression().output(mark("parameter", "put").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(mark("state", "execute").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(mark("state", "accept").multiple("\n\n"))).output(literal("\n}")),
				rule().condition((type("process"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".bpm;\n\nimport static io.intino.alexandria.bpm.State.Type.*;\nimport io.intino.alexandria.bpm.Task;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\n\nimport java.util.List;\n\nimport static io.intino.alexandria.bpm.Link.Type.*;\n\npublic abstract class Abstract")).output(mark("name", "firstUpperCase")).output(literal(" extends io.intino.alexandria.bpm.Process {\n\n\tprotected ")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\n\tAbstract")).output(mark("name", "firstUpperCase")).output(literal("(String id, ")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(id);\n\t\tthis.box = box;\n\t\tinit();\n\t}\n\n\tprivate void init() {\n\t\t")).output(expression().output(mark("state").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("link").multiple("\n"))).output(literal("\n\t}\n\n\t@Override\n\tpublic String name() {\n\t\treturn \"")).output(mark("name", "firstUpperCase")).output(literal("\";\n\t}\n\n\t")).output(mark("state", "abstractExecute").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(mark("state", "abstractaccept").multiple("\n\n"))).output(literal("\n\n\t")).output(mark("state", "method").multiple("\n\n")).output(literal("\n}")),
				rule().condition((trigger("method"))).output(literal("private Task execute")).output(mark("method", "firstUpperCase")).output(literal("() {\n\treturn new Task(Task.Type.")).output(mark("taskType")).output(literal(") {\n\t\t@Override\n\t\tpublic void execute() {\n\t\t\t")).output(mark("method", "FirstLowerCase")).output(literal("();\n\t\t}\n\n\t\t@Override\n\t\tpublic boolean accept() {\n\t\t\treturn accept")).output(mark("method", "FirstUpperCase")).output(literal("();\n\t\t}\n\t};\n}")),
				rule().condition((trigger("abstractexecute"))).output(literal("protected abstract void ")).output(mark("method", "FirstLowerCase")).output(literal("();")),
				rule().condition((trigger("execute"))).output(literal("protected void ")).output(mark("method", "FirstLowerCase")).output(literal("() {\n\n}")),
				rule().condition((trigger("abstractaccept"))).output(literal("protected boolean accept")).output(mark("method", "FirstUpperCase")).output(literal("() {\n\treturn true;\n}")),
				rule().condition((type("conditional")), (trigger("accept"))).output(literal("protected boolean accept")).output(mark("method", "FirstUpperCase")).output(literal("() {\n\treturn true;\n}")),
				rule().condition((trigger("state"))).output(literal("addState(new io.intino.alexandria.bpm.State(\"")).output(mark("label")).output(literal("\", execute")).output(mark("method", "firstUpperCase")).output(literal("()")).output(expression().output(literal(", ")).output(mark("type", "lowercase", "FirstUpperCase").multiple(", "))).output(literal("));")),
			rule().condition((trigger("link"))).output(literal("addLink(new io.intino.alexandria.bpm.Link(\"")).output(mark("from")).output(literal("\", \"")).output(mark("to")).output(literal("\", ")).output(mark("type", "linkType")).output(literal("));")),
				rule().condition((attribute("", "Line")), (trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Default")),
			rule().condition((attribute("", "Exclusive")), (trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Exclusive")),
				rule().condition((attribute("", "Inclusive")), (trigger("linktype"))).output(literal("io.intino.alexandria.bpm.Link.Type.Inclusive")),
				rule().condition((trigger("put"))).output(literal("put(")).output(mark("", "firstUpperCase")).output(literal(", ")).output(mark("", "firstLowerCase")).output(literal(");")),
				rule().condition((trigger("signature"))).output(literal("String ")).output(mark("", "firstLowerCase")),
				rule().condition((trigger("fieldname"))).output(literal("private static final String ")).output(mark("", "FirstUpperCase")).output(literal(" = \"")).output(mark("")).output(literal("\";"))
		);
	}
}