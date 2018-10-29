package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.services.EditorService.Permission;

import java.io.InputStream;

import static io.intino.alexandria.ui.services.EditorService.DocumentParameter;
import static io.intino.alexandria.ui.services.EditorService.PermissionParameter;

public abstract class EditorResource extends Resource {

	public EditorResource(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
	}

	protected io.intino.konos.alexandria.schema.Resource loadDocument() {
		String documentId = documentId();
		if (documentId == null) return null;
		return new io.intino.konos.alexandria.schema.Resource(documentId()).data(content());
	}

	protected Permission loadPermission() {
		String permission = manager.fromQuery(PermissionParameter, String.class);
		if (permission == null) return Permission.Editable;
		return permission.toLowerCase().equals("readonly") ? Permission.ReadOnly : Permission.Editable;
	}

	protected InputStream content() {
		io.intino.konos.alexandria.schema.Resource document = manager.fromForm(DocumentParameter, io.intino.konos.alexandria.schema.Resource.class);

		if (document == null)
			return manager.editorService() != null ? manager.editorService().loadDocument(documentId()) : null;

		return document.data();
	}

	protected void saveDocument(InputStream document, boolean completed) {
		if (manager.editorService() == null) return;
		String documentId = manager.fromQuery(DocumentParameter, String.class);
		io.intino.konos.alexandria.schema.Resource documentResource = new io.intino.konos.alexandria.schema.Resource(documentId).data(document);
		manager.editorService().saveDocument(documentId, documentResource, completed);
	}

	private String documentId() {
		return manager.fromQuery(DocumentParameter, String.class);
	}
}

