package io.intino.pandora.plugin.codegeneration.server.jms.service;

import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.jms.JMSService;
import org.siani.itrules.Template;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMSServiceRenderer {

	private final List<JMSService> services;
	private File gen;
	private String packageName;

	public JMSServiceRenderer(Graph graph, File gen, String packageName) {
		services = graph.find(JMSService.class);
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		Frame frame = new Frame().addTypes("jms").
				addSlot("name", service.name()).
				addSlot("package", packageName).
				addSlot("request", (AbstractFrame[]) processRequests(service.requestList()));
		service.node().findNode(JMSService.Request.class).forEach(this::processRequest);
		Commons.writeFrame(gen, StringFormatter.get().get("firstuppercase").format(service.name()).toString() + "JMSService", template().format(frame));
	}

	private Frame[] processRequests(List<JMSService.Request> requests) {
		return requests.stream().map(this::processRequest).toArray(Frame[]::new);
	}

	private Frame processRequest(JMSService.Request request) {
		return new Frame().addTypes("request").addSlot("name", request.name()).addSlot("queue", request.queue());
	}

	private Template template() {
		Template template = JMSServiceTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

}
