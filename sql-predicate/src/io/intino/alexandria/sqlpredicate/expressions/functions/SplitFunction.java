package io.intino.alexandria.sqlpredicate.expressions.functions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.FunctionCallExpression;

public class SplitFunction implements FilterFunction {

	public boolean isValid(FunctionCallExpression expr) {
		return (expr.getNumArguments() >= 2) && (expr.getNumArguments() <= 3);
	}

	public boolean returnsBoolean(FunctionCallExpression expr) {
		return false;
	}

	public Object evaluate(FunctionCallExpression expr, EvaluationContext context) throws Exception {
		String[] result;
		String src = (String) expr.getArgument(0).evaluate(context);
		String splitPattern = (String) expr.getArgument(1).evaluate(context);
		if (expr.getNumArguments() > 2)
			result = src.split(splitPattern, (Integer) expr.getArgument(2).evaluate(context));
		else result = src.split(splitPattern);
		return java.util.Arrays.asList(result);
	}
}
