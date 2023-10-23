package io.intino.alexandria.ui.displays.components.documenteditor;

import java.io.InputStream;

public interface DocumentManager {
	DocumentInfo info(String id);
	InputStream load(String id);
	void save(String id, InputStream content);

	class DocumentInfo {
		private final String id;
		private final String name;
		private final String author;
		private final boolean readonly;

		public DocumentInfo(String id, String name, String author, boolean readonly) {
			this.id = id;
			this.name = name;
			this.author = author;
			this.readonly = readonly;
		}

		public String id() {
			return id;
		}

		public String name() {
			return name;
		}

		public String author() {
			return author;
		}

		public boolean readonly() {
			return readonly;
		}
	}
}
