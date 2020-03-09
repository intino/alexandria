package io.intino.konos.builder.codegeneration.ui.passiveview;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.templates.DisplayTemplate;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.PassiveView.Request;

import java.io.File;

public class PassiveViewUpdater<C extends PassiveView> extends Updater {
	protected C element;

	public PassiveViewUpdater(CompilationContext compilationContext, C element, File file) {
		super(compilationContext, file);
		this.element = element;
	}

	public void update() {
		for (Request request : element.requestList())
			addMethod(request);
	}

	private void addMethod(Request request) {
		final String methodText = Formatters.customize(new DisplayTemplate()).render(PassiveViewRenderer.frameOf(element, request, compilationContext.packageName()));
		//TODO
	}


	private String nameOf(Request request) {
		return Formatters.firstLowerCase(Formatters.snakeCaseToCamelCase().format(request.name$()).toString());
	}
}
