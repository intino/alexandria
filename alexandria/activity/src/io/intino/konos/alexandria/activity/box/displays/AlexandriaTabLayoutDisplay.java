package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.notifiers.AlexandriaTabLayoutDisplayNotifier;
import io.intino.konos.alexandria.activity.box.schemas.PlatformInfo;
import io.intino.konos.alexandria.activity.box.schemas.UserInfo;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AlexandriaTabLayoutDisplay<DN extends AlexandriaTabLayoutDisplayNotifier> extends AlexandriaLayoutDisplay<DN> {

	public AlexandriaTabLayoutDisplay(Box box) {
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
	protected io.intino.konos.alexandria.activity.box.model.Item currentItem() {
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