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
		System.out.println(String.format("Removed %s", reference.name()));
		clean(Target.Accessor);
		clean(Target.Owner);
		cache().remove(reference.toString());
	}

	public void clean(Target target) {
		String name = displayFilename(reference.name());
		removePassiveViewFiles(target);
		remove(displayFolder(gen(target), reference.type(), target), name, target);
		remove(displayFolder(gen(target), reference.type(), target), "Abstract" + name, target);
	}

	private void removePassiveViewFiles(Target target) {
		String name = displayFilename(reference.name());
		remove(displayNotifierFolder(gen(target), target), name + "Notifier", target);
		remove(displayNotifierFolder(gen(target), target), name + "ProxyNotifier", target);
		remove(displayRequesterFolder(gen(target), target), name + "Requester", target);
		remove(displayRequesterFolder(gen(target), target), name + "ProxyRequester", target);
		remove(displayRequesterFolder(gen(target), target), name + "PushRequester", target);
	}

}
