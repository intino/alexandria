package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.SignDocumentBatchEntry;
import io.intino.alexandria.schemas.SignDocumentBatchSuccess;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.events.SignEvent;
import io.intino.alexandria.ui.displays.events.SignListener;
import io.intino.alexandria.ui.displays.notifiers.SignDocumentNotifier;
import io.intino.alexandria.ui.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class SignDocument<DN extends SignDocumentNotifier, B extends Box> extends AbstractSignDocument<DN, B> {
    private SignProvider provider;
    private BeforeSignChecker beforeSignChecker;

    public interface SignProvider { }

    public interface DocumentProvider extends SignProvider {
        InputStream document();
    }

    public interface BatchProvider extends SignProvider {
        java.util.List<InputStream> documents();
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
        this.provider = (DocumentProvider) () -> {
            try {
                return document.openStream();
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        };
    }

    public void documents(java.util.List<URL> documents) {
        this.provider = (BatchProvider) () -> documents.stream().map(d -> {
            try {
                return d.openStream();
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void documentProvider(DocumentProvider provider) {
        this.provider = provider;
    }

    public void documentProvider(BatchProvider provider) {
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
        sign();
        notifier.refreshReadonly(true);
    }

    public void batchSuccess(List<SignDocumentBatchSuccess> successList) {
        List<SignEvent.SignEventInfo> signEventInfoList = successList.stream().map(this::signInfoOf).collect(Collectors.toList());
        notifyUser(translate("Sign process finished"), UserMessage.Type.Success);
        signListener.accept(new SignEvent(this, signEventInfoList));
    }

    private SignEvent.SignEventInfo signInfoOf(SignDocumentBatchSuccess success) {
        String signature = server.signature(success.id());
        return new SignEvent.SignEventInfo(success.id(), signature, null, info(signature));
    }

    private void sign() {
        if (provider instanceof BatchProvider) signBatch(((BatchProvider) provider).documents());
        else signDocument(((DocumentProvider)provider).document());
    }

    private void signBatch(java.util.List<InputStream> documentList) {
        java.util.List<io.intino.alexandria.ui.displays.components.sign.SignDocument> documentUrlList = documentList.stream().map(d -> server.store(UUID.randomUUID().toString(), d)).collect(Collectors.toList());
        if (documentUrlList.stream().anyMatch(Objects::isNull)) {
            notifyUser(translate("Could not sign documents in batch"), UserMessage.Type.Error);
            return;
        }
        notifier.signBatch(documentUrlList.stream().map(this::schemaOf).collect(Collectors.toList()));
    }

    private void signDocument(InputStream documentStream) {
        io.intino.alexandria.ui.displays.components.sign.SignDocument document = server.store(UUID.randomUUID().toString(), documentStream);
        if (document == null) {
            notifyUser(translate("Could not sign document"), UserMessage.Type.Error);
            return;
        }
        sign(document.url());
    }

    private boolean canSign() {
        return beforeSignChecker == null || beforeSignChecker.check();
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

    private SignDocumentBatchEntry schemaOf(io.intino.alexandria.ui.displays.components.sign.SignDocument document) {
        return new SignDocumentBatchEntry().id(document.id()).url(document.url());
    }

}