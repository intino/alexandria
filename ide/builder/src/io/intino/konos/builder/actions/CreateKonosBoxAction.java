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
	private static final String TEXT = "Create Konos Box";

	public CreateKonosBoxAction() {
		super(TEXT, "Creates Konos Box", KonosIcons.ICON_16);
		this.setShortcutSet(CustomShortcutSet.fromString("control alt S"));
	}

	@Override
	public void update(AnActionEvent e) {
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
		new KonosGenerator(module, konosFiles).generate(getGenRoot(module), getSrcRoot(module));
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

		void generate(VirtualFile genDirectory, VirtualFile srcDirectory) {
			if (genDirectory == null) {
				notifyError("gen source root not found.");
				return;
			}
			final Configuration configuration = configurationOf(module);
			String generationPackage = configuration == null ? KONOS.toLowerCase() : configuration.workingPackage() + "." + KONOS.toLowerCase();
			File gen = new File(genDirectory.getPath(), generationPackage.replace(".", File.separator));
			gen.mkdirs();
			File src = new File(srcDirectory.getPath(), generationPackage.replace(".", File.separator));
			src.mkdirs();
			generate(generationPackage, gen, src);
		}

		private void generate(String packageName, File gen, File src) {
			final Stash stash = new StashBuilder(konosFiles.stream().map(pf -> new File(pf.getVirtualFile().getPath())).collect(Collectors.toList()), new Konos(), module.getName()).build();
			if (stash == null) {
				notifyError("Models have errors");
				return;
			}
			try {
				new FullRenderer(module, GraphLoader.loadGraph(stash).graph(), src, gen, packageName).execute();
			} catch (Exception e) {
				e.printStackTrace();
				notifyError(e.getMessage() == null ? e.toString() : e.getMessage());
				return;
			}
			refreshDirectory(gen);
			refreshDirectory(src);
			notifySuccess();
		}

		private void notifySuccess() {
			final VirtualFile genRoot = getGenRoot(module);
			if (genRoot != null)
				Notifications.Bus.notify(
						new Notification("Konos", "Services for " + module.getName(), "Generated", INFORMATION), module.getProject());
		}

		private void notifyError(String message) {
			Notifications.Bus.notify(
					new Notification("Konos", "Services cannot be generated", message, ERROR), module.getProject());
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
		final VirtualFile genDirectory = createGenDirectory(module);
		PsiTestUtil.addSourceRoot(module, genDirectory, JavaSourceRootType.SOURCE);
		return genDirectory;
	}

	private VirtualFile createGenDirectory(Module module) {
		try {
			final VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
			return VfsUtil.createDirectoryIfMissing(contentRoots[0], "gen");
		} catch (IOException e) {
			return null;
		}
	}

}
