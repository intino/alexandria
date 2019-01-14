package io.intino.konos.model.graph;

import io.intino.tara.magritte.Node;

import java.util.ArrayList;
import java.util.List;

public class Toolbar extends AbstractToolbar {

	List<Operation> operations = new ArrayList<>();

	public Toolbar(io.intino.tara.magritte.Node node) {
		super(node);
	}

	public List<Operation> operations() {
		return operations;
	}

	@Override
	protected void addNode$(Node node) {
		super.addNode$(node);
		if (node.is(Operation.class)) operations.add(node.as(Operation.class));
	}
}