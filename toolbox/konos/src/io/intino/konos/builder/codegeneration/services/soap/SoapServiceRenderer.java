package io.intino.konos.builder.codegeneration.services.soap;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Service.Soap.Operation;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static java.util.stream.Collectors.toList;

public class SoapServiceRenderer extends Renderer {
	private final List<Service.Soap> services;

	public SoapServiceRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.services = graph.serviceList(Service::isSoap).map(Service::asSoap).collect(toList());
	}

	public void render() {
		services.forEach((service) -> processService(service.a$(Service.Soap.class), gen(Target.Service)));
	}

	private void processService(Service.Soap service, File gen) {
		if (service.operationList().isEmpty()) return;
		FrameBuilder builder = new FrameBuilder("server").
				add("name", service.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("operation", framesOf(service.operationList()));
		final String className = snakeCaseToCamelCase(service.name$()) + "Service";
		classes().put(service.getClass().getSimpleName() + "#" + service.name$(), className);
		Commons.writeFrame(gen, className, new SoapServiceTemplate().render(builder.toFrame(), Formatters.all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(gen(Target.Service), className).getAbsolutePath()));
	}

	private Frame[] framesOf(List<Operation> operations) {
		return operations.stream().map(this::processOperation).toArray(Frame[]::new);
	}

	private Frame processOperation(Operation operation) {
		return new FrameBuilder("operation")
				.add("name", operation.name$())
				.add("operation", operation.getClass().getSimpleName())
				.add("path", customize("path", Commons.path(operation))).toFrame();
	}

}
