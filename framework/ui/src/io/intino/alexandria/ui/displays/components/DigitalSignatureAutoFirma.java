package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.DigitalSignatureAutoFirmaSetup;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.components.digitalsignature.AutoFirmaServer;
import io.intino.alexandria.ui.displays.notifiers.DigitalSignatureAutoFirmaNotifier;
import io.intino.icod.core.SignatureInfo;
import io.intino.icod.core.XadesSignatureHelper;

public class DigitalSignatureAutoFirma<DN extends DigitalSignatureAutoFirmaNotifier, B extends Box> extends AbstractDigitalSignatureAutoFirma<DN, B> {
    private AutoFirmaServer server;
    private SignFormat format = SignFormat.XAdES;

    public DigitalSignatureAutoFirma(B box) {
        super(box);
    }

    public enum SignFormat { XAdES, CAdES }

    @Override
    public void init() {
        super.init();
        server = new AutoFirmaServer((AlexandriaUiBox)box(), session()).listen();
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.setup(setup());
    }

    public BaseDigitalSignature<DN, B> signFormat(SignFormat format) {
        _signFormat(format);
        notifier.format(format.name());
        return this;
    }

    @Override
    protected SignatureInfo info(String signature) {
        if (format != SignFormat.XAdES) return new SignatureInfo(null, null, null);
        return new XadesSignatureHelper().getInfo(signature);
    }

    protected BaseDigitalSignature<DN, B> _signFormat(SignFormat format) {
        this.format = format;
        return this;
    }

    private DigitalSignatureAutoFirmaSetup setup() {
        DigitalSignatureAutoFirmaSetup result = new DigitalSignatureAutoFirmaSetup();
        result.downloadUrl(server.downloadUrl());
        result.storageUrl(server.storageUrl());
        result.retrieveUrl(server.retrieveUrl());
        return result;
    }

}