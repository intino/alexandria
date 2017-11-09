package io.intino.konos.alexandria.activity.box.model;

import io.intino.konos.alexandria.activity.box.model.mold.Block;
import io.intino.konos.alexandria.activity.box.model.mold.Stamp;

import java.util.ArrayList;
import java.util.List;

public class Mold extends Element {
	private List<Block> blockList = new ArrayList<>();
	private List<Stamp> stampList = new ArrayList<>();

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
