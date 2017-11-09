package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.notifiers.AlexandriaMenuLayoutDisplayNotifier;
import io.intino.konos.alexandria.activity.schemas.PlatformInfo;
import io.intino.konos.alexandria.activity.schemas.UserInfo;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AlexandriaMenuLayoutDisplay<DN extends AlexandriaMenuLayoutDisplayNotifier> extends AlexandriaLayoutDisplay<DN> {

	public AlexandriaMenuLayoutDisplay(Box box) {
		super(box);
	}

	@Override
	public void reset() {
	}

	@Override
	protected void refreshSelected(String label) {
		notifier.refreshSelected(label);
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
	protected void showDialog() {
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
	protected void createPanel(String item) {
	}

	@Override
	protected void showPanel() {
	}

	@Override
	protected void hidePanel() {
	}

	public void logout() {
		super.logout();
	}

	public void selectItem(String value) {
		super.selectItem(value);
	}
}