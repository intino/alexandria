package io.intino.alexandria.ui.model.dynamictable;

public class Column {
	private String label;
	private Operator operator;
	private String metric = "";
	private int countDecimals = 0;
	private String color = "transparent";

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

	public int countDecimals() {
		return countDecimals;
	}

	public Column countDecimals(int countDecimals) {
		this.countDecimals = countDecimals;
		return this;
	}

	public String color() {
		return color;
	}

	public Column color(String color) {
		this.color = color;
		return this;
	}
}
