package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Procedure.Process.Input;
import io.intino.konos.model.graph.ness.NessClient;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class TanksConnectorsRenderer {
	private final NessClient datalake;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final Set<EventSource> eventSources;
	private final Set<FeederEventSource> eventFeederSources;

	public TanksConnectorsRenderer(KonosGraph graph, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.datalake = graph.nessClient(0);
		this.eventSources = new TreeSet((Comparator<EventSource>) (o1, o2) -> namesake(o1, o2) ? 0 : -1);
		this.eventSources.addAll(graph.core$().find(EventSource.class));
		this.eventFeederSources = collectNewFeederSources(graph.nessClientList().stream().map(NessClient::feederList).flatMap(Collection::stream).collect(Collectors.toList()));
	}

	private boolean namesake(EventSource o1, EventSource o2) {
		return fullName(o1).equalsIgnoreCase(fullName(o2));
	}

	public void execute() {
		Frame frame = new Frame().addTypes("tanks").
				addSlot("package", packageName).
				addSlot("name", datalake.name$()).
				addSlot("box", boxName).
				addSlot("tank", eventSources.stream().map(this::frameOf).toArray(Frame[]::new)).
				addSlot("tank", eventFeederSources.stream().map(this::frameOf).toArray(Frame[]::new));
		frame.addSlot("clientId", new Frame(isCustom(datalake.clientID()) ? "custom" : "standard").addSlot("value", datalake.clientID()));
		if (eventSources.stream().anyMatch(h -> h.i$(Mounter.class))) frame.addSlot("tankImport", packageName);
		if (!datalake.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		Commons.writeFrame(new File(gen, "ness"), "TanksConnectors", template().format(frame));
	}

	private Frame frameOf(EventSource source) {
		final Frame frame = new Frame().addTypes("tank", source.getClass().getSimpleName().toLowerCase()).
				addSlot("name", composedName(source)).
				addSlot("box", boxName).
				addSlot("processPackage", source.i$(Input.class) ? packageName + ".procedures." + source.core$().ownerAs(Procedure.class).name$() : "").
				addSlot("messageType", fullName(source));
		if (source.schema() != null)
			frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", source.schema().name$()));
		else frame.addSlot("type", "message");
		return frame;
	}


	private Frame frameOf(FeederEventSource source) {
		final Frame frame = new Frame().addTypes("tank").
				addSlot("name", source.composedName()).
				addSlot("box", boxName).
				addSlot("messageType", source.fullName());
		frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", source.name));
		return frame;
	}

	private String fullName(EventSource source) {
		return domain() + subdomain(source) + source.name();
	}

	private String domain() {
		return datalake.domain().isEmpty() ? "" : datalake.domain() + ".";
	}

	private String composedName(EventSource source) {
		return firstUpperCase((source.subdomain().isEmpty() ? "" : snakeCaseToCamelCase().format(source.subdomain().replace(".", "_"))) + firstUpperCase(source.name()));
	}

	private String subdomain(EventSource source) {
		return source.subdomain().isEmpty() ? "" : source.subdomain() + ".";
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


	private Set<FeederEventSource> collectNewFeederSources(List<Feeder> feeders) {
		Set<FeederEventSource> sources = new TreeSet((Comparator<FeederEventSource>) (o1, o2) -> namesake(o1, o2) ? 0 : -1);
		for (Feeder feeder : feeders) {
			final String domain = feeder.core$().ownerAs(NessClient.class).domain();
			for (Schema schema : feeder.eventTypes()) sources.add(new FeederEventSource(domain, feeder.subdomain(), schema.name$()));
		}
		return sources;
	}

	private boolean namesake(FeederEventSource o1, FeederEventSource o2) {
		return o1.fullName().equalsIgnoreCase(o2.fullName());
	}

	private static class FeederEventSource {
		String domain;
		String subdomain;
		String name;

		public FeederEventSource(String domain, String subdomain, String name) {
			this.domain = domain;
			this.subdomain = subdomain;
			this.name = name;
		}

		private String fullName() {
			return (domain.isEmpty() ? "" : domain + ".") + (subdomain.isEmpty() ? "" : subdomain + ".") + name;
		}


		private String composedName() {
			return firstUpperCase((subdomain.isEmpty() ? "" : snakeCaseToCamelCase().format(subdomain.replace(".", "_"))) + firstUpperCase(name));
		}
	}
}
