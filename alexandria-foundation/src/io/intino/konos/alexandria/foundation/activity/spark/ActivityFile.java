package io.intino.konos.alexandria.foundation.activity.spark;

import java.io.InputStream;

public interface ActivityFile {
    String label();
    InputStream content();
}
