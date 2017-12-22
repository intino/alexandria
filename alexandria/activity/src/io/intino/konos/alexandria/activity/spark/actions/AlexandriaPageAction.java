package io.intino.konos.alexandria.activity.spark.actions;

import cottons.utils.StreamHelper;
import io.intino.konos.alexandria.activity.Asset;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.Browser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;


public abstract class AlexandriaPageAction {
	private final String activityName;

	public ActivitySession session;
	public String clientId;

	private static final String TemplateName = "/www/%s/%s.html";

	public AlexandriaPageAction(String activityName) {
		this.activityName = activityName;
	}

	protected abstract String title();
	protected abstract URL favicon();

	protected String template(String name) {
		try {
			byte[] templateBytes = StreamHelper.readBytes(AlexandriaPageAction.class.getResourceAsStream(format(TemplateName, activityName, name)));
			String result = new String(templateBytes);
			result = addTemplateVariables(result);
			return result;
		} catch (IOException e) {
			return "";
		}
	}

	protected String addTemplateVariables(String template) {
		String sessionId = session.id();
		String language = session.discoverLanguage();
		Browser browser = session.browser();

		template = template.replace("$title", title());
		template = template.replace("$language", language);
		template = template.replace("$currentSession", sessionId);
		template = template.replace("$client", clientId);
		template = template.replace("$baseUrl", browser.baseUrl());
		template = template.replace("$url", browser.baseUrl() + "/" + activityName);
		template = template.replace("$pushUrl", browser.pushUrl(sessionId, clientId, language));

		if (favicon() != null)
			template = template.replace("$favicon", Asset.toResource(baseAssetUrl(), favicon()).toUrl().toString());

		return template;
	}

	private URL baseAssetUrl() {
		try {
			return new URL(session.browser().baseAssetUrl());
		} catch (MalformedURLException e) {
			return null;
		}
	}

}