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
			rule().condition((allTypes("table","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/tables/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("table")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/tables/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("list","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/lists/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("list")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/lists/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("magazine","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/magazines/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("magazine")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/magazines/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("template","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/templates/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((allTypes("map","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/maps/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("map")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/maps/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((type("template")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../gen/displays/templates/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("block","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/blocks/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("block")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/blocks/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("component","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/components/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("component")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/components/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((allTypes("display","accessible")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal("Proxy from \"./displays/")).output(mark("name", "firstUppercase")).output(literal("Proxy\"")),
			rule().condition((type("display")), (trigger("import"))).output(literal("import ")).output(mark("name", "firstUppercase")).output(literal(" from \"../")).output(mark("directory")).output(literal("/displays/")).output(mark("name", "firstUppercase")).output(literal("\"")),
			rule().condition((type("display")), (trigger("export"))).output(mark("name", "firstUppercase")).output(mark("accessible")),
			rule().condition((type("accessible"))).output(literal("Proxy"))
		);
	}
}