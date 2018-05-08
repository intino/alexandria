package io.intino.konos.builder.codegeneration.datalake.process;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Process;
import io.intino.konos.model.graph.datalakeconnector.DataLakeConnectorClient;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static java.util.stream.Collectors.toList;

public class ProcessCoordinatorRenderer {
	private final DataLakeConnectorClient datalake;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<Process> processes;

	public ProcessCoordinatorRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.datalake = graph.dataLakeConnectorClientList().isEmpty() ? null : graph.dataLakeConnectorClient(0);
		this.processes = !graph.dataLakeConnectorClientList().isEmpty() ? graph.dataLakeConnectorClient(0).messageHandlerList().stream().filter(h -> h.i$(Process.class)).map(h -> h.a$(Process.class)).collect(toList()) : Collections.emptyList();
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		if (processes.isEmpty()) return;
		Frame frame = new Frame().addTypes("processes").
				addSlot("package", packageName).
				addSlot("name", datalake.name$()).
				addSlot("box", boxName).
				addSlot("process", processes());
		frame.addSlot("clientId", new Frame(isCustom(datalake.clientID()) ? "custom" : "standard").addSlot("value", datalake.clientID()));
		if (!datalake.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(new File(gen, "ness"), "ProcessCoordinator", template().format(frame));
	}

	private Frame[] processes() {
		return processes.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Process process) {
		final Frame frame = new Frame().addTypes("tank").
				addSlot("box", boxName).
				addSlot("name", composedName(process)).
				addSlot("messageType", fullName(process));
		if (process.schema() != null) {
			frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", process.schema().name$()));
		} else frame.addSlot("type", "message");
		return frame;
	}

	private String fullName(Process process) {
		return (datalake.domain().isEmpty() ? "" : datalake.domain() + ".") + subdomain(process) + name(process);
	}

	private String composedName(Process handler) {
		return handler.subdomain().isEmpty() ? "" : handler.subdomain() + firstUpperCase(name(handler));
	}

	private String subdomain(Process process) {
		return process.subdomain().isEmpty() ? "" : process.subdomain() + ".";
	}

	private String name(Process process) {
		return process.schema() == null ? process.name$() : process.schema().name$();
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}


	private Template template() {
		return Formatters.customize(ProcessCoordinatorTemplate.create()).add("shortPath", value -> {
			String[] names = value.toString().split("\\.");
			return names[names.length - 1];
		});
	}
}
