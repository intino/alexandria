package io.intino.alexandria.ui.displays.components.documenteditor;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.UISparkManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class CollaboraServer {
	private final AlexandriaUiBox box;
	private final UISession session;
	private DocumentManager documentManager;

	private static boolean ready = false;

	private static final String GetPattern = "/collaboraserver/document/:documentId";
	private static final String LoadPattern = "/collaboraserver/document/:documentId/contents";
	private static final String SavePattern = "/collaboraserver/document/:documentId/contents";

	public CollaboraServer(AlexandriaUiBox box, UISession session) {
		this.box = box;
		this.session = session;
	}

	public CollaboraServer documentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
		return this;
	}

	public CollaboraServer listen() {
		if (listening()) return this;
		DisplayRouteManager routeManager = box.routeManager();
		routeManager.get(GetPattern, this::get);
		routeManager.get(LoadPattern, this::load);
		routeManager.post(SavePattern, this::save);
		ready = true;
		return this;
	}

	public String url(String documentId) {
		return session.browser().baseUrl() + GetPattern.replace(":documentId", documentId);
	}

	private boolean listening() {
		return ready;
	}

	private static final String InfoTemplate = "{\"BaseFileName\":\"%s\",\"OwnerId\":\"me\",\"Size\":%d,\"UserId\":\"%s\",\"Version\":\"1\"," +
											   "\"UserCanWrite\":true,\"ReadOnly\":false,\"SupportsLocks\":false,\"SupportsUpdate\":true," +
										  	   "\"UserCanNotWriteRelative\":true,\"UserFriendlyName\":\"%s\"}";
	private void get(UISparkManager manager) {
		if (!canAccess(manager)) return;
		String documentId = manager.fromPath("documentId");
		DocumentManager.DocumentInfo info = documentManager.info(documentId);
		if (info == null) return;
		manager.write(String.format(InfoTemplate, info.id(), 0, info.author(), info.name()));
	}

	private void load(UISparkManager manager) {
		if (!canAccess(manager)) return;
		String documentId = manager.fromPath("documentId");
		InputStream content = documentManager.load(documentId);
		manager.write(content, documentId);
	}

	private void save(UISparkManager manager) {
		if (!canAccess(manager)) return;
		String documentId = manager.fromPath("documentId");
		byte[] bytes = manager.fromBodyAsBytes();
		documentManager.save(documentId, new ByteArrayInputStream(bytes));
		manager.write("OK");
	}

	private boolean canAccess(UISparkManager manager) {
		if (documentManager == null) return false;
		String token = manager.fromQuery("access_token");
		return session.id().equals(token);
	}

}
