package io.intino.alexandria.sqlpredicate.expressions;


import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

public record PropertyExpression(String name) implements Expression {

	@Override
	public Object evaluate(EvaluationContext context) throws Exception {
		return context.getProperty(name);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && this.getClass().equals(o.getClass()) && name.equals(((PropertyExpression) o).name);
	}
}
