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

    @Override
    public void didMount() {
        super.didMount();
        notifier.refresh(value);
    }

    @Override
    public void update() {
        super.update();
        notifier.refresh(value);
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

    public BaseText<DN, B> highlight(String textColor, String backgroundColor) {
        _textColor(textColor);
        _backgroundColor(backgroundColor);
        return _refreshHighlight();
    }

    public BaseText<DN, B> highlight(String color) {
        return highlight(null, color);
    }

    public BaseText<DN, B> clearHighlight() {
        return highlight(null, null);
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
        notifier.refreshHighlight(new Highlight().textColor(textColor).backgroundColor(backgroundColor));
        return this;
    }

}
