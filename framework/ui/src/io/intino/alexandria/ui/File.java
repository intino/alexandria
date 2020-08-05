package io.intino.alexandria.ui;

import java.net.URL;

public class File {
    private URL value;
    private String mimeType;

    public URL value() {
        return value;
    }

    public File value(URL value) {
        this.value = value;
        return this;
    }

    public String mimeType() {
        return mimeType;
    }

    public File mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }
}
