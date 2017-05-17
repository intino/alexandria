package io.intino.konos.builder.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.PsiTestUtil;
import io.intino.konos.builder.KonosIcons;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.builder.utils.KonosUtils;
import io.intino.tara.StashBuilder;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import tara.dsl.Konos;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.*;

public class CreateKonosBoxAction extends KonosAction {
	private static final Logger LOG = Logger.getInstance("CreateKonosBoxAction: ");
	private static final String KONOS = "Konos";
	private static final String BOX = "box";
	private static final String TEXT = "Create Konos Box";

	public CreateKonosBoxAction() {
		super(TEXT, "Creates Konos Box", KonosIcons.GENERATE_16);
		this.setShortcutSet(CustomShortcutSet.fromString("control alt S"));
	}

	@Override
	public void update(AnActionEvent e) {
		e.getPresentation().setIcon(KonosIcons.GENERATE_16);
		super.update(e);
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		String version = file.getParentFile().getName();
		final Module module = e.getData(LangDataKeys.MODULE);
		if (module != null) e.getPresentation().setText(TEXT + " for " + module.getName() + " (" + version + ")");
	}

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
		if (noProject(e, project) || module == null) return;
		project.save();
		FileDocumentManager.getInstance().saveAllDocuments();
		List<PsiFile> konosFiles = KonosUtils.findKonosFiles(module);
		new KonosGenerator(module, konosFiles).generate(getSrcRoot(module), getGenRoot(module), getResRoot(module), getTestRoot(module));
	}


	private boolean noProject(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}

	private class KonosGenerator {

		private final Module module;
		private final List<PsiFile> konosFiles;

		KonosGenerator(Module module, List<PsiFile> konosFiles) {
			this.module = module;
			this.konosFiles = konosFiles;
		}

		void generate(VirtualFile srcDirectory, VirtualFile genDirectory, VirtualFile resDirectory, VirtualFile testDirectory) {
			if (genDirectory == null) {
				notifyError("gen source root not found.");
				return;
			}
			final Configuration configuration = configurationOf(module);
			String generationPackage = configuration == null ? BOX : configuration.workingPackage() + "." + BOX;
			File gen = new File(genDirectory.getPath(), generationPackage.replace(".", File.separator));
			gen.mkdirs();
			File src = new File(srcDirectory.getPath(), generationPackage.replace(".", File.separator));
			File test = new File(testDirectory.getPath(), generationPackage.replace(".", File.separator));
			src.mkdirs();
			generate(generationPackage, gen, src, new File(resDirectory.getPath()), test);
		}

		private void generate(String packageName, File gen, File src, File res, File test) {
			Graph graph = loadGraph();
			if (!render(packageName, gen, src, res, test, graph)) return;
			refreshDirectories(gen, src, res, test);
			notifySuccess();
		}

		private Graph loadGraph() {
			if (!konosFiles.isEmpty()) {
				final Stash stash = new StashBuilder(konosFiles.stream().map(pf -> new File(pf.getVirtualFile().getPath())).collect(Collectors.toList()), new Konos(), module.getName()).build();
				if (stash == null) {
					notifyError("Models have errors");
					return null;
				} else return GraphLoader.loadGraph(stash).graph();
			} else return GraphLoader.loadGraph().graph();
		}

		private void refreshDirectories(File gen, File src, File res, File test) {
			refreshDirectory(gen);
			refreshDirectory(src);
			refreshDirectory(test);
			refreshDirectory(res);
		}

		private boolean render(String packageName, File gen, File src, File res, File test, Graph graph) {
			try {
				if (graph == null) throw new Exception("Model have error");
				new FullRenderer(module, graph, src, gen, res, test, packageName).execute();
			} catch (Exception e) {
				e.printStackTrace();
				notifyError(e.getMessage() == null ? e.toString() : e.getMessage());
				return false;
			}
			return true;
		}

		private void notifySuccess() {
			final VirtualFile genRoot = getGenRoot(module);
			if (genRoot != null)
				Notifications.Bus.notify(
						new Notification("Boxing", "Services for " + module.getName(), "Generated", INFORMATION), module.getProject());
		}

		private void notifyError(String message) {
			Notifications.Bus.notify(
					new Notification("Boxing", "Services cannot be generated", message, ERROR), module.getProject());
		}

		private void refreshDirectory(File dir) {
			VirtualFile vDir = VfsUtil.findFileByIoFile(dir, true);
			if (vDir == null || !vDir.isValid()) return;
			VfsUtil.markDirtyAndRefresh(true, true, true, vDir);
			vDir.refresh(true, true);
		}
	}

	private VirtualFile getGenRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "gen".equals(file.getName())) return file;
		final VirtualFile genDirectory = createDirectory(module, "gen");
		PsiTestUtil.addSourceRoot(module, genDirectory, JavaSourceRootType.SOURCE);
		return genDirectory;
	}

	private VirtualFile getResRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "res".equals(file.getName())) return file;
		final VirtualFile resDirectory = createDirectory(module, "res");
		PsiTestUtil.addSourceRoot(module, resDirectory, JavaResourceRootType.RESOURCE);
		return resDirectory;
	}

	private VirtualFile getTestRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "test".equals(file.getName())) return file;
		final VirtualFile testDirectory = createDirectory(module, "test");
		PsiTestUtil.addSourceRoot(module, testDirectory, JavaSourceRootType.TEST_SOURCE);
		return testDirectory;
	}

	private VirtualFile createDirectory(Module module, String name) {
		try {
			final VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
			return VfsUtil.createDirectoryIfMissing(contentRoots[0], name);
		} catch (IOException e) {
			return null;
		}
	}

}
