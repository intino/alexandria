package org.siani.teseo.plugin.project.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

public class TeseoFacetConfiguration implements FacetConfiguration, PersistentStateComponent<TeseoFacetConfigurationProperties> {

	private TeseoFacetConfigurationProperties properties = new TeseoFacetConfigurationProperties();

	public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
		return new FacetEditorTab[]{
		};
	}

	public void readExternal(Element element) throws InvalidDataException {
	}

	public void writeExternal(Element element) throws WriteExternalException {
	}

	public TeseoFacetConfigurationProperties getProperties() {
		return properties;
	}

	public TeseoFacetConfigurationProperties getState() {
		return properties;
	}

	public void loadState(TeseoFacetConfigurationProperties state) {
		properties = state;
	}

}
