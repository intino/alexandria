package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.util.List;

public class BooleanFunctionCallExpr extends FunctionCallExpression implements BooleanExpression {

    public BooleanFunctionCallExpr(String function, List<Expression> args) throws InvalidFunctionExpressionException {
        super(function, args);
	}

	public boolean matches(EvaluationContext context) throws Exception {
		Boolean result = (Boolean) evaluate(context);
		return result != null && result;

	}
}

