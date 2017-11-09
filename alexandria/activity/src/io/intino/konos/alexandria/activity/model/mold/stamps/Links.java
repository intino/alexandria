package io.intino.konos.alexandria.activity.model.mold.stamps;

import java.util.ArrayList;
import java.util.List;

public class Links {
	private List<Link> items = new ArrayList<>();

	public List<Link> items() {
		return this.items;
	}

	public Links add(Link item) {
		this.items.add(item);
		return this;
	}

	public static class Link {
		private String name;
		private String label;

		public String name() {
			return this.name;
		}

		public Link name(String name) {
			this.name = name;
			return this;
		}

		public String label() {
			return this.label;
		}

		public Link label(String label) {
			this.label = label;
			return this;
		}
	}
}