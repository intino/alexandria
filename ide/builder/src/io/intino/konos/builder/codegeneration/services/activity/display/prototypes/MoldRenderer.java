package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.Mold.Block;
import io.intino.konos.model.graph.Mold.Block.*;
import io.intino.konos.model.graph.Mold.Block.Tree.TreeItem;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MoldRenderer extends PrototypeRenderer {
	private final Project project;
	private final Mold mold;

	public MoldRenderer(Project project, Mold mold, File src, File gen, String packageName, String boxName) {
		super(mold, boxName, packageName, src, gen);
		this.project = project;
		this.mold = mold;
	}

	public void render() {
		Frame frame = createFrame();
		frame.addSlot("moldClass", moldClass());
		for (Block block : mold.blockList()) frame.addSlot("block", frameOf(block));
		writeSrc(frame);
		writeAbstract(frame);
	}

	private Frame frameOf(Block block) {
		Frame frame = new Frame("block")
				.addSlot("name", clean(block.name$()))
				.addSlot("expanded", block.mode().equals(Mode.Expanded))
				.addSlot("hidden", baseFrame(block))
				.addSlot("hiddenIfMobile", block.hiddenIfMobile())
				.addSlot("layout", block.layout().stream().map(Enum::name).toArray(String[]::new));
		if (!block.style().isEmpty()) frame.addSlot("blockStyle", block.style());
		if (block.height() >= 0) frame.addSlot("height", block.height());
		if (block.width() >= 0) frame.addSlot("width", block.width());
		for (Stamp stamp : block.stampList()) frame.addSlot("stamp", frameOf(stamp));
		for (Block inner : block.blockList()) frame.addSlot("block", frameOf(inner));
		return frame;
	}

	private Frame frameOf(Stamp stamp) {
		final Frame frame = baseFrame(stamp).addTypes("stamp")
				.addSlot("type", stamp.getClass().getSimpleName())
				.addSlot("common", common(stamp));
		if (stamp.i$(Picture.class)) frameOf(frame, stamp.a$(Picture.class));
		else if (stamp.i$(Rating.class)) frameOf(frame, stamp.a$(Rating.class));
		else if (stamp.i$(Tree.class)) frameOf(frame, stamp.a$(Tree.class));
		else if (stamp.i$(Location.class)) frameOf(frame, stamp.a$(Location.class));
		else if (stamp.i$(Operation.class)) frameOf(frame, stamp.a$(Operation.class));
		else if (stamp.i$(CatalogLink.class)) frameOf(frame, stamp.a$(CatalogLink.class));
		else if (stamp.i$(EmbeddedDisplay.class)) frameOf(frame, stamp.a$(EmbeddedDisplay.class));
		else if (stamp.i$(EmbeddedCatalog.class)) frameOf(frame, stamp.a$(EmbeddedCatalog.class));
		else if (stamp.i$(Icon.class)) frameOf(frame, stamp.a$(Icon.class));
		else if (stamp.i$(ItemLinks.class)) frameOf(frame, stamp.a$(ItemLinks.class));
		return frame;
	}

	@NotNull
	private Frame common(Stamp stamp) {
		final Frame frame = baseFrame(stamp).addTypes("common");
		if (!stamp.defaultStyle().isEmpty()) frame.addSlot("defaultStyle", stamp.defaultStyle());
		if (stamp.hasCustomStyle()) frame.addSlot("style", baseFrame(stamp));
		if (stamp.editable()) frame.addSlot("editable", baseFrame(stamp));
		if (stamp.height() >= 0) frame.addSlot("height", stamp.height());
		if (!stamp.label().isEmpty()) frame.addSlot("label", stamp.label());
		if (!stamp.suffix().isEmpty()) frame.addSlot("suffix", stamp.suffix());
		addValueMethod(stamp, frame);
		return frame;
	}


	private void addValueMethod(Stamp stamp, Frame frame) {
		if (stamp.i$(EmbeddedDisplay.class) || stamp.i$(EmbeddedCatalog.class) || stamp.i$(Operation.class) || stamp.i$(Page.class))
			return;
		frame.addSlot("valueMethod", baseFrame(stamp).addTypes("valueMethod")
				.addSlot("valueType", (stamp.i$(Icon.class) && stamp.a$(Icon.class).source().equals(Icon.Source.Resource)) ?
						Icon.Source.Resource.name().toLowerCase() :
						stamp.getClass().getSimpleName()));
	}

	private void frameOf(Frame frame, Picture stamp) {
		if (stamp.defaultPicture() != null)
			frame.addSlot("defaultPicture", stamp.graph().core$().store().relativePathOf(stamp.defaultPicture()));
	}

	private void frameOf(Frame frame, Rating stamp) {
		frame.addSlot("ratingIcon", stamp.polymerIcon());
	}

	private void frameOf(Frame frame, EmbeddedCatalog stamp) {
		if (stamp.filtered()) frame.addSlot("embeddedCatalogFilter", baseFrame(stamp));
		frame.addSlot("catalog", stamp.catalog().name$());
		frame.addSlot("view", stamp.views().stream().map(Layer::name$).toArray(String[]::new));
	}

	private void frameOf(Frame frame, EmbeddedDisplay stamp) {
		frame.addSlot("displayBuilder", baseFrame(stamp).addTypes("displayBuilder"));
		frame.addSlot("displayType", stamp.display().name$());
	}

	private void frameOf(Frame frame, CatalogLink stamp) {
		frame.addSlot("catalog", stamp.catalog().name$());
		if (stamp.filtered()) frame.addSlot("filter", baseFrame(stamp).addTypes("filter"));
	}

	private void frameOf(Frame frame, ItemLinks stamp) {
		frame.addSlot("title", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Icon stamp) {
		frame.addTypes(stamp.source().name() + "Icon");
		frame.addSlot("title", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Tree stamp) {
		if (stamp.root() != null) frame.addSlot("root", stamp.root().name$());
		frameOf(frame, stamp.treeItemList());
	}

	private void frameOf(Frame frame, Location stamp) {
		frame.addSlot("icon", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Operation operation) {
		frame.addTypes(operation.getClass().getSimpleName());
		if (operation.i$(OpenDialogOperation.class))
			frame.addSlot("width", operation.a$(OpenDialogOperation.class).width()).addSlot("dialogPath", baseFrame(operation));
		else if (operation.i$(DownloadOperation.class)) {
			frame.addSlot("options", operation.a$(DownloadOperation.class).options().toArray(new String[0]));
			frame.addSlot("downloadExecution", baseFrame(operation));
		} else if (operation.i$(ExportOperation.class)) {
			final ExportOperation export = operation.a$(ExportOperation.class);
			if (!export.options().isEmpty()) frame.addSlot("options", export.options().toArray(new String[0]));
			frame.addSlot("exportExecution", baseFrame(operation));
			if (export.from() != null) frame.addSlot("from", export.from().toEpochMilli());
			if (export.to() != null) frame.addSlot("to", export.to().toEpochMilli());
		} else if (operation.i$(TaskOperation.class)) frame.addSlot("taskExecution", baseFrame(operation));
	}

	private void frameOf(Frame frame, List<TreeItem> treeItems) {
		for (TreeItem item : treeItems) {
			final Frame sub = new Frame("treeItem").addSlot("label", item.label());
			if (!item.treeItemList().isEmpty()) frameOf(sub, item.treeItemList());
			frame.addSlot("treeItem", sub);
		}
	}

	private Frame baseFrame(Stamp stamp) {
		return new Frame(stamp.getClass().getSimpleName()).addSlot("mold", mold.name$()).addSlot("name", stamp.name$()).addSlot("moldClass", moldClass()).addSlot("box", box);
	}

	private Frame baseFrame(Block block) {
		Frame frame = new Frame(block.getClass().getSimpleName()).addSlot("mold", mold.name$()).addSlot("name", block.name$()).addSlot("moldClass", moldClass()).addSlot("box", box);
		if (block.hidden() == Hidden.HiddenEnabled) frame.addSlot("HiddenEnabled", "HiddenEnabled");
		return frame;
	}

	private String moldClass() {
		return mold.modelClass() == null || mold.modelClass().isEmpty() ? "java.lang.Object" : mold.modelClass();
	}

	@Override
	protected Template template() {
		return customize(AbstractMoldTemplate.create());
	}

	void writeSrc(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$());
		if (!javaFile(new File(src, DISPLAYS), newDisplay).exists())
			writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(frame));
	}

	private Template srcTemplate() {
		return customize(MoldTemplate.create());
	}

	private String clean(String name) {
		return name.replace("-", "");
	}
}
