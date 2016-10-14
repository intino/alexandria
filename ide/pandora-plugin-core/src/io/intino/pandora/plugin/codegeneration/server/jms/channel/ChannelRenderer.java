package io.intino.pandora.plugin.codegeneration.server.jms.channel;

import io.intino.pandora.plugin.Channel;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.Queue;
import io.intino.pandora.plugin.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class ChannelRenderer {
    private final List<Channel> channels;
    private final File gen;
    private final String packageName;

    public ChannelRenderer(Graph graph, File gen, String packageName) {
        channels = graph.find(Channel.class);
        this.gen = gen;
        this.packageName = packageName;
    }

    public void execute() {
        channels.forEach(this::processChannel);
    }

    private void processChannel(Channel channel) {
        Frame frame = new Frame().addTypes("channel").
                addSlot("package", packageName).
                addSlot("name", channel.name()).
                addSlot("subscription", subscriptions(channel));
        if (!channel.graph().find(Schema.class).isEmpty())
            frame.addSlot("formatImport", new Frame().addTypes("formatImport").addSlot("package", packageName));
        Commons.writeFrame(gen, snakeCaseToCamelCase(channel.name()) + "Channel", template().format(frame));
    }

    private Frame subscriptions(Channel channel) {
        return new Frame().addTypes("subscription").
                addSlot("path", channel.id()).
                addSlot("type", channel.is(Queue.class) ? "Queue" : "Topic").
                addSlot("name", channel.name());
    }

    private Template template() {
        Template template = ChannelTemplate.create();
        template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
        template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
        return template;
    }
}
