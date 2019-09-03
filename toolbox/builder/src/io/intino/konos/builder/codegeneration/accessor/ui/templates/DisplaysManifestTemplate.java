package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DisplaysManifestTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("manifest"))).output(mark("display", "import").multiple("\n")).output(literal("\nexport { ")).output(mark("display", "export").multiple(",")).output(literal(" }")),
			rule().condition((allTypes("item","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/items/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("item")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/items/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("row","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/rows/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("row")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/rows/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("template","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/templates/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("template")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/templates/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("block","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/blocks/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("block")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/blocks/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("component","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/components/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("component")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/components/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("display","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("display")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((type("display")), (trigger("export"))).output(mark("name", "firstUppercase")).output(mark("accessible"))
		);
	}
}