package io.intino.konos.alexandria.ui.spark.resources;

import io.intino.konos.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.ui.spark.UISparkManager;
import io.intino.konos.alexandria.ui.spark.actions.AlexandriaEditorSaveParameters;

import java.io.InputStream;

public abstract class EditorResource extends Resource {

	public EditorResource(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	protected InputStream loadDocument() {
		InputStream document = manager.fromForm("document", InputStream.class);

		if (document == null)
			document = manager.editorService().loadDocument(manager.fromPath("document", String.class));

		return document;
	}

	protected void saveDocument(AlexandriaEditorSaveParameters parameters) {
		manager.editorService().saveDocument(manager.fromPath("document", String.class), parameters.document(), parameters.completed());
	}

}

