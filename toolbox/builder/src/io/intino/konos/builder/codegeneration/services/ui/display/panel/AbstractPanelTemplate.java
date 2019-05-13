package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.itrules.Rule;
import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.AbstractOperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.AbstractViewTemplate;

import java.util.ArrayList;
import java.util.List;

public class AbstractPanelTemplate extends Template {

	@Override
	protected RuleSet ruleSet() {
		return new RuleSet().add(
				getAll(new AbstractPanelSkeletonTemplate().ruleSet()))
				.add(getAll(new AbstractOperationTemplate().ruleSet()))
				.add(getAll(new AbstractViewTemplate().ruleSet()));
	}

	private Rule[] getAll(Iterable<Rule> ruleSet) {
		List<Rule> rules = new ArrayList<>();
		ruleSet.forEach(rules::add);
		return rules.toArray(new Rule[0]);
	}
}