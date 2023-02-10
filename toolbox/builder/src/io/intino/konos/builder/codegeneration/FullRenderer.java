package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.accessor.PomGenerator;
import io.intino.konos.builder.codegeneration.accessor.analytic.AnalyticBuilderRenderer;
import io.intino.konos.builder.codegeneration.accessor.messaging.MessagingAccessorRenderer;
import io.intino.konos.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.konos.builder.codegeneration.accessor.ui.android.AndroidSchemaWriter;
import io.intino.konos.builder.codegeneration.accessor.ui.web.ServiceListRenderer;
import io.intino.konos.builder.codegeneration.analytic.AnalyticRenderer;
import io.intino.konos.builder.codegeneration.bpm.BpmRenderer;
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
import io.intino.konos.builder.codegeneration.services.agenda.AgendaServiceRenderer;
import io.intino.konos.builder.codegeneration.services.cli.CliRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXOperationsServiceRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerRenderer;
import io.intino.konos.builder.codegeneration.services.messaging.MessagingRequestRenderer;
import io.intino.konos.builder.codegeneration.services.messaging.MessagingServiceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTResourceRenderer;
import io.intino.konos.builder.codegeneration.services.rest.RESTServiceRenderer;
import io.intino.konos.builder.codegeneration.services.slack.SlackRenderer;
import io.intino.konos.builder.codegeneration.services.soap.SoapOperationRenderer;
import io.intino.konos.builder.codegeneration.services.soap.SoapServiceRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.compiler.shared.KonosBuildConstants.Mode;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Service;

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
			agendas();
			schemas();
			exceptions();
			rest();
			soap();
			tasks();
			jmx();
			jms();
			messageHub();
			subscribers();
			mounters();
			adapters();
			feeders();
			processes();
			analytic();
			slack();
			cli();
			box();
			main();
			ui();
			context.saveCache();
		} else if (context.mode().equals(Mode.OnlyElements)) ui();
		else accessors();
	}

	private void analytic() throws KonosException {
		new AnalyticRenderer(context, graph).execute();
	}


	private void accessors() throws KonosException {
		final PomGenerator pomGenerator = new PomGenerator(context);
		for (Service.REST rest : graph.restServiceList()) {
			final File dir = genDirectory(context.configuration().genDirectory(), "rest#", rest.name$());
			new RESTAccessorRenderer(context, rest, dir).render();
			pomGenerator.generate("rest", dir);
		}
//		for (Service.JMX jmx : graph.jmxServiceList())
//			new JMXAccessorRenderer(context, jmx, genDirectory(context.configuration().genDirectory(), "jmx#", jmx.name$())).render();
		for (Service.Messaging service : graph.messagingServiceList()) {
			final File dir = genDirectory(context.configuration().genDirectory(), "messaging#", service.name$());
			new MessagingAccessorRenderer(context, service, dir).render();
			pomGenerator.generate("messaging", dir);
		}

		if (!graph.cubeList().isEmpty()) {
			final File dir = analyticBasePath();
			new AnalyticBuilderRenderer(context, graph, new File(dir, "src"), new File(analyticBasePath(), "res")).render();
			pomGenerator.generate("analytic", dir);
		}
		androidClient();
	}

	private File analyticBasePath() {
		return new File(context.configuration().genDirectory(), "analytic#analytic");
	}

	private File genDirectory(File tempDirectory, String serviceType, String serviceName) {
		return new File(tempDirectory, serviceType + serviceName + File.separator + "src");
	}

	private void schemas() throws KonosException {
		new SchemaListRenderer(context, graph).execute();
	}

	private void exceptions() throws KonosException {
		new ExceptionRenderer(context, graph).execute();
	}

	private void rest() throws KonosException {
		new RESTResourceRenderer(context, graph).execute();
		new RESTServiceRenderer(context, graph).execute();
	}

	private void soap() throws KonosException {
		new SoapOperationRenderer(context, graph).execute();
		new SoapServiceRenderer(context, graph).execute();
	}

	private void jmx() throws KonosException {
		new JMXOperationsServiceRenderer(context, graph).execute();
		new JMXServerRenderer(context, graph).execute();
	}

	private void jms() throws KonosException {
		new MessagingRequestRenderer(context, graph).execute();
		new MessagingServiceRenderer(context, graph).execute();
	}

	private void tasks() throws KonosException {
		new ListenerRenderer(context, graph).execute();
		new SentinelsRenderer(context, graph).execute();
	}

	private void agendas() throws KonosException {
		new AgendaServiceRenderer(context, graph).execute();
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

	private void processes() throws KonosException {
		new BpmRenderer(context, graph).execute();
	}

	private void feeders() {
		new FeederRenderer(context, graph).execute();
	}

	private void slack() throws KonosException {
		new SlackRenderer(context, graph).execute();
	}

	private void cli() throws KonosException {
		new CliRenderer(context, graph).execute();
	}

	private void ui() throws KonosException {
		if (context.mode() == Mode.Normal) uiServer();
		webClient();
	}

	private void uiServer() throws KonosException {
		new io.intino.konos.builder.codegeneration.services.ui.ServiceListRenderer(context, graph).execute();
	}

	private void webClient() throws KonosException {
		ComponentRenderer.clearCache();
		new ServiceListRenderer(context, graph).execute();
		ComponentRenderer.clearCache();
	}

	private void androidClient() throws KonosException {
		ComponentRenderer.clearCache();
		new io.intino.konos.builder.codegeneration.accessor.ui.android.ServiceListRenderer(context, graph, service -> context.configuration().genDirectory()).execute();
		AndroidSchemaWriter schemaWriter = new AndroidSchemaWriter(context);
		new SchemaListRenderer(context, graph, schemaWriter.destination(), schemaWriter.packageName(), schemaWriter).execute();
		ComponentRenderer.clearCache();
	}

	private void box() throws KonosException {
		AbstractBoxRenderer renderer = new AbstractBoxRenderer(context, graph);
		renderer.execute();
		new BoxRenderer(context, graph, hasModel).execute();
		new BoxConfigurationRenderer(context, hasModel, renderer.customParameters()).execute();
	}

	private void main() {
		new MainRenderer(context, hasModel, graph).execute();
	}

	private boolean hasModel() {
		return context.configuration().model() != null && context.configuration().model().language() != null;
	}
}
