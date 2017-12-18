package io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Catalog;

import java.io.File;

/**
 * Created by oroncal on 18/12/17.
 */
public abstract class Updater {

	private final File sourceFile;
	private final Catalog catalog;
	private final Project project;
	private final String packageName;
	private final String box;

	public Updater(File sourceFile, Catalog catalog, Project project, String packageName, String box) {

		this.sourceFile = sourceFile;
		this.catalog = catalog;
		this.project = project;
		this.packageName = packageName;
		this.box = box;
	}

	public abstract void update();
}
