package io.intino.konos.alexandria.activity.schemas;

public class Item implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private java.time.Instant group;
	private java.util.List<ItemStamp> itemStampList = new java.util.ArrayList<>();
	private java.util.List<ItemBlock> itemBlockList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public java.time.Instant group() {
		return this.group;
	}

	public java.util.List<ItemStamp> itemStampList() {
		return this.itemStampList;
	}

	public java.util.List<ItemBlock> itemBlockList() {
		return this.itemBlockList;
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

	public Item itemStampList(java.util.List<ItemStamp> itemStampList) {
		this.itemStampList = itemStampList;
		return this;
	}

	public Item itemBlockList(java.util.List<ItemBlock> itemBlockList) {
		this.itemBlockList = itemBlockList;
		return this;
	}
}