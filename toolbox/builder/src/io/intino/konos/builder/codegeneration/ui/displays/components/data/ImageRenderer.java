package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.DataComponents.Image;

public class ImageRenderer extends SizedRenderer<Image> {

	public ImageRenderer(CompilationContext compilationContext, Image component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addAvatarProperties(result);
		if (element.value() != null && !element.value().isEmpty()) result.add("value", resourceMethodFrame("value", element.value()));
		if (element.defaultValue() != null && !element.defaultValue().isEmpty()) result.add("defaultValue", resourceMethodFrame("defaultValue", element.defaultValue()));
		if (element.mobileReduceFactor() != 0) result.add("mobileReduceFactor", element.mobileReduceFactor());
		return result;
	}

	private void addAvatarProperties(FrameBuilder frame) {
		if (!element.isAvatar()) return;
		Image.Avatar avatar = element.asAvatar();
		frame.add("avatar");
		frame.add("text", avatar.text());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("image", "");
	}
}
