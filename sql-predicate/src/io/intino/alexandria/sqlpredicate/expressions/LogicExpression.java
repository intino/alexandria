package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicExpression implements BooleanExpression {
	protected final List<BooleanExpression> expressions = new ArrayList<>(2);

	private LogicExpression(BooleanExpression lvalue, BooleanExpression rvalue) {
		expressions.add(lvalue);
		expressions.add(rvalue);
	}

	protected void addExpression(BooleanExpression expression) {
		expressions.add(expression);
	}

	public BooleanExpression getLeft() {
		if (expressions.size() == 2) {
			return expressions.get(0);
		}
		throw new IllegalStateException("This expression is not binary: " + this);
	}

	public BooleanExpression getRight() {
		if (expressions.size() == 2) {
			return expressions.get(1);
		}
		throw new IllegalStateException("This expression is not binary: " + this);
	}

	public abstract String getExpressionSymbol();

	@Override
	public String toString() {
		if (expressions.size() == 2) {
			return "( " + expressions.get(0) + " " + getExpressionSymbol() + " " + expressions.get(1) + " )";
		}
		StringBuilder result = new StringBuilder("(");
		int count = 0;
		for (BooleanExpression expression : expressions) {
			if (count++ > 0) result.append(" ").append(getExpressionSymbol()).append(" ");
			result.append(expression.toString());
		}
		result.append(")");
		return result.toString();
	}

	public static BooleanExpression createOR(BooleanExpression lvalue, BooleanExpression rvalue) {
		if (lvalue instanceof ORExpression orExpression) {
			orExpression.addExpression(rvalue);
			return orExpression;
		} else return new ORExpression(lvalue, rvalue);
	}

	public static BooleanExpression createAND(BooleanExpression lvalue, BooleanExpression rvalue) {
		if (lvalue instanceof ANDExpression orExpression) {
			orExpression.addExpression(rvalue);
			return orExpression;
		} else return new ANDExpression(lvalue, rvalue);
	}

	@Override
	public abstract Object evaluate(EvaluationContext message) throws Exception;

	@Override
	public abstract boolean matches(EvaluationContext message) throws Exception;

	public static class ORExpression extends LogicExpression {

		public ORExpression(BooleanExpression lvalue, BooleanExpression rvalue) {
			super(lvalue, rvalue);
		}

		@Override
		public Object evaluate(EvaluationContext message) throws Exception {
			boolean someNulls = false;
			for (BooleanExpression expression : expressions) {
				Boolean lv = (Boolean) expression.evaluate(message);
				if (lv != null && lv.booleanValue()) {
					return Boolean.TRUE;
				}
				if (lv == null) {
					someNulls = true;
				}
			}
			if (someNulls) {
				return null;
			}
			return Boolean.FALSE;
		}

		@Override
		public boolean matches(EvaluationContext message) throws Exception {
			for (BooleanExpression expression : expressions) {
				boolean lv = expression.matches(message);
				if (lv) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String getExpressionSymbol() {
			return "OR";
		}
	}

	private static class ANDExpression extends LogicExpression {

		public ANDExpression(BooleanExpression lvalue, BooleanExpression rvalue) {
			super(lvalue, rvalue);
		}

		@Override
		public Object evaluate(EvaluationContext message) throws Exception {
			boolean someNulls = false;
			for (BooleanExpression expression : expressions) {
				Boolean lv = (Boolean) expression.evaluate(message);
				if (lv != null && !lv.booleanValue()) {
					return Boolean.FALSE;
				}
				if (lv == null) {
					someNulls = true;
				}
			}
			if (someNulls) {
				return null;
			}
			return Boolean.TRUE;
		}

		@Override
		public boolean matches(EvaluationContext message) throws Exception {
			for (BooleanExpression expression : expressions) {
				boolean lv = expression.matches(message);
				if (!lv) {
					return false;
				}
			}
			return true;
		}

		@Override
		public String getExpressionSymbol() {
			return "AND";
		}
	}
}
