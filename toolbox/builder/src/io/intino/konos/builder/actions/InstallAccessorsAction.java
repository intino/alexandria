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
import io.intino.konos.builder.KonosIcons;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class InstallAccessorsAction extends KonosAction implements DumbAware {
	private static final Logger LOG = Logger.getInstance("Publish Accessor: publish");
	private static final String TEXT = "Publish Konos Accessor";

	public InstallAccessorsAction() {
		super(TEXT, "Publish Konos Accessor in Artifactory", KonosIcons.GENERATE_16);
		this.setShortcutSet(CustomShortcutSet.fromString("control alt A"));
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		e.getPresentation().setIcon(KonosIcons.PUBLISH_16);
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
		publish(Objects.requireNonNull(LangDataKeys.MODULE.getData(e.getDataContext())));
	}

	private void publish(Module module) {
		ProgressManager.getInstance().run(new Task.Backgroundable(module.getProject(), "Publishing Konos Accessor", false) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {
				final KonosGraph graph = new GraphLoader().loadGraph(module);
				final Configuration configuration = TaraUtil.configurationOf(module);
				String generationPackage = configuration != null ? configuration.workingPackage() + ".box" : "konos";
				ProgressManager.getInstance().getProgressIndicator().setIndeterminate(true);
				Settings settings = settings(module, generationPackage);
				new AccessorsPublisher(settings, graph).install();
			}
		});
	}

	private Settings settings(Module module, String packageName) {
		return new Settings().module(module).packageName(packageName).cache(new ElementCache());
	}

	private boolean projectIsNull(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}
}
