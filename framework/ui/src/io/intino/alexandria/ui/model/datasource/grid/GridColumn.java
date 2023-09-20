package io.intino.alexandria.ui.model.datasource.grid;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GridColumn<T> {
	private String name;
	private String label;
	private Type type = Type.Text;
	private String address;
	private int width = -1;
	private boolean sortable = false;
	private boolean visible;
	private boolean fixed;
	private String pattern = "dd/MM/yyyy HH:mm:ss";
	private Formatter formatter = defaultFormatter();

	public enum Type { Link, Text, Number, Date, Icon, MaterialIcon }

	public String name() {
		return name;
	}

	public GridColumn<T> name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public GridColumn<T> label(String label) {
		this.label = label;
		return this;
	}

	public Type type() {
		return type;
	}

	public GridColumn<T> type(Type type) {
		this.type = type;
		return this;
	}

	public String address() {
		return address;
	}

	public GridColumn<T> address(String address) {
		this.address = address;
		return this;
	}

	public boolean visible() {
		return visible;
	}

	public GridColumn<T> visible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public int width() {
		return width;
	}

	public GridColumn width(int width) {
		this.width = width;
		return this;
	}

	public boolean sortable() {
		return sortable;
	}

	public GridColumn sortable(boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	public boolean fixed() {
		return fixed;
	}

	public GridColumn fixed(boolean fixed) {
		this.fixed = fixed;
		return this;
	}

	public String pattern() {
		return pattern;
	}

	public GridColumn pattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public Formatter formatter() {
		return formatter;
	}

	public GridColumn formatter(Formatter formatter) {
		this.formatter = formatter;
		return this;
	}

	public interface Formatter {
		String apply(GridValue value);
	}

	private Formatter defaultFormatter() {
		return value -> {
			if (value.isInstant()) return formatInstant(value);
			return value.asText();
		};
	}

	private String formatInstant(GridValue value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
		return formatter.format(value.asInstant());
	}

}
