package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import io.intino.itrules.Rule;
import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.services.ui.display.toolbar.OperationTemplate;
import io.intino.konos.builder.codegeneration.services.ui.display.view.ViewTemplate;

import java.util.ArrayList;
import java.util.List;

public class PanelTemplate extends Template {

	@Override
	protected RuleSet ruleSet() {
		return new RuleSet().add(
				getAll(new PanelSkeletonTemplate().ruleSet()))
				.add(getAll(new OperationTemplate().ruleSet()))
				.add(getAll(new ViewTemplate().ruleSet()));
	}

	private Rule[] getAll(Iterable<Rule> ruleSet) {
		List<Rule> rules = new ArrayList<>();
		ruleSet.forEach(rules::add);
		return rules.toArray(new Rule[0]);
	}
}