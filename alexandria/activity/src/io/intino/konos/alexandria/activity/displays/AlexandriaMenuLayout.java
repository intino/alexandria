package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaMenuLayoutNotifier;
import io.intino.konos.alexandria.activity.schemas.CreatePanelParameters;
import io.intino.konos.alexandria.activity.schemas.PlatformInfo;
import io.intino.konos.alexandria.activity.schemas.UserInfo;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AlexandriaMenuLayout<DN extends AlexandriaMenuLayoutNotifier> extends AlexandriaLayout<DN> {

	public AlexandriaMenuLayout(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	public void notifyUser(String message) {
	}

	@Override
	protected void refreshOpened(String label) {
		notifier.refreshOpened(itemWithKey(label).name());
	}

	@Override
	protected void sendLoading() {
		notifier.loading();
	}

	@Override
	protected void sendLoaded() {
		notifier.loaded();
	}

	@Override
	protected void sendUser(UserInfo userInfo) {
		notifier.user(userInfo);
	}

	@Override
	protected void openDefaultItem(String item) {
		notifier.openDefaultItem(item);
	}

	@Override
	protected void sendInfo(PlatformInfo info) {
		notifier.info(info);
	}

	@Override
	protected void sendItems(List<Item> itemList) {
		notifier.refreshItemList(itemList.stream().map(this::schemaItemOf).collect(toList()));
	}

	@Override
	protected void notifyLoggedOut() {
		notifier.userLoggedOut(session().browser().homeUrl());
	}

	@Override
	protected void showDialogBox() {
	}

	@Override
	protected void currentItem(String id) {
	}

	@Override
	protected io.intino.konos.alexandria.activity.model.Item currentItem() {
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

	public void home() {
		super.home();
	}

	public void openItem(String value) {
		super.openItem(value);
	}

	public void logout() {
		super.logout();
	}
}