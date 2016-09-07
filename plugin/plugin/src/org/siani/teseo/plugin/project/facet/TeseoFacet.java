package org.siani.teseo.plugin.project.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;
import org.siani.teseo.plugin.project.facet.maven.MavenManager;

import java.io.IOException;

public class TeseoFacet extends Facet<TeseoFacetConfiguration> {
	static final FacetTypeId<TeseoFacet> ID = new FacetTypeId<>("teseo");
	private static final Logger LOG = Logger.getInstance(TeseoFacet.class.getName());

	TeseoFacet(@NotNull FacetType facetType,
			   @NotNull Module module,
			   @NotNull String name,
			   @NotNull TeseoFacetConfiguration configuration) {
		super(facetType, module, name, configuration, null);
		importTeseoLibrary(module);
		if (module.isLoaded()) {
			ApplicationManager.getApplication().assertIsDispatchThread();
			ApplicationManager.getApplication().runWriteAction(() -> createApiSourceDirectory(module));
		}
	}

	private void importTeseoLibrary(Module module) {
		final MavenManager mavenManager = new MavenManager(module);
		mavenManager.addTeseoServer();
		mavenManager.addTeseoScheduler();
	}

	private void createApiSourceDirectory(Module module) {
		final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
		final ContentEntry contentEntry = modifiableModel.getContentEntries()[0];
		try {
			VirtualFile moduleDir = contentEntry.getFile();
			if (moduleDir == null) return;
			VirtualFile sourceRoot = moduleDir.findChild("api");
			if (sourceRoot == null) sourceRoot = moduleDir.createChildDirectory(null, "api");
			final SourceFolder sourceFolder = contentEntry.addSourceFolder(sourceRoot, JavaSourceRootType.SOURCE, JpsJavaExtensionService.getInstance().createSourceRootProperties(""));
			modifiableModel.commit();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static boolean isOfType(Module aModule) {
		return aModule != null && FacetManager.getInstance(aModule).getFacetByType(ID) != null;
	}
}
