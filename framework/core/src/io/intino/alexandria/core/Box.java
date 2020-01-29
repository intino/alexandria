package io.intino.alexandria.core;

public abstract class Box {

	protected Box owner;

	public abstract Box start();

	public abstract void stop();

	public abstract Box put(Object object);

	public Box owner() {
		return owner;
	}


	public abstract BoxConfiguration configuration();
}
