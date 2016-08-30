package org.siani.teseo.plugin.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.intellij.project.facet.TaraFacetConfiguration;

import java.util.List;

import static org.jetbrains.jps.model.java.JavaResourceRootType.RESOURCE;
import static org.jetbrains.jps.model.java.JavaResourceRootType.TEST_RESOURCE;

class ForrestUtils {
	private static final String FORREST = "forrest";
	static final String STASH = ".stash";

	static String findForrest(Module module) {
		final VirtualFile resourcesRoot = getResourcesRoot(module, false);
		if (resourcesRoot == null) return null;
		for (VirtualFile children : resourcesRoot.getChildren())
			if (children.getName().toLowerCase().endsWith("-forrest" + STASH) || children.getName().equalsIgnoreCase(FORREST + STASH))
				return children.getPath();
		for (VirtualFile children : resourcesRoot.getChildren())
			if (FORREST.equalsIgnoreCase(children.getExtension())) return children.getPath();
		return null;
	}

	static String findOutLanguage(Module module) {
		final TaraFacetConfiguration facetConfiguration = TaraUtil.getFacetConfiguration(module);
		if (facetConfiguration == null) return null;
		final VirtualFile resourcesRoot = getResourcesRoot(module, false);
		if (resourcesRoot == null) return null;
		for (VirtualFile child : resourcesRoot.getChildren())
			if (child.getName().toLowerCase().endsWith("-forrest" + STASH))
				return child.getNameWithoutExtension().replace("-forrest", "").replace("-Forrest", "");
			else if (FORREST.equalsIgnoreCase(child.getExtension()))
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
