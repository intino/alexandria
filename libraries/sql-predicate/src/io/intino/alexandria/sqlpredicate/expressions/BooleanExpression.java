package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

public interface BooleanExpression extends Expression {
	boolean matches(EvaluationContext context) throws Exception;
}