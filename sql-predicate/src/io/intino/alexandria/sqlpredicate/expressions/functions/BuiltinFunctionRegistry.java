package io.intino.alexandria.sqlpredicate.expressions.functions;

import static io.intino.alexandria.sqlpredicate.expressions.FunctionCallExpression.registerFunction;

public class BuiltinFunctionRegistry {
	public static void register() {
		registerFunction("INLIST", new InListFunction());
		registerFunction("MAKELIST", new MakeListFunction());
		registerFunction("REGEX", new RegexMatchFunction());
		registerFunction("REPLACE", new ReplaceFunction());
		registerFunction("SPLIT", new SplitFunction());
	}
}

