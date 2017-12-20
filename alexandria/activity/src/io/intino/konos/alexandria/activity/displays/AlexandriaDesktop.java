package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.ActivityBox;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaDesktopNotifier;
import io.intino.konos.alexandria.activity.model.Desktop;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Settings;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;

import java.net.URL;

public class AlexandriaDesktop<DN extends AlexandriaDesktopNotifier> extends AlexandriaElementDisplay<Desktop, DN> {

	public AlexandriaDesktop(ActivityBox box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	protected void init() {
		super.init();
		AlexandriaLayout display = element().layoutDisplay();
		display.onLoading((withMessage) -> refreshLoading((Boolean) withMessage));
		display.onLoaded((value) -> refreshLoaded());
		display.settings(new Settings() {
			@Override
			public String title() {
				return element().title();
			}

			@Override
			public String subtitle() {
				return element().subtitle();
			}

			@Override
			public String logo() {
				return element().logo();
			}

			@Override
			public String favicon() {
				return element().favicon();
			}

			@Override
			public URL authServiceUrl() {
				return element().authServiceUrl();
			}
		});
		notifier.displayType(display.name());
		addAndPersonify(display);
	}

	@Override
	protected void showDialog() {
	}

	@Override
	protected void currentItem(String id) {
	}

	@Override
	protected Item currentItem() {
		return null;
	}

	@Override
	protected void notifyFiltered(boolean value) {
	}

	@Override
	protected void refreshBreadcrumbs(String breadcrumbs) {
	}

	@Override
	protected void createPanel(CreatePanelParameters params) {
	}

	@Override
	protected void showPanel() {
	}

	@Override
	protected void hidePanel() {
	}

	private void refreshLoading(boolean withMessage) {
		notifier.loading(withMessage);
	}

	private void refreshLoaded() {
		notifier.loaded();
	}

}