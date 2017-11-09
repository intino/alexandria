package io.intino.konos.server.activity.displays.molds.model;

import io.intino.konos.server.activity.displays.elements.model.Item;

public abstract class Stamp<O> {
	private String name = "";
	private String label = "";
	private Value value = empty();
	private Layout layout = Layout.Fixed;
	private int height = -1;
	private String suffix = "";
	private String defaultStyle = "";
	private Value<String> style = empty();
	private Editable editable;

	public String name() {
		return this.name;
	}

	public Stamp name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return this.label;
	}

	public Stamp label(String label) {
		this.label = label;
		return this;
	}

	public O value(Item item, String username) {
		if (item == null) return null;
		return objectValue(item.object(), username);
	}

	public abstract O objectValue(Object object, String username);

	public Stamp value(Value<O> value) {
		this.value = value;
		return this;
	}

	public Layout layout() {
		return this.layout;
	}

	public Stamp layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public int height() {
		return this.height;
	}

	public Stamp height(int height) {
		this.height = height;
		return this;
	}

	public String suffix() {
		return this.suffix;
	}

	public Stamp suffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	public String defaultStyle() {
		return this.defaultStyle;
	}

	public Stamp defaultStyle(String defaultStyle) {
		this.defaultStyle = defaultStyle;
		return this;
	}

	public String style(Item item, String username) {
		return style != null ? style.value(item, username) : empty().value(item, username);
	}

	public Stamp style(Value style) {
		this.style = style;
		return this;
	}

	public Editable.Refresh save(Item item, String value, String username) {
		return editable != null ? editable.save(item.object(), value, username) : Editable.Refresh.None;
	}

	public boolean editable() {
		return editable != null;
	}

	public Stamp editable(Editable editable) {
		this.editable = editable;
		return this;
	}

	public enum Layout {
		Fixed, Flexible;
	}

	public interface Value<O> {
		O value(Object object, String username);
	}

	public interface Editable {
		Refresh save(Object object, String value, String username);

		enum Refresh {
			None, Object, Catalog
		}
	}

	protected Value<O> value() {
		return value;
	}

	private static Value<String> empty() {
		return (item, username) -> "";
	}
}
