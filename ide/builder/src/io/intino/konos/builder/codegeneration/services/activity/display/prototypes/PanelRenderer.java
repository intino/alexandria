package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters.PanelUpdater;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Panel.Views.View;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.Panel.Views.View.Hidden.HiddenEnabled;

public class PanelRenderer extends PrototypeRenderer {
	private final Project project;

	public PanelRenderer(Project project, Panel panel, File src, File gen, String packageName, String boxName) {
		super(panel, boxName, packageName, src, gen);
		this.project = project;
	}

	public void render() {
		Frame frame = createFrame();
		writeSrc(frame);
		writeAbstract(frame.addTypes("gen"));
	}

	protected Frame createFrame() {
		final Panel panel = this.display.a$(Panel.class);
		final Frame frame = super.createFrame();
		if (panel.label() != null) frame.addSlot("label", panel.label());
		if (panel.toolbar() != null) frame.addSlot("toolbar", frameOf(panel.toolbar()));
		for (View view : panel.views().viewList()) frame.addSlot("view", frameOf(view, panel, box));
		return frame;
	}

	public static Frame frameOf(View view, Panel panel, String box) {
		final Frame frame = new Frame("view")
				.addSlot("owner", panel.name$())
				.addSlot("name", view.name$())
				.addSlot("label", view.label())
				.addSlot("box", box)
				.addSlot("layout", view.layout().name());
		if (view.hidden() == HiddenEnabled) frame.addSlot("hidden", hidden(panel, view, box));
		final ElementRenderer renderer = view.elementRenderer();
		if (renderer.i$(RenderDisplay.class)) {
			frame.addTypes("display");
			frame.addSlot("display", renderer.a$(RenderDisplay.class).display());
		}
		if (renderer.i$(RenderMold.class)) {
			frame.addTypes("mold");
			final Mold mold = renderer.a$(RenderMold.class).mold();
			frame.addSlot("mold", mold.name$());
		}
		if (renderer.i$(RenderCatalogs.class)) {
			frame.addTypes("catalogs");
			RenderCatalogs renderCatalogs = renderer.a$(RenderCatalogs.class);
			frame.addSlot("catalog", renderCatalogs.catalogs().stream().map(Layer::name$).toArray(String[]::new));
			frame.addSlot("displayLoader", "");
			if (renderCatalogs.filtered()) frame.addSlot("filter", filterFrame(view, panel, box));
		}
		return frame;
	}

	private static Frame hidden(Panel panel, View view, String box) {
		return new Frame().addSlot("panel", panel.name$()).addSlot("view", view.name$()).addSlot("box", box);
	}

	private Frame frameOf(Panel.Toolbar toolbar) {
		final Frame frame = new Frame("toolbar");
		frame.addSlot("box", box).addSlot("canSearch", toolbar.canSearch());
		if (toolbar.download() != null) frame.addSlot("operation", frameOf(toolbar.download(), display.a$(Panel.class)));
		if (toolbar.export() != null) frame.addSlot("operation", frameOf(toolbar.export(), display.a$(Panel.class)));
		if (toolbar.openDialog() != null) frame.addSlot("operation", frameOf(toolbar.openDialog(), display.a$(Panel.class)));
		if (toolbar.task() != null) frame.addSlot("operation", frameOf(toolbar.task(), display.a$(Panel.class)));
		return frame;
	}

	public static Frame frameOf(Operation operation, Panel panel) {
		Frame frame = new Frame("operation", operation.getClass().getSimpleName())
				.addSlot("name", operation.name$())
				.addSlot("title", operation.title())
				.addSlot("panel", panel.name$());
		if (operation.polymerIcon() != null) frame.addSlot("icon", operation.polymerIcon());
		return frame;
	}

	private static Frame filterFrame(View view, Panel panel, String box) {
		final String modelClass = view.elementRenderer().a$(RenderCatalogs.class).catalogs().get(0).modelClass();
		return new Frame("filter").addSlot("panel", panel.name$()).addSlot("view", view.name$()).addSlot("box", box).addSlot("modelClass", modelClass);
	}

	@Override
	protected Template template() {
		return customize(AbstractPanelTemplate.create());
	}

	protected Template srcTemplate() {
		return customize(PanelTemplate.create());
	}

	void writeSrc(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		File sourceFile = javaFile(new File(src, DISPLAYS), newDisplay);
		if (!sourceFile.exists()) writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(frame));
		else new PanelUpdater(sourceFile, display.a$(Panel.class), project, packageName, box).update();
	}
}
