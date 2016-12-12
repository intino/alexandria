package io.intino.pandora.server.activity.spark;

import java.io.InputStream;

public interface ActivityFile {
    String label();
    InputStream content();
}
