package io.intino.pandora.plugin.actions;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.JavaCreateTemplateInPackageAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import io.intino.pandora.plugin.file.PandoraFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import tara.intellij.actions.utils.TaraTemplates;
import tara.intellij.actions.utils.TaraTemplatesFactory;
import tara.intellij.lang.psi.impl.TaraModelImpl;
import tara.intellij.messages.MessageProvider;
import tara.intellij.project.module.ModuleProvider;

import java.util.List;
import java.util.Map;

import static io.intino.pandora.plugin.PandoraIcons.ICON_16;


public class CreatePandoraFileAction extends JavaCreateTemplateInPackageAction<TaraModelImpl> {

	public CreatePandoraFileAction() {
		super("Pandora File", "Creates a new Pandora File", ICON_16, true);
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
		builder.setTitle("Enter name for new Pandora File");
		builder.addKind("Pandora", ICON_16, "Pandora");
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName) {
		return "Pandora File";
	}

	@Override
	protected boolean isAvailable(DataContext dataContext) {
		PsiElement data = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
		if (!(data instanceof PsiFile || data instanceof PsiDirectory)) return false;
		Module module = ModuleProvider.moduleOf(data);
		return super.isAvailable(dataContext) && isInApi(data instanceof PsiDirectory ? (PsiDirectory) data : (PsiDirectory) data.getParent(), module);
	}

	@Nullable
	@Override
	protected PsiElement getNavigationElement(@NotNull TaraModelImpl createdElement) {
		return createdElement;
	}

	@Nullable
	@Override
	protected TaraModelImpl doCreate(PsiDirectory directory, String newName, String dsl) throws IncorrectOperationException {
		String template = TaraTemplates.getTemplate("FILE");
		String fileName = newName + "." + PandoraFileType.instance().getDefaultExtension();
		PsiFile file = TaraTemplatesFactory.createFromTemplate(directory, newName, fileName, template, true, "DSL", dsl);
		return file instanceof TaraModelImpl ? (TaraModelImpl) file : error(file);
	}

	private boolean isInApi(PsiDirectory dir, Module module) {
		for (VirtualFile root : srcContentRoots(module))
			if (isIn(root, dir) && "api".equalsIgnoreCase(root.getName())) return true;
		return false;
	}

	private boolean isIn(VirtualFile sourceRoot, PsiElement dir) {
		if (sourceRoot == null) return false;
		PsiElement parent = dir;
		while (parent != null && !sourceRoot.equals(virtualFileOf(parent)))
			parent = parent.getParent();
		return parent != null && virtualFileOf(parent).equals(sourceRoot);
	}

	private VirtualFile virtualFileOf(PsiElement element) {
		return element instanceof PsiDirectory ? ((PsiDirectory) element).getVirtualFile() : ((PsiFile) element).getVirtualFile();
	}

	private List<VirtualFile> srcContentRoots(Module module) {
		return ModuleRootManager.getInstance(module).getSourceRoots(JavaModuleSourceRootTypes.SOURCES);
	}


	private TaraModelImpl error(PsiFile file) {
		final String description = file.getFileType().getDescription();
		throw new IncorrectOperationException(MessageProvider.message("tara.file.extension.is.not.mapped.to.tara.file.type", description));
	}

	@Override
	protected void postProcess(TaraModelImpl createdElement, String templateName, Map<String, String> customProperties) {
		super.postProcess(createdElement, templateName, customProperties);
		setCaret(createdElement);
		createdElement.navigate(true);
	}

	private void setCaret(PsiFile file) {
		final PsiDocumentManager instance = PsiDocumentManager.getInstance(file.getProject());
		Document doc = instance.getDocument(file);
		if (doc == null) return;
		instance.commitDocument(doc);
	}
}