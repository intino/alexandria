package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.notifiers.SignDocumentNotifier;
import io.intino.alexandria.ui.utils.IOUtils;

import java.io.IOException;
import java.net.URL;

public class SignDocument<DN extends SignDocumentNotifier, B extends Box> extends AbstractSignDocument<DN, B> {
    private String signData;

    public SignDocument(B box) {
        super(box);
        _signFormat(SignFormat.PAdES);
    }

    public void document(URL document) {
        signData = signData(document);
    }

    public void execute() {
        notifier.refreshReadonly(true);
        sign(signData);
    }

    private String signData(URL document) {
        try {
            if (document == null) return null;
            return Base64.encode(IOUtils.toByteArray(document));
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

}