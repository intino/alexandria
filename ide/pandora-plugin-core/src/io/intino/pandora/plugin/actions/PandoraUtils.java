package io.intino.pandora.plugin.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;

import java.util.List;

import static org.jetbrains.jps.model.java.JavaResourceRootType.RESOURCE;
import static org.jetbrains.jps.model.java.JavaResourceRootType.TEST_RESOURCE;

class PandoraUtils {
	private static final String PANDORA = "pandora";
	static final String STASH = ".stash";

	static String findPandora(Module module) {
		final VirtualFile resourcesRoot = getResourcesRoot(module, false);
		if (resourcesRoot == null) return null;
		for (VirtualFile child : resourcesRoot.getChildren())
			if (PANDORA.equalsIgnoreCase(child.getExtension())) return child.getPath();
		for (VirtualFile child : resourcesRoot.getChildren())
			if (child.getName().toLowerCase().endsWith("-pandora" + STASH) || child.getName().equalsIgnoreCase(PANDORA + STASH))
				return child.getPath();
		return null;
	}

	static String findOutLanguage(Module module) {
		final Configuration facetConfiguration = TaraUtil.configurationOf(module);
		if (facetConfiguration == null) return null;
		final VirtualFile resourcesRoot = getResourcesRoot(module, false);
		if (resourcesRoot == null) return null;
		for (VirtualFile child : resourcesRoot.getChildren())
			if (child.getName().toLowerCase().endsWith("-pandora" + STASH))
				return child.getNameWithoutExtension().replace("-pandora", "").replace("-Pandora", "");
			else if (PANDORA.equalsIgnoreCase(child.getExtension()))
				return child.getNameWithoutExtension();
		return "";
	}

	private static VirtualFile getResourcesRoot(Module module, boolean test) {
		if (module == null) return null;
		final List<VirtualFile> roots = ModuleRootManager.getInstance(module).getSourceRoots(test ? TEST_RESOURCE : RESOURCE);
		if (roots.isEmpty()) return null;
		return roots.stream().filter(r -> r.getName().equals(test ? "test-res" : "res")).findAny().orElse(null);
	}
}
