package io.intino.konos.alexandria.activity.schemas;

public class Toolbar implements java.io.Serializable {

	private java.util.List<Operation> operationList = new java.util.ArrayList<>();

	public java.util.List<Operation> operationList() {
		return this.operationList;
	}

	public Toolbar operationList(java.util.List<Operation> operationList) {
		this.operationList = operationList;
		return this;
	}
}