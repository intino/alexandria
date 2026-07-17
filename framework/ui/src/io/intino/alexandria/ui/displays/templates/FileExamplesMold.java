package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.ui.AlexandriaUiBox;

import static io.intino.alexandria.ui.displays.UserMessage.Type.Success;

public class FileExamplesMold extends AbstractFileExamplesMold<AlexandriaUiBox> {

    public FileExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

	@Override
	public void init() {
		super.init();
		file2.maxSize(80 * 1024 * 1024);
		file3.maxSize(10 * 1024);
		file4.onChange(e -> notifyUser("File received: " + ((Resource)e.value()).name(), Success));
		file6.maxSize(10 * 1024);
	}
}