package io.intino.pandora.plugin.codegeneration.server.jms;

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
				addSlot("resource", (AbstractFrame[]) processResources(service.resourceList()));
		service.node().findNode(JMSService.Resource.class).forEach(this::processResource);
		Commons.writeFrame(gen, StringFormatter.get().get("firstuppercase").format(service.name()).toString() + "JMSService", template().format(frame));
	}

	private Frame[] processResources(List<JMSService.Resource> resources) {
		return resources.stream().map(this::processResource).toArray(Frame[]::new);
	}

	private Frame processResource(JMSService.Resource resource) {
		return new Frame().addTypes("resource").addSlot("name", resource.name()).addSlot("queue", resource.queue());
	}

	private Template template() {
		Template template = JMSServiceTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

}
