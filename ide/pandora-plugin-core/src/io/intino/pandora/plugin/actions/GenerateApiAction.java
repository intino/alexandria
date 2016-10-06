package io.intino.pandora.plugin.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import tara.intellij.lang.TaraIcons;
import tara.intellij.lang.psi.TaraModel;
import tara.intellij.lang.psi.TaraNode;
import tara.intellij.lang.psi.impl.TaraNodeImpl;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.intellij.project.TaraModuleType;
import tara.lang.model.Node;
import tara.lang.model.Parameter;
import tara.lang.model.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateApiAction extends AnAction {
	private static final Logger LOG = Logger.getInstance("Export Accessor: export");

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		if (projectIsNull(e, project)) return;
		generate(LangDataKeys.MODULE.getData(e.getDataContext()), LangDataKeys.PSI_FILE.getData(e.getDataContext()));
	}

	private void generate(Module module, PsiFile destiny) {
		TaraModel model = (TaraModel) destiny;
		String api = "dsl pandora\n\n";
		api += "Application(path = \"\", name = \"" + model.getPresentableName() + "-api\")\n" +
				"\tApi(title = \"" + model.getPresentableName() + " API\", path = \"log\") " + model.getPresentableName();
		final List<TaraNode> mainNodesOfFile = getMainNodesOfFile(model);
		for (Node node : mainNodesOfFile)
			api += "\n" + createCrud(node) + "\n";
		write(module, model, api);
	}

	private void write(Module module, TaraModel model, String api) {
		try {
			final File apiRoot = findApiRoot(module);
			Files.write(new File(apiRoot, module.getName().toLowerCase() + File.separator + model.getPresentableName() + "Api.tara").toPath(), api.getBytes());
		} catch (IOException ignored) {
		}
	}

	private File findApiRoot(Module module) {
		for (VirtualFile virtualFile : ModuleRootManager.getInstance(module).getSourceRoots(false))
			if (virtualFile.getName().equals("api")) return new File(virtualFile.getPath());
		return null;
	}

	private String createCrud(Node node) {
		String text = "Resource(name = \"" + node.name().toLowerCase() + "Create\", title = \"Create " + node.name().toLowerCase() + "\", path = \"" + node.name().toLowerCase() + "\", method = Put)\n";
		for (Parameter parameter : node.parameters())
			if (TaraUtil.parameterConstraintOf(parameter).size().isRequired())
				text += "\t\t\tParameter(name = \"" + parameter.name() + "\", in = query)\n";
		text += "\t\t\tResponse(SuccessOk)\n";
		text += "\t\t\tResponse(ErrorBadRequest)";
		return text;
	}


	private boolean projectIsNull(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}

	private static ArrayList<TaraNode> getMainNodesOfFile(TaraModel file) {
		Set<TaraNode> list = new LinkedHashSet<>();
		TaraNode[] nodes = PsiTreeUtil.getChildrenOfType(file, TaraNodeImpl.class);
		if (nodes == null) return new ArrayList<>(list);
		Collections.addAll(list, nodes);
		List<TaraNode> mainNodes = findMainNodes(file);
		for (TaraNode main : mainNodes) {
			if (list.contains(main)) continue;
			list.add(main);
		}
		return new ArrayList<>(list);
	}

	private static List<TaraNode> findMainNodes(TaraModel file) {
		final TaraNode[] childrenOfType = PsiTreeUtil.getChildrenOfType(file, TaraNode.class);
		if (childrenOfType == null) return Collections.emptyList();
		final List<TaraNode> rootNodes = Arrays.asList(childrenOfType);
		return rootNodes.stream().filter((node) -> !isAnnotatedAsComponent((TaraNodeImpl) node)).collect(Collectors.toList());
	}

	private static boolean isAnnotatedAsComponent(TaraNodeImpl node) {
		for (Tag flag : node.flags())
			if (flag.equals(Tag.Component)) return true;
		return false;
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		final Project project = e.getData(CommonDataKeys.PROJECT);
		final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
		boolean enabled = project != null && module != null && TaraModuleType.isTara(module);
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
		e.getPresentation().setIcon(TaraIcons.LOGO_16);
		if (enabled) e.getPresentation().setText("Generate api for model");
	}
}
