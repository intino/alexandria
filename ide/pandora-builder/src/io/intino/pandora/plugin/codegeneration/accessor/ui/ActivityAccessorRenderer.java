package io.intino.pandora.plugin.codegeneration.accessor.ui;

import com.intellij.openapi.module.Module;
import io.intino.pandora.model.Activity;
import io.intino.pandora.model.Activity.Display;
import io.intino.pandora.plugin.WidgetNotifierTemplate;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Layer;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

import static cottons.utils.StringHelper.camelCaseToSnakeCase;
import static java.io.File.separator;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.write;

public class ActivityAccessorRenderer {
	private static final String SRC_DIRECTORY = "src";
	private final Module appModule;
	private final Module webModule;
	private final Activity activity;

	ActivityAccessorRenderer(Module appModule, Module webModule, Activity activity) {
		this.appModule = appModule;
		this.webModule = webModule;
		this.activity = activity;
	}

	public void execute() {
		try {
			createConfigurationFile();
			createStaticFiles();
			createWidgets();
			createPages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createPages() throws IOException {
		for (Activity.AbstractPage page : activity.abstractPageList()) {
			Path pagePath = new File(rootDirectory(), SRC_DIRECTORY + separator + page.name() + ".html").toPath();
			if (!exists(pagePath)) write(pagePath, PageTemplate.create().format(pageFrame(page)).getBytes());
		}
	}

	private Frame pageFrame(Activity.AbstractPage page) {
		return new Frame().addTypes("page").addSlot("uses", page.uses().name()).addSlot("name", page.name());
	}

	private void createWidget(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name()).addSlot("innerDisplay", display.displays().stream().map(Layer::name).toArray(String[]::new));
		final File file = new File(rootDirectory(), SRC_DIRECTORY + separator + "widgets" + separator + display.name().toLowerCase() + "-widget.html");
		if (!file.exists())
			write(file.toPath(), WidgetTemplate.create().add("camelCaseToSnakeCase", value -> camelCaseToSnakeCase(value.toString())).format(frame).getBytes());
	}

	private void createWidgets() throws IOException {
		Frame widgets = new Frame().addTypes("widgets");
		for (Display display : activity.displayList()) {
			createNotifier(display);
			createRequester(display);
			createWidget(display);
			widgets.addSlot("widget", display.name());
		}
		write(new File(rootDirectory(), SRC_DIRECTORY + separator + "widgets" + separator + "widgets.html").toPath(), WidgetsTemplate.create().format(widgets).getBytes());
	}

	private void createRequester(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name()).addSlot("requester", (Frame[]) display.requestList().stream().map(this::frameOf).toArray(Frame[]::new));
		final File file = new File(rootDirectory(), SRC_DIRECTORY + separator + "widgets" + separator + display.name().toLowerCase() + "widget" + separator + "requester.js");
		file.getParentFile().mkdirs();
		write(file.toPath(), WidgetRequesterTemplate.create().format(frame).getBytes());
	}

	private void createNotifier(Display display) throws IOException {
		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name()).addSlot("notification", (Frame[]) display.notificationList().stream().map(this::frameOf).toArray(Frame[]::new));
		final File file = new File(rootDirectory(), SRC_DIRECTORY + separator + "widgets" + separator + display.name().toLowerCase() + "widget" + separator + "notifier-listener.js");
		file.getParentFile().mkdirs();
		write(file.toPath(), WidgetNotifierTemplate.create().format(frame).getBytes());
	}

	private Frame frameOf(Display.Request r) {
		final Frame frame = new Frame().addTypes("requester").addSlot("name", r.name());
		if (r.isType()) {
			frame.addSlot("parameter", "");
			frame.addSlot("parameterSignature", "");
		}
		frame.addSlot("method", r.responseType().name());
		return frame;
	}

	private Frame frameOf(Display.Notification n) {
		final Frame frame = new Frame().addTypes("notification").addSlot("name", n.name());
		if (n.asType() != null) {
			frame.addSlot("parameter", "");
			frame.addSlot("type", n.to().name());
		}
		return frame;
	}

	private void createConfigurationFile() throws IOException {
		final Configuration configuration = TaraUtil.configurationOf(appModule);
		Frame frame = new Frame();
		frame.addTypes("configuration", "legio");
		frame.addSlot("groupID", configuration.groupId());
		frame.addSlot("artifactID", configuration.artifactId());
		frame.addSlot("version", configuration.modelVersion());
		final Map<String, String> releaseRepositories = configuration.releaseRepositories();
		for (String repository : releaseRepositories.keySet())
			frame.addSlot("repository", new Frame().addTypes("repository", "release").addSlot("type", "Release").addSlot("url", repository).addSlot("id", releaseRepositories.get(repository)));
		File file = new File(rootDirectory(), "configuration.legio");
		if (!file.exists()) write(file.toPath(), ConfigurationTemplate.create().format(frame).getBytes());
	}

	private void createStaticFiles() throws IOException {
		File root = rootDirectory();
		new File(root, SRC_DIRECTORY + separator + "fonts").mkdirs();
		new File(root, SRC_DIRECTORY + separator + "images").mkdirs();
		new File(root, SRC_DIRECTORY + separator + "styles").mkdirs();
		new File(root, SRC_DIRECTORY + separator + "widgets").mkdirs();
		File file = new File(root, SRC_DIRECTORY + separator + "components.html");
		if (!file.exists()) copyStream(inputFrom("/ui/components.html"), new FileOutputStream(file));
		file = new File(root, SRC_DIRECTORY + separator + "main.js");
		if (!file.exists()) copyStream(inputFrom("/ui/main.js"), new FileOutputStream(file));
	}

	private File rootDirectory() {
		return new File(webModule.getModuleFilePath()).getParentFile();
	}

	private InputStream inputFrom(String path) {
		return this.getClass().getResourceAsStream(path);
	}

	private static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
	}
}
