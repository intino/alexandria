package io.intino.alexandria.ui.model.mold;

import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

import java.util.ArrayList;
import java.util.List;

public class Block {
	private String name;
	private List<Layout> layouts = new ArrayList<>();
	private int width = -1;
	private int height = -1;
	private Hidden hidden = defaultHidden();
	private boolean hiddenIfMobile = false;
	private String style = "";
	private List<Block> blockList = new ArrayList<>();
	private List<Stamp> stampList = new ArrayList<>();
	private boolean expanded = false;
	private ClassNameLoader classNameLoader = null;

	public String name() {
		return this.name;
	}

	public Block name(String name) {
		this.name = name;
		return this;
	}

	public List<Layout> layouts() {
		return layouts;
	}

	public Block add(Layout layout) {
		this.layouts.add(layout);
		return this;
	}

	public int width() {
		return width;
	}

	public Block width(int width) {
		this.width = width;
		return this;
	}

	public int height() {
		return height;
	}

	public Block height(int height) {
		this.height = height;
		return this;
	}

	public boolean hidden(Item item, UISession session) {
		return hidden != null && hidden.hidden(item != null ? item.object() : null, session);
	}

	public Block hidden(Hidden hidden) {
		this.hidden = hidden;
		return this;
	}

	public boolean hiddenIfMobile() {
		return hiddenIfMobile;
	}

	public Block hiddenIfMobile(boolean hiddenIfMobile) {
		this.hiddenIfMobile = hiddenIfMobile;
		return this;
	}

	public String style() {
		return this.style;
	}

	public Block style(String style) {
		this.style = style;
		return this;
	}

	public List<Block> blockList() {
		return blockList;
	}

	public Block add(Block block) {
		this.blockList.add(block);
		return this;
	}

	public List<Stamp> stampList() {
		return stampList;
	}

	public Block add(Stamp stamp) {
		this.stampList.add(stamp);
		return this;
	}

	public boolean expanded() {
		return this.expanded;
	}

	public Block expanded(boolean value) {
		this.expanded = value;
		return this;
	}

	public String className(Item item, UISession session) {
		return classNameLoader != null ? classNameLoader.value(item != null ? item.object() : null, session) : null;
	}

	public Block className(ClassNameLoader loader) {
		this.classNameLoader = loader;
		return this;
	}

	public enum Layout {
		Vertical, Horizontal, Fixed, Flexible, Wrap, Justified, StartJustified, CenterJustified, EndJustified, Start, Center, End;
	}

	public interface Hidden {
		boolean hidden(Object object, UISession session);
	}

	private static Hidden defaultHidden() {
		return (object, session) -> false;
	}

	public interface ClassNameLoader {
		String value(Object object, UISession session);
	}
}
