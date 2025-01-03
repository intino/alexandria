package io.intino.alexandria.ui.server;

import java.io.InputStream;

public interface UIFile {
    String label();
    InputStream content();

    default boolean embedded() {
        return false;
    }
}
