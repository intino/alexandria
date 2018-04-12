package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.services.activity.display.prototypes.updaters.MoldUpdater;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.Mold.Block;
import io.intino.konos.model.graph.Mold.Block.*;
import io.intino.konos.model.graph.Mold.Block.Breadcrumbs.TreeItem;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.validMoldName;
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
				.addSlot("name", validMoldName().format(block.name$()))
				.addSlot("expanded", block.mode().equals(Mode.Expanded))
				.addSlot("layout", block.layout().stream().map(Enum::name).toArray(String[]::new))
				.addSlot("hiddenIfMobile", block.hiddenIfMobile());
		if (block.hidden().equals(Hidden.HiddenEnabled)) frame.addSlot("hidden", baseFrame(block));
		if (!block.style().isEmpty()) frame.addSlot("blockStyle", block.style());
		if (block.height() >= 0) frame.addSlot("height", block.height());
		if (block.width() >= 0) frame.addSlot("width", block.width());
		for (Stamp stamp : block.stampList()) frame.addSlot("stamp", frameOf(stamp));
		for (Block inner : block.blockList()) frame.addSlot("block", frameOf(inner));
		return frame;
	}

	public Frame frameOf(Stamp stamp) {
		final Frame frame = baseFrame(stamp).addTypes("stamp")
				.addSlot("type", stamp.getClass().getSimpleName())
				.addSlot("common", common(stamp));

		if (stamp.i$(Picture.class)) frameOf(frame, stamp.a$(Picture.class));
		else if (stamp.i$(Rating.class)) frameOf(frame, stamp.a$(Rating.class));
		else if (stamp.i$(Breadcrumbs.class)) frameOf(frame, stamp.a$(Breadcrumbs.class));
		else if (stamp.i$(Location.class)) frameOf(frame, stamp.a$(Location.class));
		else if (stamp.i$(Operation.class)) frameOf(frame, stamp.a$(Operation.class));
		else if (stamp.i$(CatalogLink.class)) frameOf(frame, stamp.a$(CatalogLink.class));
		else if (stamp.i$(EmbeddedDisplay.class)) frameOf(frame, stamp.a$(EmbeddedDisplay.class));
		else if (stamp.i$(EmbeddedDialog.class)) frameOf(frame, stamp.a$(EmbeddedDialog.class));
		else if (stamp.i$(EmbeddedCatalog.class)) frameOf(frame, stamp.a$(EmbeddedCatalog.class));
		else if (stamp.i$(Icon.class)) frameOf(frame, stamp.a$(Icon.class));
		else if (stamp.i$(ItemLinks.class)) frameOf(frame, stamp.a$(ItemLinks.class));
		else if (stamp.i$(CardWallet.class)) frameOf(frame, stamp.a$(CardWallet.class));
		else if (stamp.i$(Map.class)) frameOf(frame, stamp.a$(Map.class));

		return frame;
	}

	@NotNull
	private Frame common(Stamp stamp) {
		final Frame frame = baseFrame(stamp).addTypes("common");
		if (!stamp.defaultStyle().isEmpty()) frame.addSlot("defaultStyle", stamp.defaultStyle());
		if (stamp.hasCustomStyle()) frame.addSlot("style", baseFrame(stamp));
		if (stamp.hasCustomClass()) frame.addSlot("className", baseFrame(stamp));
		if (stamp.hasCustomLabel()) frame.addSlot("labelLoader", baseFrame(stamp));
		if (stamp.hasCustomColor()) frame.addSlot("color", baseFrame(stamp));
		if (stamp.editable()) frame.addSlot("editable", baseFrame(stamp));
		if (stamp.height() >= 0) frame.addSlot("height", stamp.height());
		if (!stamp.label().isEmpty()) frame.addSlot("label", stamp.label());
		if (!stamp.suffix().isEmpty()) frame.addSlot("suffix", stamp.suffix());

		if (stamp.editable() || (stamp.i$(TaskOperation.class) && stamp.a$(TaskOperation.class).showMessageToUser()))
			frame.addSlot("messageLoader", baseFrame(stamp)).addSlot("mold", mold.name$());

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
			frame.addSlot("defaultPicture", stamp.defaultPicture());
	}

	private void frameOf(Frame frame, Rating stamp) {
		frame.addSlot("ratingIcon", stamp.polymerIcon());
		frame.addSlot("ratingMax", stamp.max());
	}

	private void frameOf(Frame frame, EmbeddedCatalog stamp) {
		if (stamp.maxItems() > 0) frame.addSlot("embeddedCatalogMaxItems", stamp.maxItems());
		if (stamp.filtered()) frame.addSlot("catalogFilter", baseFrame(stamp));
		frame.addSlot("catalog", stamp.catalog().name$());
		frame.addSlot("view", stamp.views().stream().map(Layer::name$).toArray(String[]::new));
	}

	private void frameOf(Frame frame, OpenCatalogOperation stamp) {
		if (stamp.filtered()) frame.addSlot("catalogFilter", baseFrame(stamp));
		if (stamp.selection() != OpenCatalogOperation.Selection.None) frame.addSlot("openCatalogOperationExecution", baseFrame(stamp));
		frame.addSlot("catalog", stamp.catalog().name$());
		frame.addSlot("view", stamp.views().stream().map(Layer::name$).toArray(String[]::new));
		frame.addSlot("width", stamp.width());
		frame.addSlot("position", stamp.position().toString());
		frame.addSlot("selection", stamp.selection().toString());
	}

	private void frameOf(Frame frame, EmbeddedDisplay stamp) {
		frame.addSlot("displayBuilder", baseFrame(stamp).addTypes("displayBuilder"));
		frame.addSlot("displayType", stamp.display().name$());
	}

	private void frameOf(Frame frame, EmbeddedDialog stamp) {
		frame.addSlot("embeddedDialogBuilder", baseFrame(stamp).addSlot("dialog", stamp.dialog().name$()).addSlot("package", packageName));
		frame.addSlot("dialogType", stamp.dialog().name$());
	}

	private void frameOf(Frame frame, CatalogLink stamp) {
		frame.addSlot("catalog", stamp.catalog().name$());
		if (stamp.filtered()) frame.addSlot("filter", baseFrame(stamp).addTypes("filter"));
		if (stamp.openItem()) frame.addSlot("itemLoader", baseFrame(stamp).addTypes("itemLoader"));
	}

	private void frameOf(Frame frame, ItemLinks stamp) {
		frame.addSlot("title", baseFrame(stamp));
	}

	private void frameOf(Frame frame, CardWallet stamp) {
		frame.addSlot("title", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Icon stamp) {
		frame.addTypes(stamp.source().name() + "Icon");
		frame.addSlot("title", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Breadcrumbs stamp) {
		if (stamp.root() != null) frame.addSlot("root", stamp.root().name$());
		frameOf(frame, stamp.treeItemList());
	}

	private void frameOf(Frame frame, Location stamp) {
		frame.addSlot("icon", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Map stamp) {
		frame.addSlot("zoom", stamp.zoom());
		frame.addSlot("latitude", stamp.latitude());
		frame.addSlot("longitude", stamp.longitude());
	}

	private void frameOf(Frame frame, Operation operation) {
		frame.addTypes(operation.getClass().getSimpleName()).addSlot("mode", operation.mode().toString());
		if (operation.alexandriaIcon() != null)
			frame.addSlot("alexandriaIcon", operation.alexandriaIcon());
		if (operation.i$(OpenDialogOperation.class)) {
			OpenDialogOperation openDialogOperation = operation.a$(OpenDialogOperation.class);
			Dialog dialog = openDialogOperation.dialog();
			if (dialog != null) frame.addSlot("width", openDialogOperation.width()).addSlot("dialogType", dialog.name$()).addSlot("dialogBuilder", frame(openDialogOperation));
		} else if (operation.i$(OpenExternalDialogOperation.class)) {
			OpenExternalDialogOperation openExternalDialogOperation = operation.a$(OpenExternalDialogOperation.class);
			frame.addSlot("width", openExternalDialogOperation.width()).addSlot("dialogPathBuilder", baseFrame(openExternalDialogOperation)).addSlot("dialogTitleBuilder", baseFrame(openExternalDialogOperation));
		} else if (operation.i$(OpenCatalogOperation.class)) {
			frameOf(frame, operation.a$(OpenCatalogOperation.class));
		} else if (operation.i$(DownloadOperation.class)) {
			frame.addSlot("options", operation.a$(DownloadOperation.class).options().toArray(new String[0]));
			frame.addSlot("downloadExecution", baseFrame(operation));
		} else if (operation.i$(ExportOperation.class)) {
			final ExportOperation export = operation.a$(ExportOperation.class);
			if (!export.options().isEmpty()) frame.addSlot("options", export.options().toArray(new String[0]));
			frame.addSlot("exportExecution", baseFrame(operation));
			if (export.from() != null) frame.addSlot("from", export.from().toEpochMilli());
			if (export.to() != null) frame.addSlot("to", export.to().toEpochMilli());
		} else if (operation.i$(PreviewOperation.class)) {
			frame.addSlot("previewExecution", baseFrame(operation));
		} else if (operation.i$(TaskOperation.class)) {
			TaskOperation taskOperation = operation.a$(TaskOperation.class);
			frame.addSlot("taskExecution", baseFrame(operation)).addSlot("mold", mold.name$());
			if (taskOperation.confirmText() != null) frame.addSlot("confirmText", taskOperation.confirmText());
		}
	}

	private void frameOf(Frame frame, List<TreeItem> treeItems) {
		for (TreeItem item : treeItems) {
			final Frame sub = new Frame("treeItem").addSlot("label", item.label());
			if (!item.treeItemList().isEmpty()) frameOf(sub, item.treeItemList());
			frame.addSlot("treeItem", sub);
		}
	}

	private Frame frame(OpenDialogOperation operation) {
		return new Frame(operation.getClass().getSimpleName()).addSlot("dialog", operation.dialog().name$()).addSlot("package", packageName);
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
		File sourceFile = javaFile(new File(src, DISPLAYS), newDisplay);
		if (!sourceFile.exists()) writeFrame(new File(src, DISPLAYS), newDisplay, srcTemplate().format(frame));
		else new MoldUpdater(sourceFile, display.a$(Mold.class), project, packageName, box).update();
	}

	protected Template srcTemplate() {
		return customize(MoldTemplate.create());
	}
}
