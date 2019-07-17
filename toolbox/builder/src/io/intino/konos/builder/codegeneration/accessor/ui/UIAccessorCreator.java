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
import io.intino.itrules.FrameBuilder;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static io.intino.konos.builder.codegeneration.Formatters.camelCaseToSnakeCase;
import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.plugin.dependencyresolution.DependencyCatalog.DependencyScope.WEB;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.newExternalProvider;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.register;
import static java.util.Arrays.stream;

public class UIAccessorCreator {
	private static final String ARTIFACT_LEGIO = "artifact.legio";
	private final Project project;
	private final Module javaModule;
	private final List<UIService> serviceList;
	private String parent;

	public UIAccessorCreator(Module module, KonosGraph graph, String parent) {
		this.project = module == null ? null : module.getProject();
		this.javaModule = module;
		this.serviceList = graph.uIServiceList();
		this.parent = parent;
	}

	public void execute() {
		if (javaModule == null) return;
		for (UIService service : serviceList) {
			new UIAccessorRenderer(javaModule, getOrCreateModule(service), service, parent).execute();
		}
	}

	private Module getOrCreateModule(UIService service) {
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
		final Configuration configuration = TaraUtil.configurationOf(javaModule);
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
		File file = new File(moduleRoot, ARTIFACT_LEGIO);
		if (!file.exists()) {
			write(file.toPath(), new ArtifactTemplate().render(builder));
			return true;
		}
		return false;
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
		((LegioConfiguration) TaraUtil.configurationOf(javaModule)).addDependency(WEB, webConf.groupId() + ":" + webConf.artifactId() + ":" + webConf.version());
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
