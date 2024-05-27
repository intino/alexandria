package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.math.BigDecimal;

public class ConstantExpression implements Expression {
	static class BooleanConstantExpression extends ConstantExpression implements BooleanExpression {
		public BooleanConstantExpression(Object value) {
			super(value);
		}

		public boolean matches(EvaluationContext context) throws Exception {
			Object object = evaluate(context);
			return object == Boolean.TRUE;
		}
	}

	public static final BooleanConstantExpression NULL = new BooleanConstantExpression(null);
	public static final BooleanConstantExpression TRUE = new BooleanConstantExpression(Boolean.TRUE);
	public static final BooleanConstantExpression FALSE = new BooleanConstantExpression(Boolean.FALSE);

	private final Object value;

	public ConstantExpression(Object value) {
		this.value = value;
	}

	public static ConstantExpression createFromDecimal(String text) {
		if (text.endsWith("l") || text.endsWith("L")) text = text.substring(0, text.length() - 1);
		Number value;
		try {
			value = Long.valueOf(text);
		} catch (NumberFormatException e) {
			value = new BigDecimal(text);
		}
		long l = value.longValue();
        if (Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE) value = value.intValue();
		return new ConstantExpression(value);
	}

	public static ConstantExpression createFromHex(String text) {
		Number value = Long.parseLong(text.substring(2), 16);
		long l = value.longValue();
		if (Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE) value = value.intValue();
		return new ConstantExpression(value);
	}

	public static ConstantExpression createFromOctal(String text) {
		Number value = Long.parseLong(text, 8);
		long l = value.longValue();
		if (Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE) value = value.intValue();
		return new ConstantExpression(value);
	}

	public static ConstantExpression createFloat(String text) {
		Number value = Double.valueOf(text);
		return new ConstantExpression(value);
	}

	public Object evaluate(EvaluationContext context) throws Exception {
		return value;
	}

	public Object getValue() {
		return value;
	}

	public String toString() {
		if (value == null) return "NULL";
		if (value instanceof Boolean) return (Boolean) value ? "TRUE" : "FALSE";
		if (value instanceof String) return encodeString((String) value);
		return value.toString();
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) return false;
		return toString().equals(o.toString());

	}

	public static String encodeString(String s) {
		StringBuilder b = new StringBuilder();
		b.append('\'');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\'') b.append(c);
			b.append(c);
		}
		b.append('\'');
		return b.toString();
	}

}
