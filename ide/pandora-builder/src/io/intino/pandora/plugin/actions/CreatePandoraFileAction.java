package io.intino.pandora.plugin.actions;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.JavaCreateTemplateInPackageAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import io.intino.pandora.plugin.PandoraIcons;
import io.intino.pandora.plugin.file.PandoraFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tara.compiler.shared.Configuration;
import tara.intellij.actions.utils.TaraTemplates;
import tara.intellij.actions.utils.TaraTemplatesFactory;
import tara.intellij.lang.psi.impl.TaraModelImpl;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.intellij.messages.MessageProvider;

import java.io.File;
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
		return (data instanceof PsiFile || data instanceof PsiDirectory) && super.isAvailable(dataContext);
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

	@SuppressWarnings("Duplicates")
	@Override
	public void update(AnActionEvent e) {
		e.getPresentation().setIcon(PandoraIcons.ICON_16);
		final Module module = e.getData(LangDataKeys.MODULE);
		boolean enabled = module != null && legioFile(module).exists();
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		String version = file.getParentFile().getName();
		final Configuration configuration = TaraUtil.configurationOf(module);
		if (configuration == null) enabled = false;
		final String interfaceVersion = configuration == null ? null : configuration.interfaceVersion();
		e.getPresentation().setVisible(enabled && version.equals(interfaceVersion));
		e.getPresentation().setEnabled(enabled && version.equals(interfaceVersion));
	}

	@NotNull
	private File legioFile(Module module) {
		File moduleRoot = new File(module.getModuleFilePath()).getParentFile();
		return new File(moduleRoot, "configuration.legio");
	}
}