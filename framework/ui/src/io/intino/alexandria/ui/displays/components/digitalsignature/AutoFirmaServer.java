package io.intino.alexandria.ui.displays.components.digitalsignature;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.icod.services.DownloadService;
import io.intino.icod.services.RetrieveService;
import io.intino.icod.services.StorageService;
import io.intino.icod.services.spark.RequestInputMessage;
import io.intino.icod.services.spark.SparkOutputMessage;

public class AutoFirmaServer {
	private final AlexandriaUiBox box;
	private final UISession session;

	private static boolean ready = false;

	private static final String DownloadPattern = "/digitalsignatures/autofirma/app";
	private static final String StoragePattern = "/digitalsignatures/autofirma/store";
	private static final String RetrievePattern = "/digitalsignatures/autofirma/retrieve";

	public AutoFirmaServer(AlexandriaUiBox box, UISession session) {
		this.box = box;
		this.session = session;
	}

	public AutoFirmaServer listen() {
		if (listening()) return this;
		DisplayRouteManager routeManager = box.routeManager();
		routeManager.get(DownloadPattern, this::download);
		routeManager.post(StoragePattern, this::store);
		routeManager.post(RetrievePattern, this::retrieve);
		ready = true;
		return this;
	}

	public String downloadUrl() {
		return session.browser().baseUrl() + DownloadPattern;
	}

	public String storageUrl() {
		return session.browser().baseUrl() + StoragePattern;
	}

	public String retrieveUrl() {
		return session.browser().baseUrl() + RetrievePattern;
	}

	private boolean listening() {
		return ready;
	}

	private void download(UISparkManager manager) {
		new DownloadService().download(new RequestInputMessage(manager.request()), new SparkOutputMessage(manager.response()));
	}

	private void store(UISparkManager manager) {
		new StorageService().store(new RequestInputMessage(manager.request()), new SparkOutputMessage(manager.response()));
	}

	private void retrieve(UISparkManager manager) {
		new RetrieveService().retrieve(new RequestInputMessage(manager.request()), new SparkOutputMessage(manager.response()));
	}

}
