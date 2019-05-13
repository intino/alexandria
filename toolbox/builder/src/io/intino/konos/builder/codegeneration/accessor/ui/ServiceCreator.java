package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.ide.highlighter.ModuleFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.WebModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.ArtifactTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.ui.UIService;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.newExternalProvider;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.register;

public class ServiceCreator extends UIRenderer {

	private final Project project;
	private final UIService service;

	private static final String LegioArtifact = "artifact.legio";

	public ServiceCreator(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.project = settings.module() == null ? null : settings.module().getProject();
		this.service = service;
	}

	public void execute() {
		if (settings.module() == null) return;
		Module webModule = getOrCreateModule();
		settings.webModule(webModule);
		new ServiceRenderer(settings, service).execute();
		final boolean created = createConfigurationFile();
		if (created) register(webModule, newExternalProvider(webModule));
	}

	private boolean createConfigurationFile() {
		final Configuration configuration = TaraUtil.configurationOf(settings.module());
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
		File file = new File(accessorRoot(), LegioArtifact);
		if (!file.exists()) {
			write(file.toPath(), ArtifactTemplate.create().format(frame));
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

	private Module getOrCreateModule() {
		return ApplicationManager.getApplication().runWriteAction((Computable<Module>) () -> {
			final ModuleManager manager = ModuleManager.getInstance(project);
			Module[] modules = manager.getModules();
			for (Module m : modules)
				if (m.getName().equals(toSnakeCase(service.name$()))) {
					addModuleDependency(m);
					return m;
				}
			Module webModule = manager.newModule(modulePath(), WebModuleType.WEB_MODULE);
			final ModifiableRootModel model = ModuleRootManager.getInstance(webModule).getModifiableModel();
			final File parent = new File(webModule.getModuleFilePath()).getParentFile();
			parent.mkdirs();
			final VirtualFile vFile = VfsUtil.findFileByIoFile(parent, true);
			final ContentEntry contentEntry = vFile != null ? model.addContentEntry(vFile) : model.addContentEntry(parent.getAbsolutePath());
			addExcludeFiles(parent, contentEntry);
			model.commit();
			addModuleDependency(webModule);
			return webModule;
		});
	}

	private void addExcludeFiles(File parent, ContentEntry contentEntry)  {
		final File lib = new File(parent, "lib");
		lib.mkdirs();
		contentEntry.addExcludeFolder(VfsUtil.findFileByIoFile(lib, true));
	}

	private String toSnakeCase(String name) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1-$2";
		return name.replaceAll(regex, replacement).toLowerCase();
	}

	@NotNull
	private String modulePath() {
		return project.getBasePath() + File.separator + toSnakeCase(service.name$()) + File.separator + toSnakeCase(service.name$()) + ModuleFileType.DOT_DEFAULT_EXTENSION;
	}

	private void addModuleDependency(Module webModule) {
		Module appModule = settings.module();
		LibraryTable table = appModule != null ? LibraryTablesRegistrar.getInstance().getLibraryTable(appModule.getProject()) : null;
		if (table == null) return;
		final ModuleRootManager manager = ModuleRootManager.getInstance(appModule);
		final ModifiableRootModel modifiableModel = manager.getModifiableModel();
		if (manager.isDependsOn(webModule)) return;
		modifiableModel.addModuleOrderEntry(webModule);
		modifiableModel.commit();
	}

}
