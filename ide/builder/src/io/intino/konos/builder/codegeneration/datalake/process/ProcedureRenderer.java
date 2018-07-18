package io.intino.konos.builder.codegeneration.datalake.process;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Procedure;
import io.intino.konos.model.graph.ness.NessClient;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class ProcedureRenderer {
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Procedure> procedures;

	public ProcedureRenderer(KonosGraph graph, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.procedures = graph.procedureList();
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		for (Procedure procedure : procedures) {
			Frame frame = new Frame().addTypes("procedure").
					addSlot("package", packageName).
					addSlot("box", boxName).
					addSlot("process", frameOf(procedure.processList(), procedure.ness()));
			frame.addSlot("clientId", new Frame(isCustom(procedure.ness().clientID()) ? "custom" : "standard").addSlot("value", procedure.ness().clientID()));
			if (!procedure.graph().schemaList().isEmpty())
				frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
			Commons.writeFrame(new File(gen, "ness"), "Procedure", template().format(frame));
		}
	}

	private Frame[] frameOf(List<Procedure.Process> processes, NessClient ness) {
		return processes.stream().map(p -> frameOf(p, ness)).toArray(Frame[]::new);
	}

	private Frame frameOf(Procedure.Process process, NessClient ness) {
		final Frame frame = new Frame().addTypes("tank").
				addSlot("box", boxName).
				addSlot("name", composedName(process)).
				addSlot("messageType", fullName(process, ness.domain()));
		if (process.input().schema() != null) {
			frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", process.input().schema().name$()));
		} else frame.addSlot("type", "message");
		return frame;
	}

	private String fullName(Procedure.Process process, String domain) {
		return (domain.isEmpty() ? "" : domain + ".") + subdomain(process) + process.input().name();
	}

	private String composedName(Procedure.Process process) {
		return (process.input().subdomain().isEmpty() ? "" : process.input().subdomain()) + firstUpperCase(process.input().name());
	}

	private String subdomain(Procedure.Process process) {
		return process.input().subdomain().isEmpty() ? "" : process.input().subdomain() + ".";
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private Template template() {
		return Formatters.customize(ProcedureTemplate.create()).add("shortPath", value -> {
			String[] names = value.toString().split("\\.");
			return names[names.length - 1];
		});
	}
}
