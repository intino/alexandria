package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import io.intino.itrules.Rule;
import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.AbstractOperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.AbstractViewTemplate;

import java.util.ArrayList;
import java.util.List;


public class AbstractCatalogTemplate extends Template {

	@Override
	protected RuleSet ruleSet() {
		return new RuleSet()
				.add(getAll(new AbstractViewTemplate().ruleSet()))
				.add(getAll(new AbstractCatalogSkeletonTemplate().ruleSet()))
				.add(getAll(new AbstractOperationTemplate().ruleSet()))
				;
	}

	private Rule[] getAll(Iterable<Rule> ruleSet) {
		List<Rule> rules = new ArrayList<>();
		ruleSet.forEach(rules::add);
		return rules.toArray(new Rule[0]);
	}
}
