package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DisplayTemplate extends Template {

	@Override
	protected RuleSet ruleSet() {
		RuleSet result = new RuleSet();
		new ComponentTemplate().ruleSet().forEach(result::add);
		new DisplaySkeletonTemplate().ruleSet().forEach(result::add);
		return result;
	}

}