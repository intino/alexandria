package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class DialogsRenderer extends UIRenderer {

	private final File gen;
	private final List<Dialog> dialogs;

	public DialogsRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		super(boxName, packageName);
		this.gen = gen;
		this.dialogs = graph.dialogList();
	}

	public void execute() {
		if (dialogs.isEmpty()) return;
		FrameBuilder frame = frameBuilder();
		for (Dialog dialog : dialogs) frame.add("dialog", dialogFrame(dialog));
		write(frame);
	}

	private Frame dialogFrame(Dialog dialog) {
		return new FrameBuilder("dialog")
				.add("name", dialog.name$())
				.add("type", dialog.getClass().getSimpleName()).toFrame();
	}

	private void write(FrameBuilder builder) {
		final String newDisplay = snakeCaseToCamelCase("Dialogs");
		writeFrame(new File(gen, DIALOGS), newDisplay, template().render(builder.toFrame()));
	}

	private Template template() {
		return Formatters.customize(new DialogsTemplate());
	}

	protected FrameBuilder frameBuilder() {
		return super.frameBuilder().add("dialogs");
	}
}