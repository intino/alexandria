package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.SignDocument;
import io.intino.alexandria.ui.displays.events.SignErrorEvent;
import io.intino.alexandria.ui.displays.events.SignEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DigitalSignatureExamplesMold extends AbstractDigitalSignatureExamplesMold<UiFrameworkBox> {
    private final List<Resource> documentsToSign = new ArrayList<>();

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
        signDocumentsField.onChange(e -> {
            if (documentsToSign.contains(e.item())) return;
            documentsToSign.add(e.item());
            signDocuments.readonly(false);
            signaturesBlock.visible(false);
        });
        signDocumentsField.onRemove(e -> {
            documentsToSign.remove(e.index());
            signDocuments.readonly(documentsToSign.isEmpty());
            signaturesBlock.visible(false);
        });
        signDocuments.onSign(this::refreshSignatures);
        signDocuments.onError(this::refreshBatchError);
        signDocuments.documentProvider(batchProvider());
        authenticate.onExecute(e -> notifyUser("Authenticated", UserMessage.Type.Info));
    }

    private void refreshSignature(SignEvent e) {
        signature.value(e.signature());
        signedBy.value(e.info().getUsername());
        error.value(null);
    }

    private void refreshSignatures(SignEvent e) {
        signatures.clear();
        signatures.addAll(new ArrayList<>(e.signatures().values()));
        signaturesBlock.visible(true);
        batchSignedBy.value(e.info().getUsername());
        batchError.value(null);
    }

    private void refreshError(SignErrorEvent e) {
        signature.value(null);
        signedBy.value(null);
        error.value("Error code: " + e.code() + "; message: " + e.message());
    }

    private void refreshBatchError(SignErrorEvent e) {
        signatures.clear();
        signaturesBlock.visible(false);
        batchSignedBy.value(null);
        batchError.value("Error code: " + e.code() + "; message: " + e.message());
    }

    private SignDocument.BatchProvider batchProvider() {
        return () -> documentsToSign.stream().map(r -> {
            try {
                r.stream().reset();
                return r.stream();
            } catch (IOException ex) {
                Logger.error(ex);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}