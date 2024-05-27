package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@SuppressWarnings("rawtypes")
public abstract class ComparisonExpression extends BinaryExpression implements BooleanExpression {
    public static final ThreadLocal<Boolean> CONVERT_STRING_EXPRESSIONS = new ThreadLocal<>();

	boolean convertStringExpressions;
    private static final Set<Character> REGEXP_CONTROL_CHARS = new HashSet<>();

	public ComparisonExpression(Expression left, Expression right) {
		super(left, right);
		convertStringExpressions = CONVERT_STRING_EXPRESSIONS.get() != null;
	}

	public static BooleanExpression createBetween(Expression value, Expression left, Expression right) {
		return LogicExpression.createAND(createGreaterThanEqual(value, left), createLessThanEqual(value, right));
	}

	public static BooleanExpression createNotBetween(Expression value, Expression left, Expression right) {
		return LogicExpression.createOR(createLessThan(value, left), createGreaterThan(value, right));
	}

	static {
		REGEXP_CONTROL_CHARS.add('.');
		REGEXP_CONTROL_CHARS.add('\\');
		REGEXP_CONTROL_CHARS.add('[');
		REGEXP_CONTROL_CHARS.add(']');
		REGEXP_CONTROL_CHARS.add('^');
		REGEXP_CONTROL_CHARS.add('$');
		REGEXP_CONTROL_CHARS.add('?');
		REGEXP_CONTROL_CHARS.add('*');
		REGEXP_CONTROL_CHARS.add('+');
		REGEXP_CONTROL_CHARS.add('{');
		REGEXP_CONTROL_CHARS.add('}');
		REGEXP_CONTROL_CHARS.add('|');
		REGEXP_CONTROL_CHARS.add('(');
		REGEXP_CONTROL_CHARS.add(')');
		REGEXP_CONTROL_CHARS.add(':');
		REGEXP_CONTROL_CHARS.add('&');
		REGEXP_CONTROL_CHARS.add('<');
		REGEXP_CONTROL_CHARS.add('>');
		REGEXP_CONTROL_CHARS.add('=');
		REGEXP_CONTROL_CHARS.add('!');
	}

	static class LikeExpression extends UnaryExpression implements BooleanExpression {
		Pattern likePattern;

		public LikeExpression(Expression right, String like, int escape) {
			super(right);

			StringBuffer regexp = new StringBuffer(like.length() * 2);
			regexp.append("\\A");
			for (int i = 0; i < like.length(); i++) {
				char c = like.charAt(i);
				if (escape == (0xFFFF & c) && shouldEscapeNext(like, i, c)) {
					i++;
					char t = like.charAt(i);
					regexp.append("\\x");
					regexp.append(Integer.toHexString(0xFFFF & t));
				} else {
					append(regexp, c);
				}
			}
			regexp.append("\\z");
			likePattern = Pattern.compile(regexp.toString(), Pattern.DOTALL);
		}

		private boolean shouldEscapeNext(String selector, int i, char escape) {
			int next = i + 1;
			if (next < selector.length()) {
				final char c = selector.charAt(next);
				return (c == '_' || c == '%' || c == escape);
			}
			return false;
		}

		private void append(StringBuffer regexp, char c) {
			if (c == '%') regexp.append(".*?");
			else if (c == '_') regexp.append(".");
			else if (REGEXP_CONTROL_CHARS.contains(c)) regexp.append("\\x").append(Integer.toHexString(0xFFFF & c));
			else regexp.append(c);
		}

		public String getExpressionSymbol() {
			return "LIKE";
		}

		public Object evaluate(EvaluationContext context) throws Exception {
			Object rv = this.getRight().evaluate(context);
			if (rv == null) return null;
			if (!(rv instanceof String)) return FALSE;
            return likePattern.matcher((String) rv).matches() ? TRUE : FALSE;
		}

		public boolean matches(EvaluationContext context) throws Exception {
			Object object = evaluate(context);
            return object == TRUE;
		}
	}

	public static BooleanExpression createLike(Expression left, String right, String escape) {
		if (escape != null && escape.length() != 1)
			throw new RuntimeException("The ESCAPE string litteral is invalid.  It can only be one character.  Litteral used: " + escape);
		int c = -1;
		if (escape != null) c = 0xFFFF & escape.charAt(0);
		return new LikeExpression(left, right, c);
	}

