package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Catalog;

import java.io.File;

public class CatalogUpdater extends Updater {

	public CatalogUpdater(File file, Catalog catalog, Project project, String packageName, String box) {
		super(file, project, packageName, box);
	}

	@Override
	public void update() {
	}
	/*
	private Catalog catalog;
	private final Template template;

	public CatalogUpdater(File sourceFile, Catalog catalog, Project project, String packageName, String box) {
		super(sourceFile, project, packageName, box);
		this.catalog = catalog;
		this.template = Formatters.customize(CatalogSrcTemplate.create());
	}

	public void update() {
		if (file == null || !(file instanceof PsiJavaFile) || ((PsiJavaFile) file).getClasses()[0] == null) return;
		final PsiClass psiClass = ((PsiJavaFile) file).getClasses()[0];
		if (!ApplicationManager.getApplication().isWriteAccessAllowed()) runWriteCommandAction(project, () -> update(psiClass));
		else update(psiClass);
	}

	private void update(PsiClass psiClass) {
		if (catalog.i$(TemporalCatalog.class)) updateTemporalMethods(psiClass);
		updateOperationMethods(psiClass);
		updateArrangementMethods(psiClass);
		updateViewMethods(psiClass);
		updateEventMethods(psiClass);
	}

	private void updateTemporalMethods(PsiClass psiClass) {
		PsiClass source = innerClass(psiClass, "Source");
		PsiClass temporal = innerClass(psiClass, "Temporal");
		if (temporal == null) psiClass.addAfter(createClass(temporalClass(), psiClass), constructorOf(psiClass));
		final String createdMethod = firstLowerCase(shortType(catalog.itemClass())) + "Created";
		if (source.findMethodsByName(createdMethod, false).length == 0)
			source.addAfter(createMethod(createdMethod, "java.time.Instant"), constructorOf(psiClass));
	}

	private void updateOperationMethods(PsiClass psiClass) {
		if (catalog.toolbar() == null) return;
		PsiClass toolbar = innerClass(psiClass, "Toolbar");
		if (toolbar == null)
			toolbar = (PsiClass) psiClass.addBefore(createInnerClass("Toolbar"), innerClass(psiClass, "Source").getPrevSibling());
		for (Operation operation : catalog.toolbar().operations()) {
			final String operationType = operation.getClass().getSimpleName();
			final PsiMethod[] methods = toolbar.findMethodsByName(firstLowerCase(operationType), false);
			if (methods.length == 0) {
				String text = operationMethodText(operation);
				if (text != null && !text.isEmpty()) toolbar.add(createMethodFromText(text));
			}
		}
	}

	private void updateArrangementMethods(PsiClass psiClass) {
		if (catalog.content() == null) return;
		PsiClass arrangements = innerClass(psiClass, "Arrangements");
		if (arrangements == null)
			arrangements = (PsiClass) psiClass.addAfter(createInnerClass("Arrangements"), innerClass(psiClass, "Source").getNextSibling());
		updateGroupings(arrangements);
		updateSortings(arrangements);
	}

	private void updateGroupings(PsiClass arrangements) {
		for (Grouping grouping : catalog.content().groupingList())
			if (arrangements.findMethodsByName(firstLowerCase(grouping.name$()), false).length == 0) {
				String text = groupingMethodText(grouping);
				if (text != null && !text.isEmpty()) arrangements.add(createMethodFromText(text));
			}
	}

	private void updateSortings(PsiClass arrangements) {
		for (Sorting sorting : catalog.content().sortingList())
			if (arrangements.findMethodsByName(firstLowerCase(sorting.name$()) + "Comparator", false).length == 0) {
				String text = sortingMethodText(sorting);
				if (text != null && !text.isEmpty()) arrangements.add(createMethodFromText(text));
			}
	}

	private void updateEventMethods(PsiClass psiClass) {
		if (catalog.events() == null) return;
		Catalog.Events.OnClickItem.CatalogEvent event = catalog.events().onClickItem().catalogEvent();
		PsiClass events = innerClass(psiClass, "Events");
		if (events == null)
			events = (PsiClass) psiClass.addAfter(createInnerClass("Events"), innerClass(psiClass, "Source").getNextSibling());
		if (event.i$(OpenPanel.class)) processPanel(event, events);
		else if (event.i$(OpenCatalog.class)) processCatalog(event, events);
		else processDialog(event, events);
	}

	private void processDialog(Catalog.Events.OnClickItem.CatalogEvent event, PsiClass events) {
		if (events.findMethodsByName("onOpenDialogPath", false).length == 0) {
			String text = openDialogMethodText(event.a$(OpenDialog.class));
			if (text != null && !text.isEmpty()) events.add(createMethodFromText(text));
		}
	}

	private void processPanel(Catalog.Events.OnClickItem.CatalogEvent event, PsiClass events) {
		if (!event.a$(OpenPanel.class).hasBreadcrumbs()) return;
		if (events.findMethodsByName("onOpenPanelBreadcrumbs", false).length == 0) {
			String text = openPanelMethodText(event.a$(OpenPanel.class));
			if (text != null && !text.isEmpty()) events.add(createMethodFromText(text));
		}
	}

	private void processCatalog(Catalog.Events.OnClickItem.CatalogEvent event, PsiClass events) {
		if (event.a$(OpenCatalog.class).openItem() && events.findMethodsByName("onOpenCatalog", false).length == 0) {
			String text = openCatalogMethodText(event.a$(OpenCatalog.class));
			if (text != null && !text.isEmpty()) events.add(createMethodFromText(text));
		}
		if (event.a$(OpenCatalog.class).filtered() && events.findMethodsByName("onOpenCatalogFilter", false).length == 0) {
			String text = openCatalogMethodText(event.a$(OpenCatalog.class));
			if (text != null && !text.isEmpty()) events.add(createMethodFromText(text));
		}
	}

	private void updateViewMethods(PsiClass psiClass) {
		DisplayView displayView = catalog.views().displayView();
		if (catalog.views() == null || displayView == null) return;
		PsiClass views = innerClass(psiClass, "Views");
		if (views == null)
			psiClass.addAfter(createClass(viewsClass(displayView), psiClass), innerClass(psiClass, "Source").getNextSibling());
		else {
			List<PsiMethod> methods = Arrays.asList(views.getMethods());
			if (!methods.isEmpty() && (methods.get(0).getName().equalsIgnoreCase(displayView.display()) || methods.get(0).getName().equalsIgnoreCase(displayView.display() + "Scope")))
				return;
			for (PsiMethod method : methods) renameViewMethod(method, displayView.display());
		}
	}

	private void renameViewMethod(PsiMethod method, String display) {
		method.getNameIdentifier().replace(factory.createIdentifier(display + (method.getName().endsWith("Scope") ? "Scope" : "")));
	}

	private String viewsClass(DisplayView displayView) {
		return template.format(frameOf(displayView, catalog, box, packageName));
	}

	private String openPanelMethodText(OpenPanel event) {
		return template.format(frameOf(event, catalog, box, catalog.itemClass()));
	}

	private String openCatalogMethodText(OpenCatalog event) {
		return template.format(frameOf(event, catalog, box, catalog.itemClass()));
	}

	private String openDialogMethodText(OpenDialog event) {
		return template.format(frameOf(event, catalog));
	}

	private String operationMethodText(Operation operation) {
		return template.format(frameOf(operation, catalog, box, catalog.itemClass(), packageName));
	}

	private String sortingMethodText(Sorting sorting) {
		return template.format(frameOf(sorting, catalog, box, catalog.itemClass()));
	}

	private String groupingMethodText(Grouping grouping) {
		return template.format(frameOf(grouping, catalog, box, catalog.itemClass()));
	}

	private PsiElement constructorOf(PsiClass psiClass) {
		return psiClass.getConstructors()[psiClass.getConstructors().length - 1].getNextSibling();
	}

	private String temporalClass() {
		return "public static class Temporal {\npublic static io.intino.konos.alexandria.activity.model.TimeRange range (" + Formatters.firstUpperCase(this.box) + "Box box, ActivitySession session){\nreturn null;\n}\n}";
	}*/
}
