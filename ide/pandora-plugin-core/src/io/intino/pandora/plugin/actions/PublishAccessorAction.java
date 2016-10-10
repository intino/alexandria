package io.intino.pandora.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class PublishAccessorAction extends Action implements DumbAware {
	private static final Logger LOG = Logger.getInstance("Export Accessor: export");


	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		if (projectIsNull(e, project)) return;
		makeAndExport(LangDataKeys.MODULE.getData(e.getDataContext()));
	}

	private void makeAndExport(Module module) {
		final CompilerManager compilerManager = CompilerManager.getInstance(module.getProject());
		compilerManager.make(compilerManager.createModulesCompileScope(new Module[]{module}, false), export(module));
	}

	private CompileStatusNotification export(final Module module) {
		return (aborted, errors, warnings, compileContext) -> {
			if (aborted || errors != 0) return;
			ProgressManager.getInstance().run(new Task.Modal(module.getProject(), "Exporting Pandora Accessor", false) {
				@Override
				public void run(@NotNull ProgressIndicator indicator) {
					ProgressManager.getInstance().getProgressIndicator().setIndeterminate(true);
					final AccessorsPublisher publishAccessor = new AccessorsPublisher(module);
					publishAccessor.publish();
				}
			});
		};
	}

	private boolean projectIsNull(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}
}
