package io.intino.alexandria.framework.box.model.renders;

import io.intino.alexandria.framework.box.model.ElementRender;
import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.ItemList;
import io.intino.alexandria.framework.box.model.layout.ElementOption;
import io.intino.alexandria.framework.box.model.Panel;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class RenderObjects extends ElementRender {
	private Panel panel;
	private Source source = null;

	public RenderObjects(ElementOption option) {
		super(option);
	}

	public Panel panel() {
		return panel;
	}

	public RenderObjects panel(Panel container) {
		this.panel = container;
		return this;
	}

	public ItemList source() {
		return source != null ? items(source.objects()) : new ItemList();
	}

	private ItemList items(List<Object> objects) {
		return new ItemList(objects.stream().map(this::item).collect(toList()));
	}

	private Item item(Object object) {
		return new Item().id(id(object)).name(name(object)).object(object);
	}

	private String id(Object object) {
		return source != null ? source.id(object) : null;
	}

	private String name(Object object) {
		return source != null ? source.name(object) : null;
	}

	public RenderObjects source(Source source) {
		this.source = source;
		return this;
	}

	public interface Source {
		List<Object> objects();
		String id(Object object);
		String name(Object object);
	}

}