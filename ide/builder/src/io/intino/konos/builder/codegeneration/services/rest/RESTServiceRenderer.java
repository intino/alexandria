package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.swagger.IndexTemplate;
import io.intino.konos.builder.codegeneration.swagger.SwaggerGenerator;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static com.intellij.platform.templates.github.ZipUtil.unzip;
import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class RESTServiceRenderer {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	private final List<RESTService> services;
	@org.jetbrains.annotations.NotNull
	private final KonosGraph graph;
	private final File gen;
	private final File res;
	private String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public RESTServiceRenderer(KonosGraph graph, File gen, File res, String packageName, String boxName, Map<String, String> classes) {
		this.services = graph.rESTServiceList();
		this.graph = graph;
		this.gen = gen;
		this.res = res;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		services.forEach((service) -> processService(service.a$(RESTService.class), gen));
		if (!services.isEmpty()) generateDoc();
	}

	private void generateDoc() {
		final File www = new File(res, "www" + File.separator + "developer");
		new SwaggerGenerator(services, www).execute();
		createIndex(www);
		copyAssets(www);
	}

	private void createIndex(File www) {
		Frame doc = new Frame().addTypes("index");
		for (RESTService service : services)
			doc.addSlot("service", new Frame().addTypes("service").addSlot("name", service.name$()).addSlot("description", service.description()));
		writeIndex(www, doc);
	}

	private void writeIndex(File www, Frame doc) {
		Commons.write(new File(www, "index.html").toPath(), IndexTemplate.create().format(doc));
	}

	private void copyAssets(File www) {
		try {
			unzip(null, www, new ZipInputStream(this.getClass().getResourceAsStream("/swagger/assets.zip")), null, null, false);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void processService(RESTService service, File gen) {
		if (service.resourceList().isEmpty()) return;
		Frame frame = new Frame().addTypes("server").
				addSlot("name", service.name$()).
				addSlot("box", boxName).
				addSlot("package", packageName).
				addSlot("resource", (AbstractFrame[]) framesOf(service.resourceList()));
		if (!service.notificationList().isEmpty()) {
			frame.addSlot("notification", notificationsFrame(service.notificationList()));
			if (graph.uIServiceList().isEmpty()) frame.addSlot("hasNotifications", notificationsFrame(service.notificationList()));
		}
		final RESTService.AuthenticatedWithCertificate secure = service.authenticatedWithCertificate();
		if (secure != null && secure.store() != null)
			frame.addSlot("secure", new Frame().addTypes("secure").addSlot("file", secure.store()).addSlot("password", secure.storePassword()));
		final String className = snakeCaseToCamelCase(service.name$()) + "Resources";
		classes.put(service.getClass().getSimpleName() + "#" + service.name$(), className);
		Commons.writeFrame(gen, className, template().format(frame));
	}

	private Frame[] notificationsFrame(List<RESTService.Notification> list) {
		List<Frame> frames = new ArrayList<>();
		for (RESTService.Notification notification : list)
			frames.add(new Frame("notification").
					addSlot("path", notification.path()).
					addSlot("name", notification.name$()));
		return frames.toArray(new Frame[0]);
	}

	private Frame[] framesOf(List<Resource> resources) {
		List<Frame> list = new ArrayList<>();
		for (Resource resource : resources) list.addAll(processResource(resource, resource.operationList()));
		return list.toArray(new Frame[0]);
	}

	private List<Frame> processResource(Resource resource, List<Resource.Operation> operations) {
		return operations.stream().map(operation -> new Frame().addTypes("resource", operation.getClass().getSimpleName())
				.addSlot("name", resource.name$())
				.addSlot("operation", operation.getClass().getSimpleName())
				.addSlot("path", customize(Commons.path(resource)))
				.addSlot("method", operation.getClass().getSimpleName())).collect(Collectors.toList());
	}

	private Frame customize(String path) {
		Frame frame = new Frame().addTypes("path");
		frame.addSlot("name", path);
		for (String parameter : Commons.extractParameters(path)) frame.addSlot("custom", parameter);
		return frame;
	}

	private Template template() {
		return Formatters.customize(RESTServiceTemplate.create());
	}
}
