package io.intino.konos.builder.codegeneration.services.ui.display.mold;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.DisplayRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.Mold.Block;
import io.intino.konos.model.graph.Mold.Block.*;
import io.intino.konos.model.graph.Mold.Block.Breadcrumbs.TreeItem;
import io.intino.tara.magritte.Layer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.validMoldName;

public class MoldRenderer extends DisplayRenderer {
	private final Project project;
	private final Mold mold;

	public MoldRenderer(Project project, Mold mold, String packageName, String boxName) {
		super(mold, boxName, packageName);
		this.project = project;
		this.mold = mold;
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder builder = super.frameBuilder();
		builder.add("moldClass", moldClass());
		for (Block block : mold.blockList()) builder.add("block", frameOf(block));
		return builder;
	}

	public Frame frameOf(Stamp stamp) {
		final FrameBuilder builder = baseFrame(stamp).add("stamp")
				.add("type", stamp.getClass().getSimpleName())
				.add("common", common(stamp));

		if (stamp.i$(Picture.class)) frameOf(builder, stamp.a$(Picture.class));
		else if (stamp.i$(Rating.class)) frameOf(builder, stamp.a$(Rating.class));
		else if (stamp.i$(Breadcrumbs.class)) frameOf(builder, stamp.a$(Breadcrumbs.class));
		else if (stamp.i$(Location.class)) frameOf(builder, stamp.a$(Location.class));
		else if (stamp.i$(Operation.class)) frameOf(builder, stamp.a$(Operation.class));
		else if (stamp.i$(CatalogLink.class)) frameOf(builder, stamp.a$(CatalogLink.class));
		else if (stamp.i$(EmbeddedDisplay.class)) frameOf(builder, stamp.a$(EmbeddedDisplay.class));
		else if (stamp.i$(EmbeddedDialog.class)) frameOf(builder, stamp.a$(EmbeddedDialog.class));
		else if (stamp.i$(EmbeddedCatalog.class)) frameOf(builder, stamp.a$(EmbeddedCatalog.class));
		else if (stamp.i$(Icon.class)) frameOf(builder, stamp.a$(Icon.class));
		else if (stamp.i$(ItemLinks.class)) frameOf(builder, stamp.a$(ItemLinks.class));
		else if (stamp.i$(CardWallet.class)) frameOf(builder, stamp.a$(CardWallet.class));
		else if (stamp.i$(Timeline.class)) frameOf(builder, stamp.a$(Timeline.class));
		else if (stamp.i$(Pie.class)) frameOf(builder, stamp.a$(Pie.class));
		else if (stamp.i$(Histogram.class)) frameOf(builder, stamp.a$(Histogram.class));
		else if (stamp.i$(Map.class)) frameOf(builder, stamp.a$(Map.class));
		return builder.toFrame();
	}

	private Frame frameOf(Block block) {
		FrameBuilder builder = new FrameBuilder("block")
				.add("name", validMoldName().format(block.name$()))
				.add("expanded", block.mode().equals(Mode.Expanded))
				.add("layout", block.layout().stream().map(Enum::name).toArray(String[]::new))
				.add("hiddenIfMobile", block.hiddenIfMobile());
		if (block.hidden().equals(Hidden.HiddenEnabled)) builder.add("hidden", baseFrame(block));
		if (block.hasCustomClass()) builder.add("blockClassName", baseFrame(block));
		if (!block.style().isEmpty()) builder.add("blockStyle", block.style());
		if (block.height() >= 0) builder.add("height", block.height());
		if (block.width() >= 0) builder.add("width", block.width());
		for (Stamp stamp : block.stampList()) builder.add("stamp", frameOf(stamp));
		for (Block inner : block.blockList()) builder.add("block", frameOf(inner));
		return builder.toFrame();
	}

