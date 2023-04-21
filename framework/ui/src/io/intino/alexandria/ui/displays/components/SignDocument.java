package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.notifiers.SignDocumentNotifier;
import io.intino.alexandria.ui.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SignDocument<DN extends SignDocumentNotifier, B extends Box> extends AbstractSignDocument<DN, B> {
    private DocumentProvider provider;
    private BeforeSignChecker beforeSignChecker;

    public interface DocumentProvider {
        InputStream document();
    }

    public interface BeforeSignChecker {
        boolean check();
        default String checkMessage() { return null; }
    }

    public SignDocument(B box) {
        super(box);
        _signFormat(SignFormat.PAdES);
    }

    public void document(URL document) {
        this.provider = () -> {
            try {
                return document.openStream();
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        };
    }

    public void documentProvider(DocumentProvider provider) {
        this.provider = provider;
    }

    public void beforeSignChecker(BeforeSignChecker checker) {
        this.beforeSignChecker = checker;
    }

    public void execute() {
        if (provider == null) {
            notifyUser(translate("Indicate document to sign"), UserMessage.Type.Error);
            return;
        }
        if (!canSign()) {
            notifyUser(translate(cantSignMessage()), UserMessage.Type.Error);
            return;
        }
        sign(base64(provider.document()));
        notifier.refreshReadonly(true);
    }

    private boolean canSign() {
        return beforeSignChecker != null && beforeSignChecker.check();
    }

    private String cantSignMessage() {
        return beforeSignChecker != null && beforeSignChecker.checkMessage() != null ? beforeSignChecker.checkMessage() : "User can't sign document";
    }

    private String base64(InputStream document) {
        try {
            if (document == null) return null;
            return Base64.encode(IOUtils.toByteArray(document));
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

}