package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.builders.DialogReferenceBuilder;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaDialogContainerNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlexandriaDialogContainer extends ActivityDisplay<AlexandriaDialogContainerNotifier, Box> {
	private AlexandriaDialog dialog;
	private Class<? extends AlexandriaDialog> dialogType;
	private List<Consumer<String>> assertionListeners = new ArrayList<>();

	public AlexandriaDialogContainer(Box box) {
		super(box);
	}

	public void dialog(AlexandriaDialog dialog) {
		this.dialog = dialog;
		this.dialogType = dialog.getClass();
	}

	public void onDialogAssertion(Consumer<String> listener) {
		this.assertionListeners.add(listener);
	}

	@Override
	public void refresh() {
		super.refresh();
		if (dialogType != null) remove(dialogType);
		if (dialog == null) return;
		dialog.onDone(this::dialogAssertionMade);
		sendInfo();
		add(dialog);
		dialog.personifyOnce(id());
	}

	private void sendInfo() {
		notifier.refreshDialog(DialogReferenceBuilder.build(dialog.label(), dialogType.getSimpleName(), dialog.width(), dialog.height()));
	}

	public void dialogAssertionMade(String modification) {
		assertionListeners.forEach(l -> l.accept(modification));
	}

	private void dialogAssertionMade(DialogExecution.Modification modification) {
		notifier.closeDialog();
		dialogAssertionMade(modification.toString());
	}
}