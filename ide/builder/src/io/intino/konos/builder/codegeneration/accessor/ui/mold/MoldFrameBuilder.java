package io.intino.konos.builder.codegeneration.accessor.ui.mold;

import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.Mold;
import io.intino.konos.model.graph.Mold.Block;
import org.siani.itrules.model.Frame;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.camelCaseToSnakeCase;
import static io.intino.konos.builder.codegeneration.Formatters.validMoldName;
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
		moldFrame.addSlot("block", mold.blockList().stream().filter(b -> !b.mode().equals(Expanded)).map(this::frameOf).toArray(Frame[]::new));
		final List<Block> expandedBlocks = mold.blockList().stream().filter(b -> b.mode().equals(Expanded)).collect(toList());

		if (!expandedBlocks.isEmpty()) {
			final Frame expanded = new Frame().addTypes("expandedBlocks");
			expanded.addSlot("block", expandedBlocks.stream().map(this::frameOf).toArray(Frame[]::new));
			moldFrame.addSlot("expandedBlocks", expanded);
		}

		return moldFrame;
	}

	private Frame frameOf(Block block) {
		Frame frame = new Frame("block")
				.addSlot("name", validMoldName().format(block.name$()))
				.addSlot("layout", block.layout().stream().map(Enum::name).toArray(String[]::new));
		if (isExpanded(block)) frame.addSlot("expanded", true);
		if (block.hiddenIfMobile()) frame.addSlot("hiddenIfMobile", block.hiddenIfMobile());
		if (!block.style().isEmpty()) frame.addSlot("style", block.style());
		if (block.height() >= 0) frame.addSlot("height", block.height());
		if (block.width() >= 0) frame.addSlot("width", block.width());
		for (Block.Stamp stamp : block.stampList()) {
			frame.addSlot("stamp", baseFrameOf(stamp));
			moldFrame.addSlot("stamp", frameOf(stamp, isExpanded(block)));
		}
		for (Block inner : block.blockList()) frame.addSlot("block", frameOf(inner));
		return frame;
	}

	private boolean isExpanded(Block block) {
		Block currentBlock = block;
		while (currentBlock != null && !currentBlock.mode().equals(Expanded))
			currentBlock = currentBlock.core$().owner().is(Block.class) ? currentBlock.core$().ownerAs(Block.class) : null;
		return currentBlock != null;
	}

	private Frame frameOf(Block.Stamp stamp, boolean expanded) {
		final Frame frame = baseFrameOf(stamp);
		List<Frame> attributeList = new ArrayList<>();
		List<Frame> propertyList = new ArrayList<>();

		addAttributes(attributeList, stamp, expanded);
		addCommonProperties(propertyList, stamp);
		addCustomProperties(propertyList, stamp);
		frame.addSlot("attribute", attributeList.toArray(new Frame[0]));
		frame.addSlot("property", propertyList.toArray(new Frame[0]));
		return frame;
	}

	private void addAttributes(List<Frame> attributeList, Block.Stamp stamp, boolean expanded) {
		if (!stamp.label().isEmpty()) attributeList.add(frameOf("label", stamp.label()));
		if (stamp.editable()) attributeList.add(frameOf("editable", stamp.editable(), "boolean"));
		if (stamp.layout() != null) attributeList.add(frameOf("layout", stamp.layout().toString()));
		if (stamp.height() != -1) attributeList.add(frameOf("height", stamp.height(), "number"));
		if (expanded) attributeList.add(frameOf("expanded", true, "boolean"));
	}

	private void addCommonProperties(List<Frame> propertyList, Block.Stamp stamp) {
		if (stamp.suffix() != null && !stamp.suffix().isEmpty()) propertyList.add(frameOf("suffix", stamp.suffix()));
		if (stamp.defaultStyle() != null && !stamp.defaultStyle().isEmpty())
			propertyList.add(frameOf("defaultStyle", stamp.defaultStyle()));
	}

	private void addCustomProperties(List<Frame> propertyList, Block.Stamp stamp) {
		if (stamp.i$(Block.Icon.class))
			propertyList.add(frameOf("iconType", stamp.a$(Block.Icon.class).source() == Block.Icon.Source.Polymer ? "alexandria" : ""));
		if (stamp.i$(Block.Rating.class)) propertyList.add(frameOf("icon", stamp.a$(Block.Rating.class).polymerIcon()));
		if (stamp.i$(Block.EmbeddedDisplay.class))
			propertyList.add(frameOf("displayType", stamp.a$(Block.EmbeddedDisplay.class).display().name$()));
		if (stamp.i$(Block.EmbeddedDialog.class))
			propertyList.add(frameOf("dialogType", stamp.a$(Block.EmbeddedDialog.class).dialog().name$()));
		if (stamp.i$(Block.EmbeddedCatalog.class))
			propertyList.add(frameOf("catalog", stamp.a$(Block.EmbeddedCatalog.class).catalog().name$()));

		if (stamp.i$(Block.Operation.class)) {
			Block.Operation operation = stamp.a$(Block.Operation.class);
			if (operation.alexandriaIcon() != null)
				propertyList.add(frameOf("alexandriaIcon", operation.alexandriaIcon()));
			propertyList.add(frameOf("mode", operation.mode().toString()));
		}

		if (stamp.i$(Block.DownloadOperation.class))
			propertyList.add(frameOf("options", String.join(",", stamp.a$(Block.DownloadOperation.class).options())));

		if (stamp.i$(Block.ExportOperation.class)) {
			final Block.ExportOperation exportOperation = stamp.a$(Block.ExportOperation.class);
			final Instant defaultTo = Instant.now(Clock.systemUTC()).plus(1, ChronoUnit.DAYS);
			propertyList.add(frameOf("options", String.join(",", exportOperation.options())));
			propertyList.add(frameOf("from", exportOperation.from() != null ? exportOperation.from().toEpochMilli() : Instant.now().toEpochMilli(), "number"));
			propertyList.add(frameOf("to", exportOperation.to() != null ? exportOperation.to().toEpochMilli() : defaultTo.toEpochMilli(), "number"));
		}

		if (stamp.i$(Block.TaskOperation.class)) {
			String confirmText = stamp.a$(Block.TaskOperation.class).confirmText();
			if (confirmText != null && !confirmText.isEmpty())
				propertyList.add(frameOf("confirm", stamp.a$(Block.TaskOperation.class).confirmText()));
		}

		if (stamp.i$(Block.Map.class)) {
			propertyList.add(frameOf("zoom", stamp.a$(Block.Map.class).zoom(), "number"));
			propertyList.add(frameOf("latitude", stamp.a$(Block.Map.class).latitude(), "number"));
			propertyList.add(frameOf("longitude", stamp.a$(Block.Map.class).longitude(), "number"));
		}
	}

	private Frame frameOf(String name, Object value) {
		return frameOf(name, value, "string");
	}

	private Frame frameOf(String name, Object value, String type) {
		return new Frame().addTypes(type).addSlot("name", name).addSlot("value", value);
	}

	private Frame baseFrameOf(Block.Stamp stamp) {
		return new Frame().addTypes("stamp").addSlot("name", validMoldName().format(stamp.name$())).addSlot("type", camelCaseToSnakeCase().format(stamp.getClass().getSimpleName()));
	}
}
