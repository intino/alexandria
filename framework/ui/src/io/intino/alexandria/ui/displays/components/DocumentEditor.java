package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.SignActionSetup;
import io.intino.alexandria.schemas.SignActionSignatureFailure;
import io.intino.alexandria.schemas.SignActionSignatureSuccess;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.sign.AutoFirmaServer;
import io.intino.alexandria.ui.displays.events.SignErrorEvent;
import io.intino.alexandria.ui.displays.events.SignErrorListener;
import io.intino.alexandria.ui.displays.events.SignEvent;
import io.intino.alexandria.ui.displays.events.SignListener;
import io.intino.alexandria.ui.displays.notifiers.DocumentEditorNotifier;
import io.intino.alexandria.ui.displays.notifiers.SignActionNotifier;
import io.intino.icod.core.SignatureInfo;
import io.intino.icod.core.XadesSignatureHelper;

public abstract class DocumentEditor<DN extends DocumentEditorNotifier, B extends Box> extends AbstractDocumentEditor<DN, B> {

	public DocumentEditor(B box) {
		super(box);
	}

}