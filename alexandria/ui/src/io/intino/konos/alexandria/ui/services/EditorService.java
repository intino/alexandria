package io.intino.konos.alexandria.ui.services;

import java.io.InputStream;

public interface EditorService {
    InputStream loadDocument(String token);
    void saveDocument(String token, InputStream document, boolean completed);
}
