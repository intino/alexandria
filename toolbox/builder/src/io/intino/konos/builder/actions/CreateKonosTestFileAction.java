package io.intino.konos.builder.actions;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.JavaCreateTemplateInPackageAction;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import io.intino.konos.builder.KonosIcons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import io.intino.tara.plugin.project.module.ModuleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

import static io.intino.konos.builder.KonosIcons.ICON_16;


public class CreateKonosTestFileAction extends JavaCreateTemplateInPackageAction<PsiJavaFile> {


	public CreateKonosTestFileAction() {
		super("Box Test File", "Creates a new Box File", ICON_16, true);
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
		builder.setTitle("Enter name for new Box Test File");
		builder.addKind("Konos", ICON_16, "Konos");
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName) {
		return "Box Test File";
	}

	@Override
	protected boolean isAvailable(DataContext dataContext) {
		PsiElement data = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
		return (data instanceof PsiFile || data instanceof PsiDirectory) && super.isAvailable(dataContext);
	}

	@Nullable
	@Override
	protected PsiElement getNavigationElement(@NotNull PsiJavaFile createdElement) {
		return createdElement;
	}

	@Nullable
	@Override
	protected PsiJavaFile doCreate(PsiDirectory directory, String newName, String dsl) throws IncorrectOperationException {
		String text = new IntinoTestRenderer(ModuleProvider.moduleOf(directory), directory, newName).execute();
		if (text == null) return null;
		String fileName = newName + "." + JavaFileType.INSTANCE.getDefaultExtension();
		final PsiFileFactory factory = PsiFileFactory.getInstance(directory.getProject());
		PsiFile file = factory.createFileFromText(fileName, JavaFileType.INSTANCE, text);
		file = (PsiFile) directory.add(file);
		return file instanceof PsiJavaFile ? (PsiJavaFile) file : null;
	}

	@Override
	protected void postProcess(PsiJavaFile createdElement, String templateName, Map<String, String> customProperties) {
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
		final String interfaceVersion = configuration == null ? null : configuration.boxVersion();
		e.getPresentation().setVisible(enabled && version.equals(interfaceVersion));
		e.getPresentation().setEnabled(enabled && version.equals(interfaceVersion));
	}
}