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
				.addSlot("hidden", block.hidden())
				.addSlot("hiddenIfMobile", block.hiddenIfMobile())
				.addSlot("layout", block.layout().stream().map(Enum::name).toArray(String[]::new));
		if (!block.style().isEmpty()) frame.addSlot("style", block.style());
		if (block.height() >= 0) frame.addSlot("height", block.height());
		if (block.width() >= 0) frame.addSlot("width", block.width());
		for (Stamp stamp : block.stampList()) frame.addSlot("stamp", frameOf(stamp));
		for (Block inner : block.blockList()) frame.addSlot("block", frameOf(inner));
		return frame;
	}

	private <T extends Layer> Frame frameOf(Stamp stamp) {
		final Frame frame = new Frame("stamp", stamp.getClass().getSimpleName());
		frame.addSlot("name", stamp.name$());
		frame.addSlot("type", stamp.getClass().getSimpleName());
		frame.addSlot("common", common(stamp));
		frame.addSlot("mold", mold.name$());
		frame.addSlot("moldClass", moldClass());
		if (stamp.i$(Picture.class)) frameOf(frame, stamp.a$(Picture.class));
		if (stamp.i$(Tree.class)) frameOf(frame, stamp.a$(Tree.class));
		if (stamp.i$(Location.class)) frameOf(frame, stamp.a$(Location.class));
		if (stamp.i$(Operation.class)) frameOf(frame, stamp.a$(Operation.class));
		return frame;
	}

	private String moldClass() {
		return mold.modelClass().isEmpty() ? "java.lang.Object" : mold.modelClass();
	}

	@NotNull
	private Frame common(Stamp stamp) {
		final Frame frame = baseFrame(stamp).addTypes("common");
		if (!stamp.defaultStyle().isEmpty())
			frame.addSlot("defaultStyle", baseFrame(stamp));
		frame.addSlot("editable", stamp.editable());
		frame.addSlot("valueType", "String");
		if (stamp.height() >= 0) frame.addSlot("height", stamp.height());
		if (!stamp.label().isEmpty()) frame.addSlot("label", stamp.label());
		if (!stamp.suffix().isEmpty()) frame.addSlot("suffix", stamp.suffix());
		return frame;
	}

	private void frameOf(Frame frame, Picture stamp) {
		if (stamp.defaultPicture() != null) frame.addSlot("defaultPicture", stamp.defaultPicture().getPath());
	}

	private void frameOf(Frame frame, Tree stamp) {
		if (stamp.root() != null) frame.addSlot("root", stamp.root().name$());
		frameOf(frame, stamp.treeItemList());
	}

	private void frameOf(Frame frame, Location stamp) {
		frame.addSlot("icon", baseFrame(stamp));
	}

	private void frameOf(Frame frame, Operation operation) {
		frame.addSlot("operationType", operation.getClass().getSimpleName()).addTypes("operation");
		if (operation.i$(OpenDialog.class)) frame.addSlot("width", operation.a$(OpenDialog.class).width());
		if (operation.i$(Download.class)) frame.addSlot("options", operation.a$(Download.class).options());
		if (operation.i$(Export.class)) {
			final Export export = operation.a$(Export.class);
			frame.addSlot("options", export.options());
			if (export.from() != null) frame.addSlot("from", export.from().toEpochMilli());
			if (export.to() != null) frame.addSlot("to", export.to());
		}
	}

	private void frameOf(Frame frame, List<TreeItem> treeItems) {
		for (TreeItem item : treeItems) {
			final Frame sub = new Frame("treeItem").addSlot("label", item.label());
			if (!item.treeItemList().isEmpty()) frameOf(sub, item.treeItemList());
			frame.addSlot("treeItem", sub);
		}
	}

	private Frame baseFrame(Stamp stamp) {
		return new Frame(stamp.getClass().getSimpleName()).addSlot("mold", mold.name$()).addSlot("name", stamp.name$()).addSlot("moldClass", moldClass());
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
