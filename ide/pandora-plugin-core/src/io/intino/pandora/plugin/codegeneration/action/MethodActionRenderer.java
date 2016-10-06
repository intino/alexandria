package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.Format;
import io.intino.pandora.plugin.Method;
import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

public class MethodActionRenderer extends ActionRenderer {
    private final Method method;

    public MethodActionRenderer(Method method, File destiny, String packageName) {
        super(destiny, packageName);
        this.method = method;
    }

    public void execute() {
        Frame frame = new Frame().addTypes("action");
        frame.addSlot("name", method.name());
        frame.addSlot("package", packageName);
        setupParameters(method.parameterList(), frame);
        frame.addSlot("returnType", Commons.returnType(method.response()));
        frame.addSlot("throws", Commons.returnType(method.response()));
        if (!method.graph().find(Format.class).isEmpty())
            frame.addSlot("formatImport", new Frame().addTypes("formatImport").addSlot("package", packageName));
        if (!alreadyRendered(destiny, method.name()))
            Commons.writeFrame(destinyPackage(destiny), method.name() + "Action", template().format(frame));
    }


    private void setupParameters(List<Parameter> parameters, Frame frame) {
        for (Parameter parameter : parameters)
            frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType())));
    }

}
