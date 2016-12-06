package io.intino.pandora.plugin.codegeneration.accessor.jmx;

import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.jmx.JMXService;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXAccessorRenderer {
	private final JMXService service;
	private File destination;
	private String packageName;

	public JMXAccessorRenderer(JMXService restService, File destination, String packageName) {
		this.service = restService;
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		new SchemaRenderer(service.graph(), destination, packageName).execute();
		processService(service);
	}

	private void createInterface(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "interface");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		for (JMXService.Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinyPackage(), service.name() + "MBean", template().format(frame));
	}

	private void processService(JMXService service) {
		Frame frame = new Frame().addTypes("accessor");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		if (!service.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		List<Frame> resourceFrames = new ArrayList<>();
		frame.addSlot("resource", (AbstractFrame[]) resourceFrames.toArray(new AbstractFrame[resourceFrames.size()]));
		Commons.writeFrame(destination, snakeCaseToCamelCase(service.name()) + "Accessor", getTemplate().format(frame));
	}


	private Template getTemplate() {
		Template template = JMXAccessorTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> {
			if (value.equals("Void")) return "void";
			else if (value.toString().contains(".")) return firstLowerCase(value.toString());
			else return value;
		});
		template.add("ValidPackage", Commons::validPackage);
		return template;
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

}
