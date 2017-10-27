package io.intino.konos.server.activity.displays.schemas;

public class Mold implements java.io.Serializable {

	private java.util.List<MoldBlock> moldBlockList = new java.util.ArrayList<>();

	public java.util.List<MoldBlock> moldBlockList() {
		return this.moldBlockList;
	}

	public Mold moldBlockList(java.util.List<MoldBlock> moldBlockList) {
		this.moldBlockList = moldBlockList;
		return this;
	}
}