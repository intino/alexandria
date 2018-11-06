package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.UIBox;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaDesktopNotifier;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.Settings;
import io.intino.alexandria.ui.model.panel.Desktop;
import io.intino.alexandria.ui.schemas.CreatePanelParameters;

import java.net.URL;

import static io.intino.alexandria.ui.model.Panel.Layout.Tab;

public class AlexandriaDesktop<DN extends AlexandriaDesktopNotifier> extends AlexandriaElementDisplay<Desktop,DN> {

	public AlexandriaDesktop(UIBox box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	public void logout() {
		session().logout();
	}

	@Override
	protected void init() {
		super.init();
		AlexandriaLayout display = createLayout();
		display.whenLogout(value -> logout());
		display.route(route());
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

	private AlexandriaLayout createLayout() {
		AlexandriaLayout layout = (element().layout() == Tab) ? new AlexandriaTabLayout(box) : new AlexandriaMenuLayout(box);
		layout.element(element());
		return layout;
	}

	public <E extends AlexandriaElementDisplay> E openElement(String label) {
		return (E) layout().openElement(label);
	}

	@Override
	public void notifyUser(String message) {
	}

	public <T extends AlexandriaLayout> T layout() {
		return (T) child(AlexandriaLayout.class);
	}

	public void home() {
		child(AlexandriaLayout.class).home();
	}

	public void openItem(String key) {
		child(AlexandriaLayout.class).openItem(key);
	}

	@Override
	protected void showDialogBox() {
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