	@NotNull
	private Frame common(Stamp stamp) {
		final FrameBuilder builder = baseFrame(stamp).add("common");
		if (!stamp.defaultStyle().isEmpty()) builder.add("defaultStyle", stamp.defaultStyle());
		if (stamp.hasCustomStyle()) builder.add("style", baseFrame(stamp));
		if (stamp.hasCustomClass()) builder.add("className", baseFrame(stamp));
		if (stamp.hasCustomLabel()) builder.add("labelLoader", baseFrame(stamp));
		if (stamp.hasCustomColor()) builder.add("color", baseFrame(stamp));
		if (stamp.editable()) builder.add("editable", baseFrame(stamp));
		if (stamp.height() >= 0) builder.add("height", stamp.height());
		if (!stamp.label().isEmpty()) builder.add("label", stamp.label());
		if (!stamp.suffix().isEmpty()) builder.add("suffix", stamp.suffix());
		addValueMethod(stamp, builder);
		return builder.toFrame();
	}

	private void addValueMethod(Stamp stamp, FrameBuilder builder) {
		if (stamp.i$(EmbeddedDisplay.class) || stamp.i$(EmbeddedCatalog.class) || stamp.i$(Operation.class) || stamp.i$(Page.class))
			return;
		builder.add("valueMethod", baseFrame(stamp).add("valueMethod")
				.add("valueType", (stamp.i$(Icon.class) && stamp.a$(Icon.class).source().equals(Icon.Source.Resource)) ?
						Icon.Source.Resource.name().toLowerCase() :
						stamp.getClass().getSimpleName()));
	}

