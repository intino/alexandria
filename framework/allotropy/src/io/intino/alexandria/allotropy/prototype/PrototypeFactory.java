package io.intino.alexandria.allotropy.prototype;

import io.intino.alexandria.allotropy.Prototype;

import java.io.File;

public interface PrototypeFactory {
    Prototype build(File file);
}
