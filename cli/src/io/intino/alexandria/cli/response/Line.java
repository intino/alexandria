package io.intino.alexandria.cli.response;

public class Line {
	private final String template;
	private final boolean multiple;
	private final String name;
	private final String dependantLine;

	public Line(String name, String template, boolean multiple) {
		this(name, template, multiple, null);
	}

	public Line(String name, String template, boolean multiple, String dependantLine) {
		this.name = name;
		this.template = template;
		this.multiple = multiple;
		this.dependantLine = dependantLine;
	}

	public String name() {
		return name;
	}

	public String template() {
		return template;
	}

	public boolean multiple() {
		return multiple;
	}

	public String dependantLine() {
		return dependantLine;
	}

}
