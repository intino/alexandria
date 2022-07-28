package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.DigitalSignatureFailure;
import io.intino.alexandria.schemas.DigitalSignatureSuccess;
import io.intino.alexandria.ui.displays.events.SignErrorEvent;
import io.intino.alexandria.ui.displays.events.SignErrorListener;
import io.intino.alexandria.ui.displays.events.SignEvent;
import io.intino.alexandria.ui.displays.events.SignListener;
import io.intino.alexandria.ui.displays.notifiers.BaseDigitalSignatureNotifier;
import io.intino.icod.core.SignatureInfo;

import java.nio.charset.StandardCharsets;

public abstract class BaseDigitalSignature<DN extends BaseDigitalSignatureNotifier, B extends Box> extends AbstractBaseDigitalSignature<DN, B> {
    private SignListener signListener;
    private SignErrorListener errorListener;
    private String text;
    public boolean readonly = false;

    public BaseDigitalSignature(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        notifier.refreshReadonly(readonly);
    }

    @Override
    public void refresh() {
        super.refresh();
        if (text != null) notifier.text(text);
    }

    public BaseDigitalSignature<DN, B> readonly(boolean readonly) {
        this.readonly = readonly;
        notifier.refreshReadonly(readonly);
        return this;
    }

    public void text(String content) {
        text = Base64.encode(content.getBytes(StandardCharsets.UTF_8));
        notifier.text(text);
    }

    public void sign() {
        notifier.sign();
    }

    public void success(DigitalSignatureSuccess success) {
        signListener.accept(new SignEvent(this, success.signature(), success.certificate(), info(success.signature())));
    }

    public void failure(DigitalSignatureFailure failure) {
        errorListener.accept(new SignErrorEvent(this, failure.code(), failure.message()));
    }

    public BaseDigitalSignature<DN, B> onSign(SignListener listener) {
        this.signListener = listener;
        return this;
    }

    public BaseDigitalSignature<DN, B> onError(SignErrorListener listener) {
        this.errorListener = listener;
        return this;
    }

    protected abstract SignatureInfo info(String signature);

}