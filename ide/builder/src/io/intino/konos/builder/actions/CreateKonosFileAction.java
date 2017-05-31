package io.intino.konos.builder.actions;

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
import io.intino.konos.builder.KonosIcons;
import io.intino.konos.builder.file.KonosFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.actions.utils.TaraTemplates;
import io.intino.tara.plugin.actions.utils.TaraTemplatesFactory;
import io.intino.tara.plugin.lang.psi.impl.TaraModelImpl;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import io.intino.tara.plugin.messages.MessageProvider;

import java.io.File;
import java.util.Map;

import static io.intino.konos.builder.KonosIcons.ICON_16;


public class CreateKonosFileAction extends JavaCreateTemplateInPackageAction<TaraModelImpl> {


	public CreateKonosFileAction() {
		super("Box File", "Creates a new Box File", ICON_16, true);
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
		builder.setTitle("Enter name for new Box File");
		builder.addKind("Konos", ICON_16, "Konos");
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName) {
		return "Box File";
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
		String fileName = newName + "." + KonosFileType.instance().getDefaultExtension();
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
		e.getPresentation().setIcon(KonosIcons.ICON_16);
		final Module module = e.getData(LangDataKeys.MODULE);
		boolean enabled = module != null;
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		String version = file.getParentFile().getName();
		final Configuration configuration = TaraUtil.configurationOf(module);
		if (configuration == null) enabled = false;
		final String interfaceVersion = configuration == null ? null : configuration.interfaceVersion();
		e.getPresentation().setVisible(enabled && version.equals(interfaceVersion));
		e.getPresentation().setEnabled(enabled && version.equals(interfaceVersion));
	}

}