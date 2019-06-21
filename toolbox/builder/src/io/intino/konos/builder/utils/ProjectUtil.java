package io.intino.konos.builder.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;

import java.io.File;

public class ProjectUtil {

	public static String moduleOutDirectory(Module module) {
		try {
			if (module == null) return "";
			CompilerModuleExtension extension = CompilerModuleExtension.getInstance(module);
			String compilerOutputUrl = extension.getCompilerOutputUrl();
			if (compilerOutputUrl == null) return "";
			String url = compilerOutputUrl.replace("file://", "").replace("file:", "");
			return new File(url).getCanonicalPath();
		} catch (Exception e) {
			Logger.getInstance(ProjectUtil.class).error(e.getMessage(), e);
			return "";
		}
	}

	public static boolean isAlexandria(Project project) {
		return project != null && project.getName().equalsIgnoreCase("alexandria");
	}
}
