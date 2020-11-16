package io.intino.alexandria.ui.model.dynamictable;

public class Column {
	private String label;
	private Operator operator;
	private String metric = "";

	public enum Operator { Sum, Average }

	public Column(String label, Operator operator) {
		this.label = label;
		this.operator = operator;
	}

	public String label() {
		return label;
	}

	public Column label(String label) {
		this.label = label;
		return this;
	}

	public Operator operator() {
		return operator;
	}

	public Column operator(Operator operator) {
		this.operator = operator;
		return this;
	}

	public String metric() {
		return metric;
	}

	public Column metric(String metric) {
		this.metric = metric;
		return this;
	}
}
