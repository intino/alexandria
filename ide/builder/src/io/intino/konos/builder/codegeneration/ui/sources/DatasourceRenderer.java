package io.intino.konos.builder.codegeneration.ui.sources;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.services.ui.templates.DatasourceTemplate;
import io.intino.konos.builder.codegeneration.ui.ElementRenderer;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Datasource;
import io.intino.konos.model.graph.classified.ClassifiedDatasource.Grouping;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.model.graph.indexed.IndexedDatasource.Sorting;

public class DatasourceRenderer extends ElementRenderer<Datasource> {

	protected DatasourceRenderer(Settings settings, Datasource element, TemplateProvider templateProvider, Target target) {
		super(settings, element, templateProvider, target);
	}

	public void execute() {
		Frame frame = buildFrame("");
		Commons.writeFrame(new File(src(), format(Sources)), snakeCaseToCamelCase(element.name$()), setup(DatasourceTemplate.create()).format(frame));
		frame = buildFrame("abstract");
		Commons.writeFrame(new File(gen(), format(Sources)), "Abstract" + snakeCaseToCamelCase(element.name$()), setup(DatasourceTemplate.create()).format(frame));
	}

	public Frame buildFrame(String type) {
		Frame frame = super.buildFrame().addTypes("datasource", type);
		frame.addSlot("name", element.name$());
		frame.addSlot("modelClass", element.modelClass());
		addGroupings(frame, type);
		addSortings(frame, type);
		return frame;
	}

	private void addGroupings(Frame frame, String type) {
		if (!element.isClassified()) return;
		element.asClassified().groupingList().forEach(g -> addGrouping(g, frame, type));
	}

	private void addGrouping(Grouping grouping, Frame frame, String type) {
		Frame result = new Frame("grouping", type);
		result.addSlot("name", grouping.name$());
		result.addSlot("modelClass", element.modelClass());
		frame.addSlot("grouping", result);
	}

	private void addSortings(Frame frame, String type) {
		if (!element.isIndexed()) return;
		element.asIndexed().sortingList().forEach(s -> addSorting(s, frame, type));
	}

	private void addSorting(Sorting sorting, Frame frame, String type) {
		Frame result = new Frame("sorting", type);
		result.addSlot("name", sorting.name$());
		result.addSlot("modelClass", element.modelClass());
		frame.addSlot("sorting", result);
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return null;
	}

}