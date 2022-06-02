package io.intino.konos.model.functions;

import java.io.File;

@FunctionalInterface
public interface DirectoryChecker {

	boolean check(File directory);
}
