package io.intino.konos.alexandria.ui.spark;

import java.io.InputStream;

public interface UIFile {
    String label();
    InputStream content();

    default boolean embedded() {
        return false;
    }
}
