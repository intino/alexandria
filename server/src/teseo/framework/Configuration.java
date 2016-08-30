package teseo.framework;

import java.io.File;
import java.net.URL;

public interface Configuration {
    String appName();
    int port();

    String title();
    String subtitle();
    File logo();
    URL logoUrl();

    boolean openInBrowser();
}
