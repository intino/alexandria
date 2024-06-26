package io.intino.konos.builder.codegeneration.services.jmx;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Service.JMX.Operation;

import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class JMXServerRenderer extends Renderer {
	private final List<Service.JMX> services;

	public JMXServerRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.services = graph.serviceList(Service::isJMX).map(Service::asJMX).collect(Collectors.toList());
	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(Service.JMX service) {
		final List<Operation> operations = service.operationList();
		if (operations.isEmpty()) return;
		FrameBuilder frame = new FrameBuilder("jmxserver")
				.add("name", service.name$())
				.add("box", boxName())
				.add("package", packageName());
		if (!service.path().isEmpty()) frame.add("path", service.path());
		writeFrame(gen(Target.Service), "JMX" + snakeCaseToCamelCase(service.name$()), new JMXServerTemplate().render(frame.toFrame(), Formatters.all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(gen(Target.Service), "JMX" + snakeCaseToCamelCase(service.name$())).getAbsolutePath()));
	}
}