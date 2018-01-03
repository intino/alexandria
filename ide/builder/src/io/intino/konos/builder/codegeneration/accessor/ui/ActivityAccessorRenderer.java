package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.openapi.module.Module;
import com.intellij.util.io.ZipUtil;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.widget.*;
import io.intino.konos.model.graph.*;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Layer;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.model.Frame;
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
import static io.intino.konos.model.graph.KonosGraph.componentsOf;
import static java.io.File.separator;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.write;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class ActivityAccessorRenderer {
	private static final String SRC_DIRECTORY = "src";
	private static final String ARTIFACT_LEGIO = "artifact.legio";
	private Module appModule;
	private final File genDirectory;
	private final Activity activity;
	private String parent;

	ActivityAccessorRenderer(Module appModule, Module webModule, Activity activity, String parent) {
		this.appModule = appModule;
		this.genDirectory = rootDirectory(webModule);
		this.activity = activity;
		this.parent = parent;
	}

	public ActivityAccessorRenderer(File rootDirectory, Activity activity) {
		this.genDirectory = rootDirectory;
		this.activity = activity;
	}

	boolean createConfigurationFile() {
		final Configuration configuration = TaraUtil.configurationOf(appModule);
		Frame frame = new Frame();
		frame.addTypes("artifact", "legio");
		frame.addSlot("groupID", configuration.groupId());
		frame.addSlot("artifactID", configuration.artifactId());
		frame.addSlot("version", configuration.version());
		final Map<String, List<String>> repositories = reduce(configuration.releaseRepositories());
		for (String id : repositories.keySet()) {
			final Frame repoFrame = new Frame().addTypes("repository", "release").addSlot("id", id);
			for (String url : repositories.get(id)) repoFrame.addSlot("url", url);
			frame.addSlot("repository", ((Frame) repoFrame));
		}
		File file = new File(genDirectory, ARTIFACT_LEGIO);
		if (!file.exists()) try {
			return write(file.toPath(), ArtifactTemplate.create().format(frame).getBytes()).toFile().exists();
		} catch (IOException ignored) {
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
			createPages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createPages() throws IOException {
		for (Activity.AbstractPage page : activity.abstractPageList()) {
			Path pagePath = new File(genDirectory, SRC_DIRECTORY + separator + page.name$() + ".html").toPath();
			if (!exists(pagePath)) write(pagePath, PageTemplate.create().format(pageFrame(page)).getBytes());
		}
	}

	private Frame pageFrame(Activity.AbstractPage page) {
		String usesDisplay = Formatters.firstUpperCase(page.uses().name$()) + (page.uses().i$(Dialog.class) ? Dialog.class.getSimpleName() : Display.class.getSimpleName());
		return new Frame().addTypes("page").addSlot("usesDisplay", usesDisplay).addSlot("uses", page.uses().name$()).addSlot("name", page.name$());
	}

	private void createWidgets() throws IOException {
		Frame widgets = new Frame().addTypes("widgets");
		for (Component component : componentsOf(activity)) {
			if (component.i$(Display.class)) createDisplayComponents(component.a$(Display.class));
			if (component.i$(Dialog.class)) createDialogWidget(component.a$(Dialog.class));
			widgets.addSlot("widget", component.name$());
		}
		write(new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + "widgets.html").toPath(), Formatters.customize(WidgetsTemplate.create()).format(widgets).getBytes());
	}

	private void createDisplayComponents(Display component) throws IOException {
		createNotifier(component);
		createRequester(component);
		createDisplayWidget(component);
	}

	private void createDisplayWidget(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("innerDisplay", display.displays().stream().map(Layer::name$).toArray(String[]::new));
		if (display.parentDisplay() != null)
			frame.addSlot("parent", new Frame().addSlot("value", display.parentDisplay()).addSlot("dsl", this.parent.substring(this.parent.lastIndexOf(".") + 1)));
		final boolean prototype = isPrototype(display);
		final String type = typeOf(display);
		frame.addSlot("attached", new Frame(prototype ? "prototype" : "display").addSlot("widget", display.name$()).addSlot("type", type));
		if (prototype) {
			frame.addSlot("imports", new Frame().addSlot("type", type));
			frame.addSlot("type", type);
		} else frame.addSlot("includes", new Frame().addSlot("widget", display.name$()));
		final List<Display.Request> requests = display.requestList().stream().filter(r -> r.registerPath() != null).collect(Collectors.toList());
		if (!requests.isEmpty()) {
			frame.addSlot("path", new Frame("path").addSlot("name", display.name$()));
			writeWidgetPaths(display, requests);
		}
		writeWidget(display, frame);
	}

	private String typeOf(Display display) {
		String type = display.getClass().getSimpleName();
		if (type.equalsIgnoreCase("layout")) return layoutType(display);
		if (type.equalsIgnoreCase("temporalCatalog")) return "temporal" + display.a$(TemporalCatalog.class).type().name() + "Catalog";
		else return type;
	}


	@NotNull
	private String layoutType(Display display) {
		return display.a$(Layout.class).mode().name();
	}

	private void writeWidgetPaths(Display display, List<Display.Request> requests) throws IOException {
		final Frame frame = new Frame("paths").addSlot("name", display.name$());
		for (Display.Request request : requests) frame.addSlot("path", pathFrame(request));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + "-paths.html");
		if (!file.exists()) write(file.toPath(), Formatters.customize(WidgetPathsTemplate.create()).format(frame).getBytes());
	}

	private Frame pathFrame(Display.Request r) {
		final Frame frame = new Frame().addTypes("path").addSlot("request", r.name$());
		if (r.isType()) frame.addTypes("parameter");
		frame.addSlot("path", r.registerPath().page().paths().get(0));
		return frame;
	}

	private void writeWidget(Display display, Frame frame) throws IOException {
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + ".html");
		if (!file.exists()) write(file.toPath(), Formatters.customize(WidgetTemplate.create()).format(frame).getBytes());
	}

	private boolean isPrototype(Display display) {
		return !display.getClass().getSimpleName().equals(Display.class.getSimpleName());
	}

	private void createDialogWidget(Dialog dialog) throws IOException {
		final Frame frame = new Frame().addTypes("dialog").addSlot("name", dialog.name$());
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(dialog.name$()).toLowerCase() + ".html");
		if (!file.exists())
			write(file.toPath(), Formatters.customize(DialogWidgetTemplate.create()).format(frame).getBytes());
	}

	private void createRequester(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("request", (Frame[]) display.requestList().stream().map(this::frameOf).toArray(Frame[]::new));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "requester.js");
		file.getParentFile().mkdirs();
		write(file.toPath(), Formatters.customize(WidgetRequesterTemplate.create()).format(frame).getBytes());
	}

	private void createNotifier(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("notification", (Frame[]) display.notificationList().stream().map(this::frameOf).toArray(Frame[]::new));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "notifier-listener.js");
		file.getParentFile().mkdirs();
		write(file.toPath(), Formatters.customize(WidgetNotifierTemplate.create()).format(frame).getBytes());
	}

	private Frame frameOf(Display.Notification n) {
		final Frame frame = new Frame().addTypes("notification").addSlot("name", n.name$()).addSlot("to", n.to().name());
		if (n.asType() != null) frame.addSlot("parameter", "");
		return frame;
	}

	private Frame frameOf(Display.Request r) {
		final Frame frame = new Frame().addTypes("request").addSlot("name", r.name$()).addSlot("widget", r.core$().owner().name());
		if (r.isType()) frame.addTypes("parameter");
		if(r.registerPath()!= null) frame.addTypes("registerPath");
		frame.addSlot("method", r.responseType().name());
		return frame;
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
