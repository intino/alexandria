package teseo.codegeneration;

import cottons.utils.Files;
import tara.magritte.Graph;
import teseo.codegeneration.schema.SchemaRenderer;
import teseo.codegeneration.server.jms.JMSResourceRenderer;
import teseo.codegeneration.server.jms.JMSServiceRenderer;
import teseo.codegeneration.server.jms.channel.ChannelRenderer;
import teseo.codegeneration.server.jms.channel.SubscriptionModelRenderer;
import teseo.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import teseo.codegeneration.server.jmx.JMXServerRenderer;
import teseo.codegeneration.server.rest.RESTResourceRenderer;
import teseo.codegeneration.server.rest.RESTServiceRenderer;
import teseo.codegeneration.server.scheduling.ScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.SchedulerRenderer;

import java.io.File;

public class FullRenderer {

	private final Graph graph;
	private final File gen;
	private final File src;
	private final String packageName;

	public FullRenderer(Graph graph, File src, File gen, String packageName) {
		this.graph = graph;
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
	}

	public void execute() {
		Files.removeDir(gen);
		schemas();
		rest();
		scheduling();
		jmx();
		jms();
		channels();
	}

	private void schemas() {
		new SchemaRenderer(graph, gen, packageName).execute();
	}

	private void rest() {
		new RESTResourceRenderer(graph, gen, src, packageName).execute();
		new RESTServiceRenderer(graph, gen, packageName).execute();
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(graph, src, gen, packageName).execute();
		new JMXServerRenderer(graph, gen, packageName).execute();
	}

	private void jms() {
		new JMSResourceRenderer(graph, src, gen, packageName).execute();
		new JMSServiceRenderer(graph, gen, packageName).execute();
	}

	private void scheduling() {
		new ScheduledTriggerRenderer(graph, src, gen, packageName).execute();
		new SchedulerRenderer(graph, gen, packageName).execute();
	}

	private void channels() {
		new SubscriptionModelRenderer(graph, src, gen, packageName).execute();
		new ChannelRenderer(graph, gen, packageName).execute();
	}

}
