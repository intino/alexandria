package io.intino.konos.alexandria.activity.schemas;

public class ItemRefreshInfo implements java.io.Serializable {

	private Mold mold;
	private Item item;

	public Mold mold() {
		return this.mold;
	}

	public Item item() {
		return this.item;
	}

	public ItemRefreshInfo mold(Mold mold) {
		this.mold = mold;
		return this;
	}

	public ItemRefreshInfo item(Item item) {
		this.item = item;
		return this;
	}
}