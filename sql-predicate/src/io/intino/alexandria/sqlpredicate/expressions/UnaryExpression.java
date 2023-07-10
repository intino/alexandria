package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class UnaryExpression implements Expression {

	private static final BigDecimal BD_LONG_MIN_VALUE = BigDecimal.valueOf(Long.MIN_VALUE);
	protected Expression right;

	public UnaryExpression(Expression left) {
		this.right = left;
	}

	public static Expression createNegate(Expression left) {
		return new UnaryExpression(left) {
			public Object evaluate(EvaluationContext context) throws Exception {
				Object rvalue = right.evaluate(context);
				if (rvalue == null) return null;
				if (rvalue instanceof Number) return negate((Number) rvalue);
				return null;
			}

			public String getExpressionSymbol() {
				return "-";
			}
		};
	}

	public static BooleanExpression createInExpression(PropertyExpression right, List<Object> elements, final boolean not) {
		Collection<Object> t;
		if (elements.size() == 0) t = null;
		else if (elements.size() < 5) t = elements;
		else t = new HashSet<>(elements);
		final Collection<Object> inList = t;
		return new BooleanUnaryExpression(right) {
			public Object evaluate(EvaluationContext context) throws Exception {
				Object rvalue = right.evaluate(context);
				if (rvalue == null) return null;
				if (rvalue.getClass() != String.class) return null;
				if ((inList != null && inList.contains(rvalue)) ^ not) return Boolean.TRUE;
				else return Boolean.FALSE;
			}

			public String toString() {
				StringBuilder answer = new StringBuilder();
				answer.append(right).append(" ").append(getExpressionSymbol()).append(" ( ");
				int count = 0;
				for (Object o : inList) {
					if (count != 0) answer.append(", ");
					answer.append(o);
					count++;
				}
				answer.append(" )");
				return answer.toString();
			}

			public String getExpressionSymbol() {
				return not ? "NOT IN" : "IN";
			}
		};
	}

	abstract static class BooleanUnaryExpression extends UnaryExpression implements BooleanExpression {
		public BooleanUnaryExpression(Expression left) {
			super(left);
		}

		public boolean matches(EvaluationContext context) throws Exception {
			Object object = evaluate(context);
			return object == Boolean.TRUE;
		}
	}

	public static BooleanExpression createNOT(BooleanExpression left) {
		return new NotExpression(left);
	}

	public static BooleanExpression createBooleanCast(Expression left) {
		return new BooleanUnaryExpression(left) {
			public Object evaluate(EvaluationContext context) throws Exception {
				Object rvalue = right.evaluate(context);
				if (rvalue == null) return null;
				if (!rvalue.getClass().equals(Boolean.class)) return Boolean.FALSE;
				return (Boolean) rvalue ? Boolean.TRUE : Boolean.FALSE;
			}

			public String toString() {
				return right.toString();
			}

			public String getExpressionSymbol() {
				return "";
			}
		};
	}

	private static Number negate(Number left) {
		Class<?> clazz = left.getClass();
		if (clazz == Integer.class) return -left.intValue();
		else if (clazz == Long.class) return -left.longValue();
		else if (clazz == Float.class) return -left.floatValue();
		else if (clazz == Double.class) return -left.doubleValue();
		else if (clazz == BigDecimal.class) {
			BigDecimal bd = (BigDecimal) left;
			bd = bd.negate();
			if (BD_LONG_MIN_VALUE.compareTo(bd) == 0) return Long.MIN_VALUE;
			return bd;
		} else throw new RuntimeException("Don't know how to negate: " + left);
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression expression) {
		right = expression;
	}


	public String toString() {
		return "(" + getExpressionSymbol() + " " + right.toString() + ")";
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object o) {
		if (o == null || !this.getClass().equals(o.getClass())) return false;
		return toString().equals(o.toString());
	}

	public abstract String getExpressionSymbol();

	private static class NotExpression extends BooleanUnaryExpression {
		public NotExpression(BooleanExpression right) {
			super(right);
		}

		public Object evaluate(EvaluationContext context) throws Exception {
			Boolean lvalue = (Boolean) right.evaluate(context);
			if (lvalue == null) return null;
			return lvalue ? Boolean.FALSE : Boolean.TRUE;
		}

		@Override
		public boolean matches(EvaluationContext context) throws Exception {
			Boolean lvalue = (Boolean) right.evaluate(context);
			return lvalue != null && !lvalue;
		}

		public String getExpressionSymbol() {
			return "NOT";
		}
	}
}
