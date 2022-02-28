package io.intino.alexandria.sumus;

public interface Index {
	Index None = idx -> false;
	boolean contains(int idx);
}
