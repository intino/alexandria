package io.intino.konos.builder.codegeneration.services.ui.display.mold;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.mold.MoldTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.Mold.Block;
import io.intino.konos.model.graph.Mold.Block.Stamp;
import org.siani.itrules.Template;

import java.io.File;
import java.util.List;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class MoldUpdater extends Updater {
	private final MoldRenderer moldRenderer;
	private final Template template;
	private PsiClass stamps;
	private Mold mold;
	private PsiClass psiClass;

	public MoldUpdater(File sourceFile, Mold mold, Project project, String packageName, String box) {
		super(sourceFile, project, packageName, box);
		this.mold = mold;
		this.moldRenderer = new MoldRenderer(project, mold, null, null, packageName, box);
		this.template = Formatters.customize(MoldTemplate.create());
		if (file != null && file instanceof PsiJavaFile && ((PsiJavaFile) file).getClasses().length != 0) {
			psiClass = ((PsiJavaFile) file).getClasses()[0];
			if ((stamps = innerClass(psiClass, "Stamps")) == null) {
				PsiClass stamps = createInnerClass("Stamps");
				psiClass.addAfter(psiClass.getConstructors()[0].getNextSibling(), stamps);
				this.stamps = stamps;
			}
		}
	}

	public void update() {
		if (psiClass == null) return;
		if (!ApplicationManager.getApplication().isWriteAccessAllowed()) runWriteCommandAction(project, this::updateMold);
		else updateMold();
	}

	private void updateMold() {
		update(mold.blockList());
	}

	private void update(List<Block> blocks) {
		for (Block block : blocks) {
			updateStamps(block.stampList());
			update(block.blockList());
		}
	}

	private void updateStamps(List<Stamp> stamps) {
		for (Stamp stamp : stamps) if (!exists(stamp)) update(stamp);
	}

	private void update(Stamp stamp) {
		String text = classFor(stamp);
		if (!text.isEmpty()) stamps.addAfter(createClass(text, psiClass), stamps.getLBrace().getNextSibling());
	}

	private boolean exists(Stamp stamp) {
		return innerClass(stamps, firstUpperCase(stamp.name$())) != null;
	}

	private String classFor(Stamp stamp) {
		return template.format(moldRenderer.frameOf(stamp));
	}


}
