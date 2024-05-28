package io.intino.konos.builder.codegeneration.ui.passiveview;

import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.PassiveView;
import io.intino.konos.dsl.PassiveView.Request;

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
		//TODO
	}
}
