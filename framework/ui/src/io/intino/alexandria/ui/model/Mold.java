package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.model.mold.Block;
import io.intino.alexandria.ui.model.mold.Stamp;

import java.util.ArrayList;
import java.util.List;

public class Mold extends Element {
	private String type;
	private List<Block> blockList = new ArrayList<>();
	private List<Stamp> stampList = new ArrayList<>();

	public String type() {
		return type;
	}

	public Mold type(String type) {
		this.type = type;
		return this;
	}

	public List<Block> blocks() {
		return this.blockList;
	}

	public Mold add(Block block) {
		this.blockList.add(block);
		return this;
	}

	public List<Stamp> stamps() {
		return this.stampList;
	}

	public Mold add(Stamp stamp) {
		this.stampList.add(stamp);
		return this;
	}
}
