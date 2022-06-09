package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.TemplateStampNotifier;

public class TemplateStamp<DN extends TemplateStampNotifier, B extends Box> extends AbstractTemplateStamp<B> {

    public TemplateStamp(B box) {
        super(box);
    }

}