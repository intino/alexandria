package org.siani.teseo.plugin.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import tara.io.StashDeserializer;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.server.web.JavaServerRenderer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.stream.Collectors;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static java.util.Arrays.asList;

public class CreateServicesAction extends Action implements DumbAware {
	private static final Logger LOG = Logger.getInstance("restApiGenerator: Generate");
	private static final String TESEO = "teseo";

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
		if (projectExists(e, project)) return;
		List<VirtualFile> files = filterTeseo(getFilesFromEvent(e));
		if (files.isEmpty()) return;
		new ApiGenerator(module).generateApi(getGenRoot(module), getApiRoot(module));
	}

	private List<VirtualFile> filterTeseo(List<VirtualFile> filesFromEvent) {
		return filesFromEvent;
	}

	@NotNull
	private static List<VirtualFile> getFilesFromEvent(AnActionEvent e) {
		VirtualFile[] files = LangDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
		if ((files == null) || (files.length == 0)) return Collections.emptyList();
		final List<VirtualFile> virtualFiles = asList(files);
		return virtualFiles.stream().filter(f -> f.getName().endsWith(".tara")).collect(Collectors.toList());
	}

	private VirtualFile getGenRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "gen".equals(file.getName())) return file;
		return null;
	}

	private VirtualFile getApiRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "api".equals(file.getName())) return file;
		return null;
	}

	private static List<VirtualFile> getSourceRoots(@NotNull Module module) {
		final Set<VirtualFile> result = new LinkedHashSet<>();
		final ModuleRootManager manager = ModuleRootManager.getInstance(module);
		result.addAll(asList(manager.getSourceRoots()));
		result.addAll(asList(manager.getContentRoots()));
		return new ArrayList<>(result);
	}

	private boolean projectExists(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}

	private class ApiGenerator {
		private final Module module;

		ApiGenerator(Module module) {
			this.module = module;
		}

		void generateApi(VirtualFile genDirectory, VirtualFile apiDirectory) {
			if (genDirectory == null) {
				notifyError("gen source root not found.");
				return;
			}
			String outLanguage = TeseoUtils.findOutLanguage(module);
			if (outLanguage == null || TESEO.equals(outLanguage)) outLanguage = module.getName().toLowerCase();
			String packageName = (TESEO + File.separator + outLanguage).replace("-", "").toLowerCase();
			File gen = new File(genDirectory.getPath(), packageName);
			gen.mkdirs();
			File api = new File(apiDirectory.getPath(), packageName);
			api.mkdirs();
			makeAndGenerate(packageName.replace(File.separator, "."), gen, api);
		}

		private void makeAndGenerate(String packageName, File gen, File api) {
			final CompilerManager compilerManager = CompilerManager.getInstance(module.getProject());
			compilerManager.make(compilerManager.createModulesCompileScope(new Module[]{module}, true), generateApi(packageName, gen, api));
		}

		private CompileStatusNotification generateApi(String packageName, File gen, File api) {
			return new CompileStatusNotification() {
				public void finished(final boolean aborted, final int errors, final int warnings, final CompileContext compileContext) {
					generate();
				}

				private void generate() {
					final String teseoFile = TeseoUtils.findTeseo(module);
					if (teseoFile == null) {
						notifyError("Teseo File corrupt or not found");
						return;
					}
					final File file = new File(teseoFile);
					final File dest = file.getName().endsWith(TeseoUtils.STASH) ? new File(file.getParent(), TeseoUtils.findOutLanguage(module) + "." + TESEO) : file;
					final Graph graph = loadGraph(dest);
					new JavaServerRenderer(graph).execute(gen, api, packageName);
					refreshDirectory(gen);
					refreshDirectory(api);
					notifySuccess();
				}
			};
		}

		private Graph loadGraph(File teseoFile) {
			final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
			final ClassLoader temporalLoader = createClassLoader(new File(CompilerModuleExtension.getInstance(module).getCompilerOutputPath().getPath()));
			Thread.currentThread().setContextClassLoader(temporalLoader);
			final Graph graph = Graph.from(StashDeserializer.stashFrom(teseoFile)).wrap(TeseoApplication.class);
			Thread.currentThread().setContextClassLoader(currentLoader);
			return graph;
		}

		private ClassLoader createClassLoader(File directory) {
			return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
				try {
					return new URLClassLoader(new URL[]{directory.toURI().toURL()}, this.getClass().getClassLoader());
				} catch (MalformedURLException e) {
					LOG.error(e.getMessage(), e);
					return null;
				}
			});
		}

		private void notifySuccess() {
			final VirtualFile genRoot = getGenRoot(module);
			if (genRoot != null)
				Notifications.Bus.notify(
						new Notification("Teseo", "Services for " + module.getName() + " generated", "", INFORMATION), module.getProject());
		}

		private void refreshDirectory(File dir) {
			VirtualFile vDir = VfsUtil.findFileByIoFile(dir, true);
			if (vDir == null || !vDir.isValid()) return;
			VfsUtil.markDirtyAndRefresh(true, true, true, vDir);
			vDir.refresh(true, true);
		}

		private void notifyError(String message) {
			Notifications.Bus.notify(
					new Notification("Teseo", "Services cannot be generated. " + message, "", ERROR), module.getProject());
		}

	}
}
