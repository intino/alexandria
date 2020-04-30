package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zip.Zip;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.swagger.SwaggerProfileGenerator;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.REST.Authentication.WithCertificate;
import io.intino.konos.model.graph.Service.REST.Resource;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Layer;
import io.intino.magritte.framework.Predicate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static java.util.stream.Collectors.toList;

public class RESTServiceRenderer extends Renderer {
	private final List<Service.REST> services;
	private final KonosGraph graph;

	public RESTServiceRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.services = graph.serviceList(Service::isREST).map(Service::asREST).collect(toList());
		this.graph = graph;
	}

	public void render() {
		services.forEach((service) -> processService(service.a$(Service.REST.class), gen()));
		if (services.stream().anyMatch(Service.REST::generateDocs)) generateApiPortal();
	}

	private void generateApiPortal() {
		final File api = new File(res(), "www" + File.separator + "api");
		copyAssets(api);
		File data = new File(api, "data");
		data.mkdirs();
		new SwaggerProfileGenerator(services, data).execute();
		createConfigFile(api);
	}

	private void copyAssets(File www) {
		try {
			Zip.unzip(new ZipInputStream(this.getClass().getResourceAsStream("/swagger/assets.zip")), www.getAbsolutePath());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void createConfigFile(File api) {
		Template template = customize(new ApiPortalConfigurationTemplate());
		FrameBuilder frame = new FrameBuilder("api").add("url", services.stream().filter(Service.REST::generateDocs).map(Layer::name$).toArray(String[]::new));
		Service.REST service = services.get(0);
		if (service.color() != null) frame.add("color", service.color());
		if (service.backgroundColor() != null) frame.add("background", service.backgroundColor());
		if (service.title() != null) frame.add("title", service.title());
		if (service.subtitle() != null) frame.add("subtitle", service.subtitle());
		else frame.add("title", "API Portal");
		if (service.logo() != null) copyLogoToImages(new File(api, "images"), service.logo());
		if (service.favicon() != null) copyFaviconToImages(new File(api, "images"), service.favicon());
		try {
			Files.write(new File(api, "config.json").toPath(), template.render(frame).getBytes());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void copyFaviconToImages(File images, String logo) {
		File resource = findResource(logo);
		if (resource == null) return;
		try {
			Files.copy(resource.toPath(), new File(images, "favicon.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void copyLogoToImages(File images, String logo) {
		File resource = findResource(logo);
		if (resource == null) return;
		try {
			Files.copy(resource.toPath(), new File(images, "logo.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File findResource(String logo) {
		File resRoot = context.configuration().resDirectory();
		if (resRoot == null) return null;
		File file = new File(resRoot.getPath(), logo);
		return file.exists() ? file : null;
	}

	private void processService(Service.REST service, File gen) {
		final String className = snakeCaseToCamelCase(service.name$()) + "Service";
		if (service.resourceList().isEmpty() && service.notificationList().isEmpty()) return;
		FrameBuilder builder = new FrameBuilder("server").
				add("name", service.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("resource", framesOf(service.resourceList(), service.authentication()));
		if (!service.notificationList().isEmpty()) {
			builder.add("notification", notificationsFrame(service.notificationList()));
			if (graph.serviceList(Service::isUI).findAny().isEmpty()) builder.add("hasNotifications", "true");
		}
		if (service.authentication() != null) {
			if (service.authentication().isWithCertificate()) {
				WithCertificate secure = service.authentication().asWithCertificate();
				if (secure != null && secure.store() != null)
					builder.add("authenticationWithCertificate", new FrameBuilder("secure").add("file", secure.store()).add("password", secure.storePassword()).toFrame());
			} else {
				builder.add("authenticator", new FrameBuilder("authenticator").add("service", service.name$()));
				createAuthenticatorClass(service.authentication(), className);
			}
		}
		classes().put(service.getClass().getSimpleName() + "#" + service.name$(), className);
		Commons.writeFrame(gen, className, template().render(builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(gen(), className).getAbsolutePath()));
	}

	private void createAuthenticatorClass(Service.REST.Authentication authentication, String service) {
		if (javaFile(src(), service + "Authenticator").exists()) return;
		FrameBuilder builder = new FrameBuilder(authentication.isBasic() ? "basic" : "bearer").add("box", boxName()).add("service", service).add("package", packageName());
		Commons.writeFrame(src(), service + "Authenticator", authenticatorTemplate().render(builder.toFrame()));
	}

	private Frame[] notificationsFrame(List<Service.REST.Notification> list) {
		List<Frame> frames = new ArrayList<>();
		for (Service.REST.Notification notification : list)
			frames.add(new FrameBuilder("notification").
					add("path", notification.path()).
					add("package", packageName()).
					add("name", notification.name$()).toFrame());
		return frames.toArray(new Frame[0]);
	}

	private Frame[] framesOf(List<Resource> resources, Service.REST.Authentication authentication) {
		return resources.stream().map(r -> processResource(r, authentication, r.operationList())).flatMap(List::stream).toArray(Frame[]::new);
	}

	private List<Frame> processResource(Resource resource, Service.REST.Authentication authentication, List<Resource.Operation> operations) {
		return operations.stream().map(operation -> frameOf(operation, resource, authentication)).collect(toList());
	}

	private Frame frameOf(Resource.Operation operation, Resource resource, Service.REST.Authentication authentication) {
		FrameBuilder builder = new FrameBuilder("resource", operation.getClass().getSimpleName())
				.add("name", resource.name$())
				.add("operation", operation.getClass().getSimpleName())
				.add("path", customize("path", Commons.path(resource)))
				.add("method", operation.getClass().getSimpleName());
		if (authentication != null)
			builder.add("authenticate", new FrameBuilder(authentication.core$().conceptList().stream().filter(Concept::isAspect).map(Predicate::name).toArray(String[]::new)));
		return builder.toFrame();
	}

	private Template template() {
		return customize(new RESTServiceTemplate());
	}

	private Template authenticatorTemplate() {
		return customize(new RestAuthenticatorTemplate());
	}
}
