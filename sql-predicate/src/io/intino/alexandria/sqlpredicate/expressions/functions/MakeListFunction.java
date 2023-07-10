package io.intino.alexandria.sqlpredicate.expressions.functions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.FunctionCallExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter function that creates a list with each argument being one element in the list.
 * For example:
 * <p/>
 * <p style="margin-left: 4em">
 * MAKELIST( '1', '2', '3' )
 * </p>
 */

public class MakeListFunction implements FilterFunction {

	public boolean isValid(FunctionCallExpression expr) {
		return true;
	}

	public boolean returnsBoolean(FunctionCallExpression expr) {
		return false;
	}

	public Object evaluate(FunctionCallExpression expr, EvaluationContext context) throws Exception {
		int num_arg = expr.getNumArguments();
		List<Object> ele_arr = new ArrayList<>(num_arg);
		int cur = 0;
		while (cur < num_arg) {
			ele_arr.add(expr.getArgument(cur).evaluate(context));
			cur++;
		}
		return ele_arr;
	}
}
