package io.intino.alexandria.ui.services;

import io.intino.alexandria.Resource;

import java.io.InputStream;

public interface EditorService {
    String DocumentParameter = "document";
    String PermissionParameter = "permission";

    InputStream loadDocument(String id);
    void saveDocument(String id, Resource document, boolean completed);

    enum Permission {
        ReadOnly, Editable
    }

    default void saveDocument(String id, InputStream document, boolean completed) {
        Resource documentResource = new Resource(DocumentParameter, document);
        saveDocument(id, documentResource, completed);
    }
}
