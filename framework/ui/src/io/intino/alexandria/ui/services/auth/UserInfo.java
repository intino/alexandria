package io.intino.alexandria.ui.services.auth;

import java.net.URL;
import java.util.List;

public interface UserInfo {
    String username();
    String fullName();
    URL photo();
    String email();
    String language();
    List<String> roleList();
}
