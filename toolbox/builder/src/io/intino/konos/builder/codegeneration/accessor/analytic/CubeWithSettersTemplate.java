package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.facts.ColumnsTemplate;

public class CubeWithSettersTemplate extends Template {

    @Override
    protected RuleSet ruleSet() {
        RuleSet rules = new CubeTemplate().ruleSet();
        RuleSet columnRules = new ColumnsTemplate().ruleSet();
        columnRules.forEach(rules::add);
        return rules;
    }
}
