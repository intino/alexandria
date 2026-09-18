package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Highlight;
import io.intino.alexandria.ui.displays.notifiers.BaseDateNotifier;

public class BaseDate<DN extends BaseDateNotifier, B extends Box> extends AbstractBaseDate<DN, B> {

    public BaseDate(B box) {
        super(box);
    }

    public BaseDate<DN, B> highlight(String textColor, String backgroundColor) {
        notifier.refreshHighlight(new Highlight().textColor(textColor).backgroundColor(backgroundColor));
        return this;
    }

    public BaseDate<DN, B> highlight(String color) {
        return highlight(null, color);
    }

    public BaseDate<DN, B> clearHighlight() {
        return highlight(null, null);
    }

}
