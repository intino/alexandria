package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaDesktopDisplayNotifier;
import io.intino.konos.alexandria.activity.model.Desktop;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Settings;

import java.net.URL;

public class AlexandriaDesktopDisplay<DN extends AlexandriaDesktopDisplayNotifier> extends AlexandriaElementDisplay<Desktop, DN> {

	public AlexandriaDesktopDisplay(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	protected void init() {
		super.init();
		AlexandriaLayoutDisplay display = element().layoutDisplay();
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
			public URL logo() {
				return element().logo();
			}

			@Override
			public URL favicon() {
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
	protected void createPanel(String item) {
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