package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.openapi.module.Module;
import com.intellij.util.io.ZipUtil;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.graph.*;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Layer;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.siani.itrules.model.Frame;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Exception;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static cottons.utils.StringHelper.camelCaseToSnakeCase;
import static java.io.File.separator;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.write;

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

	private void createDisplayWidget(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").
				addSlot("name", display.name$()).addSlot("innerDisplay", display.displays().stream().map(Layer::name$).toArray(String[]::new));
		if (display.parentDisplay() != null)
			frame.addSlot("parent", new Frame().addSlot("value", display.parentDisplay()).addSlot("dsl", this.parent.substring(this.parent.lastIndexOf(".") + 1)));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + ".html");
		if (!file.exists())
			Files.write(file.toPath(), Formatters.customize(DisplayWidgetTemplate.create()).format(frame).getBytes());
	}

	private void createWidgets() throws IOException {
		Frame widgets = new Frame().addTypes("widgets");
		for (Component component : KonosGraph.componentsOf(activity)) {
			if (component.i$(Display.class)) createDisplay(component.a$(Display.class));
			if (component.i$(Dialog.class)) createDialogWidget(component.a$(Dialog.class));
			widgets.addSlot("widget", component.name$());
		}
		Files.write(new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + "widgets.html").toPath(), Formatters.customize(WidgetsTemplate.create()).format(widgets).getBytes());
	}

	private void createDisplay(Display component) throws IOException {
		createNotifier(component);
		createRequester(component);
		createDisplayWidget(component);
	}

	private void createDialogWidget(Dialog dialog) throws IOException {
		final Frame frame = new Frame().addTypes("dialog").addSlot("name", dialog.name$());
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(dialog.name$()).toLowerCase() + ".html");
		if (!file.exists())
			Files.write(file.toPath(), Formatters.customize(DialogWidgetTemplate.create()).format(frame).getBytes());
	}

	private void createRequester(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("requester", (Frame[]) display.requestList().stream().map(this::frameOf).toArray(Frame[]::new));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "requester.js");
		file.getParentFile().mkdirs();
		Files.write(file.toPath(), Formatters.customize(WidgetRequesterTemplate.create()).format(frame).getBytes());
	}

	private void createNotifier(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("notification", (Frame[]) display.notificationList().stream().map(this::frameOf).toArray(Frame[]::new));
		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "notifier-listener.js");
		file.getParentFile().mkdirs();
		Files.write(file.toPath(), Formatters.customize(WidgetNotifierTemplate.create()).format(frame).getBytes());
	}

	private Frame frameOf(Display.Request r) {
		final Frame frame = new Frame().addTypes("requester").addSlot("name", r.name$());
		if (r.isType()) {
			frame.addSlot("parameter", "");
			frame.addSlot("parameterSignature", "");
		}
		frame.addSlot("method", r.responseType().name());
		return frame;
	}

	private Frame frameOf(Display.Notification n) {
		final Frame frame = new Frame().addTypes("notification").addSlot("name", n.name$()).addSlot("to", n.to().name());
		if (n.asType() != null) frame.addSlot("parameter", "");
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
			if (urlConnection instanceof JarURLConnection) copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
			else if (urlConnection instanceof FileURLConnection) FileUtils.copyDirectory(new File(originUrl.getPath()), destination);
			else
				throw new Exception("URLConnection[" + urlConnection.getClass().getSimpleName() + "] is not a recognized/implemented connection type.");
		} catch (Exception ignored) {
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
