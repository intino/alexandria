package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.FileInfo;
import io.intino.alexandria.ui.displays.notifiers.BaseFileNotifier;
import io.intino.alexandria.ui.resources.Asset;

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

    protected BaseFile<DN, B> _value(URL value) {
        this.value = value;
        this.mimeType = typeOf(value);
        return this;
    }

    protected FileInfo info() {
        FileInfo info = new FileInfo().value(serializedValue()).mimeType(mimeType);
        if (value != null) info.filename(value.toString());
        return info;
    }

    private String serializedValue() {
        return value != null ? Asset.toResource(baseAssetUrl(), value).toUrl().toString() : null;
    }

    private String typeOf(URL value) {
        return value != null ? MimeTypes.contentTypeOf(value) : null;
    }
}