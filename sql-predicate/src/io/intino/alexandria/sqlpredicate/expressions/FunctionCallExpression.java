package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.functions.BuiltinFunctionRegistry;
import io.intino.alexandria.sqlpredicate.expressions.functions.FilterFunction;

import java.util.HashMap;
import java.util.List;

public class FunctionCallExpression implements Expression {
	protected static final HashMap<String, FunctionRegistration> functionRegistry = new HashMap<>();
	protected String functionName;
	protected java.util.ArrayList<Expression> arguments;
	protected FilterFunction function;

	static {
		BuiltinFunctionRegistry.register();
	}

	public static boolean registerFunction(String name, FilterFunction function) {
		boolean result;
		result = true;
		synchronized (functionRegistry) {
			if (functionRegistry.containsKey(name)) result = false;
			else functionRegistry.put(name, new FunctionRegistration(function));
		}
		return result;
	}

	public static void deregisterFunction(String name) {
		synchronized (functionRegistry) {
			functionRegistry.remove(name);
		}
	}

	protected FunctionCallExpression(String function, List<Expression> args) throws InvalidFunctionExpressionException {
		FunctionRegistration funcReg;
		synchronized (functionRegistry) {
			funcReg = functionRegistry.get(function);
		}
		if (funcReg != null) {
			this.arguments = new java.util.ArrayList<>();
			this.arguments.addAll(args);
			this.functionName = function;
			this.function = funcReg.getFilterFunction();
		} else throw new InvalidFunctionExpressionException("invalid function name, \"" + function + "\"");
	}


	public static FunctionCallExpression createFunctionCall(String func_name, List<Expression> args) throws InvalidFunctionExpressionException {
		FunctionCallExpression result;
		result = new FunctionCallExpression(func_name, args);
		if (result.function.isValid(result)) {
			if (result.function.returnsBoolean(result))
				result = new BooleanFunctionCallExpr(func_name, args);
		} else throw new InvalidFunctionExpressionException("invalid call of function " + func_name);
		return result;
	}

	public int getNumArguments() {
		return arguments.size();
	}


	public Expression getArgument(int which) {
		return arguments.get(which);
	}

	public Object evaluate(EvaluationContext context) throws Exception {
		return this.function.evaluate(this, context);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(functionName).append("(");
		boolean first_f = true;
		for (Object arg : arguments) {
			if (first_f) first_f = false;
			else result.append(", ");
			result.append(arg.toString());
		}
		result.append(")");
		return result.toString();
	}

	protected static class FunctionRegistration {
		protected FilterFunction filterFunction;

		public FunctionRegistration(FilterFunction func) {
			this.filterFunction = func;
		}

		public FilterFunction getFilterFunction() {
			return filterFunction;
		}

	}

	public static class InvalidFunctionExpressionException extends Exception {
		public InvalidFunctionExpressionException(String msg) {
			super(msg);
		}

	}
}
