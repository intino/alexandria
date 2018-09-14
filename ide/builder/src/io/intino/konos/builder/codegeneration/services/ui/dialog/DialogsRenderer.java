package io.intino.konos.builder.codegeneration.services.ui.dialog;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.UIRenderer;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

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
		Frame frame = buildFrame();
		for (Dialog dialog : dialogs) frame.addSlot("dialog", dialogFrame(dialog));
		write(frame);
	}

	private Frame dialogFrame(Dialog dialog) {
		return new Frame("dialog")
				.addSlot("name", dialog.name$())
				.addSlot("type", dialog.getClass().getSimpleName());
	}

	private void write(Frame frame) {
		final String newDisplay = snakeCaseToCamelCase("Dialogs");
		writeFrame(new File(gen, DIALOGS), newDisplay, template().format(frame));
	}

	private Template template() {
		return Formatters.customize(DialogsTemplate.create());
	}

	protected Frame buildFrame() {
		return super.buildFrame().addTypes("dialogs");
	}
}
