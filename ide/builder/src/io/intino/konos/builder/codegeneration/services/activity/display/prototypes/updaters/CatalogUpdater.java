package io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Catalog;

import java.io.File;

public class CatalogUpdater extends Updater{


	public CatalogUpdater(File sourceFile, Catalog catalog, Project project, String packageName, String box) {
		super(sourceFile, catalog, project, packageName, box);
	}

	public void update() {


	}
}
