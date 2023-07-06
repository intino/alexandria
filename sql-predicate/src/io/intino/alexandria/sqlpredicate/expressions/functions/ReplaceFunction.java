package io.intino.alexandria.sqlpredicate.expressions.functions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.FunctionCallExpression;

/**
 * Function which replaces regular expression matches in a source string to a replacement literal.
 * <p/>
 * For Example:
 * REPLACE('1,2/3', '[,/]', ';') returns '1;2;3'
 */

public class ReplaceFunction implements FilterFunction {
	public boolean isValid(FunctionCallExpression expr) {
		return expr.getNumArguments() == 3;
	}

	public boolean returnsBoolean(FunctionCallExpression expr) {
		return false;
	}

	public Object evaluate(FunctionCallExpression expr, EvaluationContext context) throws Exception {
		String src = (String) expr.getArgument(0).evaluate(context);
		String regex = (String) expr.getArgument(1).evaluate(context);
		String separator = (String) expr.getArgument(2).evaluate(context);
		return src != null ? src.replaceAll(regex, separator) : src;
	}
}
