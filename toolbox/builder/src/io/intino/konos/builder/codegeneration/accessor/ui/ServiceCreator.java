package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.ide.highlighter.ModuleFileType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.WebModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.io.ZipUtil;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.ArtifactTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static io.intino.konos.builder.codegeneration.Formatters.camelCaseToSnakeCase;
import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.plugin.dependencyresolution.DependencyCatalog.DependencyScope.WEB;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.newExternalProvider;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.register;
import static java.util.Arrays.stream;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class ServiceCreator extends UIRenderer {

	private final Project project;
	private final UIService service;

	private static final String LegioArtifact = "artifact.legio";

	public ServiceCreator(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.project = settings.module() == null ? null : settings.module().getProject();
		this.service = service;
	}

	@Override
	public void render() {
		try {
			if (settings.module() == null) return;
			boolean exists = findModule(service.name$()) != null;
			settings.webModule(getOrCreateModule());
			if (!exists) createSkeleton();
			new ServiceRenderer(settings, service).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Module getOrCreateModule() {
		Module m = findModule(service.name$());
		Application application = ApplicationManager.getApplication();
		application.invokeAndWait(() -> application.runWriteAction(() -> {
			if (m != null) {
				addWebDependency(TaraUtil.configurationOf(m));
				return;
			}
			final ModuleManager manager = ModuleManager.getInstance(project);
			Module webModule = manager.newModule(modulePath(service), WebModuleType.WEB_MODULE);
			final ModifiableRootModel model = ModuleRootManager.getInstance(webModule).getModifiableModel();
			final File moduleRoot = new File(webModule.getModuleFilePath()).getParentFile();
			moduleRoot.mkdirs();
			final VirtualFile vFile = VfsUtil.findFileByIoFile(moduleRoot, true);
			final ContentEntry contentEntry = vFile != null ? model.addContentEntry(vFile) : model.addContentEntry(moduleRoot.getAbsolutePath());
			addExcludeFiles(moduleRoot, contentEntry);
			model.commit();
			boolean created = createConfigurationFile(moduleRoot, service.name$());
			if (created) addWebDependency(register(webModule, newExternalProvider(webModule)));
		}));
		return findModule(service.name$());
	}

	private boolean createConfigurationFile(File moduleRoot, String name) {
		final Configuration configuration = TaraUtil.configurationOf(settings.module());
		FrameBuilder builder = new FrameBuilder("artifact", "legio");
		builder.add("groupId", configuration.groupId());
		builder.add("artifactId", camelCaseToSnakeCase().format(name).toString());
		builder.add("version", configuration.version());
		final Map<String, List<String>> repositories = reduce(configuration.releaseRepositories());
		for (String id : repositories.keySet()) {
			final FrameBuilder repoFrameBuilder = new FrameBuilder("repository", "release").add("id", id);
			for (String url : repositories.get(id)) repoFrameBuilder.add("url", url);
			builder.add("repository", repoFrameBuilder);
		}
		File file = new File(moduleRoot, LegioArtifact);
		if (!file.exists()) {
			write(file.toPath(), new ArtifactTemplate().render(builder));
			return true;
		}
		return false;
	}

	private void createSkeleton() throws IOException {
		final File destiny = root();
		final File file = new File(destiny, "ui.zip");
		copyResourcesRecursively(this.getClass().getResource("/ui/ui.zip"), file);
		ZipUtil.extract(file, destiny, null, false);
		file.delete();
		new File(destiny, "images").mkdirs();
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

	private Map<String, List<String>> reduce(Map<String, String> map) {
		Map<String, List<String>> reduced = new HashMap<>();
		map.forEach((key, value) -> {
			reduced.putIfAbsent(value, new ArrayList<>());
			reduced.get(value).add(key);
		});
		return reduced;
	}

	private void addWebDependency(Configuration webConf) {
		((LegioConfiguration) TaraUtil.configurationOf(settings.module())).addDependency(WEB, webConf.groupId() + ":" + webConf.artifactId() + ":" + webConf.version());
	}

	private Module findModule(String name) {
		return ApplicationManager.getApplication().
				runReadAction((Computable<Module>) () ->
						stream(ModuleManager.getInstance(project).getModules()).filter(m -> m.getName().equals(toSnakeCase(name))).findFirst().orElse(null));
	}

	private void addExcludeFiles(File parent, ContentEntry contentEntry) {
		final File lib = new File(parent, "lib");
		lib.mkdirs();
		contentEntry.addExcludeFolder(Objects.requireNonNull(VfsUtil.findFileByIoFile(lib, true)));
	}

	private String toSnakeCase(String name) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1-$2";
		return name.replaceAll(regex, replacement).toLowerCase();
	}

	@NotNull
	private String modulePath(UIService service) {
		return project.getBasePath() + File.separator + toSnakeCase(service.name$()) + File.separator + toSnakeCase(service.name$()) + ModuleFileType.DOT_DEFAULT_EXTENSION;
	}

}
