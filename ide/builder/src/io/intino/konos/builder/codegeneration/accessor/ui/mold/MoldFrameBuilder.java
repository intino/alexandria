package io.intino.konos.builder.codegeneration.accessor.ui.mold;

import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.Mold;
import org.siani.itrules.model.Frame;

import java.util.List;

import static io.intino.konos.model.graph.Mold.Block.Mode.Expanded;
import static java.util.stream.Collectors.toList;

public class MoldFrameBuilder extends Frame {
	private final Activity activity;
	private final Mold mold;
	private final Frame moldFrame;

	public MoldFrameBuilder(Activity activity, Mold mold) {
		super();
		this.activity = activity;
		this.mold = mold;
		moldFrame = new Frame().addTypes("mold").addSlot("name", mold.name$());
	}

	public Frame build() {
		mold.blockList().stream().filter(b -> !b.mode().equals(Expanded)).forEach(b -> moldFrame.addSlot("block", frameOf(b)));
		final List<Mold.Block> expandedBlocks = mold.blockList().stream().filter(b -> b.mode().equals(Expanded)).collect(toList());
		if (!expandedBlocks.isEmpty()) {
			final Frame expanded = new Frame().addTypes("expandedBlocks");
			expandedBlocks.forEach(b -> expanded.addSlot("block", frameOf(b)));
			moldFrame.addSlot("expandedBlocks", expanded);
		}

		return moldFrame;

	}

	private Frame frameOf(Mold.Block block) {
		Frame frame = new Frame("block")
				.addSlot("name", clean(block.name$()))
				.addSlot("expanded", block.mode().equals(Expanded))
				.addSlot("layout", block.layout().stream().map(Enum::name).toArray(String[]::new))
				.addSlot("hiddenIfMobile", block.hiddenIfMobile());
		if (!block.style().isEmpty()) frame.addSlot("style", block.style());
		if (block.height() >= 0) frame.addSlot("height", block.height());
		if (block.width() >= 0) frame.addSlot("width", block.width());
		for (Mold.Block.Stamp stamp : block.stampList()) {
			frame.addSlot("stamp", baseFrameOf(stamp));
			moldFrame.addSlot("stamp", frameOf(stamp));
		}
		for (Mold.Block inner : block.blockList()) frame.addSlot("block", frameOf(inner));
		return frame;
	}

	private Frame frameOf(Mold.Block.Stamp stamp) {
		final Frame frame = new Frame().addTypes(stamp.getClass().getSimpleName());
		if (stamp.i$(Mold.Block.Icon.class)) frame.addSlot("icon", stamp.a$(Mold.Block.Icon.class));//TODO

		return frame;
	}

	private Frame baseFrameOf(Mold.Block.Stamp stamp) {
		return new Frame().addTypes("stamp").addSlot("name", stamp.name$());
	}

	private String clean(String name) {
		return name.replace("-", "");
	}
}
