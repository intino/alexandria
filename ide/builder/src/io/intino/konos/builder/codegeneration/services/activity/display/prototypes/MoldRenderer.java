package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Mold;

import java.io.File;

public class MoldRenderer {
	private final Project project;
	private Mold catalog;
	private final File gen;
	private final File src;
	private final String packageName;
	private final String boxName;

	public MoldRenderer(Project project, Mold catalog, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.catalog = catalog;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}


	public void render() {

	}
}
