package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SignTextNotifier;

import java.nio.charset.StandardCharsets;

public class SignText<DN extends SignTextNotifier, B extends Box> extends AbstractSignText<DN, B> {
    private String signData;

    public SignText(B box) {
        super(box);
    }

    public void text(String content) {
        signData = signMode() == SignMode.CounterSign ? content : signData(content);
    }

    private String signData(String content) {
        return Base64.encode(content.getBytes(StandardCharsets.UTF_8));
    }

    public void execute() {
        sign(signData);
    }
}