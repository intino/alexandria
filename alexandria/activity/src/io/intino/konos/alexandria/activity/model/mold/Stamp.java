package io.intino.konos.alexandria.activity.model.mold;

import io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public abstract class Stamp<O> {
	private String name = "";
	private String label = "";
	private Value<String> labelLoader;
	private Value value = empty();
	private Layout layout = Layout.Fixed;
	private int height = -1;
	private String suffix = "";
	private String defaultStyle = "";
	private Value<String> style = empty();
	private Value<String> className = empty();
	private ChangeEvent changeEvent;
	private ValidateEvent validateEvent;
	private MessageLoader messageLoader;
	private Value<Color> color = emptyColor();

	public String name() {
		return this.name;
	}

	public Stamp name(String name) {
		this.name = name;
		return this;
	}

	public String label(Item item, ActivitySession session) {
		return labelLoader != null ? labelLoader.value(item != null ? item.object() : null, session) : label;
	}

	public Stamp label(String label) {
		this.label = label;
		return this;
	}

	public Stamp labelLoader(Value<String> loader) {
		this.labelLoader = loader;
		return this;
	}

	public Color color(Item item, ActivitySession session) {
		return objectColor(item != null ? item.object() : null, session);
	}

	public Color objectColor(Object object, ActivitySession session) {
		return color != null ? color.value(object, session) : null;
	}

	public Stamp color(Value<Color> color) {
		this.color = color;
		return this;
	}

	public O value(Item item, ActivitySession session) {
		return objectValue(item != null ? item.object() : null, session);
	}

	public abstract O objectValue(Object object, ActivitySession session);

	public Stamp value(Value<O> value) {
		this.value = value;
		return this;
	}

	public Layout layout() {
		return this.layout;
	}

	public Stamp layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public int height() {
		return this.height;
	}

	public Stamp height(int height) {
		this.height = height;
		return this;
	}

	public String suffix() {
		return this.suffix;
	}

	public Stamp suffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	public String defaultStyle() {
		return this.defaultStyle;
	}

	public Stamp defaultStyle(String defaultStyle) {
		this.defaultStyle = defaultStyle;
		return this;
	}

	public String style(Item item, ActivitySession session) {
		return objectStyle(item != null ? item.object() : null, session);
	}

	public String objectStyle(Object object, ActivitySession session) {
		return style != null ? style.value(object, session) : empty().value(object, session);
	}

	public Stamp style(Value style) {
		this.style = style;
		return this;
	}

	public String className(Item item, ActivitySession session) {
		return objectClassName(item != null ? item.object() : null, session);
	}

	public String objectClassName(Object object, ActivitySession session) {
		return className != null ? className.value(object, session) : empty().value(object, session);
	}

	public Stamp className(Value className) {
		this.className = className;
		return this;
	}

	public ChangeEvent.Refresh change(Item item, String value, AlexandriaDisplay self, ActivitySession session) {
		return changeEvent != null ? changeEvent.change(item != null ? item.object() : null, value, self.id(), session) : ChangeEvent.Refresh.None;
	}

	public Stamp changeEvent(ChangeEvent event) {
		this.changeEvent = event;
		return this;
	}

	public String validate(Item item, String value, AlexandriaDisplay self, ActivitySession session) {
		return validateEvent != null ? validateEvent.validate(item != null ? item.object() : null, value, self.id(), session) : null;
	}

	public Stamp validateEvent(ValidateEvent event) {
		this.validateEvent = event;
		return this;
	}

	public String message(Item item, AlexandriaDisplay self, ActivitySession session) {
		return messageLoader != null ? messageLoader.load(item != null ? item.object() : null, self.id(), session) : null;
	}

	public Stamp messageLoader(MessageLoader loader) {
		this.messageLoader = loader;
		return this;
	}

	public boolean editable() {
		return changeEvent != null;
	}

	public enum Layout {
		Fixed, Flexible;
	}

	public interface Value<O> {
		O value(Object object, ActivitySession session);
	}

	public interface MessageLoader {
		String load(Object object, String selfId, ActivitySession session);
	}

	public interface ChangeEvent {
		Refresh change(Object object, String value, String selfId, ActivitySession session);

		enum Refresh {
			None, Object, Catalog
		}
	}

	public interface ValidateEvent {
		String validate(Object object, String value, String selfIf, ActivitySession session);
	}

	public static class Color {
		private String text;
		private String background;

		public String text() {
			return text;
		}

		public Color text(String text) {
			this.text = text;
			return this;
		}

		public String background() {
			return background;
		}

		public Color background(String background) {
			this.background = background;
			return this;
		}
	}

	protected Value<O> value() {
		return value;
	}

	private static Value<String> empty() {
		return (object, user) -> "";
	}

	private static Value<Color> emptyColor() {
		return (object, user) -> null;
	}

}
