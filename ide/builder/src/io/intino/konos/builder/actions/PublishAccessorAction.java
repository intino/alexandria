package io.intino.konos.builder.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.builder.KonosIcons;
import io.intino.konos.builder.utils.KonosUtils;
import org.jetbrains.annotations.NotNull;
import io.intino.tara.StashBuilder;
import io.intino.tara.compiler.shared.Configuration;
import tara.dsl.Konos;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class PublishAccessorAction extends KonosAction implements DumbAware {
	private static final Logger LOG = Logger.getInstance("Publish Accessor: publish");
	private static final String TEXT = "Publish Konos Accessor";

	public PublishAccessorAction() {
		super(TEXT, "Publish Konos Accessor in Artifactory", KonosIcons.ICON_16);
		this.setShortcutSet(CustomShortcutSet.fromString("control alt A"));
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		final Module module = e.getData(LangDataKeys.MODULE);
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		String version = file.getParentFile().getName();
		if (module != null) e.getPresentation().setText(TEXT + " for " + module.getName() + " (" + version + ")");
	}

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		if (projectIsNull(e, project)) return;
		publish(LangDataKeys.MODULE.getData(e.getDataContext()));
	}

	private void publish(Module module) {
		ProgressManager.getInstance().run(new Task.Modal(module.getProject(), "Publishing Konos Accessor", false) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {
				final Configuration configuration = TaraUtil.configurationOf(module);
				String generationPackage = configuration != null ? configuration.workingPackage() : "konos";
				final Stash[] stashes = KonosUtils.findKonosFiles(module).stream().map(p -> new StashBuilder(new File(p.getVirtualFile().getPath()), new Konos(), module.getName()).build()).toArray(Stash[]::new);
				final Graph graph = GraphLoader.loadGraph(stashes).graph();
				ProgressManager.getInstance().getProgressIndicator().setIndeterminate(true);
				new AccessorsPublisher(module, graph, generationPackage).publish();
			}
		});
	}

	private boolean projectIsNull(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}
}
