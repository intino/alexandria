package teseo.codegeneration.server.web;

import teseo.Application;
import teseo.Resource;
import teseo.Schema;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.helpers.Commons;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JavaServerActionRenderer {
    private final List<Application> applications;
    private File genDestination;
    private File srcDestination;
    private String packageName;

    public JavaServerActionRenderer(Graph graph) {
        applications = graph.find(Application.class);
    }

    public void execute(File genDestination, File srcDestination, String packageName) {
        this.genDestination = genDestination;
        this.srcDestination = srcDestination;
        this.packageName = packageName;
        applications.forEach(this::processApplication);
    }

    private void processApplication(Application application) {
        application.node().findNode(Resource.class).forEach(this::processResource);
    }

    private void processResource(Resource resource) {
        Frame frame = new Frame().addTypes("action");
        frame.addSlot("name", resource.name());
        frame.addSlot("package", packageName);
        frame.addSlot("doTask", doTask(resource));
        frame.addSlot("throws", throwCodes(resource));
        frame.addSlot("returnType", Commons.returnType(resource.responseList()));
        frame.addSlot("parameters", (AbstractFrame[]) parameters(resource.parameterList()));
        if(!resource.graph().find(Schema.class).isEmpty()) frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
        Commons.writeFrame(new File(genDestination, "actions"), resource.name() + "AbstractAction", template().format(frame));
        if(!Commons.javaFile(new File(srcDestination, "actions"), resource.name() + "Action").exists())
            Commons.writeFrame(new File(srcDestination, "actions"), resource.name() + "Action", template().format(frame.addTypes("impl")));
    }

    private AbstractFrame doTask(Resource resource) {
        Resource.Response response = Commons.successResponse(resource.responseList());
        return new Frame().addTypes(response.asType() == null ? "void" : response.isObject() ? "object" : "other")
                .addSlot("returnType", Commons.returnType(resource.responseList()));
    }

    private String[] throwCodes(Resource resource) {
        String[] throwCodes = Commons.nonSuccessResponse(resource.responseList()).stream().map(r -> r.code().toString()).toArray(String[]::new);
        return throwCodes.length == 0 ? new String[]{"ErrorUnknown"} : throwCodes;
    }

    private Frame[] parameters(List<Resource.Parameter> parameters) {
        return parameters.stream().map(this::parameter).toArray(Frame[]::new);
    }

    private Frame parameter(Resource.Parameter parameter) {
        return new Frame().addTypes("parameter", parameter.in().toString(), (parameter.required() ? "required" : "optional"))
                .addSlot("name", parameter.name())
                .addSlot("parameterType", parameter.asType().type());
    }

    private Template template() {
        Template template = JavaServerActionTemplate.create();
        template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
        template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
        template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
        return template;
    }

}
