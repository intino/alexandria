package io.intino.konos.builder.codegeneration.services.rest;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import io.intino.konos.builder.codegeneration.swagger.SwaggerProfileGenerator;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.rest.RESTService;
import io.intino.konos.model.graph.rest.RESTService.Resource;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static com.intellij.platform.templates.github.ZipUtil.unzip;
import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.getSourceRoots;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class RESTServiceRenderer {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	private final List<RESTService> services;
	@NotNull
	private final KonosGraph graph;
	private final File gen;
	private final File res;
	private String packageName;
	private final String boxName;
	private final Module module;
	private final Map<String, String> classes;

	public RESTServiceRenderer(KonosGraph graph, File gen, File res, String packageName, String boxName, Module module, Map<String, String> classes) {
		this.services = graph.rESTServiceList();
		this.graph = graph;
		this.gen = gen;
		this.res = res;
		this.packageName = packageName;
		this.boxName = boxName;
		this.module = module;
		this.classes = classes;
	}

	public void execute() {
		services.forEach((service) -> processService(service.a$(RESTService.class), gen));
		if (!services.isEmpty()) generateApiPortal();
	}

	private void generateApiPortal() {
		final File api = new File(res, "www" + File.separator + "api");
		copyAssets(api);
		File data = new File(api, "data");
		data.mkdirs();
		new SwaggerProfileGenerator(services, data).execute();
		createConfigFile(api);
	}

	private void copyAssets(File www) {
		try {
			unzip(null, www, new ZipInputStream(this.getClass().getResourceAsStream("/swagger/assets.zip")), null, null, false);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void createConfigFile(File api) {
		Template template = customize(ApiPortalConfigurationTemplate.create());
		Frame frame = new Frame("api").addSlot("url", services.stream().map(Layer::name$).toArray(String[]::new));
		RESTService service = services.get(0);
		if (service.color() != null) frame.addSlot("color", service.color());
		if (service.backgroundColor() != null) frame.addSlot("background", service.backgroundColor());
		if (service.title() != null) frame.addSlot("title", service.title());
		if (service.subtitle() != null) frame.addSlot("subtitle", service.subtitle());
		else frame.addSlot("title", "API Portal");
		if (service.logo() != null) copyLogoToImages(new File(api, "images"), service.logo());
		if (service.favicon() != null) copyFaviconToImages(new File(api, "images"), service.favicon());
		try {
			Files.write(new File(api, "config.json").toPath(), template.format(frame).getBytes());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void copyFaviconToImages(File images, String logo) {
		File resource = findResource(logo);
		if (resource == null) return;
		try {
			Files.copy(resource.toPath(), new File(images, "favicon.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void copyLogoToImages(File images, String logo) {
		File resource = findResource(logo);
		if (resource == null) return;
		try {
			Files.copy(resource.toPath(), new File(images, "logo.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private File findResource(String logo) {
		VirtualFile resRoot = getResRoot(module);
		if (resRoot == null) return null;
		File file = new File(resRoot.getPath(), logo);
		return file.exists() ? file : null;
	}

	private VirtualFile getResRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "res".equals(file.getName())) return file;
		return null;
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
		final String className = snakeCaseToCamelCase(service.name$()) + "Service";
		classes.put(service.getClass().getSimpleName() + "#" + service.name$(), className);
		Commons.writeFrame(gen, className, template().format(frame));
	}

	private Frame[] notificationsFrame(List<RESTService.Notification> list) {
		List<Frame> frames = new ArrayList<>();
		for (RESTService.Notification notification : list)
			frames.add(new Frame("notification").
					addSlot("path", notification.path()).
					addSlot("package", packageName).
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
				.addSlot("path", customize("path", Commons.path(resource)))
				.addSlot("method", operation.getClass().getSimpleName())).collect(Collectors.toList());
	}

	private Template template() {
		return customize(RESTServiceTemplate.create());
	}
}
