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
		frame.addSlot("moldType", mold.modelClass());
		for (Block block : mold.blockList()) frame.addSlot("block", frameOf(block));
		writeSrc(frame);
		writeAbstract(frame);
	}

	private Frame frameOf(Block block) {
		Frame frame = new Frame("block")
				.addSlot("name", clean(block.name$()))
				.addSlot("expanded", block.mode().equals(Mode.Expanded))
				.addSlot("layout", block.layout().stream().map(Enum::name).toArray(String[]::new));
		if (!block.style().isEmpty()) frame.addSlot("style", block.style());
		if (block.height() >= 0) frame.addSlot("height", block.height());
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
		frame.addSlot("moldType", mold.modelClass());
		if (stamp.i$(Picture.class)) frameOf(frame, stamp.a$(Picture.class));
		if (stamp.i$(Tree.class)) frameOf(frame, stamp.a$(Tree.class));
		if (stamp.i$(Location.class)) frameOf(frame, stamp.a$(Location.class));
		return frame;
	}

	@NotNull
	private Frame common(Stamp stamp) {
		final Frame frame = baseFrame(stamp).addTypes("common");
		if (!stamp.defaultStyle().isEmpty())
			frame.addSlot("style", baseFrame(stamp));
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

	private void frameOf(Frame frame, List<TreeItem> treeItems) {
		for (TreeItem item : treeItems) {
			final Frame sub = new Frame("treeItem").addSlot("label", item.label());
			if (!item.treeItemList().isEmpty()) frameOf(sub, item.treeItemList());
			frame.addSlot("treeItem", sub);
		}
	}

	private Frame baseFrame(Stamp stamp) {
		return new Frame().addSlot("mold", mold.name$()).addSlot("name", stamp.name$()).addSlot("moldType", mold.modelClass());
	}

	@Override
	protected Template template() {
		return customize(AbstractMoldTemplate.create());
	}

	void writeSrc(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase(display.name$() + display.getClass().getSimpleName());
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
