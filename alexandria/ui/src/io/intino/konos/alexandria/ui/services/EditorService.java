package io.intino.konos.alexandria.ui.services;

import java.io.InputStream;

public interface EditorService {
    InputStream loadDocument(String id);
    void saveDocument(String id, InputStream document, boolean completed);
}
