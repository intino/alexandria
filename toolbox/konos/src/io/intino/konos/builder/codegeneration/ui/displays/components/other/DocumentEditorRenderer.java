package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.DocumentEditor;

public class DocumentEditorRenderer extends SizedRenderer<DocumentEditor> {

	public DocumentEditorRenderer(CompilationContext compilationContext, DocumentEditor component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.document() != null) result.add("document", element.document());
		addCollaboraFacet(result);
		return result;
	}

	private void addCollaboraFacet(FrameBuilder builder) {
		if (!element.isCollabora()) return;
		builder.add("collabora");
		DocumentEditor.Collabora collabora = element.asCollabora();
		builder.add("editorUrl", collabora.editorUrl());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("dashboard", "");
	}
}
