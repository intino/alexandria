package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDisplayTemplate extends Template {

	@Override
	protected RuleSet ruleSet() {
		RuleSet result = new RuleSet();
		new ComponentTemplate().ruleSet().forEach(result::add);
		new DisplayBoxTemplate().ruleSet().forEach(result::add);
		new AbstractDisplaySkeletonTemplate().ruleSet().forEach(result::add);
		return result;
	}

}