package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.WizardNavigatorInfo;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.WizardNavigatorNotifier;

import java.util.List;
import java.util.stream.Collectors;

public class WizardNavigator<DN extends WizardNavigatorNotifier, B extends Box> extends AbstractWizardNavigator<B> {
	private Wizard<?, ?> wizard;
	private String confirmMessage;

	public WizardNavigator(B box) {
		super(box);
	}

	public WizardNavigator<DN, B> bindTo(Wizard<?, ?> wizard) {
		this.wizard = wizard;
		this.wizard.onStepArrival(e -> refresh());
		this.wizard.onFinish(e -> refresh());
		return this;
	}

	public void back() {
		wizard.back();
		refresh();
	}

	public void select(Integer pos) {
		wizard.select(pos);
		refresh();
	}

	public void next() {
		wizard.next();
		refresh();
	}

	public void finish() {
		wizard.finish();
		refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		notifier.refresh(info());
	}

	protected void _confirmMessage(String message) {
		this.confirmMessage = message;
	}

	private WizardNavigatorInfo info() {
		return new WizardNavigatorInfo().active(wizard.active()).stepsCount(visibleSteps().size()).allowNext(wizard.allowNext()).allowBack(wizard.allowBack()).allowFinish(wizard.allowFinish()).finished(wizard.finished());
	}

	private List<Step> visibleSteps() {
		return wizard.steps().stream().filter(Component::isVisible).collect(Collectors.toList());
	}
}