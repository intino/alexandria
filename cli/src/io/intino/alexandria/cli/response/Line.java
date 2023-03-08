package io.intino.alexandria.cli.response;

public class Line {
	private final String name;
	private final String template;
	private final boolean addBreak;
	private final Multiple multiple;
	private final String dependantLine;

	public Line(String name, String template, boolean addBreak, Multiple multiple) {
		this(name, template, addBreak, multiple, null);
	}

	public Line(String name, String template, boolean addBreak, Multiple multiple, String dependantLine) {
		this.name = name;
		this.template = template;
		this.addBreak = addBreak;
		this.multiple = multiple;
		this.dependantLine = dependantLine;
	}

	public String name() {
		return name;
	}

	public String template() {
		return template;
	}

	public boolean addBreak() {
		return addBreak;
	}

	public Multiple multiple() {
		return multiple;
	}

	public String dependantLine() {
		return dependantLine;
	}

	public static class Multiple {
		private final boolean value;
		private final Arrangement arrangement;

		public enum Arrangement { Horizontal, Vertical }

		public Multiple(boolean value, Arrangement arrangement) {
			this.value = value;
			this.arrangement = arrangement;
		}

		public boolean value() {
			return value;
		}

		public Arrangement arrangement() {
			return arrangement;
		}
	}
}
