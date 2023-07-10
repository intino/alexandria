package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.util.List;

/**
 * Function call expression that evaluates to a boolean value.  Selector parsing requires BooleanExpression objects for
 * Boolean expressions, such as operands to AND, and as the final result of a selector.  This provides that interface
 * for function call expressions that resolve to Boolean values.
 * <p/>
 * If a function can return different types at evaluation-time, the function implementation needs to decide whether it
 * supports casting to Boolean at parse-time.
 *
 * @see FunctionCallExpression#createFunctionCall
 */

public class BooleanFunctionCallExpr extends FunctionCallExpression implements BooleanExpression {

	public BooleanFunctionCallExpr(String func_name, List<Expression> args) throws InvalidFunctionExpressionException {
		super(func_name, args);
	}

	public boolean matches(EvaluationContext context) throws Exception {
		Boolean result = (Boolean) evaluate(context);
		return result != null && result;

	}
}

