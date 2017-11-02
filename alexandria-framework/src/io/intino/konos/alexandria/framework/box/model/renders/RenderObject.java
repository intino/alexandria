package io.intino.konos.alexandria.framework.box.model.renders;

import io.intino.konos.alexandria.framework.box.model.ElementRender;
import io.intino.konos.alexandria.framework.box.model.Item;
import io.intino.konos.alexandria.framework.box.model.layout.ElementOption;
import io.intino.konos.alexandria.framework.box.model.Panel;

public class RenderObject extends ElementRender {
	private Panel panel;
	private Object object = null;
	private Loader idLoader;
	private Loader nameLoader;

	public RenderObject(ElementOption option) {
		super(option);
	}

	public Panel panel() {
		return panel;
	}

	public RenderObject panel(Panel container) {
		this.panel = container;
		return this;
	}

	public Item item() {
		return new Item().id(id()).name(name()).object(object);
	}

	public RenderObject idLoader(Loader loader) {
		this.idLoader = loader;
		return this;
	}

	public RenderObject nameLoader(Loader loader) {
		this.nameLoader = loader;
		return this;
	}

	public Object object() {
		return object;
	}

	public RenderObject object(Object object) {
		this.object = object;
		return this;
	}

	public interface Loader {
		String value();
	}

	private String id() {
		return idLoader != null ? idLoader.value() : null;
	}

	private String name() {
		return nameLoader != null ? nameLoader.value() : null;
	}
}
