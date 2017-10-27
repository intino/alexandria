package io.intino.konos.server.activity.displays.schemas;

public class Item implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private java.time.Instant group;
	private java.util.List<ItemStamp> recordItemStampList = new java.util.ArrayList<>();
	private java.util.List<ItemBlock> recordItemBlockList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public java.time.Instant group() {
		return this.group;
	}

	public java.util.List<ItemStamp> recordItemStampList() {
		return this.recordItemStampList;
	}

	public java.util.List<ItemBlock> recordItemBlockList() {
		return this.recordItemBlockList;
	}

	public Item name(String name) {
		this.name = name;
		return this;
	}

	public Item label(String label) {
		this.label = label;
		return this;
	}

	public Item group(java.time.Instant group) {
		this.group = group;
		return this;
	}

	public Item itemStampList(java.util.List<ItemStamp> recordItemStampList) {
		this.recordItemStampList = recordItemStampList;
		return this;
	}

	public Item itemBlockList(java.util.List<ItemBlock> recordItemBlockList) {
		this.recordItemBlockList = recordItemBlockList;
		return this;
	}
}