package io.intino.konos.builder.actions;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class IntinoTestTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("intinotest"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport org.junit.Test;\n\npublic class ")).output(mark("name")).output(literal(" {\n\n\t@Test\n\tpublic void main() throws Exception {\n\t\tMain.main(new String[]{\n\t\t\t\t")).output(mark("service", "fill").multiple(",\n")).output(literal("\n\t\t});\n\t}\n}")),
			rule().condition((allTypes("rest","service")), (trigger("fill"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal(".port=\",\n\"")).output(mark("name", "firstLowerCase")).output(literal(".webDirectory=\"")).output(expression().output(mark("custom", "parameter").multiple(""))),
			rule().condition((allTypes("jms","service")), (trigger("fill"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal(".url=\",\n\"")).output(mark("name", "firstLowerCase")).output(literal(".user=\",\n\"")).output(mark("name", "firstLowerCase")).output(literal(".password=\"")).output(expression().output(mark("custom", "parameter").multiple(""))),
			rule().condition((allTypes("datalake","service")), (trigger("fill"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal(".workingDirectory=\",\n\"")).output(mark("name", "firstLowerCase")).output(literal(".nessieToken=\"")),
			rule().condition((allTypes("service","channel")), (trigger("fill"))).output(expression().output(mark("custom", "parameter").multiple(""))),
			rule().condition((allTypes("service","slack")), (trigger("fill"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal(".token=\"")),
			rule().condition((allTypes("ui","service")), (trigger("fill"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal(".port=\",\n\"")).output(mark("name", "firstLowerCase")).output(literal(".webDirectory=\"")).output(expression().output(mark("custom", "parameter").multiple(""))),
			rule().condition((allTypes("jmx","service")), (trigger("fill"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal(".port=\"")).output(expression().output(mark("custom", "parameter").multiple(""))),
			rule().condition((type("custom")), (trigger("parameter"))).output(literal(",")).output(literal("\n")).output(literal("\"")).output(mark("conf", "validname", "firstLowerCase")).output(literal(".")).output(mark("name", "validname", "firstLowerCase")).output(literal("=\"")),
			rule().condition((trigger("parameter"))),
			rule().condition((trigger("empty")))
		);
	}
}