	public static BooleanExpression createNotLike(Expression left, String right, String escape) {
		return UnaryExpression.createNOT(createLike(left, right, escape));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static BooleanExpression createInFilter(Expression left, List elements) {

		if (!(left instanceof PropertyExpression)) {
			throw new RuntimeException("Expected a property for In expression, got: " + left);
		}
		return UnaryExpression.createInExpression((PropertyExpression) left, elements, false);

	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static BooleanExpression createNotInFilter(Expression left, List elements) {

		if (!(left instanceof PropertyExpression)) {
			throw new RuntimeException("Expected a property for In expression, got: " + left);
		}
		return UnaryExpression.createInExpression((PropertyExpression) left, elements, true);

	}

	public static BooleanExpression createIsNull(Expression left) {
		return doCreateEqual(left, ConstantExpression.NULL);
	}

	public static BooleanExpression createIsNotNull(Expression left) {
		return UnaryExpression.createNOT(doCreateEqual(left, ConstantExpression.NULL));
	}

	public static BooleanExpression createNotEqual(Expression left, Expression right) {
		return UnaryExpression.createNOT(createEqual(left, right));
	}

	public static BooleanExpression createEqual(Expression left, Expression right) {
		checkEqualOperand(left);
		checkEqualOperand(right);
		checkEqualOperandCompatability(left, right);
		return doCreateEqual(left, right);
	}

	private static BooleanExpression doCreateEqual(Expression left, Expression right) {
		return new EqualsExpression(left, right);
	}

	private static class EqualsExpression extends ComparisonExpression {
		EqualsExpression(Expression left, Expression right) {
			super(left, right);
		}

		public Object evaluate(EvaluationContext context) throws Exception {
			Object lv = left.evaluate(context);
			Object rv = right.evaluate(context);
			if (lv == null ^ rv == null) return lv == null ? null : FALSE;
            if (lv == rv || lv.equals(rv)) return TRUE;
			if (lv instanceof Comparable && rv instanceof Comparable) return compare((Comparable) lv, (Comparable) rv);
			return FALSE;
		}

		@Override
		public boolean matches(EvaluationContext context) throws Exception {
			Object lv = left.evaluate(context);
			Object rv = right.evaluate(context);
			if (lv == null ^ rv == null) return false;
			if (lv == rv || lv.equals(rv)) return true;
			if (lv.getClass() == rv.getClass()) return false;
			if (lv instanceof Comparable && rv instanceof Comparable) {
				Boolean compareResult = compare((Comparable) lv, (Comparable) rv);
				return compareResult != null && compareResult;
			}
			return false;
		}

		protected boolean asBoolean(int answer) {
			return answer == 0;
		}

		public String getExpressionSymbol() {
			return "=";
		}
	}

	public static BooleanExpression createGreaterThan(final Expression left, final Expression right) {
		checkLessThanOperand(left);
		checkLessThanOperand(right);
		return new ComparisonExpression(left, right) {
			protected boolean asBoolean(int answer) {
				return answer > 0;
			}

			public String getExpressionSymbol() {
				return ">";
			}
		};
	}

	public static BooleanExpression createGreaterThanEqual(final Expression left, final Expression right) {
		checkLessThanOperand(left);
		checkLessThanOperand(right);
		return new ComparisonExpression(left, right) {
			protected boolean asBoolean(int answer) {
				return answer >= 0;
			}

			public String getExpressionSymbol() {
				return ">=";
			}
		};
	}

	public static BooleanExpression createLessThan(final Expression left, final Expression right) {
		checkLessThanOperand(left);
		checkLessThanOperand(right);
		return new ComparisonExpression(left, right) {

			protected boolean asBoolean(int answer) {
				return answer < 0;
			}

			public String getExpressionSymbol() {
				return "<";
			}
		};
	}

	public static BooleanExpression createLessThanEqual(final Expression left, final Expression right) {
		checkLessThanOperand(left);
		checkLessThanOperand(right);
		return new ComparisonExpression(left, right) {
			protected boolean asBoolean(int answer) {
				return answer <= 0;
			}

			public String getExpressionSymbol() {
				return "<=";
			}
		};
	}

	public static void checkLessThanOperand(Expression expr) {
		if (expr instanceof ConstantExpression) {
			Object value = ((ConstantExpression) expr).getValue();
			if (value instanceof Number) return;
			throw new RuntimeException("Value '" + expr + "' cannot be compared.");
		}
		if (expr instanceof BooleanExpression) throw new RuntimeException("Value '" + expr + "' cannot be compared.");
	}

	public static void checkEqualOperand(Expression expr) {
		if (expr instanceof ConstantExpression) {
			Object value = ((ConstantExpression) expr).getValue();
			if (value == null) {
				throw new RuntimeException("'" + expr + "' cannot be compared.");
			}
		}
	}

	private static void checkEqualOperandCompatability(Expression left, Expression right) {
		if (left instanceof ConstantExpression && right instanceof ConstantExpression) {
			if (left instanceof BooleanExpression && !(right instanceof BooleanExpression)) {
				throw new RuntimeException("'" + left + "' cannot be compared with '" + right + "'");
			}
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Object evaluate(EvaluationContext context) throws Exception {
		Comparable<Comparable> lv = (Comparable) left.evaluate(context);
		if (lv == null) {
			return null;
		}
		Comparable rv = (Comparable) right.evaluate(context);
		if (rv == null) {
			return null;
		}
		return compare(lv, rv);
	}

	protected Boolean compare(Comparable lv, Comparable rv) {
		Class<? extends Comparable> lc = lv.getClass();
		Class<? extends Comparable> rc = rv.getClass();
		if (lc != rc) {
			try {
				if (lc == Boolean.class) {
                    if (convertStringExpressions && rc == String.class) lv = Boolean.valueOf(lv.toString());
					else return FALSE;
				} else if (lc == Byte.class) {
					if (rc == Short.class) lv = ((Number) lv).shortValue();
					else if (rc == Integer.class) lv = ((Number) lv).intValue();
					else if (rc == Long.class) lv = ((Number) lv).longValue();
					else if (rc == Float.class) lv = ((Number) lv).floatValue();
					else if (rc == Double.class) lv = ((Number) lv).doubleValue();
					else if (convertStringExpressions && rc == String.class) rv = Byte.valueOf((String) rv);
					else return FALSE;
				} else if (lc == Short.class) {
					if (rc == Integer.class) lv = ((Number) lv).intValue();
					else if (rc == Long.class) lv = ((Number) lv).longValue();
					else if (rc == Float.class) lv = ((Number) lv).floatValue();
					else if (rc == Double.class) lv = ((Number) lv).doubleValue();
					else if (convertStringExpressions && rc == String.class) rv = Short.valueOf((String) rv);
					else return FALSE;
				} else if (lc == Integer.class) {
					if (rc == Long.class) lv = ((Number) lv).longValue();
					else if (rc == Float.class) lv = ((Number) lv).floatValue();
					else if (rc == Double.class) lv = ((Number) lv).doubleValue();
					else if (convertStringExpressions && rc == String.class) rv = Integer.valueOf((String) rv);
					else return FALSE;
				} else if (lc == Long.class) {
					if (rc == Integer.class) rv = ((Number) rv).longValue();
					else if (rc == Float.class) lv = ((Number) lv).floatValue();
					else if (rc == Double.class) lv = ((Number) lv).doubleValue();
					else if (convertStringExpressions && rc == String.class) rv = Long.valueOf((String) rv);
					else return FALSE;
				} else if (lc == Float.class) {
					if (rc == Integer.class) rv = ((Number) rv).floatValue();
					else if (rc == Long.class) rv = ((Number) rv).floatValue();
					else if (rc == Double.class) lv = ((Number) lv).doubleValue();
					else if (convertStringExpressions && rc == String.class) rv = Float.valueOf((String) rv);
					else return FALSE;
				} else if (lc == Double.class) {
					if (rc == Integer.class) rv = ((Number) rv).doubleValue();
					else if (rc == Long.class) rv = ((Number) rv).doubleValue();
					else if (rc == Float.class) rv = ((Number) rv).floatValue();
					else if (convertStringExpressions && rc == String.class) rv = Double.valueOf((String) rv);
					else return FALSE;
				} else if (convertStringExpressions && lc == String.class) {
					if (rc == Boolean.class) lv = Boolean.valueOf((String) lv);
					else if (rc == Byte.class) lv = Byte.valueOf((String) lv);
					else if (rc == Short.class) lv = Short.valueOf((String) lv);
					else if (rc == Integer.class) lv = Integer.valueOf((String) lv);
					else if (rc == Long.class) lv = Long.valueOf((String) lv);
					else if (rc == Float.class) lv = Float.valueOf((String) lv);
					else if (rc == Double.class) lv = Double.valueOf((String) lv);
					else return FALSE;
				} else return FALSE;
			} catch (NumberFormatException e) {
				return FALSE;
			}
		}
        return asBoolean(lv.compareTo(rv)) ? TRUE : FALSE;
	}

	protected abstract boolean asBoolean(int answer);

	public boolean matches(EvaluationContext context) throws Exception {
        return evaluate(context) == TRUE;
	}
}
