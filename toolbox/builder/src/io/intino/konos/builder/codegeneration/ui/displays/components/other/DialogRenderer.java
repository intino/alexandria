package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.OtherComponents;
import io.intino.konos.model.graph.OtherComponents.AbstractDialog;
import io.intino.konos.model.graph.OtherComponents.Selector;
import io.intino.konos.model.graph.animated.othercomponents.AnimatedDialog;

public class DialogRenderer extends SizedRenderer<AbstractDialog> {

	public DialogRenderer(Settings settings, AbstractDialog component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder frame = super.frameBuilder();
		frame.add("abstractdialog");
		addBinding(frame);
		return frame;
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("abstractdialog");
		result.add("title", element.title());
		addTransition(result);
		addAlertDialogProperties(result);
		addCollectionDialogProperties(result);
		return result;
	}

	private void addTransition(FrameBuilder builder) {
		if (!element.i$(AnimatedDialog.class)) return;
		AnimatedDialog block = element.a$(AnimatedDialog.class);
		AnimatedDialog.Transition transition = block.transitionList().size() > 0 ? block.transition(0) : null;
		builder.add("mode", block.mode().name());
		builder.add("transitionDirection", transition != null ? transition.direction().name() : "Right");
		builder.add("transitionDuration", transition != null ? transition.duration() : 500);
	}

	private void addAlertDialogProperties(FrameBuilder builder) {
		if (!element.i$(OtherComponents.AlertDialog.class)) return;
		OtherComponents.AlertDialog dialog = element.a$(OtherComponents.AlertDialog.class);
		builder.add("message", dialog.message());
		builder.add("accept", dialog.acceptLabel());
		builder.add("cancel", dialog.cancelLabel());
	}

	private void addCollectionDialogProperties(FrameBuilder builder) {
		if (!element.i$(OtherComponents.CollectionDialog.class)) return;
		OtherComponents.CollectionDialog dialog = element.a$(OtherComponents.CollectionDialog.class);
		builder.add("allowSearch", dialog.allowSearch());
	}

	private void addBinding(FrameBuilder builder) {
		addDecisionDialogBinding(builder);
		addCollectionDialogBinding(builder);
	}

	private void addDecisionDialogBinding(FrameBuilder builder) {
		if (!element.i$(OtherComponents.DecisionDialog.class)) return;
		OtherComponents.DecisionDialog dialog = element.a$(OtherComponents.DecisionDialog.class);

		Selector selector = dialog.selector();
		builder.add("binding", new FrameBuilder("binding", "decisiondialog")
			   .add("name", nameOf(element))
			   .add("selector", nameOf(selector)));
	}

	private void addCollectionDialogBinding(FrameBuilder builder) {
		if (!element.i$(OtherComponents.CollectionDialog.class)) return;
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
