package io.intino.konos.alexandria.activity.spark;

import java.io.InputStream;

public interface ActivityFile {
    String label();
    InputStream content();

    default boolean embedded() {
        return false;
    }
}
