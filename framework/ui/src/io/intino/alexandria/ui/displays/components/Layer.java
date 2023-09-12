package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.LayerOperation;
import io.intino.alexandria.schemas.LayerToolbar;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.events.BeforeListener;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.actionable.ExecuteListener;
import io.intino.alexandria.ui.displays.notifiers.LayerNotifier;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class Layer<DN extends LayerNotifier, B extends Box> extends AbstractLayer<B> {
	private Component<?, ?> template;
	private String title;
	private java.util.List<Listener> openListeners = new ArrayList<>();
	private BeforeListener beforeCloseListener;
	private java.util.List<Listener> closeListeners = new ArrayList<>();
	private OpenLayer<?, ?> homeAction;
	private Consumer<Layer> previousListener;
	private Function<Layer, Boolean> canPreviousResolver;
	private Consumer<Layer> nextListener;
	private Function<Layer, Boolean> canNextResolver;
	private java.util.List<LayerOperation> customOperationList = new ArrayList<>();
	private Consumer<String> executeOperationListener;

	public Layer(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		refresh();
	}

	public Layer<DN, B> bindTo(OpenLayer<?, ?> actionable) {
		this.homeAction = actionable;
		return this;
	}

	public Layer<DN, B> onPrevious(Consumer<Layer> listener, Function<Layer, Boolean> canPreviousResolver) {
		this.previousListener = listener;
		this.canPreviousResolver = canPreviousResolver;
		return this;
	}

	public Layer<DN, B> onNext(Consumer<Layer> listener, Function<Layer, Boolean> canNextResolver) {
		this.nextListener = listener;
		this.canNextResolver = canNextResolver;
		return this;
	}

	public Layer<DN, B> onExecuteOperation(Consumer<String> listener) {
		this.executeOperationListener = listener;
		return this;
	}

	public Layer<DN, B> addOperation(String name, String icon) {
		customOperationList.add(new LayerOperation().name(name).icon(icon));
		return this;
	}

	@Override
	public void refresh() {
		super.refresh();
		notifier.refreshToolbar(toolbar());
	}

	private LayerToolbar toolbar() {
		LayerToolbar result = new LayerToolbar();
		result.home(new LayerOperation().visible(homeAction != null).enabled(homeAction != null));
		result.previous(new LayerOperation().visible(previousListener != null).enabled(canPreviousResolver != null ? canPreviousResolver.apply(this) : false));
		result.next(new LayerOperation().visible(nextListener != null).enabled(canNextResolver != null ? canNextResolver.apply(this) : false));
		result.customOperations(customOperationList);
		return result;
	}

	public Layer<DN, B> onOpen(Listener listener) {
		openListeners.add(listener);
		return this;
	}

	public Layer<DN, B> onBeforeClose(BeforeListener listener) {
		beforeCloseListener = listener;
		return this;
	}

	public Layer<DN, B> onClose(Listener listener) {
		closeListeners.add(listener);
		return this;
	}

	public void open() {
		notifier.open();
		soul().pushLayer(this);
		notifyOpen();
	}

	public void close() {
		if (!beforeClose()) return;
		notifier.close();
		soul().popLayer();
		notifyClose();
	}

	private boolean beforeClose() {
		if (beforeCloseListener == null) return true;
		return beforeCloseListener.accept( new Event(this));
	}

	public void home() {
		if (homeAction == null) return;
		homeAction.openAddress();
	}

	public void previous() {
		if (previousListener == null) return;
		previousListener.accept(this);
		refresh();
	}

	public void next() {
		if (nextListener == null) return;
		nextListener.accept(this);
		refresh();
	}

	public void execute(String operation) {
		if (executeOperationListener == null) return;
		executeOperationListener.accept(operation);
	}

	public Layer<DN, B> title(String title) {
		_title(title);
		notifier.refreshTitle(title);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <C extends Component<?, ?>> C get() {
		return (C) template;
	}

	public <C extends Component<?, ?>> C template() {
		return get();
	}

	public void template(Component<?, ?> template) {
		clear();
		this.template = template;
		this.template.id(UUID.randomUUID().toString());
		add(this.template);
	}

	protected Layer<DN, B> _title(String title) {
		this.title = title;
		return this;
	}

	private void notifyOpen() {
		openListeners.forEach(l -> l.accept(new Event(this)));
	}

	private void notifyClose() {
		closeListeners.forEach(l -> l.accept(new Event(this)));
	}

}