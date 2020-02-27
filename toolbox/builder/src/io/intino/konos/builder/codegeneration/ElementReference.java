package io.intino.konos.builder.codegeneration;

import io.intino.magritte.framework.Layer;

public class ElementReference {
	private String name;
	private String type;
	private Context context;

	public enum Context {
		Display, Schema, Theme;

		public static Context from(Layer element) {
			if (element.i$(io.intino.konos.model.graph.Theme.class)) return ElementReference.Context.Theme;
			if (element.i$(io.intino.konos.model.graph.Schema.class)) return ElementReference.Context.Schema;
			if (element.i$(io.intino.konos.model.graph.Display.class)) return Context.Display;
			return null;
		}
	}

	public String name() {
		return name;
	}

	public ElementReference name(String name) {
		this.name = name;
		return this;
	}

	public String type() {
		return type;
	}

	public ElementReference type(String type) {
		this.type = type;
		return this;
	}

	public Context context() {
		return context;
	}

	public ElementReference context(Context context) {
		this.context = context;
		return this;
	}

	public static ElementReference from(String content) {
		String[] data = content.split("#");
		ElementReference result = new ElementReference().name(data[0]);
		if (data.length > 1) result.type(data[1]);
		if (data.length > 2) result.context(Context.valueOf(data[2]));
		return result;
	}

	public static ElementReference of(String name, String type, Context context) {
		return new ElementReference().name(name).type(type).context(context);
	}

	public String toString() {
		return name + "#" + type + "#" + context;
	}
}
