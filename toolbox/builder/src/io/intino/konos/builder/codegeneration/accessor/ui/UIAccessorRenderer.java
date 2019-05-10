package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.openapi.module.Module;
import com.intellij.util.io.ZipUtil;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.mold.MoldFrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.mold.MoldLayoutTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.mold.MoldTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.widget.*;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.addressable.display.AddressableRequest;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Layer;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Exception;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.camelCaseToSnakeCase;
import static io.intino.konos.builder.codegeneration.Formatters.camelCaseToSnakeCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.konos.model.graph.KonosGraph.componentFor;
import static io.intino.konos.model.graph.KonosGraph.componentsOf;
import static java.io.File.separator;
import static java.nio.file.Files.exists;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class UIAccessorRenderer {
	private static final String SRC_DIRECTORY = "src";
	private static final String ARTIFACT_LEGIO = "artifact.legio";
	private final File genDirectory;
	private final UIService service;
	private Module appModule;
	private String parent;

	UIAccessorRenderer(Module appModule, Module webModule, UIService service, String parent) {
		this.appModule = appModule;
		this.genDirectory = rootDirectory(webModule);
		this.service = service;
		this.parent = parent;
	}

	public UIAccessorRenderer(File rootDirectory, UIService service) {
		this.genDirectory = rootDirectory;
		this.service = service;
	}

	boolean createConfigurationFile() {
		final Configuration configuration = TaraUtil.configurationOf(appModule);
		FrameBuilder builder = new FrameBuilder();
		builder.add("artifact", "legio");
		builder.add("groupID", configuration.groupId());
		builder.add("artifactID", configuration.artifactId());
		builder.add("version", configuration.version());
		final Map<String, List<String>> repositories = reduce(configuration.releaseRepositories());
		for (String id : repositories.keySet()) {
			final FrameBuilder repoFrameBuilder = new FrameBuilder("repository", "release").add("id", id);
			for (String url : repositories.get(id)) repoFrameBuilder.add("url", url);
			builder.add("repository", repoFrameBuilder);
		}
		File file = new File(genDirectory, ARTIFACT_LEGIO);
		if (!file.exists()) {
			write(file.toPath(), new ArtifactTemplate().render(builder));
			return true;
		}
		return false;
	}

	private Map<String, List<String>> reduce(Map<String, String> map) {
		Map<String, List<String>> reduced = new HashMap<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!reduced.containsKey(entry.getValue())) reduced.put(entry.getValue(), new ArrayList<>());
			reduced.get(entry.getValue()).add(entry.getKey());
		}
		return reduced;
	}

	public void execute() {
		try {
			createStaticFiles();
			createWidgets();
			createResources();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createResources() {
		for (UIService.Resource resource : service.resourceList()) {
			Path resourcePath = new File(genDirectory, SRC_DIRECTORY + separator + resource.name$() + ".html").toPath();
			if (!exists(resourcePath)) write(resourcePath, new ResourceTemplate().render(resourceFrame(resource)));
		}
	}

	private Frame resourceFrame(UIService.Resource resource) {
		return new FrameBuilder("resource").add("uses", componentFor(resource).name$()).add("name", resource.name$()).add("polymer", polymerFrame(resource)).toFrame();
	}

	private Frame polymerFrame(UIService.Resource resource) {
		FrameBuilder result = new FrameBuilder("polymer");
		if (resource.isEditorPage()) result.add("editor");
		return result.toFrame();
	}

	private void createWidgets() {
		FrameBuilder widgets = new FrameBuilder("widgets");
		for (Component component : componentsOf(service)) {
			if (component.i$(Mold.class)) createMold(component.a$(Mold.class));
			if (component.i$(Display.class) && !component.i$(Mold.class)) createDisplay(component.a$(Display.class));
			if (component.i$(Dialog.class)) createDialog(component.a$(Dialog.class));
			widgets.add("widget", component.name$());
		}
		write(new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + "widgets.html").toPath(), customize(new WidgetsTemplate()).render(widgets));
	}

	private void createDisplay(Display component) {
		createNotifier(component);
		createRequester(component);
		createDisplayWidget(component);
	}

	private void createMold(Mold mold) {
		Frame frame = frameOf(mold);
		createMoldLayout(mold.name$(), frame);
		createMold(mold.name$(), frame);
	}

	private Frame frameOf(Mold mold) {
		return new MoldFrameBuilder(service, mold).build();
	}

	private void createMoldLayout(String name, Frame frame) {
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + name.toLowerCase() + separator + camelCaseToSnakeCase().format(name) + "-layout.html");
		file.getParentFile().mkdirs();
		write(file.toPath(), customize(new MoldLayoutTemplate()).render(frame));
	}

	private void createMold(String name, Frame frame) {
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase().format(name) + ".html");
		if (!file.exists()) write(file.toPath(), customize(new MoldTemplate()).render(frame));
	}

	private void createDisplayWidget(Display display) {
		final FrameBuilder frame = new FrameBuilder("widget").add("name", display.name$()).add("innerDisplay", display.displays().stream().map(Layer::name$).toArray(String[]::new));
		if (display.isAccessible()) frame.add("accessible");
		if (display.parentDisplay() != null)
			frame.add("parent", new FrameBuilder().add("value", display.parentDisplay()).add("dsl", this.parent.substring(this.parent.lastIndexOf(".") + 1)));
		final boolean prototype = isPrototype(display);
		final String type = typeOf(display);
		if (prototype) frame.add("prototype", new FrameBuilder("prototype").add("widget", display.name$()).add("type", type));
		if (!prototype) frame.add("attached", new FrameBuilder("display").add("widget", display.name$()).add("type", type));
		if (prototype) {
			frame.add("imports", new FrameBuilder().add("type", type));
			frame.add("type", type);
		}
		final List<Display.Request> requests = display.requestList().stream().filter(Display.Request::isAddressable).collect(Collectors.toList());
		if (!requests.isEmpty()) {
			frame.add("routes", new FrameBuilder("routes").add("name", display.name$()));
			writeWidgetRoutes(display, requests);
		}
		writeWidget(display, frame);
	}

	private String typeOf(Display display) {
		String type = display.getClass().getSimpleName();
		if (display.i$(Panel.class) && display.a$(Panel.class).isDesktop()) return "desktop";
		else if (type.equalsIgnoreCase("temporalCatalog")) return "temporal" + display.a$(TemporalCatalog.class).type().name() + "Catalog";
		else return type;
	}

	private void writeWidgetRoutes(Display display, List<Display.Request> requests) {
		final FrameBuilder builder = new FrameBuilder("routes").add("name", display.name$());
		for (Display.Request request : requests) builder.add("route", routeFrame(request));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + "-routes.html");
		write(file.toPath(), customize(new WidgetRoutesTemplate()).render(builder));
	}

	private Frame routeFrame(Display.Request r) {
		final FrameBuilder builder = new FrameBuilder("route").add("request", r.name$());
		if (r.isType()) builder.add("parameter");
		if (r.isAddressable()) {
			AddressableRequest addressableRequest = r.asAddressable();
			if (addressableRequest.listenForChanges()) builder.add("listenForChanges");
			builder.add("routePath", routePathFrame(r));
			builder.add("value", addressableRequest.addressableResource().path());
		}
		return builder.toFrame();
	}

	private Frame routePathFrame(Display.Request r) {
		FrameBuilder builder = new FrameBuilder("routePath");
		AddressableRequest addressable = r.asAddressable();
		if (addressable.encoded()) builder.add("encoded");
		return builder.add("value", addressable.addressableResource().path()).toFrame();
	}

	private void writeWidget(Display display, FrameBuilder frame) {
		File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + ".html");
		if (!file.exists()) write(file.toPath(), customize(new WidgetTemplate()).render(frame));
		if (display.isAccessible()) {
			frame.add("accessible");
			file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + "-proxy.html");
			write(file.toPath(), customize(new WidgetTemplate()).render(frame));
		}
	}

	private boolean isPrototype(Display display) {
		return !display.getClass().getSimpleName().equals(Display.class.getSimpleName());
	}

	private void createDialog(Dialog dialog) {
		final FrameBuilder frame = new FrameBuilder("dialog").add("name", dialog.name$());
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(dialog.name$()).toLowerCase() + ".html");
		if (!file.exists())
			write(file.toPath(), customize(new DialogWidgetTemplate()).render(frame));
	}

	private void createRequester(Display display) {
		final FrameBuilder frame = new FrameBuilder("widget").add("name", display.name$()).add("request", display.requestList().stream().map(this::frameOf).toArray(Frame[]::new));
		File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "requester.js");
		file.getParentFile().mkdirs();
		write(file.toPath(), customize(new WidgetRequesterTemplate()).render(frame));
		if (display.isAccessible()) {
			frame.add("accessible");
			file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + "proxy" + separator + "requester.js");
			file.getParentFile().mkdirs();
			write(file.toPath(), customize(new WidgetRequesterTemplate()).render(frame));
		}
	}

	private void createNotifier(Display display) {
		final FrameBuilder frame = new FrameBuilder("widget").add("name", display.name$()).add("notification", display.notificationList().stream().map(this::frameOf).toArray(Frame[]::new));
		File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "notifier-listener.js");
		file.getParentFile().mkdirs();
		write(file.toPath(), customize(new WidgetNotifierTemplate()).render(frame));
		if (display.isAccessible()) {
			frame.add("accessible");
			file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + "proxy" + separator + "notifier-listener.js");
			file.getParentFile().mkdirs();
			write(file.toPath(), customize(new WidgetNotifierTemplate()).render(frame));
		}
	}

	private Frame frameOf(Display.Notification n) {
		final FrameBuilder builder = new FrameBuilder("notification").add("name", n.name$()).add("to", n.to().name());
		if (n.asType() != null) builder.add("parameter", "");
		return builder.toFrame();
	}

	private Frame frameOf(Display.Request r) {
		final FrameBuilder builder = new FrameBuilder("request").add("name", r.name$()).add("widget", r.core$().owner().name());
		if (r.isType()) builder.add("parameter");
		if (r.isAddressable()) builder.add("addressable");
		builder.add("method", r.responseType().name());
		return builder.toFrame();
	}

	private void createStaticFiles() throws IOException {
		final File src = new File(genDirectory, SRC_DIRECTORY);
		src.mkdirs();
		final File file = new File(src, "ui.zip");
		copyResourcesRecursively(this.getClass().getResource("/ui/ui.zip"), file);
		ZipUtil.extract(file, src, null, false);
		file.delete();
		new File(src, "images").mkdirs();
		new File(src, "widgets").mkdirs();
	}

	private File rootDirectory(Module webModule) {
		return new File(webModule.getModuleFilePath()).getParentFile();
	}

	private void copyResourcesRecursively(URL originUrl, File destination) {
		try {
			URLConnection urlConnection = originUrl.openConnection();
			if (urlConnection instanceof JarURLConnection)
				copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
			else if (urlConnection instanceof FileURLConnection) {
				FileUtils.copyFile(new File(originUrl.getPath()), destination);
			} else throw new Exception("URLConnection[" + urlConnection.getClass().getSimpleName() +
					"] is not a recognized/implemented connection type.");
		} catch (Exception e) {
			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
		}
	}

	private void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection) throws IOException {
		JarFile jarFile = jarConnection.getJarFile();
		for (Enumeration list = jarFile.entries(); list.hasMoreElements(); ) {
			JarEntry entry = (JarEntry) list.nextElement();
			if (entry.getName().startsWith(jarConnection.getEntryName())) {
				String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());
				if (!entry.isDirectory()) try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
					FileUtils.copyInputStreamToFile(entryInputStream, new File(destination, fileName));
				}
				else new File(destination, fileName).exists();
			}
		}
	}
}
