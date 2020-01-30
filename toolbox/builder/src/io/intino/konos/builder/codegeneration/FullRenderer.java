package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.bpm.BpmRenderer;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.codegeneration.datahub.DatalakeRenderer;
import io.intino.konos.builder.codegeneration.datahub.adapter.AdapterRenderer;
import io.intino.konos.builder.codegeneration.datahub.messagehub.MessageHubRenderer;
import io.intino.konos.builder.codegeneration.datahub.mounter.MounterFactoryRenderer;
import io.intino.konos.builder.codegeneration.datahub.mounter.MounterRenderer;
import io.intino.konos.builder.codegeneration.datahub.subscriber.SubscriberRenderer;
import io.intino.konos.builder.codegeneration.exception.ExceptionRenderer;
import io.intino.konos.builder.codegeneration.feeder.FeederRenderer;
import io.intino.konos.builder.codegeneration.main.MainRenderer;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.codegeneration.sentinel.ListenerRenderer;
import io.intino.konos.builder.codegeneration.sentinel.SentinelsRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXOperationsServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerRenderer;
import io.intino.konos.builder.codegeneration.services.messaging.MessagingRequestRenderer;
import io.intino.konos.builder.codegeneration.services.messaging.MessagingServiceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTResourceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTServiceRenderer;
import io.intino.konos.builder.codegeneration.services.slack.SlackRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.KonosGraph;

import javax.annotation.Nullable;

public class FullRenderer {
	@Nullable
	private final KonosGraph graph;
	private final CompilationContext context;
	private final boolean hasModel;

	public FullRenderer(KonosGraph graph, CompilationContext context) {
		this.graph = graph;
		this.context = context;
		this.hasModel = hasModel();
	}

	public void execute() {
		clean();
		render();
	}

	private void clean() {
		new FullCleaner(context, graph).execute();
	}

	private void render() {
		schemas();
		exceptions();
		rest();
		tasks();
		jmx();
		jms();
		datalake();
		messageHub();
		subscribers();
		mounters();
		adapters();
		feeders();
		processes();
		slack();
		ui();
		box();
		main();
//		InterfaceToJavaImplementation.nodeMap.clear();
//		InterfaceToJavaImplementation.nodeMap.putAll(settings.classes());
	}

	private void schemas() {
		new SchemaListRenderer(context, graph).execute();
	}

	private void exceptions() {
		new ExceptionRenderer(context, graph).execute();
	}

	private void rest() {
		new RESTResourceRenderer(context, graph).execute();
		new RESTServiceRenderer(context, graph).execute();
	}

	private void jmx() {
		new JMXOperationsServiceRenderer(context, graph).execute();
		new JMXServerRenderer(context, graph).execute();
	}

	private void jms() {
		new MessagingRequestRenderer(context, graph).execute();
		new MessagingServiceRenderer(context, graph).execute();
	}

	private void tasks() {
		new ListenerRenderer(context, graph).execute();
		new SentinelsRenderer(context, graph).execute();
	}

	private void datalake() {
		new DatalakeRenderer(context, graph).execute();
	}

	private void messageHub() {
		new MessageHubRenderer(context, graph).execute();
	}

	private void mounters() {
		new MounterFactoryRenderer(context, graph).execute();
		new MounterRenderer(context, graph).execute();
	}

	private void subscribers() {
		new SubscriberRenderer(context, graph).execute();
	}

	private void adapters() {
		new AdapterRenderer(context, graph).execute();
	}

	private void processes() {
		new BpmRenderer(context, graph).execute();
	}

	private void feeders() {
		new FeederRenderer(context, graph).execute();
	}

	private void slack() {
		new SlackRenderer(context, graph).execute();
	}

	private void ui() {
		ElementCache cache = context.cache();
		ComponentRenderer.clearCache();
		io.intino.konos.builder.codegeneration.cache.ElementCache serverCache = cache.clone();
		uiServer(serverCache);
		uiClient(cache.clone());
		cache.putAll(serverCache);
	}

	private void uiServer(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new io.intino.konos.builder.codegeneration.services.ui.ServiceListRenderer(context.clone().cache(cache), graph).execute();
	}

	private void uiClient(io.intino.konos.builder.codegeneration.cache.ElementCache cache) {
		new io.intino.konos.builder.codegeneration.accessor.ui.ServiceListRenderer(context.clone().cache(cache), graph).execute();
	}

	private void box() {
		AbstractBoxRenderer renderer = new AbstractBoxRenderer(context, graph, hasModel);
		renderer.execute();

		new BoxRenderer(context, graph, hasModel).execute();
		new BoxConfigurationRenderer(context, hasModel, renderer.customParameters()).execute();
	}

	private void main() {
		new MainRenderer(context, hasModel).execute();
	}

	private boolean hasModel() {
		return context.configuration().model() != null && context.configuration().model().language() != null;
	}
}
