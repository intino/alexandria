package io.intino.konos.alexandria.ui.spark.resources;

import io.intino.konos.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.ui.spark.UISparkManager;

import java.io.InputStream;

public abstract class EditorResource extends Resource {

	public EditorResource(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	protected io.intino.konos.alexandria.schema.Resource loadDocument() {
		return new io.intino.konos.alexandria.schema.Resource(documentId()).data(content());
	}

	protected InputStream content() {
		InputStream document = manager.fromForm("content", InputStream.class);

		if (document == null && manager.editorService() != null)
			document = manager.editorService().loadDocument(documentId());

		return document;
	}

	protected void saveDocument(InputStream document, boolean completed) {
		if (manager.editorService() == null) return;
		manager.editorService().saveDocument(manager.fromQuery("document", String.class), document, completed);
	}

	private String documentId() {
		return manager.fromQuery("document", String.class);
	}
}

