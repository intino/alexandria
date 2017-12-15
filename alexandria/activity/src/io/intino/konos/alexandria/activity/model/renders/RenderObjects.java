package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.ItemList;
import io.intino.konos.alexandria.activity.model.Panel;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class RenderObjects extends ElementRender {
	private Panel panel;
	private Source source = null;

	public Panel panel() {
		return panel;
	}

	public RenderObjects panel(Panel container) {
		this.panel = container;
		return this;
	}

	public ItemList source() {
		return source != null ? items(source.entries()) : new ItemList();
	}

	public RenderObjects source(Source source) {
		this.source = source;
		return this;
	}

	private ItemList items(List<Source.Entry> objects) {
		return new ItemList(objects.stream().map(this::item).collect(toList()));
	}

	private Item item(Source.Entry entry) {
		return new Item().id(entry.id()).name(entry.name()).object(entry.object());
	}

	public interface Source {
		List<Entry> entries();

		interface Entry {
			String id();
			String name();
			Object object();
		}
	}

}