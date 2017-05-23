package io.intino.konos;

public abstract class Box {

	protected Box owner;

	public abstract void open();

	public abstract Box put(Object object);

}
