package io.intino.pandora.plugin.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import io.intino.pandora.plugin.file.PandoraFileType;
import tara.intellij.lang.psi.TaraModel;

import java.util.*;

public class PandoraUtils {

	public static List<PsiFile> findPandoraFiles(Module module) {
		if (module == null) return Collections.emptyList();
		final Application application = ApplicationManager.getApplication();
		return application.isReadAccessAllowed() ?
				pandoraFiles(module) :
				application.runReadAction((Computable<List<PsiFile>>) () -> pandoraFiles(module));
	}

	private static List<PsiFile> pandoraFiles(Module module) {
		List<PsiFile> pandoraFiles = new ArrayList<>();
		Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PandoraFileType.instance(), GlobalSearchScope.moduleScope(module));
		files.stream().filter(Objects::nonNull).forEach(file -> {
			TaraModel pandoraFile = (TaraModel) PsiManager.getInstance(module.getProject()).findFile(file);
			if (pandoraFile != null) pandoraFiles.add(pandoraFile);
		});
		return pandoraFiles;
	}
}
