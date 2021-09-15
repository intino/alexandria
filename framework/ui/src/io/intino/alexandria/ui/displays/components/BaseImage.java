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
    private String filename;
    private String mimeType;

    public BaseImage(B box) {
        super(box);
    }

    public URL value() {
        return value;
    }

    public void value(URL value) {
        value(value, null);
    }

    public void value(URL value, String mimeType) {
        _value(value, mimeType != null ? mimeType : typeOf(value), null);
        refresh();
    }

    public void value(URL value, String mimeType, String filename) {
        _value(value, mimeType != null ? mimeType : typeOf(value), filename);
        refresh();
    }

    public void value(java.io.File file) {
        _value(file);
        refresh();
    }

    public void value(io.intino.alexandria.ui.File file) {
        _value(file);
        refresh();
    }

    public String filename() {
        return filename;
    }

    public String mimeType() {
        return mimeType;
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
        return file != null ? _value(file.value(), file.mimeType(), file.filename()) : _value(null, null, null);
    }

    protected BaseImage<DN, B> _value(URL value) {
        return _value(value, typeOf(value), null);
    }

    protected BaseImage<DN, B> _value(URL value, String mimeType, String filename) {
        this.value = value;
        this.mimeType = mimeType;
        this.filename = filename;
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