package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.Resource;
import io.intino.alexandria.ui.displays.events.ExecuteItemTaskEvent;
import io.intino.alexandria.ui.displays.events.OpenItemCatalogEvent;
import io.intino.alexandria.ui.displays.events.OpenItemDialogEvent;
import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.konos.framework.Box;
import io.intino.alexandria.ui.displays.providers.ElementViewDisplayProvider;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.model.mold.stamps.CatalogLink;
import io.intino.alexandria.ui.model.mold.stamps.operations.DownloadOperation;
import io.intino.konos.alexandria.ui.schemas.*;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.intino.alexandria.ui.helpers.ElementHelper.*;

public abstract class AlexandriaElementView<N extends AlexandriaDisplayNotifier, P extends ElementViewDisplayProvider> extends ActivityDisplay<N, Box> {
	private P provider;
	private View view;
	private List<Consumer<Boolean>> loadingListeners = new ArrayList<>();
	private List<Consumer<OpenItemDialogEvent>> openItemDialogListeners = new ArrayList<>();
	private List<Consumer<OpenItemCatalogEvent>> openItemCatalogListeners = new ArrayList<>();
	private List<Consumer<ExecuteItemTaskEvent>> executeItemTaskListeners = new ArrayList<>();

	public AlexandriaElementView(Box box) {
		super(box);
	}

	P provider() {
		return provider;
	}

	public void provider(P provider) {
		this.provider = provider;
	}

	public <V extends View> V view() { return (V) this.view; }

	public void view(View view) {
		this.view = view;
	}

	public void onLoading(Consumer<Boolean> listener) {
		loadingListeners.add(listener);
	}

	public void onOpenItemDialog(Consumer<OpenItemDialogEvent> listener) {
		openItemDialogListeners.add(listener);
	}

	public void onOpenItemCatalog(Consumer<OpenItemCatalogEvent> listener) {
		openItemCatalogListeners.add(listener);
	}

	public void onExecuteItemTask(Consumer<ExecuteItemTaskEvent> listener) {
		executeItemTaskListeners.add(listener);
	}

	@Override
	public void refresh() {
		notifyLoading(true);
		super.refresh();
		notifyLoading(false);
	}

	public void refresh(io.intino.konos.alexandria.ui.schemas.Item... items) {
		Stream.of(items).forEach(this::refresh);
	}

	abstract void refresh(io.intino.konos.alexandria.ui.schemas.Item item);
	abstract void refresh(io.intino.konos.alexandria.ui.schemas.Item item, boolean highlight);
	abstract void refreshValidation(String validationMessage, Stamp stamp, io.intino.konos.alexandria.ui.schemas.Item item);

	public void reset() {
	}

	Item itemOf(String id) {
		return provider.item(new String(Base64.getDecoder().decode(id)));
	}

	void notifyLoading(boolean value) {
		loadingListeners.forEach(l -> l.accept(value));
	}

	UIFile downloadItemOperation(DownloadItemParameters params) {
		List<Stamp> stamps = provider().stamps(view.mold());
		Stamp stamp = stamps.stream().filter(s -> s.name().equals(params.stamp())).findFirst().orElse(null);
		if (stamp == null) return null;
		Resource resource = ((DownloadOperation)stamp).execute(itemOf(params.item()), params.option(), session());
		return new UIFile() {
			@Override
			public String label() {
				return resource.label();
			}

			@Override
			public InputStream content() {
				return resource.content();
			}
		};
	}

	UIFile downloadOperation(ElementOperationParameters value, List<Item> selection) {
		Resource resource = provider().downloadOperation(value, selection);
		return new UIFile() {
			@Override
			public String label() {
				return resource.label();
			}

			@Override
			public InputStream content() {
				return resource.content();
			}
		};
	}

	void openItemDialogOperation(OpenItemParameters params) {
		openItemDialogOperation(openItemDialogEvent(itemOf(params.item()), provider.stamp(view.mold(), params.stamp()), session()));
	}

	void openItemDialogOperation(OpenItemDialogEvent event) {
		openItemDialogListeners.forEach(l -> l.accept(event));
	}

	void openItemCatalogOperation(OpenItemParameters params) {
		openItemCatalogOperation(openItemCatalogEvent(itemOf(params.item()), provider().stamp(view.mold(), params.stamp()), params.position(), provider.element(), session()));
	}

	void openItemCatalogOperation(OpenItemCatalogEvent event) {
		openItemCatalogListeners.forEach(l -> l.accept(event));
	}

	void openElement(String key) {
		provider.openElement(key);
	}

	void openElement(String key, String ownerId) {
		provider.openElement(key, ownerId);
	}

	void openElement(OpenElementParameters params) {
		Stamp stamp = provider.stamp(view.mold(), params.stamp().name());
		if (!(stamp instanceof CatalogLink)) return;

		CatalogLink catalogLinkStamp = (CatalogLink)stamp;
		AlexandriaAbstractCatalog display = provider.openElement(catalogLinkStamp.catalog().name());

		Item source = itemOf(params.item());
		if (display instanceof AlexandriaTemporalCatalog && provider.range() != null)
			((AlexandriaTemporalCatalog) display).selectRange(provider.range());

		if (catalogLinkStamp.openItemOnLoad()) display.openItem(catalogLinkStamp.item(source, session()));
		else {
			if (catalogLinkStamp.filtered())
				display.filterAndNotify(item -> catalogLinkStamp.filter(source, (Item) item, session()));
			display.refresh();
		}
	}

	void executeItemTaskOperation(ExecuteItemTaskParameters params) {
		executeItemTaskOperation(executeItemTaskEvent(itemOf(params.item()), provider.stamp(view.mold(), params.stamp()), this));
	}

	void executeItemTaskOperation(ExecuteItemTaskEvent event) {
		executeItemTaskListeners.forEach(l -> l.accept(event));
	}

	void executeOperation(ElementOperationParameters params, List<Item> selection) {
		provider().executeOperation(params, selection);
	}

	void changeItem(ChangeItemParameters params) {
		Item item = itemOf(params.item());
		provider().changeItem(item, provider().stamp(view.mold(), params.stamp()), params.value());
	}

	void validateItem(io.intino.konos.alexandria.ui.schemas.ValidateItemParameters params) {
		Item item = itemOf(params.item());
		provider().validateItem(item, provider().stamp(view.mold(), params.stamp()), params.value());
	}
}
