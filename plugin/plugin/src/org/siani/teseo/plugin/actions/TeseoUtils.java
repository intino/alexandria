package org.siani.teseo.plugin.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.intellij.project.facet.TaraFacetConfiguration;

import java.util.List;

import static org.jetbrains.jps.model.java.JavaResourceRootType.RESOURCE;
import static org.jetbrains.jps.model.java.JavaResourceRootType.TEST_RESOURCE;

class TeseoUtils {
	private static final String TESEO = "teseo";
	static final String STASH = ".stash";

	static String findTeseo(Module module) {
		final VirtualFile resourcesRoot = getResourcesRoot(module, false);
		if (resourcesRoot == null) return null;
		for (VirtualFile child : resourcesRoot.getChildren())
			if (TESEO.equalsIgnoreCase(child.getExtension())) return child.getPath();
		for (VirtualFile child : resourcesRoot.getChildren())
			if (child.getName().toLowerCase().endsWith("-teseo" + STASH) || child.getName().equalsIgnoreCase(TESEO + STASH))
				return child.getPath();
		return null;
	}

	static String findOutLanguage(Module module) {
		final TaraFacetConfiguration facetConfiguration = TaraUtil.getFacetConfiguration(module);
		if (facetConfiguration == null) return null;
		final VirtualFile resourcesRoot = getResourcesRoot(module, false);
		if (resourcesRoot == null) return null;
		for (VirtualFile child : resourcesRoot.getChildren())
			if (child.getName().toLowerCase().endsWith("-teseo" + STASH))
				return child.getNameWithoutExtension().replace("-teseo", "").replace("-Teseo", "");
			else if (TESEO.equalsIgnoreCase(child.getExtension()))
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
