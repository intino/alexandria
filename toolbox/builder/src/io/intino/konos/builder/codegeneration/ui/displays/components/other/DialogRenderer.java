package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;
import io.intino.konos.model.OtherComponents;
import io.intino.konos.model.OtherComponents.AbstractDialog;
import io.intino.konos.model.OtherComponents.Dialog;
import io.intino.konos.model.OtherComponents.Selector;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class DialogRenderer extends SizedRenderer<AbstractDialog> {

	public DialogRenderer(CompilationContext compilationContext, AbstractDialog component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		builder.add("abstractdialog");
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("abstractdialog");
		result.add("title", element.title());
		if (element.isFullScreen()) result.add("fullscreen", true);
		if (element.isModal()) result.add("modal", true);
		addTransition(result);
		addAlertDialogProperties(result);
		addCollectionDialogProperties(result);
		return result;
	}

	private void addTransition(FrameBuilder builder) {
		if (!element.i$(conceptOf(Dialog.Animated.class))) return;
		Dialog.Animated block = element.a$(Dialog.Animated.class);
		Dialog.Animated.Transition transition = block.transition();
		builder.add("mode", block.mode().name());
		builder.add("transitionDirection", transition != null ? transition.direction().name() : "Right");
		builder.add("transitionDuration", transition != null ? transition.duration() : 500);
	}

	private void addAlertDialogProperties(FrameBuilder builder) {
		if (!element.i$(conceptOf(OtherComponents.AlertDialog.class))) return;
		OtherComponents.AlertDialog dialog = element.a$(OtherComponents.AlertDialog.class);
		builder.add("message", dialog.message());
		builder.add("closeLabel", dialog.closeLabel());
		if (dialog.acceptLabel() != null) builder.add("acceptLabel", dialog.acceptLabel());
	}

	private void addCollectionDialogProperties(FrameBuilder builder) {
		if (!element.i$(conceptOf(OtherComponents.CollectionDialog.class))) return;
		OtherComponents.CollectionDialog dialog = element.a$(OtherComponents.CollectionDialog.class);
		builder.add("allowSearch", dialog.allowSearch());
	}

	private void addBinding(FrameBuilder builder) {
		addDecisionDialogBinding(builder);
		addCollectionDialogBinding(builder);
	}

	private void addDecisionDialogBinding(FrameBuilder builder) {
		if (!element.i$(conceptOf(OtherComponents.DecisionDialog.class))) return;
		OtherComponents.DecisionDialog dialog = element.a$(OtherComponents.DecisionDialog.class);

		Selector selector = dialog.selector();
		builder.add("binding", new FrameBuilder("binding", "decisiondialog")
			   .add("name", nameOf(element))
			   .add("selector", nameOf(selector)));
	}

	private void addCollectionDialogBinding(FrameBuilder builder) {
		if (!element.i$(conceptOf(OtherComponents.CollectionDialog.class))) return;
		OtherComponents.CollectionDialog dialog = element.a$(OtherComponents.CollectionDialog.class);

		CatalogComponents.Collection collection = dialog.collection();
		builder.add("binding", new FrameBuilder("binding", "collectiondialog")
   			   .add("name", nameOf(element))
			   .add("collection", nameOf(collection)));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("dialog", "");
	}
}
