package io.intino.alexandria.ui.server.pages;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.services.push.Browser;
import io.intino.alexandria.ui.utils.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public abstract class WebPage extends UiPage {
	private final String uiServiceName;

	private static final String AppSeparator = "_##_";
	private static final String TemplateName = "/www/%s/%s.html";

	protected WebPage(String uiServiceName) {
		this.uiServiceName = uiServiceName;
	}

	protected abstract String title();
	protected abstract URL favicon();

	protected String template(String name) {
		return template(name, Collections.emptyList());
	}

	protected String template(String name, List<Unit> usedUnits) {
		session.browser().origin(Browser.Origin.Web);
		byte[] templateBytes = locateTemplate(name);
		String result = new String(templateBytes);
		result = addTemplateVariables(result, usedUnits);
		return result;
	}

	private byte[] locateTemplate(String name) {
		try {
			byte[] result = StreamUtil.readBytes(Page.class.getResourceAsStream(format(TemplateName, uiServiceName, name)));
			if (result.length == 0) result = locateInWebDirectories(name);
			return result;
		} catch (IOException e) {
			return new byte[0];
		}
	}

	private static final String WebDirectoryName = "%s/%s/%s.html";
	private byte[] locateInWebDirectories(String name) {
		try {
			for (String directory : webDirectories) {
				File file = new File(format(WebDirectoryName, directory, uiServiceName, name));
				if (file.exists()) return Files.readAllBytes(file.toPath());
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return new byte[0];
	}

	protected String addTemplateVariables(String template, List<Unit> usedUnits) {
		String sessionId = session.id();
		String language = session.discoverLanguage();
		Browser browser = session.browser();

		template = template.replaceAll("\\$title", title() != null ? title() : "");
		template = template.replaceAll("\\$language", language != null ? language : "");
		template = template.replaceAll("\\$currentSession", sessionId);
		template = template.replaceAll("\\$client", clientId);
		template = template.replaceAll("\\$baseUrls", String.join(",", baseUrls(usedUnits, browser)));
		template = template.replaceAll("\\$baseUrl", browser.baseUrl());
		template = template.replaceAll("\\$basePath", browser.basePath());
		template = template.replaceAll("\\$url", browser.baseUrl() + "/" + uiServiceName);
		template = template.replaceAll("\\$pushConnections", String.join(",", pushConnections(usedUnits, sessionId, language, browser)));
		template = template.replaceAll("\\$googleApiKey", googleApiKey != null ? googleApiKey : "");
		template = template.replaceAll("\\$favicon", favicon() != null ? Asset.toResource(baseAssetUrl(), favicon()).toUrl().toString() : "");

		return template;
	}

	private List<String> pushConnections(List<Unit> usedUnits, String sessionId, String language, Browser browser) {
		List<String> pushList = usedUnits.stream().filter(unit -> unit != null && !unit.url().isEmpty())
				.map(unit -> unit.name() + AppSeparator + browser.pushUrl(sessionId, clientId, language, unit.url(), unit.socketPath()))
				.collect(Collectors.toList());
		pushList.add("Default" + AppSeparator + browser.pushUrl(sessionId, clientId, language));
		return pushList;
	}

	private List<String> baseUrls(List<Unit> usedUnits, Browser browser) {
		List<String> pushList = usedUnits.stream().filter(unit -> unit != null && !unit.url().isEmpty())
				.map(unit -> unit.name() + AppSeparator + unit.url())
				.collect(Collectors.toList());
		pushList.add("Default" + AppSeparator + browser.baseUrl());
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