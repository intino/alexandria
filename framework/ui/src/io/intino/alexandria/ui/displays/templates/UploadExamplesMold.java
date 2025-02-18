package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.UserMessage;

import java.util.stream.Collectors;

public class UploadExamplesMold extends AbstractUploadExamplesMold<UiFrameworkBox> {

	public UploadExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		upload1.onUploading(e -> notifyUser("Uploading...", UserMessage.Type.Loading));
		upload1.onExecute(e -> notifyUser("Files uploaded: " + e.values().stream().map(Resource::name).collect(Collectors.joining(", ")), UserMessage.Type.Info));
		upload2.onUploading(e -> notifyUser("Uploading...", UserMessage.Type.Loading));
		upload2.onExecute(e -> notifyUser("Files uploaded: " + e.values().stream().map(Resource::name).collect(Collectors.joining(", ")), UserMessage.Type.Info));
	}
}