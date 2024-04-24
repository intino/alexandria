package io.intino.alexandria.sqlpredicate.expressions.functions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.FunctionCallExpression;
import io.intino.alexandria.sqlpredicate.parser.LRUCache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter function that matches a value against a regular expression.
 * <p/>
 * <p style="margin-left: 4em">
 * REGEX( 'A.B', 'A-B' )
 * </p>
 * <p/>
 * Note that the regular expression is not anchored; use the anchor characters, ^ and $, as-needed.  For example,
 * REGEX( 'AA', 'XAAX' ) evaluates to true while REGEX( '^AA$' , 'XAAX' ) evaluates to false.
 */
public class RegexMatchFunction implements FilterFunction {
	protected static final LRUCache<String, Pattern> compiledExprCache = new LRUCache<>(100);

	public boolean isValid(FunctionCallExpression expr) {
		return expr.getNumArguments() == 2;
	}

	public boolean returnsBoolean(FunctionCallExpression expr) {
		return true;
	}

	public Object evaluate(FunctionCallExpression expr, EvaluationContext context) throws Exception {
		Pattern pat;
		Object reg = expr.getArgument(0).evaluate(context);
		if (reg != null) {
			String reg_str = reg instanceof String ? (String) reg : reg.toString();
			Object cand = expr.getArgument(1).evaluate(context);
			if (cand != null) {
				pat = getCompiledPattern(reg_str);
				Matcher matchEng = pat.matcher(cand instanceof String ? (String) cand : cand.toString());
				return matchEng.find();
			}
		}

		return Boolean.FALSE;
	}

	protected Pattern getCompiledPattern(String reg_ex_str) {
		Pattern result;
		synchronized (compiledExprCache) {
			result = compiledExprCache.get(reg_ex_str);
		}
		if (result == null) {
			result = Pattern.compile(reg_ex_str);
			synchronized (compiledExprCache) {
				compiledExprCache.put(reg_ex_str, result);
			}
		}
		return result;
	}
}