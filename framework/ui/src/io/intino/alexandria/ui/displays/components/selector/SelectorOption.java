package io.intino.alexandria.ui.displays.components.selector;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.Block;

import java.util.ArrayList;
import java.util.List;

public interface SelectorOption {
	String name();
	String id();
	void update();
	<T extends Display> T parent(Class<T> type);

	default List<Block> ancestors() {
		List<Block> result = new ArrayList<>();
		Block parent = parent(Block.class);
		while (parent != null) {
			result.add(parent);
			parent = (Block)parent.parent(Block.class);
		}
		return result;
	}
}
