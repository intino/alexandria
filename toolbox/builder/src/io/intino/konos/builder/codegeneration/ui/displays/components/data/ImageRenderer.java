package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.DataComponents.Image;

public class ImageRenderer extends SizedRenderer<Image> {

	public ImageRenderer(CompilationContext compilationContext, Image component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addAvatarProperties(result);
		if (element.value() != null && !element.value().isEmpty()) result.add("value", resourceMethodFrame("value", element.value()));
		if (element.defaultValue() != null && !element.defaultValue().isEmpty()) result.add("defaultValue", resourceMethodFrame("defaultValue", element.defaultValue()));
		if (element.mobileReduceFactor() != 0) result.add("mobileReduceFactor", element.mobileReduceFactor());
		if (element.allowFullscreen()) result.add("allowFullScreen", element.allowFullscreen());
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
