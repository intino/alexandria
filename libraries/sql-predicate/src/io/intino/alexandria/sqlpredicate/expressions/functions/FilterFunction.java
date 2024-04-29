package io.intino.alexandria.sqlpredicate.expressions.functions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.FunctionCallExpression;

public interface FilterFunction {
	boolean isValid(FunctionCallExpression expr);

	boolean returnsBoolean(FunctionCallExpression expr);

	Object evaluate(FunctionCallExpression expr, EvaluationContext context) throws Exception;
}

