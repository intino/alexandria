package io.intino.alexandria.ui.displays;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.TemplateNotifier;

public class Template<DN extends TemplateNotifier, B extends Box> extends Display<DN, B> {

    public Template(B box) {
        super(box);
    }

}