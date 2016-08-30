package org.siani.teseo.plugin.project.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.siani.teseo.plugin.TeseoIcons;

import javax.swing.*;

public class TeseoFacetType extends FacetType<TeseoFacet, TeseoFacetConfiguration> {
	private static final String STRING_ID = "teseo-tara";


	public TeseoFacetType() {
		super(TeseoFacet.ID, STRING_ID, "Teseo");
	}

	public TeseoFacetConfiguration createDefaultConfiguration() {
		return new TeseoFacetConfiguration();
	}

	public TeseoFacet createFacet(@NotNull Module module,
								  String name,
								  @NotNull TeseoFacetConfiguration configuration,
								  @Nullable Facet underlyingFacet) {
		return new TeseoFacet(this, module, name, configuration);
	}

	public boolean isSuitableModuleType(ModuleType moduleType) {
		return moduleType instanceof JavaModuleType;
	}

	@NotNull
	@Override
	public String getDefaultFacetName() {
		return "Teseo";
	}

	@Override
	public Icon getIcon() {
		return TeseoIcons.ICON_16;
	}

}
