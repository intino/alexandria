package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.FileInfo;
import io.intino.alexandria.ui.File;
import io.intino.alexandria.ui.displays.notifiers.BaseFileNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseFile<DN extends BaseFileNotifier, B extends Box> extends AbstractBaseFile<DN, B> {
    private URL value;
    private String mimeType;

    public BaseFile(B box) {
        super(box);
    }

    public URL value() {
        return value;
    }

    public void value(URL value) {
        value(value, null);
    }

    public void value(URL value, String mimeType) {
        _value(value, mimeType != null ? mimeType : typeOf(value));
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

    public String mimeType() {
        return mimeType;
    }

    protected BaseFile<DN, B> _value(java.io.File file) {
        try {
            return _value(value != null ? file.toURI().toURL() : null);
        } catch (MalformedURLException e) {
            Logger.error(e);
            return this;
        }
    }

    protected BaseFile<DN, B> _value(File file) {
        return file != null ? _value(file.value(), file.mimeType()) : _value(null, null);
    }

    protected BaseFile<DN, B> _value(URL value) {
        return _value(value, typeOf(value));
    }

    protected BaseFile<DN, B> _value(URL value, String mimeType) {
        this.value = value;
        this.mimeType = mimeType;
        return this;
    }

    protected FileInfo info() {
        FileInfo info = new FileInfo().value(serializedValue()).mimeType(mimeType);
        if (value != null) info.filename(value.toString());
        return info;
    }

    protected String typeOf(URL value) {
        return value != null ? MimeTypes.contentTypeOf(value) : null;
    }

    private String serializedValue() {
        return value != null ? Asset.toResource(baseAssetUrl(), value).toUrl().toString() : null;
    }

}