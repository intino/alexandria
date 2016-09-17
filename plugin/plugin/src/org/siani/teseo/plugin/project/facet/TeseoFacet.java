package org.siani.teseo.plugin.project.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.siani.teseo.plugin.TeseoFileType;
import org.siani.teseo.plugin.project.facet.maven.MavenManager;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.intellij.project.facet.TaraFacetConfiguration;

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
			TaraFacetConfiguration taraFacet = TaraUtil.getFacetConfiguration(module);
			if (taraFacet != null) taraFacet.addSupportedLanguage(TeseoFileType.instance().getDefaultExtension());
		}
	}

	private void importTeseoLibrary(Module module) {
		final MavenManager mavenManager = new MavenManager(module);
		mavenManager.addTeseoServer();
		mavenManager.addTeseoScheduler();
	}

	public static boolean isOfType(Module aModule) {
		return aModule != null && FacetManager.getInstance(aModule).getFacetByType(ID) != null;
	}
}
