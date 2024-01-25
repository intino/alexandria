package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DocumentEditorNotifier;

public abstract class DocumentEditor<DN extends DocumentEditorNotifier, B extends Box> extends AbstractDocumentEditor<DN, B> {

	public DocumentEditor(B box) {
		super(box);
	}

}