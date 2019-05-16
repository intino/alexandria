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
		remove(displayFolder(gen(), reference.type(), target), name, target);
	}

	private void removePassiveViewFiles(Target target) {
		String name = displayFilename(reference.name());
		remove(displayNotifierFolder(gen(), target), name + "Notifier", target);
		remove(displayNotifierFolder(gen(), target), name + "ProxyNotifier", target);
		remove(displayRequesterFolder(gen(), target), name + "Requester", target);
		remove(displayRequesterFolder(gen(), target), name + "ProxyRequester", target);
		remove(displayRequesterFolder(gen(), target), name + "PushRequester", target);
	}

}