	private void frameOf(FrameBuilder frame, Picture stamp) {
		if (stamp.defaultPicture() != null)
			frame.add("defaultPicture", stamp.defaultPicture());
		if (stamp.avatar())
			frame.add("avatarProperties", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder frame, Rating stamp) {
		frame.add("ratingIcon", stamp.polymerIcon());
		frame.add("ratingMax", stamp.max());
	}

	private void frameOf(FrameBuilder frame, EmbeddedCatalog stamp) {
		if (stamp.maxItems() > 0) frame.add("embeddedCatalogMaxItems", stamp.maxItems());
		if (stamp.filtered()) frame.add("catalogFilter", baseFrame(stamp));
		frame.add("catalog", stamp.catalog().name$());
		frame.add("view", stamp.views().stream().map(Layer::name$).toArray(String[]::new));
	}

	private void frameOf(FrameBuilder builder, OpenCatalogOperation stamp) {
		if (stamp.filtered()) builder.add("catalogFilter", baseFrame(stamp));
		if (stamp.selection() != OpenCatalogOperation.Selection.None) builder.add("openCatalogOperationExecution", baseFrame(stamp));
		builder.add("catalog", stamp.catalog().name$());
		builder.add("view", stamp.views().stream().map(Layer::name$).toArray(String[]::new));
		builder.add("width", stamp.width());
		builder.add("position", stamp.position().toString());
		builder.add("selection", stamp.selection().toString());
	}

	private void frameOf(FrameBuilder builder, EmbeddedDisplay stamp) {
		builder.add("displayBuilder", baseFrame(stamp).add("displayBuilder"));
		if (stamp.display() != null) builder.add("displayType", stamp.display().name$());
	}

	private void frameOf(FrameBuilder builder, EmbeddedDialog stamp) {
		builder.add("embeddedDialogBuilder", baseFrame(stamp).add("dialog", stamp.dialog().name$()).add("package", packageName));
		builder.add("dialogType", stamp.dialog().name$());
	}

	private void frameOf(FrameBuilder builder, CatalogLink stamp) {
		builder.add("catalog", stamp.catalog().name$());
		if (stamp.filtered()) builder.add("filter", baseFrame(stamp).add("filter"));
		if (stamp.openItem()) builder.add("itemLoader", baseFrame(stamp).add("itemLoader"));
	}

	private void frameOf(FrameBuilder builder, ItemLinks stamp) {
		builder.add("title", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, CardWallet stamp) {
		builder.add("title", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, Timeline stamp) {
		builder.add("title", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, Pie stamp) {
		builder.add("title", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, Histogram stamp) {
		builder.add("title", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, Icon stamp) {
		builder.add(stamp.source().name() + "Icon");
		builder.add("title", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, Breadcrumbs stamp) {
		if (stamp.root() != null) builder.add("root", stamp.root().name$());
		frameOf(builder, stamp.treeItemList());
	}

	private void frameOf(FrameBuilder builder, Location stamp) {
		builder.add("icon", baseFrame(stamp));
	}

	private void frameOf(FrameBuilder builder, Map stamp) {
		builder.add("zoom", stamp.zoom());
		builder.add("latitude", stamp.latitude());
		builder.add("longitude", stamp.longitude());
	}

	private void frameOf(FrameBuilder builder, Operation operation) {
		builder.add(operation.getClass().getSimpleName()).add("mode", operation.mode().toString());
		if (operation.alexandriaIcon() != null)
			builder.add("alexandriaIcon", operation.alexandriaIcon());
		if (operation.i$(OpenDialogOperation.class)) {
			OpenDialogOperation openDialogOperation = operation.a$(OpenDialogOperation.class);
			Dialog dialog = openDialogOperation.dialog();
			if (dialog != null)
				builder.add("width", openDialogOperation.width()).add("dialogType", dialog.name$()).add("dialogBuilder", frame(openDialogOperation));
		} else if (operation.i$(OpenExternalDialogOperation.class)) {
			OpenExternalDialogOperation openExternalDialogOperation = operation.a$(OpenExternalDialogOperation.class);
			builder.add("width", openExternalDialogOperation.width()).add("dialogPathBuilder", baseFrame(openExternalDialogOperation)).add("dialogTitleBuilder", baseFrame(openExternalDialogOperation));
		} else if (operation.i$(OpenCatalogOperation.class)) {
			frameOf(builder, operation.a$(OpenCatalogOperation.class));
		} else if (operation.i$(DownloadOperation.class)) {
			builder.add("options", operation.a$(DownloadOperation.class).options().toArray(new String[0]));
			builder.add("downloadExecution", baseFrame(operation));
		} else if (operation.i$(ExportOperation.class)) {
			final ExportOperation export = operation.a$(ExportOperation.class);
			if (!export.options().isEmpty()) builder.add("options", export.options().toArray(new String[0]));
			builder.add("exportExecution", baseFrame(operation));
			if (export.from() != null) builder.add("from", export.from().toEpochMilli());
			if (export.to() != null) builder.add("to", export.to().toEpochMilli());
		} else if (operation.i$(PreviewOperation.class)) {
			builder.add("previewExecution", baseFrame(operation));
		} else if (operation.i$(TaskOperation.class)) {
			TaskOperation taskOperation = operation.a$(TaskOperation.class);
			builder.add("taskExecution", baseFrame(operation)).add("mold", mold.name$());
			if (taskOperation.confirmText() != null) builder.add("confirmText", taskOperation.confirmText());
		}
	}

	private void frameOf(FrameBuilder builder, List<TreeItem> treeItems) {
		for (TreeItem item : treeItems) {
			final FrameBuilder sub = new FrameBuilder("treeItem").add("label", item.label());
			if (!item.treeItemList().isEmpty()) frameOf(sub, item.treeItemList());
			builder.add("treeItem", sub.toFrame());
		}
	}

	private Frame frame(OpenDialogOperation operation) {
		return new FrameBuilder(operation.getClass().getSimpleName()).add("dialog", operation.dialog().name$()).add("package", packageName).toFrame();
	}

	private FrameBuilder baseFrame(Stamp stamp) {
		return new FrameBuilder(stamp.getClass().getSimpleName()).add("mold", mold.name$()).add("name", stamp.name$()).add("moldClass", moldClass()).add("box", box);
	}

	private FrameBuilder baseFrame(Block block) {
		FrameBuilder frame = new FrameBuilder(block.getClass().getSimpleName()).add("mold", mold.name$()).add("name", block.name$()).add("moldClass", moldClass()).add("box", box);
		if (block.hidden() == Hidden.HiddenEnabled) frame.add("HiddenEnabled", "HiddenEnabled");
		return frame;
	}

	private String moldClass() {
		return mold.modelClass() == null || mold.modelClass().isEmpty() ? "java.lang.Object" : mold.modelClass();
	}

	@Override
	protected Template srcTemplate() {
		return Formatters.customize(new MoldTemplate());
	}

	@Override
	protected Template genTemplate() {
		return Formatters.customize(new AbstractMoldTemplate());
	}

	@Override
	protected Updater updater(String displayName, File sourceFile) {
		return new MoldUpdater(sourceFile, display().a$(Mold.class), project, packageName, box);
	}
}
