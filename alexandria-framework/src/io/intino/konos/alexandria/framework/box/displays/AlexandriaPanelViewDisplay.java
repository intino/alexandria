package io.intino.konos.alexandria.framework.box.displays;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.foundation.activity.displays.ActivityDisplay;
import io.intino.konos.alexandria.foundation.activity.displays.DisplayNotifier;
import io.intino.konos.alexandria.framework.box.displays.providers.ElementViewDisplayProvider;
import io.intino.konos.alexandria.framework.box.model.Panel;
import io.intino.konos.alexandria.framework.box.schemas.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlexandriaPanelViewDisplay<DN extends DisplayNotifier> extends ActivityDisplay<DN> implements ElementViewDisplay<ElementViewDisplayProvider> {
	private Panel context;
	private io.intino.konos.alexandria.framework.box.model.Item target;
	private ElementView<Panel> view;
	private ElementViewDisplayProvider provider;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<OpenItemEvent>> openItemListeners = new ArrayList<>();
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();

	public AlexandriaPanelViewDisplay(Box box) {
		super(box);
	}

	public Panel context() {
		return this.context;
	}

	public void context(Panel context) {
		this.context = context;
	}

	public io.intino.konos.alexandria.framework.box.model.Item target() {
		return this.target;
	}

	public void target(io.intino.konos.alexandria.framework.box.model.Item target) {
		this.target = target;
	}

	public ElementView<Panel> view() {
		return view;
	}

	@Override
	public void view(ElementView view) {
		this.view = view;
	}

	@Override
	public void refresh(Item... items) {
		refresh();
	}

	public ElementViewDisplayProvider provider() {
		return this.provider;
	}

	public void provider(ElementViewDisplayProvider provider) {
		this.provider = provider;
	}

	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	@Override
	public void onOpenItem(Consumer<OpenItemEvent> listener) {
		openItemListeners.add(listener);
	}

	@Override
	public void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	@Override
	public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
	}

	protected void notifyLoading(Boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	protected void notifyOpenItem(OpenItemEvent params) {
		openItemListeners.forEach(l -> l.accept(params));
	}

	protected void notifyOpenItemDialog(OpenItemDialogEvent params) {
		openItemDialogListeners.forEach(l -> l.accept(params));
	}

	protected void notifyExecuteItemTaskOperation(ExecuteItemTaskEvent params) {
		executeItemTaskListeners.forEach(l -> l.accept(params));
	}

}
