package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.ui.passiveview.PassiveViewUpdater;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView.Request;

import java.io.File;


public class DisplayUpdater<D extends Display> extends PassiveViewUpdater<D> {
	private Display display;
	private String packageName;

	protected DisplayUpdater(CompilationContext compilationContext, D display, File file) {
		super(compilationContext, display, file);
		this.display = display;
		this.packageName = compilationContext.packageName();
		this.file = file;
	}

	public void update() {
		element.requestList().forEach(this::addMethod);
	}

	private void addMethod(Request request) {
//		final String methodText = Formatters.customize(new DisplayTemplate()).render(PassiveViewRenderer.frameOf(element, request, settings.packageName()));
//		psiClass.addAfter(factory.createMethodFromText(methodText, psiClass), psiClass.getMethods()[psiClass.getMethods().length - 1]);
		//TODO
	}

	private String nameOf(Request request) {
		return Formatters.firstLowerCase(Formatters.snakeCaseToCamelCase().format(request.name$()).toString());
	}
}
