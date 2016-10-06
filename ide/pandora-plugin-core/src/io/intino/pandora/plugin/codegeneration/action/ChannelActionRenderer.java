package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.model.Frame;

import java.io.File;

public class ChannelActionRenderer extends ActionRenderer {
    private final Channel model;


    public ChannelActionRenderer(Channel model, File destiny, String packageName) {
        super(destiny, packageName);
        this.model = model;
    }

    public void execute() {
        Frame frame = new Frame().addTypes("action");
        frame.addSlot("name", model.name());
        frame.addSlot("package", packageName);
        setupMessage(model.message(), frame);
        frame.addSlot("returnType", "void");
        if (!alreadyRendered(destiny, model.name()))
            Commons.writeFrame(destinyPackage(destiny), model.name() + "Action", template().format(frame));
    }

    private void setupMessage(Channel.Message message, Frame frame) {
        frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", "message").addSlot("type", formatType(message.asType())));
    }

}
