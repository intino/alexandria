package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

public interface Expression {
	Object evaluate(EvaluationContext context) throws Exception;
}
