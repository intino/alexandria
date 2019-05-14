package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.helpers.DisplayHelper;
import org.apache.maven.model.Notifier;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.ui.UIRenderer.Notifiers;
import static io.intino.konos.builder.codegeneration.ui.UIRenderer.Requesters;
import static io.intino.konos.builder.helpers.Commons.format;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class DisplayCleaner extends Cleaner {
	private ElementReference reference;

	public DisplayCleaner(Settings settings, ElementReference reference) {
		super(settings);
		this.reference = reference;
	}

	@Override
	public void execute() {
		removePassiveViewFiles();
		remove(src(), DisplayHelper.displayFile());
		remove(gen(), DisplayHelper.displayPath());
	}

	private void removePassiveViewFiles() {
		remove(new File(gen(), format(Notifiers)), snakeCaseToCamelCase(reference.name()) + "Notifier");
		remove(new File(gen(), format(Notifiers)), snakeCaseToCamelCase(reference.name()) + "ProxyNotifier");
		remove(new File(gen(), format(Requesters)), snakeCaseToCamelCase(reference.name()) + "Requester");
		remove(new File(gen(), format(Requesters)), snakeCaseToCamelCase(reference.name()) + "ProxyRequester");
		remove(new File(gen(), format(Requesters)), snakeCaseToCamelCase(reference.name()) + "PushRequester");
	}
}
