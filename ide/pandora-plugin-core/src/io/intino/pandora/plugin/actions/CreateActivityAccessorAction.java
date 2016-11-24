package io.intino.pandora.plugin.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import tara.StashBuilder;
import tara.dsl.Pandora;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.io.Stash;
import tara.magritte.Graph;

import java.io.File;
import java.util.Arrays;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;

public class CreateActivityAccessorAction extends PandoraAction implements DumbAware {
	private static final Logger LOG = Logger.getInstance("Create Activity Accessor");

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		if (projectIsNull(e, project)) return;
		create(LangDataKeys.MODULE.getData(e.getDataContext()));
	}

	private void create(Module module) {
		final Stash[] stashes = PandoraUtils.findPandoraFiles(module).stream().map(p -> new StashBuilder(new File(p.getVirtualFile().getPath()), new Pandora(), module.getName()).build()).toArray(Stash[]::new);
		if (Arrays.stream(stashes).filter(stash -> stash == null).count() > 0) {
			notifyError(module, "Models have errors");
			return;
		} else if (TaraUtil.configurationOf(module) == null) {
			notifyError(module, "Module configuration not found");
			return;
		}
		final Graph graph = GraphLoader.loadGraph(stashes).graph();
		new ActivityAccessorCreator(module, graph).execute();
		notifySuccess(module);
	}

	private void notifySuccess(Module module) {
		Notifications.Bus.notify(
				new Notification("Pandora", "Activity accessor for " + module.getName(), "Generated", INFORMATION), module.getProject());
	}

	private void notifyError(Module module, String message) {
		Notifications.Bus.notify(
				new Notification("Pandora", "Activity cannot be generated.", message, ERROR), module.getProject());
	}

	private boolean projectIsNull(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}
}
