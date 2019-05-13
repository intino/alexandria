package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractDisplayTemplate extends Template {

	@Override
	protected RuleSet ruleSet() {
		RuleSet result = new RuleSet();
		new AbstractDisplaySkeletonTemplate().ruleSet().forEach(result::add);
		new ComponentTemplate().ruleSet().forEach(result::add);
		return result;
	}

}