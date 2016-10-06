package io.intino.pandora.plugin.codegeneration.server.jms.channel;

import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.Format;
import io.intino.pandora.plugin.codegeneration.action.ChannelActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class SubscriptionModelRenderer {
    private final List<Channel> channels;
    private File src;
    private File gen;
    private String packageName;

    public SubscriptionModelRenderer(Graph graph, File src, File gen, String packageName) {
        channels = graph.find(Channel.class);
        this.src = src;
        this.gen = gen;
        this.packageName = packageName;
    }

    public void execute() {
        channels.forEach(this::processModel);
    }

    private void processModel(Channel channel) {
        final Frame frame = new Frame().addTypes("subscription").
                addSlot("type", "Consumer").
                addSlot("package", packageName).
                addSlot("name", channel.name()).
                addSlot("message", message(channel.message()));
        if (!channel.graph().find(Format.class).isEmpty())
            frame.addSlot("formatImport", new Frame().addTypes("formatImport").addSlot("package", packageName));
        Commons.writeFrame(new File(gen, "subscriptions"), snakeCaseToCamelCase(channel.name()) + "Subscription", template().format(frame));
        createCorrespondingAction(channel);
    }


    private void createCorrespondingAction(Channel channel) {
        new ChannelActionRenderer(channel, src, packageName).execute();
    }

    private Frame message(Channel.Message message) {
        return new Frame().addTypes("message", message.asType().type(), message.asType().getClass().getSimpleName()).addSlot("type", message.asType().type());
    }

    private Template template() {
        Template template = SubscriptionModelTemplate.create();
        template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
        template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
        return template;
    }
}
