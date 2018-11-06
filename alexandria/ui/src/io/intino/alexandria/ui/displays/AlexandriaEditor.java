package io.intino.alexandria.ui.displays;

import io.intino.alexandria.Resource;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaEditorNotifier;
import io.intino.alexandria.ui.model.Editor;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.schemas.CreatePanelParameters;
import io.intino.alexandria.ui.services.EditorService;
import io.intino.konos.framework.Box;

public abstract class AlexandriaEditor<T extends AlexandriaDisplay> extends AlexandriaElementDisplay<Editor, AlexandriaEditorNotifier> {
	private Resource document;
	private EditorService.Permission permission;
	private T display = null;

	public AlexandriaEditor(Box box) {
		super(box);
	}

	@Override
	protected void init() {
		super.init();
		notifier.displayType(display.name());
		prepare(display, document, permission);
		add(display);
		display.personifyOnce(id());
	}

	public void document(Resource document) {
		this.document = document;
	}

	public void permission(EditorService.Permission permission) {
		this.permission = permission;
	}

	public T display() {
		return this.display;
	}

	public void display(T display) {
		this.display = display;
	}

	public void notifySaved() {
		notifier.saved();
	}

	public abstract void prepare(T display, Resource document, EditorService.Permission permission);
	public abstract void save();

	@Override
	public void reset() {
	}

	@Override
	public void notifyUser(String message) {
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

}