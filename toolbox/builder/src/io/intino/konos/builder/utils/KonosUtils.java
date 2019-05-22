package io.intino.konos.builder.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import io.intino.konos.builder.file.KonosFileType;
import io.intino.tara.plugin.lang.psi.TaraModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KonosUtils {

	public static List<PsiFile> findKonosFiles(Module module) {
		if (module == null) return Collections.emptyList();
		final Application application = ApplicationManager.getApplication();
		return application.isReadAccessAllowed() ?
				konosFiles(module) :
				application.runReadAction((Computable<List<PsiFile>>) () -> konosFiles(module));
	}

	private static List<PsiFile> konosFiles(Module module) {
		List<PsiFile> konosFiles = new ArrayList<>();
		Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(ID.create("filetypes"), KonosFileType.instance(), GlobalSearchScope.moduleScope(module));
		files.stream().filter(o -> o != null && !o.getCanonicalFile().getName().contains("Misc")).forEach(file -> {
			TaraModel konosFile = (TaraModel) PsiManager.getInstance(module.getProject()).findFile(file);
			if (konosFile != null) konosFiles.add(konosFile);
		});
		return konosFiles;
	}
}
