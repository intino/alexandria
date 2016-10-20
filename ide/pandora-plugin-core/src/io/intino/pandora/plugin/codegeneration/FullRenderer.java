package io.intino.pandora.plugin.codegeneration;

import cottons.utils.Files;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.channel.ChannelRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.channel.SubscriptionModelRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.service.JMSNotificationRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.service.JMSRequestRenderer;
import io.intino.pandora.plugin.codegeneration.server.jms.service.JMSServiceRenderer;
import io.intino.pandora.plugin.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import io.intino.pandora.plugin.codegeneration.server.jmx.JMXServerRenderer;
import io.intino.pandora.plugin.codegeneration.server.rest.RESTResourceRenderer;
import io.intino.pandora.plugin.codegeneration.server.rest.RESTServiceRenderer;
import io.intino.pandora.plugin.codegeneration.server.slack.SlackRenderer;
import io.intino.pandora.plugin.codegeneration.server.task.TaskRenderer;
import io.intino.pandora.plugin.codegeneration.server.task.TaskerRenderer;
import tara.magritte.Graph;

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
		formats();
		rest();
		scheduling();
		jmx();
		jms();
		channels();
		slack();
	}

	private void formats() {
		new SchemaRenderer(graph, gen, packageName).execute();
	}

	private void rest() {
		new RESTResourceRenderer(graph, gen, src, packageName).execute();
		new RESTServiceRenderer(graph, gen, packageName).execute();
//		graph.find(RESTService.class).forEach(r -> new RESTAccessorRenderer(r, gen, packageName).execute());
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(graph, src, gen, packageName).execute();
		new JMXServerRenderer(graph, gen, packageName).execute();
	}

	private void jms() {
		new JMSRequestRenderer(graph, src, gen, packageName).execute();
		new JMSServiceRenderer(graph, gen, packageName).execute();
		new JMSNotificationRenderer(graph, src, gen, packageName).execute();
	}

	private void scheduling() {
		new TaskRenderer(graph, src, gen, packageName).execute();
		new TaskerRenderer(graph, gen, packageName).execute();
	}

	private void channels() {
		new SubscriptionModelRenderer(graph, src, gen, packageName).execute();
		new ChannelRenderer(graph, gen, packageName).execute();
	}

	private void slack() {
		new SlackRenderer(graph, src, packageName).execute();
		new ChannelRenderer(graph, gen, packageName).execute();
	}
}
