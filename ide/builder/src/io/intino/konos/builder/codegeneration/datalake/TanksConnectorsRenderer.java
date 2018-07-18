package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.EventHandler;
import io.intino.konos.model.graph.Mounter;
import io.intino.konos.model.graph.ness.NessClient;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class TanksConnectorsRenderer {
	private final NessClient datalake;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final Set<EventHandler> handlers;

	public TanksConnectorsRenderer(KonosGraph graph, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.datalake = graph.nessClient(0);
		this.handlers = new TreeSet((Comparator<EventHandler>) (o1, o2) -> namesake(o1, o2) ? 0 : -1);
		this.handlers.addAll(graph.core$().find(EventHandler.class));
	}

	private boolean namesake(EventHandler o1, EventHandler o2) {
		return fullName(o1).equalsIgnoreCase(fullName(o2));
	}

	public void execute() {
		Frame frame = new Frame().addTypes("tanks").
				addSlot("package", packageName).
				addSlot("name", datalake.name$()).
				addSlot("box", boxName).
				addSlot("tank", handlers.stream().map(this::frameOf).toArray(Frame[]::new));
		frame.addSlot("clientId", new Frame(isCustom(datalake.clientID()) ? "custom" : "standard").addSlot("value", datalake.clientID()));
		if (handlers.stream().anyMatch(h -> h.i$(Mounter.class))) frame.addSlot("tankImport", packageName);
		if (!datalake.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(new File(gen, "ness"), "TanksConnectors", template().format(frame));
	}

	private Frame frameOf(EventHandler handler) {
		final Frame frame = new Frame().addTypes("tank", handler.getClass().getSimpleName().toLowerCase()).
				addSlot("name", composedName(handler)).
				addSlot("box", boxName).
				addSlot("messageType", fullName(handler));
		if (handler.schema() != null) {
			frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", handler.schema().name$()));
		} else frame.addSlot("type", "message");
		return frame;
	}

	private String fullName(EventHandler handler) {
		return domain() + subdomain(handler) + handler.name();
	}

	private String domain() {
		return datalake.domain().isEmpty() ? "" : datalake.domain() + ".";
	}

	private String composedName(EventHandler handler) {
		return firstUpperCase((handler.subdomain().isEmpty() ? "" : Formatters.snakeCaseToCamelCase().format(handler.subdomain().replace(".", "_"))) + firstUpperCase(handler.name()));
	}

	private String subdomain(EventHandler handler) {
		return handler.subdomain().isEmpty() ? "" : handler.subdomain() + ".";
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private Template template() {
		return Formatters.customize(TanksConnectorsTemplate.create()).add("shortPath", value -> {
			String[] names = value.toString().split("\\.");
			return names[names.length - 1];
		});
	}
}
