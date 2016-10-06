package io.intino.pandora.plugin.codegeneration.server.jms;

import io.intino.pandora.plugin.Format;
import io.intino.pandora.plugin.Method;
import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.codegeneration.action.MethodActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.jms.JMSService;
import io.intino.pandora.plugin.jms.JMSService.Resource;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class JMSResourceRenderer {
    private static final String RESOURCES = "resources";
    private final List<JMSService> services;
    private File gen;
    private File src;
    private String packageName;

    public JMSResourceRenderer(Graph graph, File gen, File src, String packageName) {
        services = graph.find(JMSService.class);
        this.gen = gen;
        this.src = src;
        this.packageName = packageName;
    }

    public void execute() {
        services.forEach(this::processService);
    }

    private void processService(JMSService service) {
        service.node().findNode(Resource.class).forEach(this::processResource);
    }

    private void processResource(Resource resource) {
        Frame frame = fillResourceFrame(resource);
        writeFrame(new File(gen, RESOURCES), snakeCaseToCamelCase(resource.name()) + "Resource", template().format(frame));
        createCorrespondingAction(resource);
    }

    private void createCorrespondingAction(Resource resource) {
        new MethodActionRenderer(resource, src, packageName).execute();
    }

    private Frame fillResourceFrame(Resource resource) {
        final String returnType = Commons.returnType(resource.response());
        Frame frame = new Frame().addTypes("resource").
                addSlot("name", resource.name()).
                addSlot("package", packageName).
                addSlot("call", new Frame().addTypes(returnType)).
                addSlot("parameter", (AbstractFrame[]) parameters(resource.parameterList()));
        if (!returnType.equals("void"))
            frame.addSlot("returnType", returnType).addSlot("returnMessageType", messageType(resource.response()));
        if (!resource.graph().find(Format.class).isEmpty())
            frame.addSlot("formatImport", new Frame().addTypes("formatImport").addSlot("package", packageName));
        return frame;
    }

    private String messageType(Method.Response response) {
        return response.isFile() ? "Bytes" : "Text";
    }

    private Frame[] parameters(List<Parameter> parameters) {
        return parameters.stream().map(this::parameter).toArray(Frame[]::new);
    }

    private Frame parameter(Parameter parameter) {
        return new Frame().addTypes("parameter", parameter.asType().getClass().getSimpleName())
                .addSlot("name", parameter.name())
                .addSlot("type", parameter.asType().type());
    }

    private Template template() {
        Template template = JMSResourceTemplate.create();
        template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
        template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
        template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
        return template;
    }
}
