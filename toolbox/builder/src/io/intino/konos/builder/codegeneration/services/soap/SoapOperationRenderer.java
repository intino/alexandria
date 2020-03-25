package io.intino.konos.builder.codegeneration.services.soap;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.SoapOperationActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.Soap.Operation;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class SoapOperationRenderer extends Renderer {
	private static final String OPERATIONS_PACKAGE = "soap/operations";
	private final List<Service.Soap> services;

	public SoapOperationRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.services = graph.serviceList(Service::isSoap).map(Service::asSoap).collect(Collectors.toList());
	}

	public void render() {
		services.forEach(this::processService);
	}

	private void processService(Service.Soap service) {
		service.operationList().forEach(this::processOperation);
	}

	private void processOperation(Operation operation) {
		Frame frame = frameOf(operation);
		final String className = snakeCaseToCamelCase(operation.name$()) + "Operation";
		File operationsPackage = new File(gen(), OPERATIONS_PACKAGE);
		Commons.writeFrame(operationsPackage, className, template().render(frame));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(operation), javaFile(operationsPackage, className).getAbsolutePath()));
		createCorrespondingAction(operation);
	}

	private void createCorrespondingAction(Operation operation) {
		new SoapOperationActionRenderer(context, operation).execute();
	}

	private Frame frameOf(Operation operation) {
		FrameBuilder builder = new FrameBuilder("operation");
		addCommons(operation.name$(), builder);
		if (operation.input() != null)
			builder.add("input", input(operation.input().asObject().type(), type(operation.input().asObject().schema()), operation.input().xmlns()));
		if (operation.output() != null)
			builder.add("returnType", new FrameBuilder().add("value", type(operation.output().asObject().schema())).add("xmlns", operation.output().xmlns()));
		return builder.toFrame();
	}

	private Frame input(String name, String type, String xmlns) {
		return new FrameBuilder("input").add("name", name).add("type", type).add("xmlns", xmlns).toFrame();
	}

	private void addCommons(String name, FrameBuilder builder) {
		builder.add("package", packageName());
		builder.add("name", name);
		builder.add("box", boxName());
	}

	private String type(Schema schema) {
		return String.join(".", packageName(), "schemas") + "." + firstUpperCase(schema.name$());
	}

	private Template template() {
		return Formatters.customize(new SoapOperationTemplate());
	}
}
