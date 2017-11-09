package io.intino.konos.alexandria.activity.box.spark;

import java.io.InputStream;

public interface ActivityFile {
    String label();
    InputStream content();
}
