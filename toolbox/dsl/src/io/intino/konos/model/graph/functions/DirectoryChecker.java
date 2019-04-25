package io.intino.konos.model.graph.functions;

import java.io.File;

@FunctionalInterface
public interface DirectoryChecker {

	boolean check(File directory);
}
