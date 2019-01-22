package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.openapi.module.Module;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;

public class UIAccessorRenderer {
//	private static final String SRC_DIRECTORY = "src";
//	private static final String ARTIFACT_LEGIO = "artifact.legio";
//	private Module appModule;
//	private final File genDirectory;
//	private final UIService service;
//	private String parent;
//
	public UIAccessorRenderer(Module appModule, Module webModule, UIService service, String parent) {
//		this.appModule = appModule;
//		this.genDirectory = rootDirectory(webModule);
//		this.service = service;
//		this.parent = parent;
	}
//
	public UIAccessorRenderer(File rootDirectory, UIService service) {
//		this.genDirectory = rootDirectory;
//		this.service = service;
	}
//
	boolean createConfigurationFile() {
//		final Configuration configuration = TaraUtil.configurationOf(appModule);
//		Frame frame = new Frame();
//		frame.addTypes("artifact", "legio");
//		frame.addSlot("groupID", configuration.groupId());
//		frame.addSlot("artifactID", configuration.artifactId());
//		frame.addSlot("version", configuration.version());
//		final Map<String, List<String>> repositories = reduce(configuration.releaseRepositories());
//		for (String id : repositories.keySet()) {
//			final Frame repoFrame = new Frame().addTypes("repository", "release").addSlot("id", id);
//			for (String url : repositories.get(id)) repoFrame.addSlot("url", url);
//			frame.addSlot("repository", ((Frame) repoFrame));
//		}
//		File file = new File(genDirectory, ARTIFACT_LEGIO);
//		if (!file.exists()) {
//			write(file.toPath(), ArtifactTemplate.create().format(frame));
//			return true;
//		}
		return false;
	}
//
//	private Map<String, List<String>> reduce(Map<String, String> map) {
//		Map<String, List<String>> reduced = new HashMap<>();
//		for (Map.Entry<String, String> entry : map.entrySet()) {
//			if (!reduced.containsKey(entry.getValue())) reduced.put(entry.getValue(), new ArrayList<>());
//			reduced.get(entry.getValue()).add(entry.getKey());
//		}
//		return reduced;
//	}
//
	public void execute() {
//		try {
//			createStaticFiles();
//			createWidgets();
//			createResources();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
//
//	private void createResources() {
//		for (UIService.Resource resource : service.resourceList()) {
//			Path resourcePath = new File(genDirectory, SRC_DIRECTORY + separator + resource.name$() + ".html").toPath();
//			if (!exists(resourcePath)) write(resourcePath, ResourceTemplate.create().format(resourceFrame(resource)));
//		}
//	}
//
//	private Frame resourceFrame(UIService.Resource resource) {
//		Component uses = elementFor(resource);
//		Frame result = new Frame().addTypes("resource").addSlot("uses", uses.name$()).addSlot("name", resource.name$());
//		result.addSlot("polymer", polymerFrame(resource));
//		return result;
//	}
//
//	private Frame polymerFrame(UIService.Resource resource) {
//		Frame result = new Frame("polymer");
//		if (resource.isEditorPage()) result.addTypes("editor");
//		return result;
//	}
//
//	private void createWidgets() {
//		Frame widgets = new Frame().addTypes("widgets");
//		for (Component component : elementsFor(service)) {
//			if (component.i$(Mold.class)) createMold(component.a$(Mold.class));
//			if (component.i$(Display.class) && !component.i$(Mold.class)) createDisplay(component.a$(Display.class));
//			if (component.i$(Dialog.class)) createDialog(component.a$(Dialog.class));
//			widgets.addSlot("widget", component.name$());
//		}
//		write(new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + "widgets.html").toPath(), customize(WidgetsTemplate.create()).format(widgets));
//	}
//
//	private void createDisplay(Display component) {
//		createNotifier(component);
//		createRequester(component);
//		createDisplayWidget(component);
//	}
//
//	private void createMold(Mold mold) {
//		Frame frame = frameOf(mold);
//		createMoldLayout(mold.name$(), frame);
//		createMold(mold.name$(), frame);
//	}
//
//	private Frame frameOf(Mold mold) {
//		return new MoldFrameBuilder(service, mold).build();
//	}
//
//	private void createMoldLayout(String name, Frame frame) {
//		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + name.toLowerCase() + separator + camelCaseToSnakeCase().format(name) + "-layout.html");
//		file.getParentFile().mkdirs();
//		write(file.toPath(), customize(MoldLayoutTemplate.create()).format(frame));
//	}
//
//	private void createMold(String name, Frame frame) {
//		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase().format(name) + ".html");
//		if (!file.exists()) write(file.toPath(), customize(MoldTemplate.create()).format(frame));
//	}
//
//	private void createDisplayWidget(Display display) {
//		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("innerDisplay", display.displays().stream().map(Layer::name$).toArray(String[]::new));
//		if (display.isAccessible()) frame.addTypes("accessible");
//		if (display.parentDisplay() != null)
//			frame.addSlot("parent", new Frame().addSlot("value", display.parentDisplay()).addSlot("dsl", this.parent.substring(this.parent.lastIndexOf(".") + 1)));
//		final boolean prototype = isPrototype(display);
//		final String type = typeOf(display);
//		if (prototype) frame.addSlot("prototype", new Frame("prototype").addSlot("widget", display.name$()).addSlot("type", type));
//		if (!prototype) frame.addSlot("attached", new Frame("display").addSlot("widget", display.name$()).addSlot("type", type));
//		if (prototype) {
//			frame.addSlot("imports", new Frame().addSlot("type", type));
//			frame.addSlot("type", type);
//		}
//		final List<Display.Request> requests = display.requestList().stream().filter(Display.Request::isAddressable).collect(Collectors.toList());
//		if (!requests.isEmpty()) {
//			frame.addSlot("routes", new Frame("routes").addSlot("name", display.name$()));
//			writeWidgetRoutes(display, requests);
//		}
//		writeWidget(display, frame);
//	}
//
//	private String typeOf(Display display) {
//		String type = display.getClass().getSimpleName();
//		if (display.i$(Panel.class) && display.a$(Panel.class).isDesktop()) return "desktop";
//		else if (type.equalsIgnoreCase("temporalCatalog")) return "temporal" + display.a$(TemporalCatalog.class).type().name() + "Catalog";
//		else return type;
//	}
//
//	private void writeWidgetRoutes(Display display, List<Display.Request> requests) {
//		final Frame frame = new Frame("routes").addSlot("name", display.name$());
//		for (Display.Request request : requests) frame.addSlot("route", routeFrame(request));
//		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + "-routes.html");
//		write(file.toPath(), customize(WidgetRoutesTemplate.create()).format(frame));
//	}
//
//	private Frame routeFrame(Display.Request r) {
//		final Frame frame = new Frame().addTypes("route").addSlot("request", r.name$());
//		if (r.isType()) frame.addTypes("parameter");
//		if (r.isAddressable()) {
//			AddressableRequest addressableRequest = r.asAddressable();
//			if (addressableRequest.listenForChanges()) frame.addTypes("listenForChanges");
//			frame.addSlot("routePath", routePathFrame(r));
//			frame.addSlot("value", addressableRequest.addressableResource().path());
//		}
//		return frame;
//	}
//
//	private Frame routePathFrame(Display.Request r) {
//		Frame frame = new Frame("routePath");
//		AddressableRequest addressable = r.asAddressable();
//		if (addressable.encoded()) frame.addTypes("encoded");
//		frame.addSlot("value", addressable.addressableResource().path());
//		return frame;
//	}
//
//	private void writeWidget(Display display, Frame frame) {
//		File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + ".html");
//		if (!file.exists()) write(file.toPath(), customize(WidgetTemplate.create()).format(frame));
//		if (display.isAccessible()) {
//			frame.addTypes("accessible");
//			file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(display.name$()).toLowerCase() + "-proxy.html");
//			write(file.toPath(), customize(WidgetTemplate.create()).format(frame));
//		}
//	}
//
//	private boolean isPrototype(Display display) {
//		return !display.getClass().getSimpleName().equals(Display.class.getSimpleName());
//	}
//
//	private void createDialog(Dialog dialog) {
//		final Frame frame = new Frame().addTypes("dialog").addSlot("name", dialog.name$());
//		final File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + camelCaseToSnakeCase(dialog.name$()).toLowerCase() + ".html");
//		if (!file.exists())
//			write(file.toPath(), customize(DialogWidgetTemplate.create()).format(frame));
//	}
//
//	private void createRequester(Display display) {
//		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("request", (Frame[]) display.requestList().stream().map(this::frameOf).toArray(Frame[]::new));
//		File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "requester.js");
//		file.getParentFile().mkdirs();
//		write(file.toPath(), customize(WidgetRequesterTemplate.create()).format(frame));
//		if (display.isAccessible()) {
//			frame.addTypes("accessible");
//			file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + "proxy" + separator + "requester.js");
//			file.getParentFile().mkdirs();
//			write(file.toPath(), customize(WidgetRequesterTemplate.create()).format(frame));
//		}
//	}
//
//	private void createNotifier(Display display) {
//		final Frame frame = new Frame().addTypes("widget").addSlot("name", display.name$()).addSlot("notification", (Frame[]) display.notificationList().stream().map(this::frameOf).toArray(Frame[]::new));
//		File file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + separator + "notifier-listener.js");
//		file.getParentFile().mkdirs();
//		write(file.toPath(), customize(WidgetNotifierTemplate.create()).format(frame));
//		if (display.isAccessible()) {
//			frame.addTypes("accessible");
//			file = new File(genDirectory, SRC_DIRECTORY + separator + "widgets" + separator + display.name$().toLowerCase() + "proxy" + separator + "notifier-listener.js");
//			file.getParentFile().mkdirs();
//			write(file.toPath(), customize(WidgetNotifierTemplate.create()).format(frame));
//		}
//	}
//
//	private Frame frameOf(Display.Notification n) {
//		final Frame frame = new Frame().addTypes("notification").addSlot("name", n.name$()).addSlot("to", n.to().name());
//		if (n.asType() != null) frame.addSlot("parameter", "");
//		return frame;
//	}
//
//	private Frame frameOf(Display.Request r) {
//		final Frame frame = new Frame().addTypes("request").addSlot("name", r.name$()).addSlot("widget", r.core$().owner().name());
//		if (r.isType()) frame.addTypes("parameter");
//		if (r.isAddressable()) frame.addTypes("addressable");
//		frame.addSlot("method", r.responseType().name());
//		return frame;
//	}
//
//	private void createStaticFiles() throws IOException {
//		final File src = new File(genDirectory, SRC_DIRECTORY);
//		src.mkdirs();
//		final File file = new File(src, "ui.zip");
//		copyResourcesRecursively(this.getClass().getResource("/ui/ui.zip"), file);
//		ZipUtil.extract(file, src, null, false);
//		file.delete();
//		new File(src, "images").mkdirs();
//		new File(src, "widgets").mkdirs();
//	}
//
//	private File rootDirectory(Module webModule) {
//		return new File(webModule.getModuleFilePath()).getParentFile();
//	}
//
//	private void copyResourcesRecursively(URL originUrl, File destination) {
//		try {
//			URLConnection urlConnection = originUrl.openConnection();
//			if (urlConnection instanceof JarURLConnection)
//				copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
//			else if (urlConnection instanceof FileURLConnection) {
//				FileUtils.copyFile(new File(originUrl.getPath()), destination);
//			} else throw new Exception("URLConnection[" + urlConnection.getClass().getSimpleName() +
//					"] is not a recognized/implemented connection type.");
//		} catch (Exception e) {
//			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
//		}
//	}
//
//	private void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection) throws IOException {
//		JarFile jarFile = jarConnection.getJarFile();
//		for (Enumeration list = jarFile.entries(); list.hasMoreElements(); ) {
//			JarEntry entry = (JarEntry) list.nextElement();
//			if (entry.getName().startsWith(jarConnection.getEntryName())) {
//				String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());
//				if (!entry.isDirectory()) try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
//					FileUtils.copyInputStreamToFile(entryInputStream, new File(destination, fileName));
//				}
//				else new File(destination, fileName).exists();
//			}
//		}
//	}
}
