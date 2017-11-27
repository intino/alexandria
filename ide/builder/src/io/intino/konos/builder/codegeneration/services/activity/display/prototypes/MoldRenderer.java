package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Mold;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class MoldRenderer extends PrototypeRenderer {
	private final Project project;

	public MoldRenderer(Project project, Mold catalog, File src, File gen, String packageName, String boxName) {
		super(catalog, boxName, packageName, src, gen);
		this.project = project;
	}


	public void render() {
		final Mold mold = this.display.a$(Mold.class);
		Frame frame = createFrame();
		frame.addSlot("moldType", mold.modelClass());
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	@Override
	protected Template template() {
		return customize(MoldTemplate.create());
	}
}
