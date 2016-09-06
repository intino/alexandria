package org.siani.teseo.plugin.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.CompilerModuleExtension;
import tara.io.StashDeserializer;
import tara.magritte.Graph;
import teseo.TeseoApplication;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");


	static Graph loadGraph(Module module, File teseoFile) {
		if (!teseoFile.exists()) return null;
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		final ClassLoader temporalLoader = createClassLoader(new File(CompilerModuleExtension.getInstance(module).getCompilerOutputPath().getPath()));
		Thread.currentThread().setContextClassLoader(temporalLoader);
		final Graph graph = Graph.from(StashDeserializer.stashFrom(teseoFile)).wrap(TeseoApplication.class);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph;
	}

	private static ClassLoader createClassLoader(File directory) {
		return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
			try {
				return new URLClassLoader(new URL[]{directory.toURI().toURL()}, GraphLoader.class.getClassLoader());
			} catch (MalformedURLException e) {
				LOG.error(e.getMessage(), e);
				return null;
			}
		});
	}
}
