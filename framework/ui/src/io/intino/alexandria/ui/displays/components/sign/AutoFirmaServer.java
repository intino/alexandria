package io.intino.alexandria.ui.displays.components.sign;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.utils.IOUtils;
import io.intino.icod.core.DefaultConfiguration;
import io.intino.icod.core.ServerDocumentManager;
import io.intino.icod.services.DownloadService;
import io.intino.icod.services.RetrieveService;
import io.intino.icod.services.StorageService;
import io.intino.icod.services.spark.RequestInputMessage;
import io.intino.icod.services.spark.SparkOutputMessage;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class AutoFirmaServer {
	private final AlexandriaUiBox box;
	private final UISession session;

	private static boolean ready = false;

	private static final String DownloadPattern = "/digitalsignatures/autofirma/app";
	private static final String StoragePattern = "/digitalsignatures/autofirma/store";
	private static final String RetrievePattern = "/digitalsignatures/autofirma/retrieve";
	private static final String BatchPreSignerPattern = "/digitalsignatures/autofirma/batch/pre";
	private static final String BatchPostSignerPattern = "/digitalsignatures/autofirma/batch/post";
	private static final String RepositoryPattern = "/digitalsignatures/autofirma/repository";

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
		routeManager.post(BatchPreSignerPattern, this::batchPreSigner);
		routeManager.post(BatchPostSignerPattern, this::batchPostSigner);
		routeManager.get(RepositoryPattern, this::document);
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

	public String batchPreSignerUrl() {
		return session.browser().baseUrl() + BatchPreSignerPattern;
	}

	public String batchPostSignerUrl() {
		return session.browser().baseUrl() + BatchPostSignerPattern;
	}

	public String repositoryUrl(String id) {
		return session.browser().baseUrl() + RepositoryPattern + "?id=" + id;
	}

	public String signature(String id) {
		try {
			ServerDocumentManager documentManager = new ServerDocumentManager(new DefaultConfiguration());
			return Files.readString(documentManager.getSignature(id).toPath());
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	public SignDocument store(String id, InputStream document) {
		try {
			ServerDocumentManager documentManager = new ServerDocumentManager(new DefaultConfiguration());
			documentManager.putRepositoryDocument(id + ".pdf", document);
			return new SignDocument(id, repositoryUrl(id));
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
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

	private void document(UISparkManager manager) {
		ServerDocumentManager documentManager = new ServerDocumentManager(new DefaultConfiguration());
		manager.write(documentManager.getRepositoryDocument(manager.fromQuery("id") + ".pdf"));
	}

	private void batchPreSigner(UISparkManager manager) {
		// TODO
	}

	private void batchPostSigner(UISparkManager manager) {
		// TODO
	}

}
