package io.intino.pandora.plugin.actions;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import io.intino.pandora.plugin.file.PandoraFileType;
import tara.intellij.lang.psi.TaraModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.jetbrains.jps.model.java.JavaResourceRootType.RESOURCE;
import static org.jetbrains.jps.model.java.JavaResourceRootType.TEST_RESOURCE;

class PandoraUtils {
	private static final String PANDORA = "pandora";
	private static final String STASH = ".stash";

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

	private static VirtualFile getResourcesRoot(Module module, boolean test) {
		if (module == null) return null;
		final List<VirtualFile> roots = ModuleRootManager.getInstance(module).getSourceRoots(test ? TEST_RESOURCE : RESOURCE);
		if (roots.isEmpty()) return null;
		return roots.stream().filter(r -> r.getName().equals(test ? "test-res" : "res")).findAny().orElse(null);
	}

	public static List<PsiFile> findPandoraFiles(Module module) {
		List<PsiFile> pandoraFiles = new ArrayList<>();
		if (module == null) return pandoraFiles;
		Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PandoraFileType.instance(), GlobalSearchScope.moduleScope(module));
		files.stream().filter(file -> file != null).forEach(file -> {
			TaraModel pandoraFile = (TaraModel) PsiManager.getInstance(module.getProject()).findFile(file);
			if (pandoraFile != null) pandoraFiles.add(pandoraFile);
		});
		return pandoraFiles;
	}
}
