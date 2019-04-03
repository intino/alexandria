package io.intino.konos.model.graph;

public class Collection extends AbstractCollection {

	public Collection(io.intino.tara.magritte.Node node) {
		super(node);
	}

	public boolean hasMold() {
		return isList() || isDetail() || isMap() || isGrid();
	}

	public Mold mold() {
		if (isList()) return asList().mold();
		if (isDetail()) return asDetail().mold();
		if (isMap()) return asMap().mold();
		if (isGrid()) return asGrid().mold();
		return null;
	}
}