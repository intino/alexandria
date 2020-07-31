package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Highlight;
import io.intino.alexandria.ui.displays.notifiers.BaseTextNotifier;

public class BaseText<DN extends BaseTextNotifier, B extends Box> extends AbstractBaseText<DN, B> {
    private String value;
    private String textColor = null;
    private String backgroundColor = null;

    public BaseText(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public void value(String value) {
        _value(value);
        notifier.refresh(value);
    }

    public void error(String error) {
        notifier.refreshError(error);
    }

    protected BaseText<DN, B> _value(String value) {
        this.value = value;
        return this;
    }

    protected BaseText<DN, B> _textColor(String color) {
        this.textColor = color;
        return this;
    }

    protected BaseText<DN, B> _backgroundColor(String color) {
        this.backgroundColor = color;
        return this;
    }

    protected BaseText<DN, B> _refreshHighlight() {
        if (textColor == null && backgroundColor == null) return this;
        notifier.refreshHighlight(new Highlight().textColor(textColor).backgroundColor(backgroundColor));
        return this;
    }

}