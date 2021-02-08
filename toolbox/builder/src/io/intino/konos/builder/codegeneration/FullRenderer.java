package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.accessor.analytic.AnalyticBuilderRenderer;
import io.intino.konos.builder.codegeneration.accessor.jmx.JMXAccessorRenderer;
import io.intino.konos.builder.codegeneration.accessor.messaging.MessagingAccessorRenderer;
import io.intino.konos.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.konos.builder.codegeneration.analytic.AnalyticRenderer;
import io.intino.konos.builder.codegeneration.bpm.BpmRenderer;
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
import io.intino.konos.builder.codegeneration.services.soap.SoapOperationRenderer;
import io.intino.konos.builder.codegeneration.services.soap.SoapServiceRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.compiler.shared.KonosBuildConstants.Mode;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;

public class FullRenderer {
	private final KonosGraph graph;
	private final CompilationContext context;
	private final boolean hasModel;

	public FullRenderer(KonosGraph graph, CompilationContext context) {
		this.graph = graph;
		this.context = context;
		this.hasModel = hasModel();
	}

	public void execute() throws KonosException {
		render();
	}

	private void render() throws KonosException {
		if (context.mode().equals(Mode.Normal)) {
			schemas();
			exceptions();
			rest();
			soap();
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
			analytic();
			slack();
			box();
			main();
			ui();
			context.saveCache();
		} else if (context.mode().equals(Mode.OnlyElements)) ui();
		else accessors();
	}

	private void analytic() {
		new AnalyticRenderer(context, graph).execute();
	}


	private void accessors() {
		graph.restServiceList().forEach(service -> new RESTAccessorRenderer(context, service, genDirectory(context.configuration().genDirectory(), "rest#", service.name$())).render());
		graph.jmxServiceList().forEach(service -> new JMXAccessorRenderer(context, service, genDirectory(context.configuration().genDirectory(), "jmx#", service.name$())).render());
		graph.messagingServiceList().forEach(service -> new MessagingAccessorRenderer(context, service, genDirectory(context.configuration().genDirectory(), "messaging#", service.name$())).render());
		if (!graph.cubeList().isEmpty())
			new AnalyticBuilderRenderer(context, graph, new File(context.configuration().genDirectory(), "analytic#analytic" + File.separator + "src")).render();
	}

	private File genDirectory(File tempDirectory, String serviceType, String serviceName) {
		return new File(tempDirectory, serviceType + serviceName + File.separator + "src");
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

	private void soap() {
		new SoapOperationRenderer(context, graph).execute();
		new SoapServiceRenderer(context, graph).execute();
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

	private void mounters() throws KonosException {
		new MounterFactoryRenderer(context, graph).execute();
		new MounterRenderer(context, graph).execute();
	}

	private void subscribers() throws KonosException {
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
		ComponentRenderer.clearCache();
		if (context.mode() == Mode.Normal) uiServer();
		uiClient();
	}

	private void uiServer() {
		new io.intino.konos.builder.codegeneration.services.ui.ServiceListRenderer(context, graph).execute();
	}

	private void uiClient() {
		new io.intino.konos.builder.codegeneration.accessor.ui.ServiceListRenderer(context, graph).execute();
	}

	private void box() {
		AbstractBoxRenderer renderer = new AbstractBoxRenderer(context, graph);
		renderer.execute();
		new BoxRenderer(context, hasModel).execute();
		new BoxConfigurationRenderer(context, hasModel, renderer.customParameters()).execute();
	}

	private void main() {
		new MainRenderer(context, hasModel).execute();
	}

	private boolean hasModel() {
		return context.configuration().model() != null && context.configuration().model().language() != null;
	}
}
