package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.DocumentEditorCollaboraInfo;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.components.documenteditor.CollaboraServer;
import io.intino.alexandria.ui.displays.components.documenteditor.DocumentManager;
import io.intino.alexandria.ui.displays.notifiers.DocumentEditorCollaboraNotifier;

public class DocumentEditorCollabora<DN extends DocumentEditorCollaboraNotifier, B extends Box> extends AbstractDocumentEditorCollabora<DN, B> {
	private CollaboraServer server;
	private String documentId;
	private String editorUrl;

	public DocumentEditorCollabora(B box) {
		super(box);
	}

	public DocumentEditorCollabora<DN, B> document(String id) {
		this._document(id);
		return this;
	}

	public DocumentEditorCollabora<DN, B> documentManager(DocumentManager documentManager) {
		server.documentManager(accessToken(), documentManager);
		return this;
	}

	public DocumentEditorCollabora<DN, B> editorUrl(String url) {
		_editorUrl(url);
		return this;
	}

	@Override
	public void init() {
		super.init();
		server = new CollaboraServer((AlexandriaUiBox)box()).listen();
		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		server.register(accessToken());
		notifier.refresh(new DocumentEditorCollaboraInfo().editorUrl(editorUrl).accessToken(accessToken()).documentUrl(documentUrl()));
	}

	@Override
	public void unregister() {
		super.unregister();
		server.unregister(accessToken());
	}

	protected DocumentEditorCollabora<DN, B> _editorUrl(String url) {
		this.editorUrl = url;
		return this;
	}

	protected DocumentEditorCollabora<DN, B> _document(String id) {
		this.documentId = id;
		return this;
	}

	private String documentUrl() {
		if (documentId == null) return null;
		return server.url(session().browser().baseUrl(), documentId);
	}

	private String accessToken() {
		return session().id() + "-" + id();
	}

}