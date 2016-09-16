package teseo.codegeneration.action;

import org.siani.itrules.model.Frame;
import teseo.Channel;
import teseo.InputChannel;

import java.io.File;

import static teseo.helpers.Commons.writeFrame;

public class ChannelActionRenderer extends ActionRenderer {
	private final InputChannel channel;


	public ChannelActionRenderer(InputChannel channel, File destiny, String packageName) {
		super(destiny, packageName);
		this.channel = channel;

	}

	public void execute() {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", channel.name());
		frame.addSlot("package", packageName);
		setupMessage(channel.message(), frame);
		frame.addSlot("returnType", "void");
		if (!alreadyRendered(destiny, channel.name()))
			writeFrame(destinyPackage(destiny), channel.name() + "Action", template().format(frame));
	}


	private void setupMessage(Channel.Message message, Frame frame) {
		frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", "message").addSlot("type", formatType(message.asType())));
	}

}
