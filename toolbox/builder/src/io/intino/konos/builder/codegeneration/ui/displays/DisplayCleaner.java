package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;

public class DisplayCleaner extends Cleaner {
	private ElementReference reference;

	public DisplayCleaner(Settings settings, ElementReference reference) {
		super(settings);
		this.reference = reference;
	}

	@Override
	public void execute() {
		clean(Target.Accessor);
		clean(Target.Service);
		cache().remove(reference.toString());
	}

	public void clean(Target target) {
		String name = displayFilename(reference.name());
		removePassiveViewFiles(target);
		remove(displayFolder(gen(), reference.type(), target), name);
	}

	private void removePassiveViewFiles(Target target) {
		String name = displayFilename(reference.name());
		remove(displayNotifierFolder(gen(), target), name + "Notifier");
		remove(displayNotifierFolder(gen(), target), name + "ProxyNotifier");
		remove(displayRequesterFolder(gen(), target), name + "Requester");
		remove(displayRequesterFolder(gen(), target), name + "ProxyRequester");
		remove(displayRequesterFolder(gen(), target), name + "PushRequester");
	}

}
