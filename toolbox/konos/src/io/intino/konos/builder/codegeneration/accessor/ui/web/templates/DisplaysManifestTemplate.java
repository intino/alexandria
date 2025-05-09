package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class DisplaysManifestTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("manifest")).output(placeholder("display", "import").multiple("\n")).output(literal("\nexport { ")).output(placeholder("display", "export").multiple(",")).output(literal(" }")));
		rules.add(rule().condition(all(allTypes("item", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/items/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("item"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/items/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("row", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/rows/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("row"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/rows/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("table", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/tables/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("table"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/tables/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("list", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/lists/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("list"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/lists/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("magazine", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/magazines/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("magazine"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/magazines/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("template", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/templates/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("map", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/maps/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("map"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/maps/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("template"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../gen/displays/templates/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("block", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/blocks/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("block"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/blocks/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("component", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/components/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("component"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/components/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("display", "exposed"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal("Proxy from \"./displays/")).output(placeholder("name", "firstUppercase")).output(literal("Proxy\"")));
		rules.add(rule().condition(all(allTypes("display"), trigger("import"))).output(literal("import ")).output(placeholder("name", "firstUppercase")).output(literal(" from \"../")).output(placeholder("directory")).output(literal("/displays/")).output(placeholder("name", "firstUppercase")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("display"), trigger("export"))).output(placeholder("name", "firstUppercase")).output(placeholder("exposed")));
		rules.add(rule().condition(allTypes("exposed")).output(literal("Proxy")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}