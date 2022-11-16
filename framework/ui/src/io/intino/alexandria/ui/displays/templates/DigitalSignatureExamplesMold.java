package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.events.SignErrorEvent;
import io.intino.alexandria.ui.displays.events.SignEvent;

import java.nio.file.Paths;

public class DigitalSignatureExamplesMold extends AbstractDigitalSignatureExamplesMold<UiFrameworkBox> {

    public DigitalSignatureExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        signText.text("hola mundo");
        signDocument.document(DigitalSignatureExamplesMold.class.getResource("/data/example.pdf"));
        //autoFirma.readonly(true);
        signText.onSign(this::refreshSignature);
        signDocument.onSign(this::refreshSignature);
        signText.onError(this::refreshError);
        signDocument.onError(this::refreshError);
    }

    private void refreshSignature(SignEvent e) {
        signature.value(e.signature());
        signedBy.value(e.info().getUsername());
        error.value(null);
    }

    private void refreshError(SignErrorEvent e) {
        signature.value(null);
        signedBy.value(null);
        error.value("Error code: " + e.code() + "; message: " + e.message());
    }
}