package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.File;
import io.intino.alexandria.ui.displays.notifiers.BaseImageNotifier;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseImage<DN extends BaseImageNotifier, B extends Box> extends AbstractBaseImage<DN, B> {
    private URL value;
    private String mimeType;

    public BaseImage(B box) {
        super(box);
    }

    public URL value() {
        return value;
    }

    public void value(URL value) {
        _value(value);
        refresh();
    }

    public void value(URL value, String mimeType) {
        _value(value, mimeType != null ? mimeType : typeOf(value));
        refresh();
    }

    public void value(java.io.File file) {
        _value(file);
        refresh();
    }

    public void value(File file) {
        _value(file);
        refresh();
    }

    protected BaseImage<DN, B> _value(java.io.File file) {
        try {
            return _value(value != null ? file.toURI().toURL() : null);
        } catch (MalformedURLException e) {
            Logger.error(e);
            return this;
        }
    }

    protected BaseImage<DN, B> _value(File file) {
        return file != null ? _value(file.value(), file.mimeType()) : _value(null, null);
    }

    protected BaseImage<DN, B> _value(URL value) {
        return _value(value, typeOf(value));
    }

    protected BaseImage<DN, B> _value(URL value, String mimeType) {
        this.value = value;
        this.mimeType = mimeType;
        return this;
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    public void refresh() {
        notifier.refresh(serializedValue());
    }

    protected String typeOf(URL value) {
        return value != null ? MimeTypes.contentTypeOf(value) : null;
    }

    String serializedValue() {
        return null;
    }

}