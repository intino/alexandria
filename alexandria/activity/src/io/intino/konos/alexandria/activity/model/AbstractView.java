package io.intino.konos.alexandria.activity.model;

public class AbstractView {
	private String name;
	private String label;
	private Hidden hidden = null;

	public String name() {
		return name;
	}

	public AbstractView name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public AbstractView label(String label) {
		this.label = label;
		return this;
	}

	public boolean hidden(Item item, String username) {
		return hidden != null && hidden.hidden(item != null ? item.object() : null, username);
	}

	public AbstractView hidden(Hidden hidden) {
		this.hidden = hidden;
		return this;
	}

	public interface Hidden {
		boolean hidden(Object object, String username);
	}
}
