package io.intino.pandora.server.activity.spark;

import java.net.URL;

public interface AssetLoader {
    URL asset(String name);
}
