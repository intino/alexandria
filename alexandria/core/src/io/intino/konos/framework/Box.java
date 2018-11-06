package io.intino.konos.framework;

public abstract class Box {

	protected Box owner;

	public abstract Box open();

	public abstract void close();

	public abstract Box put(Object object);

	public Box owner() {
		return owner;
	}


	public abstract BoxConfiguration configuration();
}
