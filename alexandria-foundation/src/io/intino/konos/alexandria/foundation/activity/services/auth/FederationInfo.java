package io.intino.konos.alexandria.foundation.activity.services.auth;

import java.net.URI;
import java.net.URL;

public interface FederationInfo {
    String name();
    String title();
    String subtitle();
    URL logo();
    URI pushServerUri();
}
