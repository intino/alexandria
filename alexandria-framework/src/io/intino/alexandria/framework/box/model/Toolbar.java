package io.intino.alexandria.framework.box.model;

import io.intino.alexandria.framework.box.model.toolbar.Operation;

import java.util.ArrayList;
import java.util.List;

public class Toolbar {
	protected boolean canSearch;
	private List<Operation> operationList = new ArrayList<>();

	public boolean canSearch() {
		return canSearch;
	}

	public Toolbar canSearch(boolean value) {
		this.canSearch = value;
		return this;
	}

	public List<Operation> operations() {
		return this.operationList;
	}

	public Toolbar add(Operation operation) {
		this.operationList.add(operation);
		return this;
	}
}
