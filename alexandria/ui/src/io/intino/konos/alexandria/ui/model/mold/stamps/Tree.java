package io.intino.konos.alexandria.ui.model.mold.stamps;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Tree {
	private TreeItem root = null;
	private List<TreeItem> items = new ArrayList();

	public TreeItem root() {
		return this.root;
	}

	public List<TreeItem> items() {
		return this.items;
	}

	public Tree add(TreeItem item) {
		add(item, false);
		return this;
	}

	public Tree add(TreeItem item, boolean isRoot) {
		items.add(item);
		if (isRoot) root = item;
		return this;
	}

	public static class TreeItem {
		private String name;
		private String label;
		private List<TreeItem> children = new ArrayList();

		public String name() {
			return this.name;
		}

		public TreeItem name(String name) {
			this.name = new String(Base64.getEncoder().encode(name.getBytes()));
			return this;
		}

		public String label() {
			return this.label;
		}

		public TreeItem label(String label) {
			this.label = label;
			return this;
		}

		public List<TreeItem> children() {
			return this.children;
		}

		public TreeItem add(TreeItem item) {
			this.children.add(item);
			return this;
		}
	}
}