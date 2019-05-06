package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.DataComponents.Image;
import io.intino.konos.model.graph.avatar.datacomponents.AvatarImage;
import org.siani.itrules.model.Frame;

public class ImageRenderer extends SizedRenderer<Image> {

	public ImageRenderer(Settings settings, Image component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addAvatarProperties(result);
		if (element.value() != null && !element.value().isEmpty()) result.addSlot("value", resourceMethodFrame("value", element.value()));
		if (element.defaultValue() != null && !element.defaultValue().isEmpty()) result.addSlot("defaultValue", resourceMethodFrame("defaultValue", element.defaultValue()));
		return result;
	}

	private void addAvatarProperties(Frame frame) {
		if (!element.isAvatar()) return;
		AvatarImage avatar = element.asAvatar();
		frame.addTypes("avatar");
		frame.addSlot("text", avatar.text());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("image", "");
	}
}
