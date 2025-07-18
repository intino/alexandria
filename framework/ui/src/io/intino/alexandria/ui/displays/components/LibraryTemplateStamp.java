package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.notifiers.LibraryTemplateStampNotifier;

import java.util.HashMap;

public abstract class LibraryTemplateStamp<DN extends LibraryTemplateStampNotifier, B extends Box> extends AbstractLibraryTemplateStamp<B> {
	private String template;
	private final java.util.Map<String, String> parameters = new HashMap<>();

	public LibraryTemplateStamp(B box) {
		super(box);
	}

	public String template() {
		return template;
	}

	public LibraryTemplateStamp<DN, B> template(String value) {
		return _template(value);
	}

	public LibraryTemplateStamp<DN, B> add(String name, String value) {
		this.parameters.put(name, value);
		return this;
	}

	protected LibraryTemplateStamp<DN, B> _template(String value) {
		this.template = value;
		return this;
	}

	public boolean existsTemplate(String name) {
		return name != null;
	}

	public boolean existsTemplate() {
		return existsTemplate(template);
	}

	@Override
	public void refresh() {
		super.refresh();
		if (template == null) {
			notifyUser(translate("Template type must be defined"), UserMessage.Type.Error);
			return;
		}
		if (!existsTemplate()) {
			notifyUser(translate("Template not found in library"), UserMessage.Type.Error);
			return;
		}
		notifier.refresh(url());
	}

	private static final String Url = "%s/_alexandria-displays/%s?session=%s&client=%s&token=%s";
	private String url() {
		String token = session().client().id();
		StringBuilder result = new StringBuilder();
		result.append(String.format(Url, session().browser().baseUrl(), template.toLowerCase(), session().id(), session().client().id(), token));
		parameters.forEach((key, value) -> result.append("&").append(key).append("=").append(value));
		return result.toString();
	}

}