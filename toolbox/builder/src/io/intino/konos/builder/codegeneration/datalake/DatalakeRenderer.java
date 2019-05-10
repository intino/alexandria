package io.intino.konos.builder.codegeneration.datalake;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Procedure.Process.Input;
import io.intino.konos.model.graph.ness.NessClient;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public class DatalakeRenderer {
	private final NessClient datalake;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final KonosGraph graph;
	private final Set<EventSource> eventSources;
	private final Set<FeederEventSource> eventFeederSources;

	public DatalakeRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.graph = graph;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.datalake = graph.nessClient(0);
		this.eventSources = collectEventSources(graph);
		this.eventFeederSources = collectNewFeederSources(graph.nessClientList().stream().map(NessClient::feederList).flatMap(Collection::stream).collect(Collectors.toList()));
	}

	public void execute() {
		FrameBuilder builder = new FrameBuilder("tanks").
				add("package", packageName).
				add("name", datalake.name$()).
				add("box", boxName).
				add("tank", tanks());
		builder.add("clientId", new FrameBuilder(isCustom(datalake.clientID()) ? "custom" : "standard").add("value", datalake.clientID()).toFrame());
		if (eventSources.stream().anyMatch(h -> h.i$(Mounter.class))) builder.add("tankImport", packageName);
		if (!datalake.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName).toFrame());
		Commons.writeFrame(new File(gen, "datalake"), "Datalake", template().render(builder.toFrame()));
	}

	private Frame[] tanks() {
		List<Frame> frames = new ArrayList<>();
		frames.addAll(eventFeederSources.stream().map(this::frameOf).collect(Collectors.toList()));
		frames.addAll(eventSources.stream().map(this::frameOf).collect(Collectors.toList()));
		return frames.toArray(new Frame[0]);
	}

	private Frame frameOf(EventSource source) {
		final String messageType = messageType(source);
		final FrameBuilder builder = new FrameBuilder("tank", source.getClass().getSimpleName().toLowerCase()).
				add("messageType", messageType).
				add("box", boxName).
				add("name", source.name()).
				add("fullname", fullName(source));
		if (source.i$(Input.class)) builder.add("handler", handlers(messageType));
		type(source, builder);
		return builder.toFrame();
	}

	private Frame frameOf(FeederEventSource source) {
		final FrameBuilder builder = new FrameBuilder("tank").
				add("messageType", source.composedName()).
				add("box", boxName).
				add("name", source.name).
				add("fullname", source.fullName());
		builder.add("type", new FrameBuilder("schema").add("package", packageName).add("name", source.name).toFrame());
		return builder.toFrame();
	}

	private Frame[] handlers(String name) {
		final List<Input> inputs = graph.core$().find(Input.class).stream().filter(s -> composedName(s).equals(name)).collect(Collectors.toList());
		List<Frame> frames = new ArrayList<>();
		for (Input input : inputs) {
			final FrameBuilder builder = new FrameBuilder("handler").
					add("box", boxName).
					add("processPackage", input.i$(Input.class) ? packageName + ".procedures." + input.core$().ownerAs(Procedure.class).name$() : "").
					add("output", input.core$().ownerAs(Procedure.Process.class).outputList().stream().map(this::fullName).toArray(String[]::new)).
					add("processName", input.i$(Input.class) ? input.core$().ownerAs(Procedure.Process.class).name$() : "");
			type(input, builder);
			frames.add(builder.toFrame());
		}
		return frames.toArray(new Frame[0]);
	}

	private void type(EventSource source, FrameBuilder builder) {
		builder.add("type", source.schema() != null ? new FrameBuilder("schema").add("package", packageName).add("name", source.schema().name$()).toFrame() : "message");
	}

	private String fullName(EventSource source) {
		return domain() + subdomain(source) + source.name();
	}

	private String messageType(EventSource source) {
		return source.schema() != null ? source.schema().name$() : source.name();
	}

	private String domain() {
		return datalake.domain().isEmpty() ? "" : datalake.domain() + ".";
	}

	private String composedName(EventSource source) {
		return firstUpperCase((source.subdomain().isEmpty() ? "" : snakeCaseToCamelCase().format(source.subdomain().replace(".", "_"))) + source.name());
	}

	private String subdomain(EventSource source) {
		return source.subdomain().isEmpty() ? "" : source.subdomain() + ".";
	}

	private boolean isCustom(String value) {
		return value != null && value.startsWith("{");
	}

	private Template template() {
		return Formatters.customize(new DatalakeTemplate()).add("shortPath", value -> {
			String[] names = value.toString().split("\\.");
			return names[names.length - 1];
		});
	}

	private String fullName(Procedure.Process.Output output) {
		final String domain = output.core$().ownerAs(Procedure.class).ness().domain();
		final String subdomain = output.subdomain();
		return (domain.isEmpty() ? "" : domain + ".") + (subdomain.isEmpty() ? "" : subdomain + ".") + output.name();
	}

	private Set<FeederEventSource> collectNewFeederSources(List<Feeder> feeders) {
		Set<FeederEventSource> sources = new TreeSet((Comparator<FeederEventSource>) (o1, o2) -> compare(o1, o2));
		feeders.forEach(feeder -> {
			final String domain = feeder.core$().ownerAs(NessClient.class).domain();
			for (Schema schema : feeder.eventTypes()) {
				final FeederEventSource e = new FeederEventSource(domain, feeder.subdomain(), schema.name$());
				if (!isInEventSources(e)) sources.add(e);
			}
		});
		return sources;
	}

	private boolean isInEventSources(FeederEventSource source) {
		return eventSources.stream().anyMatch(eventSource -> fullName(eventSource).equals(source.fullName()));
	}

	private int compare(EventSource o1, EventSource o2) {
		return fullName(o1).toLowerCase().compareTo(fullName(o2).toLowerCase());
	}

	private int compare(FeederEventSource o1, FeederEventSource o2) {
		return o1.fullName().toLowerCase().compareTo(o2.fullName().toLowerCase());
	}

	private Set<EventSource> collectEventSources(KonosGraph graph) {
		Set<EventSource> events = new TreeSet((Comparator<EventSource>) (o1, o2) -> compare(o1, o2));
		final List<EventSource> c = graph.core$().find(EventSource.class);
		c.sort(inputFirst());
		events.addAll(c);
		return events;
	}

	private Comparator<? super EventSource> inputFirst() {
		return (Comparator<EventSource>) (o1, o2) -> {
			if (o1.i$(Input.class) && !o2.i$(Input.class)) return -1;
			if (o2.i$(Input.class) && !o1.i$(Input.class)) return 1;
			return 0;
		};
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
