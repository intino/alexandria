package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Image;
import io.intino.konos.model.graph.avatar.childcomponents.AvatarImage;
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
		if (element.default$() != null && !element.default$().isEmpty()) result.addSlot("defaultValue", resourceMethodFrame("defaultImage", element.default$()));
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
