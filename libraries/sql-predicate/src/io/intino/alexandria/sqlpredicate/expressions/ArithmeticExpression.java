package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

public abstract class ArithmeticExpression extends BinaryExpression {
	protected static final int INTEGER = 1;
	protected static final int LONG = 2;
	protected static final int DOUBLE = 3;

	public ArithmeticExpression(Expression left, Expression right) {
		super(left, right);
	}

	public static Expression createPlus(Expression left, Expression right) {
		return new ArithmeticExpression(left, right) {
			protected Object evaluate(Object lvalue, Object rvalue) {
				if (lvalue instanceof String text) return text + rvalue;
                else if (lvalue instanceof Number) return plus((Number) lvalue, asNumber(rvalue));
				throw new RuntimeException("Cannot call plus operation on: " + lvalue + " and: " + rvalue);
			}

			public String getExpressionSymbol() {
				return "+";
			}
		};
	}

	public static Expression createMinus(Expression left, Expression right) {
		return new ArithmeticExpression(left, right) {
			protected Object evaluate(Object lvalue, Object rvalue) {
				if (lvalue instanceof Number) return minus((Number) lvalue, asNumber(rvalue));
				throw new RuntimeException("Cannot call minus operation on: " + lvalue + " and: " + rvalue);
			}

			public String getExpressionSymbol() {
				return "-";
			}
		};
	}

	public static Expression createMultiply(Expression left, Expression right) {
		return new ArithmeticExpression(left, right) {

			protected Object evaluate(Object lvalue, Object rvalue) {
				if (lvalue instanceof Number) return multiply((Number) lvalue, asNumber(rvalue));
				throw new RuntimeException("Cannot call multiply operation on: " + lvalue + " and: " + rvalue);
			}

			public String getExpressionSymbol() {
				return "*";
			}
		};
	}

	public static Expression createDivide(Expression left, Expression right) {
		return new ArithmeticExpression(left, right) {

			protected Object evaluate(Object lvalue, Object rvalue) {
                if (lvalue instanceof Number) return divide((Number) lvalue, asNumber(rvalue));
				throw new RuntimeException("Cannot call divide operation on: " + lvalue + " and: " + rvalue);
			}

			public String getExpressionSymbol() {
				return "/";
			}
		};
	}

	public static Expression createMod(Expression left, Expression right) {
		return new ArithmeticExpression(left, right) {

			protected Object evaluate(Object lvalue, Object rvalue) {
                if (lvalue instanceof Number) return mod((Number) lvalue, asNumber(rvalue));
				throw new RuntimeException("Cannot call mod operation on: " + lvalue + " and: " + rvalue);
			}

			public String getExpressionSymbol() {
				return "%";
			}
		};
	}

	protected Number plus(Number left, Number right) {
		return switch (numberType(left, right)) {
			case INTEGER -> left.intValue() + right.intValue();
			case LONG -> left.longValue() + right.longValue();
			default -> left.doubleValue() + right.doubleValue();
		};
	}

	protected Number minus(Number left, Number right) {
		return switch (numberType(left, right)) {
			case INTEGER -> left.intValue() - right.intValue();
			case LONG -> left.longValue() - right.longValue();
			default -> left.doubleValue() - right.doubleValue();
		};
	}

	protected Number multiply(Number left, Number right) {
		return switch (numberType(left, right)) {
			case INTEGER -> left.intValue() * right.intValue();
			case LONG -> left.longValue() * right.longValue();
			default -> left.doubleValue() * right.doubleValue();
		};
	}

	protected Number divide(Number left, Number right) {
		return left.doubleValue() / right.doubleValue();
	}

	protected Number mod(Number left, Number right) {
		return left.doubleValue() % right.doubleValue();
	}

	private int numberType(Number left, Number right) {
		if (isDouble(left) || isDouble(right)) return DOUBLE;
		else if (left instanceof Long || right instanceof Long) return LONG;
		else return INTEGER;
	}

	private boolean isDouble(Number n) {
		return n instanceof Float || n instanceof Double;
	}

	protected Number asNumber(Object value) {
		if (value instanceof Number) return (Number) value;
		else throw new RuntimeException("Cannot convert value: " + value + " into a number");
	}

	public Object evaluate(EvaluationContext context) throws Exception {
		Object lvalue = left.evaluate(context);
		if (lvalue == null) return null;
		Object rvalue = right.evaluate(context);
        return rvalue == null ? null : evaluate(lvalue, rvalue);
	}

	protected abstract Object evaluate(Object lvalue, Object rvalue);
}
