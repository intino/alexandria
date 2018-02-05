package io.intino.konos.builder.codegeneration.services.activity.dialog;

import io.intino.konos.builder.codegeneration.services.activity.display.DisplaysTemplate;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class DialogsRenderer {
	private static final String DIALOGS = "dialogs";

	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Dialog> dialogs;

	public DialogsRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.gen = gen;
		this.packageName = packageName;
		this.dialogs = graph.dialogList();
		this.boxName = boxName;
	}

	public void execute() {
		if (dialogs.isEmpty()) return;
		Frame frame = createFrame();
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
		return customize(DialogsTemplate.create());
	}

	private Template customize(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private Frame createFrame() {
		return new Frame("dialogs")
				.addSlot("box", boxName)
				.addSlot("package", packageName);
	}
}
