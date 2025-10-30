package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;

public class FileExamplesMold extends AbstractFileExamplesMold<AlexandriaUiBox> {

    public FileExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

	@Override
	public void init() {
		super.init();
		file2.maxSize(80 * 1024 * 1024);
		file3.maxSize(10 * 1024);
		file6.maxSize(10 * 1024);
	}
}