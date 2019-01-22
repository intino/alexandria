package io.intino.alexandria.ui.spark.pages;

import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.services.push.Browser;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.utils.StreamUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;


public abstract class AlexandriaPage {
	private final String uiServiceName;

	public UISession session;
	public String clientId;
	public String googleApiKey;
	public String device;
	public String token;

	private static final String TemplateName = "/www/%s/%s.html";

	public AlexandriaPage(String uiServiceName) {
		this.uiServiceName = uiServiceName;
	}

	protected abstract String title();
	protected abstract URL favicon();

	protected String template(String name) {
		return template(name, Collections.emptyList());
	}

	protected String template(String name, List<String> usedAppsUrls) {
		try {
			byte[] templateBytes = StreamUtil.readBytes(AlexandriaPage.class.getResourceAsStream(format(TemplateName, uiServiceName, name)));
			String result = new String(templateBytes);
			result = addTemplateVariables(result, usedAppsUrls);
			return result;
		} catch (IOException e) {
			return "";
		}
	}

	protected String addTemplateVariables(String template, List<String> usedAppsUrls) {
		String sessionId = session.id();
		String language = session.discoverLanguage();
		Browser browser = session.browser();

		template = template.replace("$title", title() != null ? title() : "");
		template = template.replace("$language", language != null ? language : "");
		template = template.replace("$currentSession", sessionId);
		template = template.replace("$client", clientId);
		template = template.replace("$baseUrl", browser.baseUrl());
		template = template.replace("$basePath", browser.basePath());
		template = template.replace("$url", browser.baseUrl() + "/" + uiServiceName);
		template = template.replace("$pushUrls", String.join(",", pushUrls(usedAppsUrls, sessionId, language, browser)));
		template = template.replace("$googleApiKey", googleApiKey != null ? googleApiKey : "");
		template = template.replace("$favicon", favicon() != null ? Asset.toResource(baseAssetUrl(), favicon()).toUrl().toString() : "");

		return template;
	}

	private List<String> pushUrls(List<String> usedAppsUrls, String sessionId, String language, Browser browser) {
		List<String> pushList = usedAppsUrls.stream().filter(appUrl -> appUrl != null && !appUrl.isEmpty())
											.map(appUrl-> browser.pushUrl(sessionId, clientId, language, appUrl))
											.collect(Collectors.toList());
		pushList.add(browser.pushUrl(sessionId, clientId, language));
		return pushList;
	}

	private URL baseAssetUrl() {
		try {
			return new URL(session.browser().baseAssetUrl());
		} catch (MalformedURLException e) {
			return null;
		}
	}

}