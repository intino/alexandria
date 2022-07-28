package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;

public class DigitalSignatureExamplesMold extends AbstractDigitalSignatureExamplesMold<UiFrameworkBox> {

    public DigitalSignatureExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        autoFirma.text("hola mundo");
        //autoFirma.readonly(true);
        autoFirma.onSign(e -> {
            signature.value(e.signature());
            signedBy.value(e.info().getUsername());
            error.value(null);
        });
        autoFirma.onError(e -> {
            signature.value(null);
            signedBy.value(null);
            error.value("Error code: " + e.code() + "; message: " + e.message());
        });
    }
}