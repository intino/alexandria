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
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;
import teseo.codegeneration.FullRenderer;

import java.io.File;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static tara.intellij.lang.psi.impl.TaraUtil.getSourceRoots;

public class CreateShellAction extends Action implements DumbAware {
	private static final Logger LOG = Logger.getInstance("ShellGenerator: Generate");
	private static final String TESEO = "teseo";

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
		if (noProject(e, project) || module == null) return;
		new ShellGenerator(module).generateShell(getGenRoot(module), TaraUtil.getSrcRoot(module));
	}

	private VirtualFile getGenRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "gen".equals(file.getName())) return file;
		return null;
	}

	private boolean noProject(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}

	private class ShellGenerator {
		private final Module module;

		ShellGenerator(Module module) {
			this.module = module;
		}

		void generateShell(VirtualFile genDirectory, VirtualFile srcDirectory) {
			if (genDirectory == null) {
				notifyError("gen source root not found.");
				return;
			}
			String outLanguage = TeseoUtils.findOutLanguage(module);
			if (outLanguage == null || TESEO.equals(outLanguage)) outLanguage = module.getName().toLowerCase();
			String packageName = (outLanguage + File.separator + TESEO).replace("-", "").toLowerCase();
			File gen = new File(genDirectory.getPath(), packageName);
			gen.mkdirs();
			File src = new File(srcDirectory.getPath(), packageName);
			src.mkdirs();
			makeAndGenerate(packageName.replace(File.separator, "."), gen, src);
		}

		private void makeAndGenerate(String packageName, File gen, File api) {
			final CompilerManager compilerManager = CompilerManager.getInstance(module.getProject());
			compilerManager.make(compilerManager.createModulesCompileScope(new Module[]{module}, true), generateShell(packageName, gen, api));
		}

		private CompileStatusNotification generateShell(String packageName, File gen, File src) {
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
					final Graph graph = GraphLoader.loadGraph(module, dest);
					new FullRenderer(graph, src, gen, packageName).execute();
					refreshDirectory(gen);
					refreshDirectory(src);
					notifySuccess();
				}
			};
		}

		private void notifySuccess() {
			final VirtualFile genRoot = getGenRoot(module);
			if (genRoot != null)
				Notifications.Bus.notify(
						new Notification("Teseo", "Services for " + module.getName(), "Generated", INFORMATION), module.getProject());
		}

		private void refreshDirectory(File dir) {
			VirtualFile vDir = VfsUtil.findFileByIoFile(dir, true);
			if (vDir == null || !vDir.isValid()) return;
			VfsUtil.markDirtyAndRefresh(true, true, true, vDir);
			vDir.refresh(true, true);
		}

		private void notifyError(String message) {
			Notifications.Bus.notify(
					new Notification("Teseo", "Services cannot be generated.", message, ERROR), module.getProject());
		}

	}
}
