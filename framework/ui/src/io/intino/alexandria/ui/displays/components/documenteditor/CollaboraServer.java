package io.intino.alexandria.ui.displays.components.documenteditor;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.spark.UISparkManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollaboraServer {
	private final AlexandriaUiBox box;

	private static final Set<String> AccessTokens = new HashSet<>();
	private static final Map<String, DocumentManager> DocumentManagers = new HashMap<>();
	private static boolean Ready = false;

	private static final String GetPattern = "/collaboraserver/document/:documentId";
	private static final String LoadPattern = "/collaboraserver/document/:documentId/contents";
	private static final String SavePattern = "/collaboraserver/document/:documentId/contents";

	public CollaboraServer(AlexandriaUiBox box) {
		this.box = box;
	}

	public void documentManager(String accessToken, DocumentManager documentManager) {
		DocumentManagers.put(accessToken, documentManager);
	}

	public void register(String accessToken) {
		AccessTokens.add(accessToken);
	}

	public void unregister(String accessToken) {
		AccessTokens.remove(accessToken);
		DocumentManagers.remove(accessToken);
	}

	public CollaboraServer listen() {
		if (listening()) return this;
		DisplayRouteManager routeManager = box.routeManager();
		routeManager.get(GetPattern, this::get);
		routeManager.get(LoadPattern, this::load);
		routeManager.post(SavePattern, this::save);
		Ready = true;
		return this;
	}

	public String url(String baseUrl, String documentId) {
		return baseUrl + GetPattern.replace(":documentId", documentId);
	}

	private boolean listening() {
		return Ready;
	}

	private static final String InfoTemplate = "{\"BaseFileName\":\"%s\",\"OwnerId\":\"me\",\"Size\":%d,\"UserId\":\"%s\",\"Version\":\"1\"," +
											   "\"UserCanWrite\":%b,\"ReadOnly\":%b,\"SupportsLocks\":false,\"SupportsUpdate\":%b," +
										  	   "\"UserCanNotWriteRelative\":true,\"UserFriendlyName\":\"%s\"}";
	private void get(UISparkManager manager) {
		if (!canAccess(manager)) return;
		String accessToken = manager.fromQuery("access_token");
		if (!DocumentManagers.containsKey(accessToken)) return;
		String documentId = manager.fromPath("documentId");
		DocumentManager.DocumentInfo info = DocumentManagers.get(accessToken).info(documentId);
		if (info == null) return;
		manager.write(String.format(InfoTemplate, clean(info.id()), 0, clean(info.author()), !info.readonly(), info.readonly(), !info.readonly(), clean(info.name())));
	}

	private String clean(String value) {
		return value != null ? value.replace("\"", "\\\"") : value;
	}

	private void load(UISparkManager manager) {
		if (!canAccess(manager)) return;
		String accessToken = manager.fromQuery("access_token");
		if (!DocumentManagers.containsKey(accessToken)) return;
		String documentId = manager.fromPath("documentId");
		InputStream content = DocumentManagers.get(accessToken).load(documentId);
		manager.write(content, documentId);
	}

	private void save(UISparkManager manager) {
		if (!canAccess(manager)) return;
		String accessToken = manager.fromQuery("access_token");
		if (!DocumentManagers.containsKey(accessToken)) return;
		String documentId = manager.fromPath("documentId");
		byte[] bytes = manager.fromBodyAsBytes();
		DocumentManagers.get(accessToken).save(documentId, new ByteArrayInputStream(bytes));
		manager.write("OK");
	}

	private boolean canAccess(UISparkManager manager) {
		String token = manager.fromQuery("access_token");
		if (!DocumentManagers.containsKey(token)) return false;
		return AccessTokens.contains(token);
	}

}
